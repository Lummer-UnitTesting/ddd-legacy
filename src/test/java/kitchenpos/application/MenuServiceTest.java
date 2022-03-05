package kitchenpos.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Product;
import kitchenpos.infra.PurgomalumClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

  private static final MenuGroup menuGroup = new MenuGroup("야식");
  private static final Product product = new Product("닭발", BigDecimal.valueOf(18000));

  @InjectMocks
  private MenuService menuService;

  @Mock
  private MenuRepository menuRepository;

  @Mock
  private PurgomalumClient purgomalumClient;

  @Mock
  private MenuProductService menuProductService;

  @Mock
  private MenuGroupService menuGroupService;

  private Menu defaultMenu;

  private final List<MenuProduct> menuProducts = new ArrayList<>();

  @BeforeEach
  void setup() {
    menuProducts.add(new MenuProduct(product, 1));

    defaultMenu = new Menu("A세트", BigDecimal.valueOf(1000), menuGroup, true, menuProducts);
  }

  @Test
  void 메뉴_생성() {
    // given
    final Menu menu = new Menu(defaultMenu);

    doReturn(menuGroup).when(menuGroupService).fetchById(menuGroup.getId());
    doReturn(menuProducts).when(menuProductService).create(menu.getMenuProducts(), menu.mapToProductIds());
    doReturn(new Menu(menu)).when(menuRepository).save(any(Menu.class));

    // when
    final Menu createdMenu = menuService.create(menu);

    // then
    assertNotEquals(menu.getId(), createdMenu.getId());
    assertEquals(menu.getName(), createdMenu.getName());
    assertEquals(menu.getMenuGroup(), createdMenu.getMenuGroup());
    assertEquals(menu.getPrice(), createdMenu.getPrice());
    assertEquals(menu.getMenuProducts(), createdMenu.getMenuProducts());
  }

  @Test
  void 메뉴의_이름은_비속어가_포함되면_안된다() {
    // given
    final Menu menu = new Menu("해머천재", BigDecimal.valueOf(1000), menuGroup, true, menuProducts);
    doReturn(true).when(purgomalumClient).containsProfanity("해머천재");

    // when

    // then
    assertThrows(IllegalArgumentException.class, () -> menuService.create(menu));
  }

  @Test
  void 메뉴_가격_수정() {
    // given
    final Menu menu = new Menu("A세트", BigDecimal.valueOf(0), menuGroup, true, menuProducts);
    doReturn(Optional.of(defaultMenu)).when(menuRepository).findById(defaultMenu.getId());

    // when
    final Menu changedPrice = menuService.changePrice(defaultMenu.getId(), menu);

    // then
    assertEquals(BigDecimal.valueOf(0), changedPrice.getPrice());
  }

  @Test
  void 메뉴를_display_상태로_변경() {
    // given
    final Menu menu = new Menu(defaultMenu);
    doReturn(Optional.of(menu)).when(menuRepository).findById(menu.getId());

    // when
    final Menu displayed = menuService.display(menu.getId());

    // then
    assertTrue(displayed.isDisplayed());
  }

  @Test
  void 메뉴를_hide_상태로_변경() {
    // given
    final Menu menu = new Menu(defaultMenu);
    doReturn(Optional.of(menu)).when(menuRepository).findById(menu.getId());

    // when
    final Menu displayed = menuService.hide(menu.getId());

    // then
    assertFalse(displayed.isDisplayed());
  }
}
