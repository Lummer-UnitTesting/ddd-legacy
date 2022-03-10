package kitchenpos.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import kitchenpos.infra.PurgomalumClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MenuGroupServiceTest {
  private static final Product product = new Product("닭발", BigDecimal.valueOf(18000));

  @InjectMocks
  private MenuGroupService menuGroupService;

  @Mock
  private MenuGroupRepository menuGroupRepository;

//  @Mock
//  private ProductService productService;
//
//  @Mock
//  private ProductRepository productRepository;
//
//  @Mock
//  private PurgomalumClient purgomalumClient;
//
//  @Mock
//  private MenuGroupService menuGroupService;
//
//  private Menu defaultMenu;

  @BeforeEach
  void setup() {
  }

  @Test
  void 메뉴그룹_생성() {
    // given
    final String groupName = "시즌메뉴";
    MenuGroup menuGroup = new MenuGroup(groupName);

    doReturn(menuGroup).when(menuGroupRepository).save(any(MenuGroup.class));

    // when
    menuGroupService.create(menuGroup);

    // then
    assertEquals(groupName, menuGroup.getName());
  }

  @Test
  void 메뉴그룹_id로_찾기() {
    // given
    final String groupName = "시즌메뉴";
    MenuGroup menuGroup = new MenuGroup(groupName);

    doReturn(Optional.of(menuGroup)).when(menuGroupRepository).findById(menuGroup.getId());

    // when
    MenuGroup fetchedMenu = menuGroupService.fetchById(menuGroup.getId());

    // then
    assertEquals(menuGroup.getId(), fetchedMenu.getId());
  }
}
