package WebStoreGroup.WebStore.User;

import WebStoreGroup.WebStore.Order.Order;
import WebStoreGroup.WebStore.Product.Product;
import WebStoreGroup.WebStore.Product.ProductReview;
import WebStoreGroup.WebStore.User.DTO.ValidMarker;
import com.sun.istack.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "UserApi")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_generator")
    @SequenceGenerator(name="user_generator", sequenceName = "user_seq", allocationSize=1)
    private Long id;

    @NotEmpty(message = "Name should not be empty")
    @Size(min=2,max=15,message = "Name should be between 2 and 15 characters")
    private String username;

    private String password;

    @Email(message = "Not valid email")
    private String email;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime created;

    @UpdateTimestamp
    private LocalDateTime updated;


    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> roles ;

    @OneToMany(fetch = FetchType.LAZY,mappedBy="user")
    private List<Order> orders;


    public User(){}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username, String password, String email, List<Role> roles) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = roles;
    }

    public User(String username, String password, List<Role> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
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



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = ( User) o;
        return id != null &&
                id.equals(user.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

