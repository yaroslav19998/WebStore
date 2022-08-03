package WebStoreGroup.WebStore.Characteristic.Entities;

import WebStoreGroup.WebStore.Product.Product;
import javax.validation.constraints.NotNull;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "product_characteristic_decimal_value" )
public class ProductCharacteristicDecimalValue {
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

    @NotNull(message = "Value should be required")
    private BigDecimal value;


    public ProductCharacteristicDecimalValue() {
    }

    public ProductCharacteristicDecimalValue(Characteristic characteristic, Product product,BigDecimal value) {
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

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductCharacteristicDecimalValue that = (ProductCharacteristicDecimalValue) o;
        return Objects.equals(characteristic, that.characteristic) &&
                Objects.equals(product, that.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(characteristic, product);
    }
}
