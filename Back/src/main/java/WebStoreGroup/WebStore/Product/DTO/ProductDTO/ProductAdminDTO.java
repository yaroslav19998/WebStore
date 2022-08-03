package WebStoreGroup.WebStore.Product.DTO.ProductDTO;

import java.time.LocalDateTime;

public interface ProductAdminDTO extends ProductInfoForUserDTO{

        LocalDateTime getCreated();
        LocalDateTime getUpdated();
}
