package WebStoreGroup.WebStore.Characteristic.Entities;

import WebStoreGroup.WebStore.Product.Product;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "product_characteristic_single_string_value" )
public class ProductCharacteristicSingleStringValue implements Serializable {

    @EmbeddedId
    private ProductCharacteristicValueId id;

    @ManyToOne(fetch= FetchType.LAZY,optional = false)
    @MapsId("characteristicId")
    @NotNull(message = "Characteristic should be required")
    private Characteristic characteristic;

    @ManyToOne(fetch= FetchType.LAZY,optional = false)
    @MapsId("productId")
    @NotNull(message = "Product should be required")
    private Product product;

    @ManyToOne(fetch= FetchType.LAZY,optional = false)
    @JoinColumn(name="valueId",nullable = false)
    @NotNull(message = "Value should be required")
    private CharacteristicSingleStringValue value;


    public ProductCharacteristicSingleStringValue() {
    }

    public ProductCharacteristicSingleStringValue(Characteristic characteristic, Product product,CharacteristicSingleStringValue value) {
        this.id = new ProductCharacteristicValueId(characteristic.getId(),product.getId());
        this.characteristic = characteristic;
        this.product = product;
        this.value=value;
    }

    public ProductCharacteristicValueId getId() {
        return id;
    }

    public void setId(ProductCharacteristicValueId id) {
        this.id = id;
    }

    public Characteristic getCharacteristic() {
        return characteristic;
    }

    public void setCharacteristic(Characteristic characteristic) {
        this.characteristic = characteristic;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public CharacteristicSingleStringValue getValue() {
        return value;
    }

    public void setValue(CharacteristicSingleStringValue value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductCharacteristicSingleStringValue that = (ProductCharacteristicSingleStringValue) o;
        return Objects.equals(characteristic, that.characteristic) &&
                Objects.equals(product, that.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(characteristic, product);
    }
}
