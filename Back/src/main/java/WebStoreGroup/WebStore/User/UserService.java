package WebStoreGroup.WebStore.User;

import WebStoreGroup.WebStore.DTOUtils.ConverterDTO;
import WebStoreGroup.WebStore.Exceptions.NotFoundException;
import WebStoreGroup.WebStore.DTOUtils.PageDTO;
import WebStoreGroup.WebStore.Exceptions.UserAlreadyExistException;
import WebStoreGroup.WebStore.Order.DTO.OrderInfoDTO;
import WebStoreGroup.WebStore.Order.OrderRepository;
import WebStoreGroup.WebStore.Product.DTO.ProductReviewDTO.ProductReviewDTO;
import WebStoreGroup.WebStore.Product.ProductReviewRepository;
import WebStoreGroup.WebStore.User.DTO.UserAdminDTO;
import WebStoreGroup.WebStore.User.DTO.UserIdNameDTO;
import WebStoreGroup.WebStore.User.DTO.UserInfoDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service

public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ProductReviewRepository productReviewRepository;
    private final ConverterDTO converterDTO;
    @Value("${default-page-size}")
    private int pageSize;

    @Autowired
    public UserService(UserRepository userRepository, OrderRepository orderRepository, ProductReviewRepository productReviewRepository, ConverterDTO converterDTO) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.productReviewRepository = productReviewRepository;
        this.converterDTO = converterDTO;
    }


    public UserAdminDTO createUser(User user) {
        if(checkUsername(user.getUsername())){
            throw new UserAlreadyExistException("User with name " + user.getUsername() +" already exist");
        }
        return converterDTO.convert(UserAdminDTO.class, userRepository.save(user));
    }

    @PreAuthorize("#id.toString()==authentication.name or hasRole('ROLE_ADMIN')")
    public UserAdminDTO updateUser(Long id,User user) {

        User userFromDb=userRepository.getById(id);
        if(checkUsername(user.getUsername())&& !userFromDb.getUsername().equals(user.getUsername())){
            throw new UserAlreadyExistException("User with name" + user.getUsername() +"already exist");
        }
        List<String> ignoredFields=new ArrayList<>(Arrays.asList("id","orders", "created", "updated"));
        if(user.getPassword()==null){
            ignoredFields.add("password");
        }
        if(user.getRoles()==null){
            ignoredFields.add("roles");
        }
        BeanUtils.copyProperties(user,userFromDb,  ignoredFields.toArray(new String[0]));
        userFromDb.getRoles().stream().forEach(role->System.out.println(role.getName()));
        return converterDTO.convert(UserAdminDTO.class, userRepository.save(userFromDb));
    }

    public void deleteUser(Long id) {
        System.out.println("delete");
        userRepository.deleteById(id);
    }



    @PreAuthorize("#userId.toString()==authentication.name or hasRole('ROLE_ADMIN')")
    public PageDTO<OrderInfoDTO> getUserOrders(Long userId, Integer page) {
        Page<Long> orderIdsPage=orderRepository.findByUser_Id(userId, PageRequest.of(page,pageSize,Sort.by("id").descending()));
        if (orderIdsPage != null &&  orderIdsPage.hasContent()) {
            return new PageDTO<>( orderRepository.findByIdIn(
                    orderIdsPage.getContent(),OrderInfoDTO.class,Sort.by("id").descending()),
                    orderIdsPage.getNumber(),
                    orderIdsPage.getTotalPages(),
                    orderIdsPage.getTotalElements());
        }
        return new PageDTO<>(Collections.emptyList(), 0, 0);
    }

    @PreAuthorize("#userId.toString()==authentication.name or hasRole('ROLE_ADMIN')")
    public PageDTO<ProductReviewDTO> getUserReviews( Long userId, Integer page) {
        Page<ProductReviewDTO> productReviewDTOPage=productReviewRepository.findByUser_Id(userId,PageRequest.of(page,pageSize,
                Sort.by("updated").descending()));
        if (productReviewDTOPage != null &&  productReviewDTOPage.hasContent()) {
            return new PageDTO<>( productReviewDTOPage.getContent(), productReviewDTOPage.getNumber(), productReviewDTOPage.getTotalPages(),
                    productReviewDTOPage.getTotalElements());
        }
        return new PageDTO<>(Collections.emptyList(), 0, 0);
    }

    public <T extends UserIdNameDTO> T getUserById(Long id, Class<T> type) {
        return userRepository.findUserById(id,type).orElseThrow(()-> new NotFoundException("User: "+id+ " not found"));
    }
    public UserInfoDTO getUserInfoById(Long id) {
        return userRepository.findInfoUserById(id).orElseThrow(()-> new NotFoundException("User: "+id+ " not found"));
    }
    public User getUser(Long id) {
        return userRepository.findById(id).orElseThrow(()-> new NotFoundException("User: "+id+ " not found"));
    }


    public PageDTO<UserAdminDTO> getUsers(String username,Long roleId,Integer page) {
        Page<Long> userIdsPage;

        if(username!=null && roleId!=null)
        {
            userIdsPage=userRepository.findByUsernameContainingIgnoreCaseAndRolesId(username,roleId,PageRequest.of(page,pageSize,Sort.by("id").descending()));
        }
        else if(roleId!=null)
        {
            userIdsPage=userRepository.findByRolesId(roleId,PageRequest.of(page,pageSize,Sort.by("id").descending()));
        }
        else if(username!=null)
        {
            userIdsPage=userRepository.findByUsernameContainingIgnoreCase(username,PageRequest.of(page,pageSize,Sort.by("id").descending()));
        }
        else {
            userIdsPage=userRepository.findAllBy(PageRequest.of(page,pageSize,Sort.by("id").descending()));
        }
        if ( userIdsPage != null &&  userIdsPage.hasContent()) {
            return new PageDTO<>(userRepository.findByIdIn(userIdsPage.getContent(),UserAdminDTO.class, Sort.by("id").descending()),
                    userIdsPage.getNumber(),userIdsPage.getTotalPages());
        }
        return new PageDTO<>(Collections.emptyList(), 0, 0);
    }


    public  boolean checkUsername(String username){
        return userRepository.existsByUsername(username);
    }
    public long getUserIdByUsername(String username) {
        return userRepository.getIdByUsername(username);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.getRoles());
    }
}
