package WebStoreGroup.WebStore.Characteristic.Entities.DTO;

public class UpdateStringCharacteristicValueDTO {
    private Long id;
    private String value;

    public UpdateStringCharacteristicValueDTO(Long id, String value) {
        this.id = id;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
