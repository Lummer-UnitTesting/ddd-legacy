package kitchenpos.domain.fixture;

import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

public class MenuProductFixture {
  public static MenuProduct create(final long quantity) {
    Product product = ProductFixture.create();
    return new MenuProduct(product, quantity);
  }
}
