package kitchenpos.domain;

public enum OrderStatus {
    WAITING,
    ACCEPTED,
    SERVED,
    DELIVERING,
    DELIVERED,
    COMPLETED;

    public static OrderStatus nextStatus(OrderStatus current, boolean isDelivery) {
        if (isDelivery) {
            if (current == OrderStatus.DELIVERED) {
                return COMPLETED;
            }
        } else {
            if (current == OrderStatus.SERVED) {
                return COMPLETED;
            }
        }

        return OrderStatus.values()[current.ordinal() + 1];
    }
}
