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
    private final CatalogClient catalog;
    private final InventoryClient inventory;
    private final ReviewClient reviews;

    ProductPageService(
            CatalogClient catalog,
            InventoryClient inventory,
            ReviewClient reviews
    ) {
        this.catalog = catalog;
        this.inventory = inventory;
        this.reviews = reviews;
    }

    public Mono<ProductPage> productPage(String sku) {
        Duration timeout = Duration.ofSeconds(2);

        Mono<Product> product = catalog.getProduct(sku)
                .timeout(timeout);

        Mono<Stock> stock = inventory.getStock(sku)
                .timeout(timeout)
                .onErrorReturn(Stock.unknown(sku));

        Mono<List<Review>> reviewList = reviews.getReviews(sku)
                .collectList()
                .timeout(timeout)
                .onErrorReturn(List.of());

        return Mono.zip(product, stock, reviewList, ProductPage::new);
    }
}
