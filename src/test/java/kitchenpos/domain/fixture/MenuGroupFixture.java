package kitchenpos.domain.fixture;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFixture {
  private static final String MENU_GROUP_NAME = "야식";

  public static MenuGroup create(final String name) {
    return new MenuGroup(name);
  }

  public static MenuGroup create() {
    return new MenuGroup(MENU_GROUP_NAME);
  }
}
