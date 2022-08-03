package WebStoreGroup.WebStore.Brand;

import WebStoreGroup.WebStore.Category.Category;
import WebStoreGroup.WebStore.Product.Product;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Entity
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "brand_generator")
    @SequenceGenerator(name="brand_generator", sequenceName = "brand_seq", allocationSize=1)
    private Long id;

    @Column(name = "name")
    @NotEmpty(message = "Name should not be empty")
    private String name;

    @OneToMany(mappedBy = "brand", fetch = FetchType.LAZY)
    private List<Product> products;

    @ManyToMany(mappedBy = "brands")
    private Set<Category> categories;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime created;

    @UpdateTimestamp
    private LocalDateTime updated;


    public Brand() {
    }

    public Brand(String name) {
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    @PreRemove
    private void preRemove() {
        Iterator<Product> productIterator = getProducts().iterator();

        while (productIterator.hasNext()) {
            Product rootProduct = productIterator.next();
            rootProduct.setBrand(null);
            productIterator.remove();
            // iterator remove removes whatever .next() returned from the backing array (be aware; is implimented for most BUT NOT ALL collections, additionally for some collections creating a new collection can be more efficient

        }
        Iterator<Category> categoryIterator = getCategories().iterator();

        while (categoryIterator.hasNext()) {
            Category rootCategory = categoryIterator.next();
            rootCategory.removeBrand(this);
            categoryIterator.remove();
            // iterator remove removes whatever .next() returned from the backing array (be aware; is implimented for most BUT NOT ALL collections, additionally for some collections creating a new collection can be more efficient

        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Brand)) return false;
        Brand brand = (Brand) o;
        return id != null &&
                id.equals(brand.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
