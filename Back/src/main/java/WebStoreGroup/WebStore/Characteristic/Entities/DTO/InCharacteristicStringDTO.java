package WebStoreGroup.WebStore.Characteristic.Entities.DTO;


import WebStoreGroup.WebStore.Characteristic.Entities.Characteristic;

import javax.validation.Valid;
import java.util.List;

public class InCharacteristicStringDTO {

    @Valid
    private Characteristic characteristic;

    private List<String> stringList;

    public Characteristic getCharacteristic() {
        return characteristic;
    }

    public void setCharacteristic(Characteristic characteristic) {
        this.characteristic = characteristic;
    }

    public List<String> getStringList() {
        return stringList;
    }

    public void setStringList(List<String> stringList) {
        this.stringList = stringList;
    }
}
