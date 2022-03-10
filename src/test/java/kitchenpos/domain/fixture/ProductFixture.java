package kitchenpos.domain.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductFixture {
  private static final String PRODUCT_NAME = "닭발";
  private static final BigDecimal PRODUCT_PRICE = BigDecimal.valueOf(18000);

  public static Product create() {
    return new Product(PRODUCT_NAME, PRODUCT_PRICE);
  }
}
