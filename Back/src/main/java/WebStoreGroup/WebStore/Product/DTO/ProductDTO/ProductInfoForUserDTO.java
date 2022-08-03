package WebStoreGroup.WebStore.Product.DTO.ProductDTO;

import WebStoreGroup.WebStore.Characteristic.Entities.DTO.ProductCharacteristicsDTO.CharacteristicSingleStringValueDTO;
import WebStoreGroup.WebStore.Characteristic.Entities.DTO.ProductCharacteristicsDTO.CharacteristicMultipleStringValueDTO;
import WebStoreGroup.WebStore.Characteristic.Entities.DTO.ProductCharacteristicsDTO.ProductCharacteristicValueDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

public interface ProductInfoForUserDTO extends  ProductShortInfoForUserDTO {
    Set<ProductCharacteristicValueDTO<BigDecimal>> getDecimalCharacteristics();
    Set<ProductCharacteristicValueDTO<Integer>> getIntCharacteristics();
    Set<ProductCharacteristicValueDTO<LocalDateTime>> getDateCharacteristics();
    Set<CharacteristicMultipleStringValueDTO> getMultipleStringCharacteristics();
    Set<CharacteristicSingleStringValueDTO> getSingleStringCharacteristics();

}
