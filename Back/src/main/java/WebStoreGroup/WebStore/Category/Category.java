package WebStoreGroup.WebStore.Category;

import WebStoreGroup.WebStore.Brand.Brand;
import WebStoreGroup.WebStore.Characteristic.Entities.Characteristic;
import WebStoreGroup.WebStore.Characteristic.Entities.CharacteristicMultipleStringValue;
import WebStoreGroup.WebStore.Product.Product;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_generator")
    @SequenceGenerator(name="category_generator", sequenceName = "category_seq", allocationSize=1)
    private Long id;

    @NotEmpty(message = "Name should not be empty")
    private String name;

    @OneToMany(mappedBy = "category")
    private List<Product> products;

    @ManyToOne(fetch = FetchType.LAZY)
    private Category parentCategory;

    @PositiveOrZero(message = "Category depth can't be negative")
    private Byte depth;

    @OneToMany(mappedBy = "parentCategory",fetch = FetchType.LAZY)
    private Set<Category> subCategories;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "category_characteristic",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "characteristic_id"))
    private Set<Characteristic> characteristics;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "category_brand",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "brand_id"))
    private Set<Brand> brands;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime created;

    @UpdateTimestamp
    private LocalDateTime updated;


    public Category() {
    }
    public Category(String name) {
        this.name = name;
    }

    public Category(String name, Category parentCategory, Set<Category> subCategories,byte depth) {
        this.name = name;
        this.parentCategory = parentCategory;
        this.subCategories = subCategories;
        this.depth=depth;
    }
    @PreRemove
    private void preRemove() {

        Iterator<Category> it=subCategories.iterator();

        while(it.hasNext()){
            Category rootCategory=it.next();
            rootCategory.setParentCategory(null);
            rootCategory.setDepth((byte) 0);
            it.remove();
        }

        subCategories=null;

        Iterator<Product> itProduct=getProducts().iterator();

        while( itProduct.hasNext()){
            Product rootProduct=itProduct.next();
            rootProduct.setCategory(null);

            itProduct.remove();
        }
        setProducts(null);
    }
    public Set<Brand> getBrands() {
        return brands;
    }

    public void setBrands(Set<Brand> brands) {
        this.brands = brands;
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

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Category getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(Category parentCategory) {
        this.parentCategory = parentCategory;
    }

    public Byte getDepth() {
        return depth;
    }

    public void setDepth(Byte depth) {
        this.depth = depth;
    }

    public Set<Category> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(Set<Category> subCategories) {
        this.subCategories = subCategories;
    }

    public Set<Characteristic> getCharacteristics() {
        return characteristics;
    }

    public void setCharacteristics(Set<Characteristic> characteristics) {
        this.characteristics = characteristics;
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


    public void removeCharacteristic(Characteristic characteristic) {
        characteristics.remove(characteristic);
    }

    public void removeBrand(Brand brand) {
        brands.remove(brand);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;
        Category category = (Category) o;
        return id != null &&
                id.equals(category.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
