package WebStoreGroup.WebStore.Characteristic.Entities;

import WebStoreGroup.WebStore.Product.Product;
import javax.validation.constraints.NotNull;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "product_characteristic_int_value" )
public class ProductCharacteristicIntValue {
    @EmbeddedId
    private ProductCharacteristicValueId id;

    @ManyToOne(fetch= FetchType.LAZY)
    @MapsId("characteristicId")
    @NotNull(message = "Characteristic should be required")
    private Characteristic characteristic;

    @ManyToOne(fetch= FetchType.LAZY)
    @MapsId("productId")
    @NotNull(message = "Product should be required")
    private Product product;

    @NotNull(message = "Value should be required")
    private int value;


    public ProductCharacteristicIntValue() {
    }


    public ProductCharacteristicIntValue(Characteristic characteristic, Product product,int value) {
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

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductCharacteristicIntValue that = (ProductCharacteristicIntValue) o;
        return Objects.equals(characteristic, that.characteristic) &&
                Objects.equals(product, that.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(characteristic, product);
    }
}
