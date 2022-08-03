package WebStoreGroup.WebStore.Characteristic.Entities;

import WebStoreGroup.WebStore.Category.Category;
import WebStoreGroup.WebStore.Product.Product;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Set;

@Entity
public class Characteristic {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "characteristic_generator")
    @SequenceGenerator(name="characteristic_generator", sequenceName = "characteristic_seq", allocationSize=1)
    private Long id;

    @NotEmpty(message = "Name should not be empty")
    private String name;

    @OneToMany(mappedBy = "characteristic",fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<CharacteristicMultipleStringValue> characteristicMultipleStringValues;

    @OneToMany(mappedBy = "characteristic",fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<CharacteristicSingleStringValue> characteristicSingleStringValues;

    @ManyToMany(mappedBy = "characteristics")
    private Set<Category> categories;


    @OneToMany(mappedBy = "characteristic",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Set<ProductCharacteristicDateValue> productCharacteristicDateValues;


    @OneToMany(mappedBy = "characteristic",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Set<ProductCharacteristicDecimalValue> productCharacteristicDecimalValues;


    @OneToMany(mappedBy = "characteristic",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Set<ProductCharacteristicIntValue> productCharacteristicIntValues;

    @OneToMany(mappedBy = "characteristic",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Set<ProductCharacteristicSingleStringValue> productCharacteristicSingleStringValues;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime created;

    @UpdateTimestamp
    private LocalDateTime updated;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Data type should be required")
    private CharacteristicValueType valueType;

    public Characteristic() {
    }

    public Characteristic(String name, CharacteristicValueType valueType) {
        this.name = name;
        this.valueType = valueType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<CharacteristicMultipleStringValue> getCharacteristicMultipleStringValues() {
        return characteristicMultipleStringValues;
    }

    public void setCharacteristicMultipleStringValues(Set<CharacteristicMultipleStringValue> characteristicMultipleStringValues) {
        this.characteristicMultipleStringValues = characteristicMultipleStringValues;
    }

    public Set<CharacteristicSingleStringValue> getCharacteristicSingleStringValues() {
        return characteristicSingleStringValues;
    }

    public void setCharacteristicSingleStringValues(Set<CharacteristicSingleStringValue> characteristicSingleStringValues) {
        this.characteristicSingleStringValues = characteristicSingleStringValues;
    }

    public CharacteristicValueType getValueType() {
        return valueType;
    }

    public void setValueType(CharacteristicValueType valueType) {
        this.valueType = valueType;
    }

    public Set<ProductCharacteristicDateValue> getProductCharacteristicDateValues() {
        return productCharacteristicDateValues;
    }

    public void setProductCharacteristicDateValues(Set<ProductCharacteristicDateValue> productCharacteristicDateValues) {
        this.productCharacteristicDateValues = productCharacteristicDateValues;
    }

    public Set<ProductCharacteristicDecimalValue> getProductCharacteristicDecimalValues() {
        return productCharacteristicDecimalValues;
    }

    public void setProductCharacteristicDecimalValues(Set<ProductCharacteristicDecimalValue> productCharacteristicDecimalValues) {
        this.productCharacteristicDecimalValues = productCharacteristicDecimalValues;
    }

    public Set<ProductCharacteristicIntValue> getProductCharacteristicIntValues() {
        return productCharacteristicIntValues;
    }

    public void setProductCharacteristicIntValues(Set<ProductCharacteristicIntValue> productCharacteristicIntValues) {
        this.productCharacteristicIntValues = productCharacteristicIntValues;
    }

    public Set<ProductCharacteristicSingleStringValue> getProductCharacteristicSingleStringValues() {
        return productCharacteristicSingleStringValues;
    }

    public void setProductCharacteristicSingleStringValues(Set<ProductCharacteristicSingleStringValue> productCharacteristicSingleStringValues) {
        this.productCharacteristicSingleStringValues = productCharacteristicSingleStringValues;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    @PreRemove
    private void preRemove() {
        Iterator<Category> it=getCategories().iterator();

        while(it.hasNext()){
            Category rootCategory=it.next();
            rootCategory.removeCharacteristic(this);
            it.remove();
        }
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Characteristic)) return false;
        Characteristic characteristic = (Characteristic) o;
        return id != null &&
                id.equals(characteristic.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
