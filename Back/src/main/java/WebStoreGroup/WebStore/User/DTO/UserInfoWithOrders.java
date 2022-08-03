package WebStoreGroup.WebStore.User.DTO;

import WebStoreGroup.WebStore.Order.DTO.OrderInfoDTO;

import java.util.List;

public interface UserInfoWithOrders  extends UserInfoDTO{
    List<OrderInfoDTO> getOrders();
}
