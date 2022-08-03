package WebStoreGroup.WebStore.Characteristic.Entities;

import WebStoreGroup.WebStore.Product.Product;
import com.fasterxml.jackson.annotation.JsonFormat;

import org.hibernate.annotations.NotFound;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "product_characteristic_date_value" )
public class ProductCharacteristicDateValue {
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
    private LocalDate value;


    public ProductCharacteristicDateValue() {
    }

    public ProductCharacteristicDateValue(Characteristic characteristic, Product product, LocalDate value) {
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

    public LocalDate getValue() {
        return value;
    }

    public void setValue(LocalDate value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductCharacteristicDateValue that = (ProductCharacteristicDateValue) o;
        return Objects.equals(characteristic, that.characteristic) &&
                Objects.equals(product, that.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(characteristic, product);
    }
}
