package kitchenpos.domain.fixture;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderType;

public class OrderFixture {

  private static final String DELIVERY_ADDRESS = "서울시 강남구 도산대로 155";

  public static Order createEatIn() {
    Order order = new Order();

    order.setType(OrderType.EAT_IN);
    order.setStatus(OrderStatus.WAITING);
    order.setOrderDateTime(LocalDateTime.now());
    order.setOrderLineItems(List.of(OrderLineItemFixture.create()));

    OrderTable orderTable = new OrderTable("1번");
    order.setOrderTable(orderTable);
    order.setOrderTableId(orderTable.getId());

    return order;
  }

  public static Order createDelivery() {
    Order order = new Order();

    order.setType(OrderType.DELIVERY);
    order.setStatus(OrderStatus.WAITING);
    order.setOrderDateTime(LocalDateTime.now());
    order.setOrderLineItems(List.of(OrderLineItemFixture.create()));

    order.setDeliveryAddress(DELIVERY_ADDRESS);
    return order;
  }
}
