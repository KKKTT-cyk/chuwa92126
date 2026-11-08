import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;

record Product(String sku, String name, BigDecimal price) { }

record Stock(String sku, int available, boolean known) {
    static Stock unknown(String sku) {
        return new Stock(sku, 0, false);
    }
}

record Review(String author, int rating, String text) { }

record ProductPage(Product product, Stock stock, List<Review> reviews) { }

interface CatalogClient {
    Mono<Product> getProduct(String sku);
}

interface InventoryClient {
    Mono<Stock> getStock(String sku);
}

interface ReviewClient {
    Flux<Review> getReviews(String sku);
}

class ProductPageService {

    private static final Duration CALL_TIMEOUT = Duration.ofSeconds(2);

    private final CatalogClient catalog;
    private final InventoryClient inventory;
    private final ReviewClient reviews;

    ProductPageService(CatalogClient catalog, InventoryClient inventory,
                       ReviewClient reviews) {
        this.catalog = catalog;
        this.inventory = inventory;
        this.reviews = reviews;
    }

    public Mono<ProductPage> productPage(String sku) {

        // Required: a failure or timeout here fails the whole page.
        Mono<Product> productMono = catalog.getProduct(sku)
                .timeout(CALL_TIMEOUT);

        // Optional: degrade to Stock.unknown(sku) on failure or timeout.
        Mono<Stock> stockMono = inventory.getStock(sku)
                .timeout(CALL_TIMEOUT)
                .onErrorResume(e -> Mono.just(Stock.unknown(sku)));

        // Optional: degrade to an empty list on failure or timeout.
        // collectList() first, so the 2 s budget covers the whole call
        // rather than the gap between two reviews.
        Mono<List<Review>> reviewsMono = reviews.getReviews(sku)
                .collectList()
                .timeout(CALL_TIMEOUT)
                .onErrorResume(e -> Mono.just(List.of()));

        // zip subscribes to all three at once -> the calls run concurrently.
        // If productMono fails, zip propagates the error and cancels the rest.
        return Mono.zip(productMono, stockMono, reviewsMono)
                .map(results -> new ProductPage(results.getT1(),
                                                results.getT2(),
                                                results.getT3()));
    }
}
