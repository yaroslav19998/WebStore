package WebStoreGroup.WebStore.Category.DTO;

import WebStoreGroup.WebStore.Brand.DTO.BrandInfoDTO;
import WebStoreGroup.WebStore.Characteristic.Entities.DTO.CharacteristicWithValuesDTO;

import java.util.Set;

public interface CategoryCharacteristicsBrandsDTO extends CategoryInfoDTO{
     Set<CharacteristicWithValuesDTO> getCharacteristics();
     Set<BrandInfoDTO> getBrands();
}
