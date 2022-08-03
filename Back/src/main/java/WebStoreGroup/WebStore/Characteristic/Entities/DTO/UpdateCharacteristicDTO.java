package WebStoreGroup.WebStore.Characteristic.Entities.DTO;

import WebStoreGroup.WebStore.Characteristic.Entities.Characteristic;

import javax.validation.Valid;
import java.util.Set;

public class UpdateCharacteristicDTO {
    @Valid
    private Characteristic characteristic;

    private Set<UpdateStringCharacteristicValueDTO> added;
    private Set<UpdateStringCharacteristicValueDTO> deleted;

    public Characteristic getCharacteristic() {
        return characteristic;
    }

    public void setCharacteristic(Characteristic characteristic) {
        this.characteristic = characteristic;
    }

    public Set<UpdateStringCharacteristicValueDTO> getAdded() {
        return added;
    }

    public void setAdded(Set<UpdateStringCharacteristicValueDTO> added) {
        this.added = added;
    }

    public Set<UpdateStringCharacteristicValueDTO> getDeleted() {
        return deleted;
    }

    public void setDeleted(Set<UpdateStringCharacteristicValueDTO> deleted) {
        this.deleted = deleted;
    }
}
