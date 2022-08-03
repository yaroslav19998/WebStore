package WebStoreGroup.WebStore.Product.DTO.Search;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.time.LocalDate;

public class DateParam {

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    LocalDate minValue;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    LocalDate maxValue;

    public LocalDate getMinValue() {
        return minValue;
    }

    public void setMinValue(LocalDate minValue) {
        this.minValue = minValue;
    }

    public LocalDate getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(LocalDate maxValue) {
        this.maxValue = maxValue;
    }
}
