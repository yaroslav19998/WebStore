package WebStoreGroup.WebStore.Order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;


@Embeddable
public class ProductInOrderId implements Serializable {

    private Long orderId;
    private Long productId;

    public ProductInOrderId(){}

    public ProductInOrderId(Long orderId, Long productId) {
        this.orderId = orderId;
        this.productId = productId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductInOrderId that = (ProductInOrderId) o;
        return orderId.equals(that.orderId) &&
                productId.equals(that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, productId);
    }

}
