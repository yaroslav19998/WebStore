package WebStoreGroup.WebStore.Characteristic.Entities.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;

public interface CharacteristicAdminDTO extends CharacteristicWithValuesDTO {
    LocalDateTime getCreated();
    LocalDateTime getUpdated();
}
