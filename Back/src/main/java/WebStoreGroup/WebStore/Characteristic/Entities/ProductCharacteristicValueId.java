package WebStoreGroup.WebStore.Characteristic.Entities;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ProductCharacteristicValueId implements Serializable {
    private Long characteristicId;
    private Long productId;

    public ProductCharacteristicValueId(){}

    public ProductCharacteristicValueId(Long characteristicId, Long productId) {
        this.characteristicId = characteristicId;
        this.productId = productId;
    }
    public Long getCharacteristicId() {
        return characteristicId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setCharacteristicId(Long characteristicId) {
        this.characteristicId = characteristicId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductCharacteristicValueId that = (ProductCharacteristicValueId) o;
        return characteristicId.equals(that.characteristicId) &&
                productId.equals(that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(characteristicId, productId);
    }
}
