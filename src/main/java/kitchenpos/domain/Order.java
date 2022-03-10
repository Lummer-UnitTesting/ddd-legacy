package kitchenpos.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
@Table(name = "orders")
@Entity
public class Order {
    @Column(name = "id", columnDefinition = "varbinary(16)")
    @Id
    private UUID id;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderType type;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "order_date_time", nullable = false)
    private LocalDateTime orderDateTime;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(
        name = "order_id",
        nullable = false,
        columnDefinition = "varbinary(16)",
        foreignKey = @ForeignKey(name = "fk_order_line_item_to_orders")
    )
    private List<OrderLineItem> orderLineItems;

    @Column(name = "delivery_address")
    private String deliveryAddress;

    @ManyToOne
    @JoinColumn(
        name = "order_table_id",
        columnDefinition = "varbinary(16)",
        foreignKey = @ForeignKey(name = "fk_orders_to_order_table")
    )
    private OrderTable orderTable;

    @Transient
    private UUID orderTableId;

    public Order() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public OrderType getType() {
        return type;
    }

    public void setType(final OrderType type) {
        validateType(type);
        this.type = type;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(final OrderStatus status) {
        this.status = status;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public void setOrderDateTime(final LocalDateTime orderDateTime) {
        this.orderDateTime = orderDateTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public void setOrderLineItems(final List<OrderLineItem> orderLineItems) {
        validateOrderLineItems(orderLineItems);
        this.orderLineItems = orderLineItems;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(final String deliveryAddress) {
        validDeliveryAddress(deliveryAddress);
        this.deliveryAddress = deliveryAddress;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public void setOrderTable(final OrderTable orderTable) {
        this.orderTable = orderTable;
    }

    public UUID getOrderTableId() {
        return orderTableId;
    }

    public void setOrderTableId(final UUID orderTableId) {
        this.orderTableId = orderTableId;
    }

    public BigDecimal calculateSum() {
        BigDecimal sum = BigDecimal.ZERO;

        for (final OrderLineItem orderLineItem : orderLineItems) {
            sum = sum.add(orderLineItem.calculateTotalPrice());
        }

        return sum;
    }

    public Order changeToNextStatus(OrderStatus nextStatus) {
        boolean isDelivery = type == OrderType.DELIVERY;

        if (!nextStatus.equals(OrderStatus.nextStatus(status, isDelivery))) {
            throw new IllegalStateException();
        }

        this.status = nextStatus;

        return this;
    }

    private void validDeliveryAddress(String deliveryAddress) {
        if (type == OrderType.DELIVERY) {
            throw new IllegalArgumentException();
        }

        if (Objects.isNull(deliveryAddress) || deliveryAddress.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private void validateType(final OrderType type) {
        if (Objects.isNull(type)) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderLineItems(final List<OrderLineItem> orderLineItems) {
        if (Objects.isNull(orderLineItems) || orderLineItems.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }
}
