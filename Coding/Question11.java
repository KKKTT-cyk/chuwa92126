import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;

public class Question11 {

    record Product(String sku, String name, BigDecimal price) {
    }

    record Stock(String sku, int available, boolean known) {
        static Stock unknown(String sku) {
            return new Stock(sku, 0, false);
        }
    }

    record Review(String author, int rating, String text) {
    }

    record ProductPage(Product product, Stock stock, List<Review> reviews) {
    }

    interface CatalogClient {
        Mono<Product> getProduct(String sku);
    }

    interface InventoryClient {
        Mono<Stock> getStock(String sku);
    }

    interface ReviewClient {
        Flux<Review> getReviews(String sku);
    }

    static class ProductPageService {
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
            Mono<Product> product = Mono.defer(() -> catalog.getProduct(sku))
                    .timeout(CALL_TIMEOUT);

            Mono<Stock> stock = Mono.defer(() -> inventory.getStock(sku))
                    .timeout(CALL_TIMEOUT)
                    .onErrorReturn(Stock.unknown(sku));

            Mono<List<Review>> reviewList = Flux.defer(() -> reviews.getReviews(sku))
                    .collectList()
                    .timeout(CALL_TIMEOUT)
                    .onErrorReturn(List.of());

            return Mono.zip(product, stock, reviewList)
                    .map(tuple -> new ProductPage(
                            tuple.getT1(), tuple.getT2(), tuple.getT3()));
        }
    }
}
