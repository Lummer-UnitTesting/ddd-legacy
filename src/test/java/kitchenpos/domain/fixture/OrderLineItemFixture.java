package kitchenpos.domain.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.OrderLineItem;

public class OrderLineItemFixture {
  public static OrderLineItem create() {
    final Menu menu = MenuFixture.create();
    return new OrderLineItem(menu, 1, menu.getPrice());
  }
}
