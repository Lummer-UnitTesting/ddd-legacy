package kitchenpos.domain;

import java.util.Objects;
import java.util.stream.Collectors;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
@Table(name = "menu")
@Entity
public class Menu {
    @Column(name = "id", columnDefinition = "varbinary(16)")
    @Id
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @ManyToOne(optional = false)
    @JoinColumn(
        name = "menu_group_id",
        columnDefinition = "varbinary(16)",
        foreignKey = @ForeignKey(name = "fk_menu_to_menu_group")
    )
    private MenuGroup menuGroup;

    @Column(name = "displayed", nullable = false)
    private boolean displayed;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(
        name = "menu_id",
        nullable = false,
        columnDefinition = "varbinary(16)",
        foreignKey = @ForeignKey(name = "fk_menu_product_to_menu")
    )
    private List<MenuProduct> menuProducts;

    @Transient
    private UUID menuGroupId;

    public Menu() {
    }

    public Menu(String name, BigDecimal price, MenuGroup menuGroup,
        boolean displayed, List<MenuProduct> menuProducts) {
        validate(price, menuProducts, name);

        this.id = UUID.randomUUID();
        this.name = name;
        this.price = price;
        this.displayed = displayed;
        this.menuGroup = menuGroup;
        this.menuGroupId = menuGroup.getId();
        this.menuProducts = menuProducts;
    }

    public Menu(final Menu menu) {
        this(menu.getName(), menu.getPrice(), menu.getMenuGroup(), menu.isDisplayed(),
            menu.getMenuProducts());
    }

    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public void setMenuGroup(final MenuGroup menuGroup) {
        this.menuGroup = menuGroup;
    }

    public boolean isDisplayed() {
        return displayed;
    }

    public void setDisplayed(final boolean displayed) {
        this.displayed = displayed;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public void setMenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public UUID getMenuGroupId() {
        return menuGroupId;
    }

    public void setMenuGroupId(final UUID menuGroupId) {
        this.menuGroupId = menuGroupId;
    }

    public void changePrice(BigDecimal price) {
        validatePrice(price);
        validatePrice(price, menuProducts);
        this.price = price;
    }

    public void display() {
        this.displayed = true;
    }

    public void hide() {
        this.displayed = false;
    }

    public List<UUID> mapToProductIds() {
        return menuProducts.stream()
            .map(MenuProduct::getProductId)
            .collect(Collectors.toList());
    }

    private void validate(BigDecimal price, List<MenuProduct> menuProducts, String name) {
        validatePrice(price);
        validateMenuProducts(menuProducts);
        validateName(name);
        validatePrice(price, menuProducts);
    }

    private void validatePrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    private void validateMenuProducts(List<MenuProduct> menuProducts) {
        if (Objects.isNull(menuProducts) || menuProducts.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private void validateName(String name) {
        if (Objects.isNull(name)) {
            throw new IllegalArgumentException();
        }
    }

    private void validatePrice(BigDecimal price, List<MenuProduct> menuProducts) {
        BigDecimal sum = calculateMenuPrice(menuProducts);

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
    }

    private BigDecimal calculateMenuPrice(List<MenuProduct> menuProducts) {
        return menuProducts.stream()
            .map(MenuProduct::calculateTotalPrice)
            .reduce((sum, menuProductPrice) -> sum = sum.add(menuProductPrice))
            .orElse(BigDecimal.ZERO);
    }
}
