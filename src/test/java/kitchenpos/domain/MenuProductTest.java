package kitchenpos.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

public class MenuProductTest {

  private static final Product product = new Product("닭발", BigDecimal.valueOf(18000));

  @Test
  void 메뉴상품_생성() {
    // given
    final long quantity = 1;

    // when
    final MenuProduct menuProduct = new MenuProduct(product, quantity);

    // then
    assertEquals(product, menuProduct.getProduct());
    assertEquals(quantity, menuProduct.getQuantity());
  }

  @Test
  void 메뉴상품의_수량은_0이상이다() {
    // given
    final long quantity = 0;
    final long illegalQuantity = -1;

    // when
    final MenuProduct menuProduct = new MenuProduct(product, quantity);

    // then
    assertEquals(quantity, menuProduct.getQuantity());
    assertThrows(IllegalArgumentException.class, () -> new MenuProduct(product, illegalQuantity));
  }

  @Test
  void 메뉴상품의_가격은_상품의_수량x가격이다() {
    // given
    final BigDecimal productPrice = product.getPrice();
    final long quantity = 1;
    final BigDecimal totalPrice = productPrice.multiply(BigDecimal.valueOf(quantity));

    // when
    final MenuProduct menuProduct = new MenuProduct(product, quantity);

    // then
    assertEquals(totalPrice, menuProduct.calculateTotalPrice());
  }
}
