package kitchenpos.mocker;

import java.util.UUID;
import kitchenpos.domain.OrderTable;
import org.springframework.lang.Nullable;

public class OrderTableMocker {
  public static OrderTable create() {
    return OrderTable.builder()
        .id(UUID.randomUUID())
        .name("dummy")
        .empty(true)
        .build();
  }

  public static OrderTable createNotEmptyTable() {
    return OrderTable.builder()
        .id(UUID.randomUUID())
        .name("dummy")
        .empty(false)
        .build();
  }


  public static class OrderTableMockBuilder {
    private final OrderTable.OrderTableBuilder builder;

    public OrderTableMockBuilder() {
      this.builder = OrderTable.builder()
          .id(UUID.randomUUID())
          .name("dummy");
    }

    public OrderTableMockBuilder name(@Nullable String name) {
      this.builder.name(name);
      return this;
    }

    public OrderTableMockBuilder numberOfGuests(int numberOfGuests) {
      this.builder.numberOfGuests(numberOfGuests);
      return this;
    }

    public OrderTableMockBuilder empty(boolean empty) {
      this.builder.empty(empty);
      return this;
    }

    public OrderTable build() {
      return this.builder.build();
    }
  }
}
