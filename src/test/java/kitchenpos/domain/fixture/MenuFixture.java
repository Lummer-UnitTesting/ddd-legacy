package kitchenpos.domain.fixture;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;

public class MenuFixture {
  private static final String MENU_NAME = "A세트";
  private static final BigDecimal MENU_PRICE = BigDecimal.valueOf(10000);

  public static Menu create() {
    return new Menu(MENU_NAME, MENU_PRICE, MenuGroupFixture.create(), true,
        List.of(MenuProductFixture.create(1)));
  }
}
