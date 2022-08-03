package WebStoreGroup.WebStore.Characteristic.Entities.DTO.ProductCharacteristicsDTO;

import WebStoreGroup.WebStore.Characteristic.Entities.DTO.CharacteristicInfoDTO;
import WebStoreGroup.WebStore.Characteristic.Entities.DTO.CharacteristicWithValuesDTO;

public interface CharacteristicMultipleStringValueDTO {
    Long getId();
    CharacteristicInfoDTO getCharacteristic();
    String getValue();
}
