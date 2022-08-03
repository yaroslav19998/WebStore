package WebStoreGroup.WebStore.User;

import WebStoreGroup.WebStore.Brand.Brand;
import WebStoreGroup.WebStore.Brand.DTO.BrandAdminDTO;
import WebStoreGroup.WebStore.Brand.DTO.BrandInfoDTO;
import WebStoreGroup.WebStore.secutity.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("roles")
public class RoleController {

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("")
    private ResponseEntity<List<Role>> getRoles(){


        return new ResponseEntity<>(roleService.getRoles(), HttpStatus.OK);
    }
    @GetMapping("{id}")
    private ResponseEntity<Role> getRoleById(@PathVariable("id") Long id)

    {
        return new ResponseEntity<>(roleService.getRoleById(id), HttpStatus.OK);
    }
    @PostMapping(" ")
    private ResponseEntity<Role> createRole(@Valid @RequestBody Role role)
    {
        return new ResponseEntity<>(roleService.createRole(role),HttpStatus.CREATED);
    }
    @DeleteMapping("{id}")
    private ResponseEntity<?> deleteRole(@PathVariable("id") Long id)
    {
        roleService.deleteRole(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PutMapping("{id}")
    private ResponseEntity<Role> updateRole(@Valid @RequestBody Role role)
    {
         return new ResponseEntity<>(roleService.updateRole(role),HttpStatus.OK);
    }
}
