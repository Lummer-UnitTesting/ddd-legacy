package kitchenpos.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.EnumSource.Mode.EXCLUDE;

import kitchenpos.domain.fixture.OrderFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

public class OrderTest {

  private static Order eatInOrder;
  private static Order deliveryOrder;

  @BeforeEach
  void setup() {
    eatInOrder = OrderFixture.createEatIn();
    deliveryOrder = OrderFixture.createDelivery();
  }

  @Test
  void 주문의_초기_상태는_WAITING이다() {
    assertEquals(OrderStatus.WAITING, eatInOrder.getStatus());
    assertEquals(OrderStatus.WAITING, deliveryOrder.getStatus());
  }

  @ParameterizedTest
  @EnumSource(
      value = OrderStatus.class,
      names = {"ACCEPTED"},
      mode = EXCLUDE
  )
  void 주문을_수락할_때_WAITING_상태여야한다(OrderStatus orderStatus) {
    assertThrows(IllegalStateException.class, () -> eatInOrder.changeToNextStatus(orderStatus));
    assertThrows(IllegalStateException.class, () -> deliveryOrder.changeToNextStatus(orderStatus));
  }

  @Test
  void 주문을_수락할_수_있다() {
    eatInOrder.changeToNextStatus(OrderStatus.ACCEPTED);
    deliveryOrder.changeToNextStatus(OrderStatus.ACCEPTED);

    assertEquals(OrderStatus.ACCEPTED, eatInOrder.getStatus());
    assertEquals(OrderStatus.ACCEPTED, deliveryOrder.getStatus());
  }

  @ParameterizedTest
  @EnumSource(
      value = OrderStatus.class,
      names = {"SERVED"},
      mode = EXCLUDE
  )
  void 주문을_서빙할_때_ACCEPTED_상태여야한다(OrderStatus orderStatus) {
    eatInOrder.setStatus(OrderStatus.ACCEPTED);
    deliveryOrder.setStatus(OrderStatus.ACCEPTED);

    assertThrows(IllegalStateException.class, () -> eatInOrder.changeToNextStatus(orderStatus));
    assertThrows(IllegalStateException.class, () -> deliveryOrder.changeToNextStatus(orderStatus));
  }

  @Test
  void 주문을_서빙할_수_있다() {
    // given
    eatInOrder.setStatus(OrderStatus.ACCEPTED);
    deliveryOrder.setStatus(OrderStatus.ACCEPTED);

    // when
    eatInOrder.changeToNextStatus(OrderStatus.SERVED);
    deliveryOrder.changeToNextStatus(OrderStatus.SERVED);

    // then
    assertEquals(OrderStatus.SERVED, eatInOrder.getStatus());
    assertEquals(OrderStatus.SERVED, deliveryOrder.getStatus());
  }

  @ParameterizedTest
  @EnumSource(
      value = OrderStatus.class,
      names = {"DELIVERING"},
      mode = EXCLUDE
  )
  void 배달을_시작할_때_SERVED_상태여야한다(OrderStatus orderStatus) {
    deliveryOrder.setStatus(OrderStatus.SERVED);

    assertThrows(IllegalStateException.class, () -> deliveryOrder.changeToNextStatus(orderStatus));
  }

  @Test
  void 배달을_시작할_수_있다() {
    // given
    deliveryOrder.setStatus(OrderStatus.SERVED);

    // when
    deliveryOrder.changeToNextStatus(OrderStatus.DELIVERING);

    // then
    assertEquals(OrderStatus.DELIVERING, deliveryOrder.getStatus());
  }

  @ParameterizedTest
  @EnumSource(
      value = OrderStatus.class,
      names = {"DELIVERED"},
      mode = EXCLUDE
  )
  void 배달을_완료할_때_DELIVERING_상태여야한다(OrderStatus orderStatus) {
    deliveryOrder.setStatus(OrderStatus.DELIVERING);

    assertThrows(IllegalStateException.class, () -> deliveryOrder.changeToNextStatus(orderStatus));
  }

  @Test
  void 배달을_완료할_수_있다() {
    // given
    deliveryOrder.setStatus(OrderStatus.DELIVERING);

    // when
    deliveryOrder.changeToNextStatus(OrderStatus.DELIVERED);

    // then
    assertEquals(OrderStatus.DELIVERED, deliveryOrder.getStatus());
  }

  @ParameterizedTest
  @EnumSource(
      value = OrderStatus.class,
      names = {"COMPLETED"},
      mode = EXCLUDE
  )
  void 주문을_완료할_때_DELIVERED_상태여야한다(OrderStatus orderStatus) {
    deliveryOrder.setStatus(OrderStatus.DELIVERED);

    assertThrows(IllegalStateException.class, () -> deliveryOrder.changeToNextStatus(orderStatus));
  }

  @ParameterizedTest
  @EnumSource(
      value = OrderStatus.class,
      names = {"COMPLETED"},
      mode = EXCLUDE
  )
  void 주문을_완료할_때_SERVED_상태여야한다(OrderStatus orderStatus) {
    eatInOrder.setStatus(OrderStatus.SERVED);

    assertThrows(IllegalStateException.class, () -> eatInOrder.changeToNextStatus(orderStatus));
  }

  @Test
  void 주문을_완료할_수_있다() {
    // given
    eatInOrder.setStatus(OrderStatus.SERVED);
    deliveryOrder.setStatus(OrderStatus.DELIVERED);

    // when
    eatInOrder.changeToNextStatus(OrderStatus.COMPLETED);
    deliveryOrder.changeToNextStatus(OrderStatus.COMPLETED);

    // then
    assertEquals(OrderStatus.COMPLETED, eatInOrder.getStatus());
    assertEquals(OrderStatus.COMPLETED, deliveryOrder.getStatus());
  }
}
