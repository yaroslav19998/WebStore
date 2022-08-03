package WebStoreGroup.WebStore.Order.DTO;

import WebStoreGroup.WebStore.Order.Order;
import WebStoreGroup.WebStore.Order.ProductInOrder;
import WebStoreGroup.WebStore.User.User;
import com.sun.istack.NotNull;

import javax.persistence.Column;
import javax.validation.Valid;
import java.util.Set;

public class UpdateOrderDTO {

    @Valid
    private Order order;


    private Set<ProductInOrder> deletedProducts;

    public UpdateOrderDTO(Order order, Set<ProductInOrder> deletedProducts) {
        this.order = order;
        this.deletedProducts = deletedProducts;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Set<ProductInOrder> getDeletedProducts() {
        return deletedProducts;
    }

    public void setDeletedProducts(Set<ProductInOrder> deletedProducts) {
        this.deletedProducts = deletedProducts;
    }
}
