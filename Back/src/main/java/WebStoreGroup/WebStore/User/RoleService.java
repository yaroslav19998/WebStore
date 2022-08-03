package WebStoreGroup.WebStore.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    private RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    public Role getRoleById(Long id) {
        return roleRepository.getById(id);
    }

    public Role updateRole(Role role) {
        return roleRepository.save(role);
    }

    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }

    public Role findRoleByName(String name) {
        return roleRepository.findByName(name);
    }
}
