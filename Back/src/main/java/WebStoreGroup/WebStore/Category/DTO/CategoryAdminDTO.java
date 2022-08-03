package WebStoreGroup.WebStore.Category.DTO;

import WebStoreGroup.WebStore.Brand.Brand;
import WebStoreGroup.WebStore.Brand.DTO.BrandInfoDTO;
import WebStoreGroup.WebStore.Characteristic.Entities.DTO.CharacteristicInfoDTO;

import java.time.LocalDateTime;
import java.util.Set;

public interface CategoryAdminDTO extends CategoryInfoDTO {
    CategoryIdNameDTO getParentCategory();
    Set<CharacteristicInfoDTO> getCharacteristics();
    Set<BrandInfoDTO> getBrands();
    Set<CategoryInfoDTO> getSubCategories();
    LocalDateTime getCreated();
    LocalDateTime getUpdated();
}
