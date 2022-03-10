package kitchenpos.application;

import java.util.NoSuchElementException;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroup create(final MenuGroup request) {
        final MenuGroup menuGroup = new MenuGroup(request.getName());
        return menuGroupRepository.save(menuGroup);
    }

    public MenuGroup fetchById(UUID id) {
        return menuGroupRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Transactional(readOnly = true)
    public List<MenuGroup> findAll() {
        return menuGroupRepository.findAll();
    }
}
