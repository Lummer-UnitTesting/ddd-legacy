package kitchenpos.mocker;

import java.math.BigDecimal;
import java.util.UUID;
import kitchenpos.domain.Product;

public class ProductMocker {
  public static Product create() {
    return Product.builder()
        .id(UUID.randomUUID())
        .name("dummy")
        .price(BigDecimal.ONE)
        .build();
  }

  public static class ProductMockBuilder {
    private final Product.ProductBuilder builder;

    public ProductMockBuilder() {
      this.builder = Product.builder()
          .id(UUID.randomUUID())
          .name("dummy")
          .price(BigDecimal.ONE);
    }

    public ProductMockBuilder name(String name) {
      this.builder.name(name);
      return this;
    }

    public ProductMockBuilder price(BigDecimal price) {
      this.builder.price(price);
      return this;
    }

    public Product build() {
      return this.builder.build();
    }
  }
}
