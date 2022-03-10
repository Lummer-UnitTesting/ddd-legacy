package kitchenpos.mocker;

import java.util.UUID;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderType;

public class OrderMocker {
  public static Order create() {
    return Order.builder()
        .type(OrderType.EAT_IN)
        .build();
  }

  public static Order createWithType(OrderType type) {
    return Order.builder()
        .type(type)
        .build();
  }

  public static class OrderMockBuilder {
    private final Order.OrderBuilder builder;

    public OrderMockBuilder() {
      this.builder = Order.builder()
          .id(UUID.randomUUID());
    }

    public Order build() {
      return this.builder.build();
    }
  }
}
