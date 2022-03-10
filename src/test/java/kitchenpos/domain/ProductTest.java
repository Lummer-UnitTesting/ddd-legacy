package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import kitchenpos.mocker.ProductMocker.ProductMockBuilder;
import org.junit.jupiter.api.Test;

public class ProductTest {

  @Test
  void 제품의_가격은_반드시_0_이상의_정수여야한다() {
    Product sut = new ProductMockBuilder()
        .price(BigDecimal.valueOf(-1))
        .build();

    assertThat(sut.hasInvalidPrice()).isTrue();
  }
}
