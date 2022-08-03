package WebStoreGroup.WebStore.Characteristic.Entities.DTO;

import WebStoreGroup.WebStore.Characteristic.Entities.CharacteristicValueType;

import java.util.Set;

public interface CharacteristicWithValuesDTO extends CharacteristicInfoDTO {

    Set<StringValueInterface> getCharacteristicMultipleStringValues();
    Set<StringValueInterface> getCharacteristicSingleStringValues();
}
