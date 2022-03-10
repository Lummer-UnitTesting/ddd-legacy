package kitchenpos.domain;

import kitchenpos.mocker.OrderTableMocker;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class OrderTableTest {

  @Test
  void 손님이_앉는_경우에는_공석이_아니다() {
    OrderTable sut = OrderTableMocker.create();
    sut.sit();

    Assertions.assertThat(sut.isEmpty()).isFalse();
  }

  @Test
  void 테이블은_다음_손님이_앉을_수_있도록_치워야한다() {
    OrderTable sut = OrderTableMocker.create();
    sut.clear();

    Assertions.assertThat(sut.isEmpty()).isTrue();
    Assertions.assertThat(sut.getNumberOfGuests()).isEqualTo(0);
  }
}
