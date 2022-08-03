package WebStoreGroup.WebStore.Characteristic.Entities.DTO;

import WebStoreGroup.WebStore.Characteristic.Entities.CharacteristicMultipleStringValue;
import WebStoreGroup.WebStore.Characteristic.Entities.CharacteristicSingleStringValue;
import WebStoreGroup.WebStore.Characteristic.Entities.CharacteristicValueType;
import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.Set;

public class CharacteristicRelation {

    private Long id;

    private String name;
    private CharacteristicValueType valueType;

    private Set<CharacteristicMultipleStringValue> characteristicMultipleStringValues;

    private Set<CharacteristicSingleStringValue> characteristicSingleStringValues;

    public CharacteristicRelation(Long id, String name, CharacteristicValueType valueType, Set<CharacteristicMultipleStringValue> characteristicMultipleStringValues, Set<CharacteristicSingleStringValue> characteristicSingleStringValues) {
        this.id = id;
        this.name = name;
        this.valueType = valueType;
        this.characteristicMultipleStringValues = characteristicMultipleStringValues;
        this.characteristicSingleStringValues = characteristicSingleStringValues;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CharacteristicValueType getValueType() {
        return valueType;
    }

    public void setValueType(CharacteristicValueType valueType) {
        this.valueType = valueType;
    }

    public Set<CharacteristicMultipleStringValue> getCharacteristicMultipleStringValues() {
        return characteristicMultipleStringValues;
    }

    public void setCharacteristicMultipleStringValues(Set<CharacteristicMultipleStringValue> characteristicMultipleStringValues) {
        this.characteristicMultipleStringValues = characteristicMultipleStringValues;
    }

    public Set<CharacteristicSingleStringValue> getCharacteristicSingleStringValues() {
        return characteristicSingleStringValues;
    }

    public void setCharacteristicSingleStringValues(Set<CharacteristicSingleStringValue> characteristicSingleStringValues) {
        this.characteristicSingleStringValues = characteristicSingleStringValues;
    }
}
