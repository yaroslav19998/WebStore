package WebStoreGroup.WebStore.Characteristic.Entities;

import WebStoreGroup.WebStore.Category.Category;
import WebStoreGroup.WebStore.Product.Product;
import org.springframework.security.core.parameters.P;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Iterator;
import java.util.Set;

@Entity
@Table(name = "characteristic_multiple_string_value" )
public class CharacteristicMultipleStringValue {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @NotEmpty(message = "Value should not be empty")
    private String value;

    @ManyToOne(optional = false,fetch= FetchType.LAZY)
    @JoinColumn(name="characteristicId",nullable =false)
    @NotNull(message = "Characteristic should be required")
    private Characteristic characteristic;

    @ManyToMany(mappedBy = "multipleStringCharacteristics")
    private Set<Product> products;

    public CharacteristicMultipleStringValue() {
    }

    public CharacteristicMultipleStringValue(String value, Characteristic characteristic) {
        this.value = value;
        this.characteristic = characteristic;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Characteristic getCharacteristic() {
        return characteristic;
    }

    public void setCharacteristic(Characteristic characteristic) {
        this.characteristic = characteristic;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    @PreRemove
    private void PreRemove() {
        Iterator<Product> it=getProducts().iterator();

        while(it.hasNext()){
            Product rootProduct=it.next();
            rootProduct.removeMultipleString(this);
            it.remove();
        }
    }

    public void removeProduct(Product p) {
        this.products.remove(p);
        p.getMultipleStringCharacteristics().remove(this);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CharacteristicMultipleStringValue)) return false;
        CharacteristicMultipleStringValue characteristicMultipleStringValue = (CharacteristicMultipleStringValue) o;
        return id != null &&
                id.equals(characteristicMultipleStringValue.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
