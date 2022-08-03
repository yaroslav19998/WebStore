package WebStoreGroup.WebStore.Characteristic.Entities;

import WebStoreGroup.WebStore.Product.Product;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "characteristic_single_string_value" )
public class CharacteristicSingleStringValue {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @NotEmpty(message = "Value should not be empty")
    private String value;

    @ManyToOne(optional = false,fetch= FetchType.LAZY)
    @JoinColumn(name="characteristicId",nullable =false)
    @NotNull(message = "Characteristic should be required")
    private Characteristic characteristic;

    @OneToMany(mappedBy = "value",fetch= FetchType.LAZY,cascade = CascadeType.ALL)
    private Set<ProductCharacteristicSingleStringValue> productCharacteristic;

    public CharacteristicSingleStringValue() {
    }

    public CharacteristicSingleStringValue(String value, Characteristic characteristic) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CharacteristicSingleStringValue)) return false;
        CharacteristicSingleStringValue characteristicSingleStringValue = (CharacteristicSingleStringValue) o;
        return id != null &&
                id.equals(characteristicSingleStringValue.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
