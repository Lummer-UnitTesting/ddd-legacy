package kitchenpos.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MenuTest {

  private static final MenuGroup menuGroup = new MenuGroup("야식");

  private Menu defaultMenu;
  private final List<MenuProduct> menuProducts = new ArrayList<>();

  @BeforeEach
  void setup() {
    final Product product = new Product("닭발", BigDecimal.valueOf(18000));
    menuProducts.add(new MenuProduct(product, 1));

    defaultMenu = new Menu("A세트", BigDecimal.valueOf(1000), menuGroup, true, menuProducts);
  }

  @Test
  void 메뉴_생성() {
    // given
    final String name = "A세트";
    final boolean displayed = true;
    final BigDecimal price = BigDecimal.valueOf(1000);

    // when
    Menu menu = new Menu(name, price, menuGroup, displayed, menuProducts);

    // then
    assertEquals(price, menu.getPrice());
    assertEquals(name, menu.getName());
    assertEquals(displayed, menu.isDisplayed());
    assertEquals(menuGroup, menu.getMenuGroup());
    assertEquals(menuGroup.getId(), menu.getMenuGroupId());
    assertEquals(menuProducts, menu.getMenuProducts());
  }

  @Test
  void 메뉴이름은_null이_아닌_값이다() {
    // given
    final String name = null;
    final BigDecimal price = BigDecimal.valueOf(1000);

    // when

    // then
    assertThrows(IllegalArgumentException.class, () ->
        new Menu(name, price, menuGroup, true, menuProducts));
  }

  @Test
  void 메뉴가격은_null이_아닌_값이다() {
    // given
    final BigDecimal illegalPrice = null;

    // when

    // then
    assertThrows(IllegalArgumentException.class, () ->
        new Menu("A세트", illegalPrice, menuGroup, true, menuProducts));
  }

  @Test
  void 메뉴에_등록되는_상품은_반드시_존재해야한다() {
    // given
    final List<MenuProduct> emptyMenuProduct = new ArrayList<>();
    final List<MenuProduct> nullMenuProduct = null;
    BigDecimal price = BigDecimal.valueOf(1000);

    // when

    // then
    assertThrows(IllegalArgumentException.class, () ->
        new Menu("A세트", price, menuGroup, true, emptyMenuProduct));
    assertThrows(IllegalArgumentException.class, () ->
        new Menu("A세트", price, menuGroup, true, nullMenuProduct));
  }

  @Test
  void 메뉴의_가격은_0_이상의_양수이다() {
    // given
    final BigDecimal zero = BigDecimal.valueOf(0);
    final BigDecimal illegalPrice = BigDecimal.valueOf(-1000);
    List<MenuProduct> menuProducts = defaultMenu.getMenuProducts();

    // when
    Menu menu = new Menu("A세트", zero, menuGroup, true, menuProducts);

    // then
    assertEquals(zero, menu.getPrice());
    assertThrows(IllegalArgumentException.class, () ->
        new Menu("A세트", illegalPrice, menuGroup, true, menuProducts));
  }

  @Test
  void 메뉴의_가격을_변경할_수_있다() {
    // given
    Menu menu = new Menu(defaultMenu);

    BigDecimal negativePrice = BigDecimal.valueOf(-1000);
    BigDecimal positivePrice = BigDecimal.valueOf(1000);

    // when
    menu.changePrice(positivePrice);

    // then
    assertEquals(positivePrice, menu.getPrice());
    assertThrows(IllegalArgumentException.class, () -> menu.changePrice(negativePrice));
  }

  @Test
  void 메뉴의_display_상태를_변경할_수_있다() {
    // given
    Menu displayedMenu = new Menu(defaultMenu);
    Menu hiddenMenu = new Menu(defaultMenu);

    // when
    displayedMenu.display();
    hiddenMenu.hide();

    // then
    assertTrue(displayedMenu.isDisplayed());
    assertFalse(hiddenMenu.isDisplayed());
  }

  @Test
  void 메뉴의_가격은_메뉴상품의_가격총합보다_작아야_한다() {
    // given
    final BigDecimal tooBigPrice = BigDecimal.valueOf(50000);
    Menu menu = new Menu(defaultMenu);

    // when

    // then
    assertThrows(IllegalArgumentException.class, () ->
        new Menu("A세트", tooBigPrice, menuGroup, true, menuProducts));
    assertThrows(IllegalArgumentException.class, () -> menu.changePrice(tooBigPrice));
  }
}
