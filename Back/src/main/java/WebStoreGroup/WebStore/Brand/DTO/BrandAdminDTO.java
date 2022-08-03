package WebStoreGroup.WebStore.Brand.DTO;

import java.time.LocalDateTime;

public interface BrandAdminDTO extends BrandInfoDTO {
    LocalDateTime getCreated();
    LocalDateTime getUpdated();
}
