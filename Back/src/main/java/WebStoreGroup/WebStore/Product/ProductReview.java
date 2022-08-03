package WebStoreGroup.WebStore.Product;

import WebStoreGroup.WebStore.User.User;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class ProductReview {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_review_generator")
    @SequenceGenerator(name="product_review_generator", sequenceName = "product_review_seq", allocationSize=1)
    private Long id;

    @ManyToOne(optional = false,fetch = FetchType.LAZY)
    @JoinColumn(name="product_id",nullable = false)
    @NotNull(message = "Product should be required")
    private Product product;

    @ManyToOne(optional = false,fetch = FetchType.LAZY)
    @JoinColumn(name="user_id",nullable = false)
    @NotNull(message = "User should be required")
    private User user;

    @NotNull(message = "Rate should be required")
    @Min(value = 1, message = "Rate should not be less than 1")
    @Max(value = 5, message = "Rate should not be greater than 5")
    private byte rate;

    private String review;

    private String pros;
    private String cons;

    @CreationTimestamp
    private LocalDateTime created;

    @UpdateTimestamp
    private LocalDateTime updated;

    public ProductReview() {
    }

    public ProductReview(Product product, User user, byte rate, String review) {
        this.product = product;
        this.user = user;
        this.rate = rate;
        this.review = review;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public byte getRate() {
        return rate;
    }

    public void setRate(byte rate) {
        this.rate = rate;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getPros() {
        return pros;
    }

    public void setPros(String pros) {
        this.pros = pros;
    }

    public String getCons() {
        return cons;
    }

    public void setCons(String cons) {
        this.cons = cons;
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductReview)) return false;
        ProductReview productReview = (ProductReview) o;
        return id != null &&
                id.equals(productReview.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
