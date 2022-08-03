package WebStoreGroup.WebStore.Characteristic.Entities.DTO.ProductCharacteristicsDTO;

import WebStoreGroup.WebStore.Characteristic.Entities.DTO.CharacteristicInfoDTO;

public interface ProductCharacteristicValueDTO<T> {
    CharacteristicInfoDTO getCharacteristic();
    T getValue();
}
