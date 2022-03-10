package kitchenpos.application;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class MenuProductService {
  private final ProductService productService;
  private final ProductRepository productRepository;

  public MenuProductService(
      final ProductService productService,
      final ProductRepository productRepository
  ) {
    this.productService = productService;
    this.productRepository = productRepository;
  }

  public List<MenuProduct> create(final List<MenuProduct> menuProductRequests,
      final List<UUID> productIds) {
    validateProductSize(menuProductRequests, productIds);

    final List<MenuProduct> menuProducts = new ArrayList<>();

    for (final MenuProduct menuProductRequest : menuProductRequests) {
      final Product product = productService.fetchById(menuProductRequest.getProductId());
      final MenuProduct menuProduct = new MenuProduct();

      menuProduct.setProduct(product);
      menuProduct.setQuantity(menuProductRequest.getQuantity());
      menuProducts.add(menuProduct);
    }

    return menuProducts;
  }

  private void validateProductSize(final List<MenuProduct> menuProductRequests,
      final List<UUID> productIds) {
    final List<Product> products = productRepository.findAllById(productIds);

    if (products.size() != menuProductRequests.size()) {
      throw new IllegalArgumentException();
    }
  }
}
