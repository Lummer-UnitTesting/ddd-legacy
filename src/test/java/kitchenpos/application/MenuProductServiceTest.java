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
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
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
public class MenuProductServiceTest {

  private static final Product product = new Product("닭발", BigDecimal.valueOf(18000));

  @InjectMocks
  private MenuProductService menuProductService;

  @Mock
  private MenuRepository menuRepository;

  @Mock
  private ProductService productService;

  @Mock
  private ProductRepository productRepository;

  @Mock
  private PurgomalumClient purgomalumClient;

  @Mock
  private MenuGroupService menuGroupService;

  private Menu defaultMenu;

  private final List<MenuProduct> defaultMenuProducts = new ArrayList<>();
  private final MenuProduct defaultMenuProduct = new MenuProduct(product, 1);
  @BeforeEach
  void setup() {
    defaultMenuProducts.add(defaultMenuProduct);
  }

  @Test
  void 메뉴제품_생성() {
    // given
    final List<MenuProduct> menuProducts = new ArrayList<>(defaultMenuProducts);
    final List<UUID> productIds = List.of(product.getId());

    doReturn(product).when(productService).fetchById(defaultMenuProduct.getProductId());
    doReturn(List.of(product)).when(productRepository).findAllById(productIds);

    // when
    final List<MenuProduct> createdMenuProduct = menuProductService.create(menuProducts,
        productIds);

    // then
    assertEquals(1, createdMenuProduct.size());
    assertNotEquals(product.getId(), createdMenuProduct.get(0).getProductId());
    assertEquals(product, createdMenuProduct.get(0).getProduct());
    assertEquals(1, createdMenuProduct.get(0).getQuantity());
  }
}
