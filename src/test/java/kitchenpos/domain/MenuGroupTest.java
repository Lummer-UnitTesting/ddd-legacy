package kitchenpos.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class MenuGroupTest {

  @Test
  void 메뉴그룹_생성() {
    // given
    final String name = "나혼자 포케";

    // when
    final MenuGroup menuGroup = new MenuGroup(name);

    // then
    assertEquals(name, menuGroup.getName());
  }

  @Test
  void 메뉴그룹의_이름은_null이_아닌_값이다() {
    // given
    final String nullName = null;
    final String name = "포케";

    // when
    MenuGroup menuGroup = new MenuGroup(name);

    // then
    assertEquals(name, menuGroup.getName());
    assertThrows(IllegalArgumentException.class, () -> new MenuGroup(nullName));
  }
}
