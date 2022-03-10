package kitchenpos.mocker;

import java.math.BigDecimal;
import kitchenpos.domain.OrderLineItem;

public class OrderLineItemMocker {
  public static OrderLineItem create() {
    return OrderLineItem.builder()
        .price(BigDecimal.ONE)
        .build();
  }

}
