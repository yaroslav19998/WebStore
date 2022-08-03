package WebStoreGroup.WebStore.Characteristic.Entities.DTO;

import WebStoreGroup.WebStore.Characteristic.Entities.CharacteristicValueType;

public interface CharacteristicInfoDTO {
    long getId();
    String getName();
    CharacteristicValueType getValueType();
}
