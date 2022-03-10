package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import kitchenpos.infra.PurgomalumClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final PurgomalumClient purgomalumClient;
    private final MenuService menuService;

    @Autowired
    public ProductService(
        final ProductRepository productRepository,
        final PurgomalumClient purgomalumClient,
        final MenuService menuService
    ) {
        this.productRepository = productRepository;
        this.purgomalumClient = purgomalumClient;
        this.menuService = menuService;
    }

    @Transactional
    public Product create(final Product request) {
        validateOnCreate(request);

        Product product = Product.builder()
            .id(UUID.randomUUID())
            .name(request.getName())
            .price(request.getPrice())
            .build();

        return productRepository.save(product);
    }

    @Transactional
    public Product changePrice(final UUID productId, final Product request) {
        validateOnUpdate(request);

        final Product product = fetchById(productId);
        final BigDecimal price = request.getPrice();
        product.setPrice(price);
        final Product updated = productRepository.save(product);

        afterUpdate(updated);
        return updated;
    }

    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Product fetchById(final UUID id) {
        return productRepository.findById(id)
            .orElseThrow(NoSuchElementException::new);
    }

    // Bean Validation 대체 가능
    private void validateOnCreate(Product product) {
        if (product.hasInvalidPrice()) {
            throw new IllegalArgumentException();
        }
        if (purgomalumClient.containsProfanity(product.getName())) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOnUpdate(Product product) {
        if (product.hasInvalidPrice()) {
            throw new IllegalArgumentException();
        }
    }

    // model stream
    private void afterUpdate(Product updated) {
        menuService.hideInvalidPriceMenu(updated.getId());
    }
}
