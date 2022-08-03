package WebStoreGroup.WebStore.Product;

import WebStoreGroup.WebStore.Brand.Brand;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.time.LocalDateTime;

@Entity
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_img_generator")
    @SequenceGenerator(name="product_img_generator", sequenceName = "product_img_seq", allocationSize=1)
    private long id;

    @ManyToOne(optional = false,fetch = FetchType.LAZY)
    @JoinColumn(name="product_id")
    @NotNull(message = "Product should be required")
    private Product product;

    @Column(insertable = true,updatable = false)
    private String filePath;

    @JsonProperty(value="is_main")
    private boolean is_main;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime created;

    @UpdateTimestamp
    private LocalDateTime updated;

    @PreRemove
    private void deleteImage(){
        if(filePath!=null){
        File file = new File(filePath);
        file.delete();}
    }
    public ProductImage(){

    }
    public ProductImage( String filePath, boolean is_main) {
        this.filePath = filePath;
        this.is_main = is_main;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    @JsonProperty(value="is_main")
    public boolean getIs_main() {
        return is_main;
    }

    public void setIs_main(boolean is_main) {
        this.is_main = is_main;
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
    public String toString() {
        return "ProductImage{" +
                "id=" + id +
                ", product=" + product.getId() +
                ", filePath='" + filePath + '\'' +
                ", is_main=" + is_main +
                ", created=" + created +
                ", updated=" + updated +
                '}';
    }
}
