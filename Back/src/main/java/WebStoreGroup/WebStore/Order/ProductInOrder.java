package WebStoreGroup.WebStore.Order;


import WebStoreGroup.WebStore.Product.Product;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name="product_in_order")
public class ProductInOrder implements Serializable {

    @EmbeddedId
    private ProductInOrderId id;

    @ManyToOne(fetch= FetchType.LAZY,optional = false)
    @MapsId("orderId")
    @NotNull(message = "Order should be required")
    private Order order;

    @ManyToOne(fetch= FetchType.LAZY,optional = false)
    @MapsId("productId")
    @NotNull(message = "Product should be required")
    private Product product;

    @NotNull(message = "Quantity should be required")
    @Positive(message = "Quantity can't be negative")
    private int quantity;

    @NotNull(message = "Price should be required")
    @PositiveOrZero(message = "Price can't be negative")
    private BigDecimal price;

    public ProductInOrder() {
    }

    public ProductInOrder(Order order, Product product) {
        this.order = order;
        this.product = product;
        this.id = new ProductInOrderId(order.getId(),product.getId());
    }

    public ProductInOrder(Order order, Product product, int quantity) {
        this.order = order;
        this.product = product;
        this.id=new ProductInOrderId(order.getId(),product.getId());
        this.quantity =quantity;
    }

    public ProductInOrderId getId() {
        return id;
    }


    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @PrePersist
    public void prePersist(){
        this.setPrice(this.product.getPrice().multiply(new BigDecimal(this.quantity)));
        product.setSales(product.getSales()+quantity);
    }

    @PreRemove
    public void preRemove(){
        product.setSales(product.getSales()-quantity);
    }
    @PreUpdate
    public void preUpdate(){
        this.setPrice(this.product.getPrice().multiply(new BigDecimal(this.quantity)));
        order.calculatePrice();
        product.calculateSales();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductInOrder that = (ProductInOrder) o;
        return Objects.equals(order, that.order) &&
                Objects.equals(product, that.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(order, product);
    }


}
