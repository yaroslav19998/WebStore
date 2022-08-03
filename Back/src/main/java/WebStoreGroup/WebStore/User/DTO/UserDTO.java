package WebStoreGroup.WebStore.User.DTO;

import WebStoreGroup.WebStore.User.Role;
import WebStoreGroup.WebStore.User.User;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.security.PublicKey;
import java.time.LocalDateTime;
import java.util.List;

public class UserDTO {

    @NotEmpty(message = "Name should not be empty")
    @Size(min=2,max=15,message = "Name should be between 2 and 15 characters")
    private String username;

    @NotEmpty(groups = {ValidMarker.OnCreate.class},message = "Password should not be empty")
    @Size(min=8,max=12,message = "Password should be between 8 and 12 characters")
    private String password;

    @Email(message = "Not valid email")
    private String email;

    private List<Role> roles ;

    public UserDTO(String username, String password, String email, List<Role> roles) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = roles;
    }
    public User convertToUser(){
        return new User(getUsername() ,getPassword() ,getEmail(),getRoles());
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
