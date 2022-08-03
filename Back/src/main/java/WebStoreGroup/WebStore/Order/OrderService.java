package WebStoreGroup.WebStore.Order;

import WebStoreGroup.WebStore.DTOUtils.ConverterDTO;
import WebStoreGroup.WebStore.Exceptions.NotFoundException;
import WebStoreGroup.WebStore.DTOUtils.PageDTO;
import WebStoreGroup.WebStore.Helpers.ValidMarker;
import WebStoreGroup.WebStore.Order.DTO.InProductInOrderDTO;
import WebStoreGroup.WebStore.Order.DTO.OrderInfoDTO;
import WebStoreGroup.WebStore.Order.DTO.UpdateOrderDTO;
import WebStoreGroup.WebStore.Product.Product;
import WebStoreGroup.WebStore.Product.ProductRepository;
import WebStoreGroup.WebStore.User.User;
import WebStoreGroup.WebStore.User.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.*;

@Service
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductInOrderRepository productInOrderRepository;
    private final ConverterDTO converterDTO;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Value("${default-page-size}")
    private int pageSize;

    @Autowired
    public OrderService(OrderRepository orderRepository, ProductInOrderRepository productInOrderRepository, ConverterDTO converterDTO, UserRepository userRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productInOrderRepository = productInOrderRepository;
        this.converterDTO = converterDTO;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }




    public OrderInfoDTO createOrder(InProductInOrderDTO orderDTO) {
        orderDTO.getOrderInfo().setStatus(OrderStatus.WAITING);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            Optional<User> user = userRepository.findById(Long.parseLong(authentication.getName()));
            orderDTO.getOrderInfo().setUser(user.get());
        }
        Order newOrder = orderRepository.save(orderDTO.getOrderInfo());
        newOrder.setProducts(new HashSet<>());
        orderDTO.getProductsInOrder().forEach(productInOrder -> newOrder.addProduct(productInOrder.getProduct(), productInOrder.getQuantity()));
        Order order1 = orderRepository.save(newOrder);
//        BigDecimal fullPrice = order1.getProducts().stream().map(ProductInOrder::getPrice).reduce(BigDecimal::add).get();
//        order1.setFullPrice(fullPrice);
        addPrice(order1);
        return converterDTO.convert(OrderInfoDTO.class, orderRepository.save(order1));
    }

    @Validated({ValidMarker.ValidPrice.class})
    public void addPrice(@Valid Order order){
        BigDecimal fullPrice = order.getProducts().stream().map(ProductInOrder::getPrice).reduce(BigDecimal::add).get();
        order.setFullPrice(fullPrice);
    }
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    public OrderInfoDTO updateOrder(Long id, UpdateOrderDTO updateOrderDTO) {
        Order orderFromDb = orderRepository.getById(id);
        orderFromDb.getProducts().forEach(pr->System.out.println(pr.getPrice()));
        BeanUtils.copyProperties(updateOrderDTO.getOrder(), orderFromDb, "id", "products", "fullPrice", "user", "created", "updated");
        updateOrderDTO.getDeletedProducts().forEach(productInOrder -> {
            productInOrderRepository.deleteByOrder_IdAndProduct_Id(id, productInOrder.getProduct().getId());
        });
        orderFromDb.setProducts(new HashSet<>());
        updateOrderDTO.getOrder().getProducts().forEach(productInOrder ->
                orderFromDb.addProduct(productInOrder.getProduct(), productInOrder.getQuantity())
        );
        return converterDTO.convert(OrderInfoDTO.class, orderRepository.saveAndFlush(orderFromDb));
    }


    public PageDTO<OrderInfoDTO> getOrders(OrderStatus status, Integer page) {
        Page<Long> orderIdsPage = null;
        if (status != null) {
            orderIdsPage = orderRepository.findByStatus(status, PageRequest.of(page, pageSize, Sort.by("id").descending()));
        } else {
            orderIdsPage = orderRepository.findAllBy(PageRequest.of(page, pageSize, Sort.by("id").descending()));
        }

        if (orderIdsPage != null && orderIdsPage.hasContent()) {
            return new PageDTO<>(orderRepository.findByIdIn(orderIdsPage.getContent(), OrderInfoDTO.class, Sort.by("id").descending()), orderIdsPage.getNumber(), orderIdsPage.getTotalPages());
        }
        return new PageDTO<>(Collections.emptyList(), 0, 0);
    }

    public OrderInfoDTO getOrderById(Long id) {
        return orderRepository.findById(id, OrderInfoDTO.class).orElseThrow(() -> new NotFoundException("Order: " + id + " not found"));
    }

}
