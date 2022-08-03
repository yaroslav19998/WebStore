package WebStoreGroup.WebStore.Order.DTO;

import WebStoreGroup.WebStore.Order.Order;
import WebStoreGroup.WebStore.Order.ProductInOrder;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

public class InProductInOrderDTO {

    @Valid
    @NotNull(message = "Order is mandatory")
    private Order orderInfo;

    @NotEmpty(message = "Products list can't be empty")
    private Set<ProductInOrder> productsInOrder;


    public Order getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(Order orderInfo) {
        this.orderInfo = orderInfo;
    }

    public Set<ProductInOrder> getProductsInOrder() {
        return productsInOrder;
    }

    public void setProductsInOrder(Set<ProductInOrder> productsInOrder) {
        this.productsInOrder = productsInOrder;
    }
}
