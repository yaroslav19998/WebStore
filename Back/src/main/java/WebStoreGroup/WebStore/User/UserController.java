package WebStoreGroup.WebStore.User;

import WebStoreGroup.WebStore.DTOUtils.PageDTO;
import WebStoreGroup.WebStore.Order.DTO.OrderInfoDTO;
import WebStoreGroup.WebStore.Product.DTO.ProductReviewDTO.ProductReviewDTO;
import WebStoreGroup.WebStore.User.DTO.*;
import WebStoreGroup.WebStore.secutity.JWTUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;
    private final JWTUtils jwtUtils;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoleService roleService;

    @Autowired
    public UserController(UserService userService, JWTUtils jwtUtils, BCryptPasswordEncoder bCryptPasswordEncoder, RoleService roleService) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleService = roleService;
    }



    @GetMapping("")
    public ResponseEntity<PageDTO<UserAdminDTO>> getUsers(@RequestParam(name = "username",required = false) String username,
                                                        @RequestParam(name = "roleId",required = false) Long roleId,
                                                        @RequestParam(name = "page",required = false,defaultValue = "0") Integer page) {
        return new ResponseEntity<>(userService.getUsers(username,roleId,page), HttpStatus.OK);

    }
    @GetMapping("{id}")
    public ResponseEntity<? extends UserIdNameDTO> getUserById(@PathVariable Long id,
                                                          @RequestHeader(value = "ResponseResourceFormat", required = false) String resourceFormat) {

        if(resourceFormat!=null && resourceFormat.equals("admin")){
            return new ResponseEntity<>(userService.getUserById(id,UserAdminDTO.class), HttpStatus.OK);
        }
        return new ResponseEntity<>(userService.getUserInfoById(id), HttpStatus.OK);

    }

    @GetMapping("{id}/orders")
    public ResponseEntity<PageDTO<OrderInfoDTO>> getUserOrdersById(@PathVariable Long id,
                                                                @RequestParam(name = "page",required = false,defaultValue = "0") Integer page) {
        return new ResponseEntity<>(userService.getUserOrders(id,page), HttpStatus.OK);

    }
    @GetMapping("{id}/reviews")
    public ResponseEntity<PageDTO<ProductReviewDTO>> getUserReviewsById(@PathVariable Long id,
                                                                     @RequestParam(name = "page",required = false,defaultValue = "0") Integer page) {
        return new ResponseEntity<>(userService.getUserReviews(id, page), HttpStatus.OK);

    }
    @PostMapping("")
    @Validated({ValidMarker.OnCreate.class})
    public ResponseEntity<UserAdminDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
        User user= userDTO.convertToUser();
        if(user.getPassword()!=null){
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }
        return new ResponseEntity<>(userService.createUser(user), HttpStatus.CREATED);

    }
    @PutMapping("{id}")
    @Validated({ValidMarker.OnUpdate.class})
    public ResponseEntity<UserAdminDTO> updateUser(@PathVariable("id") Long id,@Valid @RequestBody UserDTO userDTO) {
        User user=userDTO.convertToUser();
            if(user.getPassword()!=null){
                user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            }
         return new ResponseEntity<>(userService.updateUser(id,user), HttpStatus.OK);

    }
    @DeleteMapping("{id}")
    private ResponseEntity<?> deleteUser(@PathVariable("id") Long id)
    {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Validated({ValidMarker.OnCreate.class})
    @PostMapping("registration")
    public ResponseEntity<?> registration(@Valid @RequestBody UserDTO userDTO) {

        User user= userDTO.convertToUser();
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        ArrayList<Role> roles = new ArrayList<>();
        roles.add(roleService.findRoleByName("ROLE_USER"));
        user.setRoles(roles);
        userService.createUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("refresh_token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                Map<String, String> tokens = jwtUtils.RefreshToken(refresh_token);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            } catch (Exception exception) {
                response.setHeader("error", exception.getMessage());
                response.setStatus(UNAUTHORIZED.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", exception.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }
    }
}
