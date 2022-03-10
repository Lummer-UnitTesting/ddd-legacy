package kitchenpos.mocker;

import kitchenpos.domain.Menu;

public class MenuMocker {
  public static Menu create() {
    return Menu.builder()
        .build();
  }

}
