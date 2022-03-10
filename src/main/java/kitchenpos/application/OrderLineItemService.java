package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderType;
import org.springframework.stereotype.Service;

@Service
public class OrderLineItemService {

  private final MenuService menuService;

  public OrderLineItemService(
      final MenuService menuService
  ) {
    this.menuService = menuService;
  }

  public List<OrderLineItem> create(List<OrderLineItem> orderLineItemRequests, OrderType type) {
    if (orderLineItemRequests == null) {
      throw new IllegalArgumentException();
    }
    return orderLineItemRequests.stream()
        .map(orderLineItem -> create(orderLineItem, type))
        .collect(Collectors.toList());
  }

  private OrderLineItem create(OrderLineItem orderLineItemRequest, OrderType type) {
    final long quantity = orderLineItemRequest.getQuantity();
    if (type != OrderType.EAT_IN && quantity < 0) {
      throw new IllegalArgumentException();
    }

    final Menu menu = menuService.findDisplayedById(orderLineItemRequest.getMenuId());
    if (menu.getPrice().compareTo(orderLineItemRequest.getPrice()) != 0) {
      throw new IllegalArgumentException();
    }

    return OrderLineItem.builder()
        .menu(menu)
        .quantity(quantity)
        .build();
  }
}
