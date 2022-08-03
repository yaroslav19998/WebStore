package WebStoreGroup.WebStore.Order.DTO;

import WebStoreGroup.WebStore.Order.OrderStatus;
import WebStoreGroup.WebStore.User.DTO.UserIdNameDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface OrderInfoDTO {
    long getId();
    String getCustomerEmail();
    String getCustomerPhone();
    String getCustomerAddress();
    String getCustomerName();
    OrderStatus getStatus();
    LocalDateTime getCreated();
    LocalDateTime getUpdated();
    BigDecimal getFullPrice();
    UserIdNameDTO getUser();
    Set<ProductsInOrderDTO> getProducts();
}
