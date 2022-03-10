package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.WAITING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doReturn;

import java.time.LocalDateTime;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.OrderType;
import kitchenpos.infra.KitchenridersClient;
import kitchenpos.mocker.OrderMocker;
import kitchenpos.mocker.OrderTableMocker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

  @Mock
  private OrderRepository orderRepository;
  @Mock
  private MenuRepository menuRepository;
  @Mock
  private OrderTableRepository orderTableRepository;
  @Mock
  private KitchenridersClient kitchenridersClient;
  @Spy
  private OrderLineItemService orderLineItemService;
  @InjectMocks
  private OrderService service;


  @BeforeEach
  void setUp() {
  }

  @ParameterizedTest
  @EnumSource(OrderType.class)
  void 주문_유형은_세_가지_존재한다(OrderType type) {
    // 주문 유형은 테이크아웃, 배달, 매장 내 식사가 있으며, 주문 유형이 없을 경우 예외를 발생시킨다.
    Order sut = OrderMocker.createWithType(type);

    Order created = service.create(sut);

    assertThat(created.getType()).isEqualTo(sut.getType());
  }

  @Test
  void 기본_생성_시나리오_테스트() {
    Order sut = OrderMocker.create();
    LocalDateTime beforeCreated = LocalDateTime.now();

    Order created = service.create(sut);

    assertThat(created.getType()).isEqualTo(sut.getType());
    assertThat(created.getId()).isEqualTo(sut.getId());
    assertThat(created.getOrderLineItems()).isEqualTo(sut.getOrderLineItems());
    assertThat(created.getStatus()).isEqualTo(WAITING);
    assertThat(beforeCreated.isBefore(created.getOrderDateTime())).isTrue();
  }

  @Test
  void 주문_메뉴의_목록은_비어있을_수_없다() {
    // 주문 유형은 테이크아웃, 배달, 매장 내 식사가 있으며, 주문 유형이 없을 경우 예외를 발생시킨다.
    Order sut = OrderMocker.create();
    sut.setOrderLineItems(null);

    assertThatThrownBy(() -> service.create(sut))
        .isExactlyInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void 주문_메뉴_목록이_현재_진열되어있지_않으면_에러를_내뱉는다() {

  }

  //주문 유형이 배달일 때, 배달 받을 주소가 비어있을 경우, 예외를 발생시킨다.
  @Test
  void testCreate5() {
    Order sut = OrderMocker.createWithType(OrderType.DELIVERY);
    sut.setDeliveryAddress(null);

    assertThatThrownBy(() -> service.create(sut))
        .isExactlyInstanceOf(IllegalArgumentException.class);
  }

  // 주문 유형이 매장 내 식사일 때, 매장 내 테이블 번호(order_table_id)는 필수로 입력해야 한다.
  @Test
  void testCreate6() {
    Order sut = OrderMocker.createWithType(OrderType.EAT_IN);
    sut.setOrderTableId(null);

    assertThatThrownBy(() -> service.create(sut))
        .isExactlyInstanceOf(IllegalArgumentException.class);
  }

  //주문 전에 미리 테이블을 배정받아야 한다.
  void testCreate7() {
    Order sut = OrderMocker.createWithType(OrderType.EAT_IN);
    OrderTable orderTable = OrderTableMocker.create();
    sut.setOrderTableId(orderTable.getId());

    assertThatThrownBy(() -> service.create(sut))
        .isExactlyInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void 주문은_고유한_아이디로_구분한다() {
    Order order1 = OrderMocker.create();
    Order order2 = OrderMocker.create();
    doReturn(order1).when(orderRepository).save(any());

    service.create(order1);
    service.create(order2);

    assertThat(order1.getId()).isNotEqualTo(order2.getId());
  }
}
