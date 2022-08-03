package WebStoreGroup.WebStore.Order;

import WebStoreGroup.WebStore.DTOUtils.PageDTO;
import WebStoreGroup.WebStore.Order.DTO.InProductInOrderDTO;
import WebStoreGroup.WebStore.Order.DTO.OrderInfoDTO;
import WebStoreGroup.WebStore.Order.DTO.UpdateOrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("orders")
public class OrderController {
    private final OrderService orderService;
    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @GetMapping("")
    private ResponseEntity<PageDTO<OrderInfoDTO>> getOrders(@RequestParam(name = "status",required = false) OrderStatus status,
                                                      @RequestParam(name = "page",required = false,defaultValue = "0") Integer page)

    {
        PageDTO<OrderInfoDTO> orders= orderService.getOrders(status,page);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
    @GetMapping("{id}")
    private ResponseEntity<OrderInfoDTO> getOrderById(@PathVariable("id") Long id)
    {
        return new ResponseEntity<>(orderService.getOrderById(id), HttpStatus.OK);
    }
    @PostMapping("")
    private ResponseEntity<OrderInfoDTO> createOrder(@Valid @RequestBody InProductInOrderDTO order){
        OrderInfoDTO orderInfoDTO=orderService.createOrder(order);
        return new ResponseEntity<>(orderInfoDTO,HttpStatus.CREATED);
    }
    @DeleteMapping("{id}")
    private ResponseEntity<?> deleteOrder(@PathVariable("id") Long id)
    {
        orderService.deleteOrder(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PutMapping("{id}")
    private ResponseEntity<OrderInfoDTO> updateOrder(@PathVariable("id") Long id,@Valid @RequestBody UpdateOrderDTO order)
    {

        OrderInfoDTO updateOrder = orderService.updateOrder(id,order);
        System.out.println("7");
        return new ResponseEntity<>(updateOrder,HttpStatus.OK);
    }
    @GetMapping("statuses")
    private ResponseEntity<List<OrderStatus>> getAllStatuses()

    {
        List<OrderStatus> statuses = new ArrayList<>(Arrays.asList(OrderStatus.values()));
        return new ResponseEntity<>(statuses, HttpStatus.OK);
    }

}
