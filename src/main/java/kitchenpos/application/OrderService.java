package kitchenpos.application;

import static kitchenpos.domain.OrderType.EAT_IN;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderType;
import kitchenpos.infra.KitchenridersClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderLineItemService orderLineItemService;
    private final OrderTableService orderTableService;
    private final KitchenridersClient kitchenridersClient;

    public OrderService(
        final OrderRepository orderRepository,
        final OrderLineItemService orderLineItemService,
        final OrderTableService orderTableService,
        final KitchenridersClient kitchenridersClient
    ) {
        this.orderRepository = orderRepository;
        this.orderLineItemService = orderLineItemService;
        this.orderTableService = orderTableService;
        this.kitchenridersClient = kitchenridersClient;
    }

    @Transactional
    public Order create(final Order request) {
        final OrderType type = request.getType();
        final List<OrderLineItem> orderLineItems =
            orderLineItemService.create(request.getOrderLineItems(), type);

        Order order = Order.builder()
            .id(UUID.randomUUID())
            .type(type)
            .deliveryAddress(request.getDeliveryAddress())
            .status(OrderStatus.WAITING)
            .orderDateTime(LocalDateTime.now())
            .orderLineItems(orderLineItems)
            .build();

        if(type == EAT_IN) {
            final OrderTable orderTable =
                orderTableService.findNotEmptyById(request.getOrderTableId());
            order.setOrderTable(orderTable);
        }

        return orderRepository.save(order);
    }

    @Transactional
    public Order accept(final UUID orderId) {
        final Order order = fetchById(orderId);

        if (order.getType() == OrderType.DELIVERY) {
            BigDecimal sum = order.calculateSum();
            kitchenridersClient.requestDelivery(orderId, sum, order.getDeliveryAddress());
        }

        return order.changeToNextStatus(OrderStatus.ACCEPTED);
    }

    @Transactional
    public Order serve(final UUID orderId) {
        final Order order = fetchById(orderId);
        return order.changeToNextStatus(OrderStatus.SERVED);
    }

    @Transactional
    public Order startDelivery(final UUID orderId) {
        final Order order = fetchById(orderId);
        return order.changeToNextStatus(OrderStatus.DELIVERING);
    }

    @Transactional
    public Order completeDelivery(final UUID orderId) {
        final Order order = fetchById(orderId);
        return order.changeToNextStatus(OrderStatus.DELIVERED);
    }

    @Transactional
    public Order complete(final UUID orderId) {
        final Order order = fetchById(orderId);

        if (order.getType() == OrderType.EAT_IN) {
            order.getOrderTable().clear();
        }

        return order.changeToNextStatus(OrderStatus.COMPLETED);
    }

    @Transactional(readOnly = true)
    public Order fetchById(final UUID orderId) {
        return orderRepository.findById(orderId).orElseThrow(NoSuchElementException::new);
    }

    @Transactional(readOnly = true)
    public List<Order> findAll() {
        return orderRepository.findAll();
    }
}
