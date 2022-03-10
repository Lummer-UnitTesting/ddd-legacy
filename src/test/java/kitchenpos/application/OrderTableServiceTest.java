package kitchenpos.application;

import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.mocker.OrderTableMocker;
import kitchenpos.mocker.OrderTableMocker.OrderTableMockBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OrderTableServiceTest {

  @Mock
  private OrderRepository orderRepository;

  @Mock
  private OrderTableRepository repository;

  @InjectMocks
  private OrderTableService service;

  @BeforeEach
  void setUp() {

  }

  @Test
  void 테이블에는_이름이_있어야_한다() {
    OrderTable sut = new OrderTableMockBuilder()
        .name(null)
        .build();
    Assertions.assertThatThrownBy(() -> service.create(sut))
        .isExactlyInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void 매장내_테이블은_고유한_아이디로_구분한다() {
    OrderTable table1 = OrderTableMocker.create();
    OrderTable table2 = OrderTableMocker.create();
    Mockito.doReturn(Mockito.any()).when(repository).save(Mockito.any());

    service.create(table1);
    service.create(table2);

    Assertions.assertThat(table1.getId()).isNotEqualTo(table2.getId());
  }

  @Test
  void 테이블에_손님이_앉을_경우_공석이_아니다() {
    OrderTable sut = OrderTableMocker.create();
    Mockito.doReturn(sut).when(repository).findById(sut.getId());
    Mockito.doReturn(sut).when(repository).save(sut);

    service.sit(sut.getId());

    Assertions.assertThat(sut.isEmpty()).isFalse();
  }

  @Test
  void 공석이_아닌_테이블의_손님_수만_변경될_수_있다() {
    OrderTable sut = new OrderTableMockBuilder()
        .empty(false)
        .build();
    Mockito.doReturn(sut).when(repository).findById(sut.getId());

    Assertions.assertThatThrownBy(() -> service.changeNumberOfGuests(sut.getId(), sut))
        .isExactlyInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void 손님_수는_반드시_0_이상이다() {
    OrderTable sut = new OrderTableMockBuilder()
        .numberOfGuests(-1)
        .build();

    Assertions.assertThatThrownBy(() -> service.changeNumberOfGuests(sut.getId(), sut))
        .isExactlyInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void 손님이_식사를_완료한_경우에_테이블을_치운다() {
    OrderTable sut = new OrderTableMockBuilder()
        .numberOfGuests(4)
        .empty(false)
        .build();
    Mockito.doReturn(sut).when(repository).findById(sut.getId());
    Mockito.doReturn(true)
        .when(orderRepository)
        .existsByOrderTableAndStatusNot(sut, OrderStatus.COMPLETED);
    Mockito.doReturn(Mockito.any()).when(repository).save(Mockito.any());

    OrderTable cleared = service.clear(sut.getId());

    Assertions.assertThat(cleared.getNumberOfGuests()).isEqualTo(0);
    Assertions.assertThat(cleared.isEmpty()).isTrue();
  }
}
