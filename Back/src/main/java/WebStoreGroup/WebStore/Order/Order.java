package WebStoreGroup.WebStore.Order;

import WebStoreGroup.WebStore.Helpers.ValidMarker;
import WebStoreGroup.WebStore.Product.Product;
import WebStoreGroup.WebStore.User.User;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name="orders")
public class Order implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_generator")
    @SequenceGenerator(name="order_generator", sequenceName = "order_seq", allocationSize=1)
    private Long id;

    @Column(name="customeremail")
    @Email(message = "Not valid email")
    @NotEmpty(message = "Email should not be empty")
    private String customerEmail;

    @Column(name="customerphone")
    @NotEmpty(message = "Phone should not be empty")
    private String customerPhone;

    @Column(name="customeraddress")
    @NotEmpty(message = "Address should not be empty")
    private String customerAddress;

    @Column(name="customername")
    @NotEmpty(message = "Name should not be empty")
    private String customerName;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @Column(name="fullprice",nullable = false)
    @PositiveOrZero(groups = ValidMarker.ValidPrice.class,message = "Price can't be negative")
    @NotNull(groups = ValidMarker.ValidPrice.class,message = "Price should be required")
    private BigDecimal fullPrice;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime created;

    @UpdateTimestamp
    private LocalDateTime updated;

    @Enumerated
    @Column(columnDefinition = "smallint")
    @NotNull(message = "Status should be required")
    private OrderStatus status;

    @OneToMany(mappedBy = "order",
            cascade = CascadeType.ALL)
    private Set<ProductInOrder> products;


    public void calculatePrice(){
        if(this.products!=null){
            this.products.forEach(pr->System.out.println(pr.getPrice()));
            this.fullPrice=this.products.stream().map(ProductInOrder::getPrice).reduce(BigDecimal::add).get();
        }
    }

//    public void preUpdate(){
//        System.out.println("order preUpdate");
//    }
    public Order() {
    }

    public Order(String customerEmail, String customerPhone, String customerAddress, String customerName,  OrderStatus status, Set<ProductInOrder> products) {
        this.customerEmail = customerEmail;
        this.customerPhone = customerPhone;
        this.customerAddress = customerAddress;
        this.customerName = customerName;
        this.status = status;
        this.products = products;
    }
    public void addProduct(Product product,int count) {
        ProductInOrder productInOrder=new ProductInOrder(this, product, count);
        products.add(productInOrder);
    }

    public void removeProduct(Product product) {
        for (Iterator<ProductInOrder> iterator = products.iterator();
             iterator.hasNext(); ) {
            ProductInOrder productInOrder = iterator.next();

            if (productInOrder.getOrder().equals(this) &&
                    productInOrder.getProduct().equals(product)) {
                iterator.remove();
                productInOrder.setOrder(null);
                productInOrder.setProduct(null);
            }
        }
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public BigDecimal getFullPrice() {
        return fullPrice;
    }

    public void setFullPrice(BigDecimal fullPrice) {
        this.fullPrice = fullPrice;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Set<ProductInOrder> getProducts() {
        return products;
    }

    public void setProducts(Set<ProductInOrder> products) {
        this.products = products;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id != null &&
                id.equals(order.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
