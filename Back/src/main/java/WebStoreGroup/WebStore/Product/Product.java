package WebStoreGroup.WebStore.Product;

import WebStoreGroup.WebStore.Characteristic.Entities.*;
import WebStoreGroup.WebStore.Brand.Brand;
import WebStoreGroup.WebStore.Category.Category;
import WebStoreGroup.WebStore.Order.ProductInOrder;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name="product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_generator")
    @SequenceGenerator(name="product_generator", sequenceName = "product_seq", allocationSize=1)
    private Long id;

    @NotEmpty(message = "Name should not be empty")
    private String name;

    private String description;

    @NotNull(message = "Price should be required")
    @PositiveOrZero(message = "Price can't be negative")
    private BigDecimal price;

    @ManyToOne( fetch = FetchType.LAZY,optional = true)
    @JoinColumn(name="category_id",nullable = true)
    private Category category;

    @ManyToOne( fetch = FetchType.LAZY,optional = true)
    @JoinColumn(name="brand_id",nullable = true)
    private Brand brand;


    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime created;

    @UpdateTimestamp
    private LocalDateTime updated;

    @OneToMany(mappedBy = "product",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private Set<ProductCharacteristicSingleStringValue> singleStringCharacteristics;
    @OneToMany(mappedBy = "product",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private Set<ProductCharacteristicDecimalValue> decimalCharacteristics;
    @OneToMany(mappedBy = "product",
            cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ProductCharacteristicDateValue> dateCharacteristics;
    @OneToMany(mappedBy = "product",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private Set<ProductCharacteristicIntValue> intCharacteristics;

    @OneToMany(mappedBy = "product",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    @OrderBy("is_main DESC")
    private Set<ProductImage> images=new HashSet<>();

    @OneToMany(mappedBy = "product",
            cascade = CascadeType.ALL,
           fetch = FetchType.LAZY)
    @OrderBy("id DESC")
    private Set<ProductReview> reviews;

    @ManyToMany
    @JoinTable(name = "product_characteristic_multiple_string_value",
    joinColumns = @JoinColumn(name = "product_id"),
    inverseJoinColumns = @JoinColumn(name = "multiple_string_value_id"))
    private Set<CharacteristicMultipleStringValue> multipleStringCharacteristics;

    @OneToMany(mappedBy = "product",
            cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ProductInOrder> orders;

    @NotNull(message = "Sales must be required")
    @PositiveOrZero(message = "Sales can't be negative")
    private int sales=0;

    @Column(precision = 2,scale = 1)
    private BigDecimal averageRate;


    public Product() {

    }

    public Product(String name, String description, BigDecimal price, Category category, Brand brand) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.brand = brand;
    }
    public void addReview(ProductReview review) {
        reviews.add(review);
        BigDecimal sum=this.reviews.stream().map(r->new BigDecimal(r.getRate())).reduce(BigDecimal.ZERO, BigDecimal::add);
        setAverageRate(sum.divide(new BigDecimal(this.reviews.size())));
        review.setProduct(this);
    }

    public void removeReview(ProductReview review) {
        reviews.remove(review);
        if(this.reviews!=null){
            BigDecimal sum=this.reviews.stream().map(r->new BigDecimal(r.getRate())).reduce(BigDecimal.ZERO, BigDecimal::add);
            setAverageRate(sum.divide(new BigDecimal(this.reviews.size())));

        }
        else {
            this.setAverageRate(new BigDecimal(0));
        }
        review.setProduct(null);
    }
    public void addImage(ProductImage image) {
        images.add(image);
        image.setProduct(this);
    }

    public void removeImage(ProductImage image) {
        images.remove(image);
        image.setProduct(null);
    }

    public void removeMultipleString(CharacteristicMultipleStringValue stringValue) {
        multipleStringCharacteristics.remove(stringValue);
    }
    public void calculateSales(){
        this.sales=orders.stream().mapToInt(ProductInOrder::getQuantity).sum();
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public int getSales() {
        return sales;
    }

    public void setSales(int sales) {
        this.sales = sales;
    }

    public BigDecimal getAverageRate() {
        return averageRate;
    }

    public void setAverageRate(BigDecimal averageRate) {
        this.averageRate = averageRate;
    }

    public Set<ProductCharacteristicSingleStringValue> getSingleStringCharacteristics() {
        return singleStringCharacteristics;
    }

    public void setSingleStringCharacteristics(Set<ProductCharacteristicSingleStringValue> singleStringCharacteristics) {
        this.singleStringCharacteristics = singleStringCharacteristics;
    }

    public Set<ProductCharacteristicDateValue> getDateCharacteristics() {
        return dateCharacteristics;
    }

    public void setDateCharacteristics(Set<ProductCharacteristicDateValue> dateCharacteristics) {
        this.dateCharacteristics = dateCharacteristics;
    }

    public Set<ProductCharacteristicIntValue> getIntCharacteristics() {
        return intCharacteristics;
    }

    public void setIntCharacteristics(Set<ProductCharacteristicIntValue> intCharacteristics) {
        this.intCharacteristics = intCharacteristics;
    }

    public Set<CharacteristicMultipleStringValue> getMultipleStringCharacteristics() {
        return multipleStringCharacteristics;
    }

    public void setMultipleStringCharacteristics(Set<CharacteristicMultipleStringValue> multipleStringCharacteristics) {
        this.multipleStringCharacteristics = multipleStringCharacteristics;
    }

    public Set<ProductCharacteristicDecimalValue> getDecimalCharacteristics() {
        return decimalCharacteristics;
    }

    public void setDecimalCharacteristics(Set<ProductCharacteristicDecimalValue> decimalCharacteristics) {
        this.decimalCharacteristics = decimalCharacteristics;
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

    public Set<ProductImage> getImages() {
        return images;
    }

    public void setImages(Set<ProductImage> images) {
        this.images = images;
    }

    public Set<ProductReview> getReviews() {
        return reviews;
    }

    public void setReviews(Set<ProductReview> reviews) {
        this.reviews = reviews;
    }

    public void setOrders(Set<ProductInOrder> orders) {
        this.orders = orders;
    }

    public Set<ProductInOrder> getOrders() {
        return orders;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", images=" + images +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return id != null &&
                id.equals(product.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
