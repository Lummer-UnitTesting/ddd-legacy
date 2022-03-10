package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import kitchenpos.domain.Product;
import kitchenpos.mocker.ProductMocker;
import kitchenpos.mocker.ProductMocker.ProductMockBuilder;
import kitchenpos.domain.ProductRepository;
import kitchenpos.infra.PurgomalumClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

  @Mock
  private ProductRepository repository;

  @Mock
  private PurgomalumClient purgomalumClient;

  @Spy
  private MenuService menuService;

  @InjectMocks
  private ProductService service;


  @BeforeEach
  void setUp() {
  }

  @Test
  void 비속어는_이름에_포함될_수_없다() {
    Product containsProfanity = new Product();
    containsProfanity.setName("비속어");

    Mockito.doReturn(true).when(purgomalumClient).containsProfanity(Mockito.eq("비속어"));

    assertThatThrownBy(() -> service.create(containsProfanity))
        .isExactlyInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void 가격은_음수일_수_없다() {
    Product invalidPrice = new ProductMockBuilder()
        .price(BigDecimal.valueOf(-1))
        .build();

    assertThatThrownBy(() -> service.create(invalidPrice))
        .isExactlyInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void 등록된_상품을_조회할_수_있다() {
    List<Product> list = List.of(ProductMocker.create());

    Mockito.doReturn(list).when(repository).findAll();

    assertThat(service.findAll()).isEqualTo(list);
  }

  @Test
  void 상품을_못_찾을_경우_예외를_발생시킨다() {
    assertThatThrownBy(() -> service.fetchById(UUID.randomUUID()))
        .isExactlyInstanceOf(IllegalArgumentException.class);
  }
}
