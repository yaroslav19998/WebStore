package WebStoreGroup.WebStore.User;


import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Entity
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_generator")
    @SequenceGenerator(name="role_generator", sequenceName = "role_seq", allocationSize=1)
    private Long id;

    @NotEmpty(message = "Name should not be empty")
    @Column(unique = true)
    private String name;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime created;


    @UpdateTimestamp
    private LocalDateTime updated;



    public Role(){}
    public Role(String name) {
        this.name = name;
    }

    public Role(long id, String name) {
        this.id = id;
        this.name = name;
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
    public String getAuthority() {
        return getName() ;
    }
}
