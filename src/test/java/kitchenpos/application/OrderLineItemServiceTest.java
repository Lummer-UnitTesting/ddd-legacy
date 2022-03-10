package kitchenpos.application;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import kitchenpos.domain.Menu;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderType;
import kitchenpos.mocker.OrderLineItemMocker;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.EnumSource.Mode;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OrderLineItemServiceTest {

  @Mock
  private MenuService menuService;

  @InjectMocks
  private OrderLineItemService service;

  @Test
  void 주문_목록에_있는_메뉴는_반드시_조회할_수_있어야_한다() {
    var sut = List.of(OrderLineItemMocker.create());

    doThrow(NoSuchElementException.class).when(menuService).findDisplayedById(Mockito.any());

    Assertions.assertThatThrownBy(() -> service.create(sut, OrderType.EAT_IN))
        .isExactlyInstanceOf(NoSuchElementException.class);
  }

  @ParameterizedTest
  @EnumSource(
      value = OrderType.class,
      names = {"EAT_IN"},
      mode = Mode.EXCLUDE
  )
  void 매장내_식사가_아닌_경우_수량은_반드시_0_이상의_정수여야_한다(OrderType type) {
    OrderLineItem sut = OrderLineItemMocker.create();
    sut.setQuantity(-1);

    Assertions.assertThatThrownBy(() -> service.create(List.of(sut), type))
        .isExactlyInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void 메뉴의_가격과_주문_가격은_반드시_항상_정합성이_유지되어야_한다() {
    OrderLineItem sut = OrderLineItemMocker.create();
    Menu menu = new Menu();
    menu.setPrice(sut.getPrice().add(BigDecimal.ONE));
    doReturn(menu).when(menuService).findDisplayedById(Mockito.any());

    Assertions.assertThatThrownBy(() -> service.create(List.of(sut), OrderType.EAT_IN))
        .isExactlyInstanceOf(IllegalArgumentException.class);
  }
}
