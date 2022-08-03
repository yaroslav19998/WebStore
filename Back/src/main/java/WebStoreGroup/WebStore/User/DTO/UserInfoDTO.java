package WebStoreGroup.WebStore.User.DTO;

import WebStoreGroup.WebStore.Order.DTO.OrderInfoDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface UserInfoDTO extends UserIdNameDTO {
    LocalDateTime getCreated();
    LocalDateTime getUpdated();
    String getEmail();
}
