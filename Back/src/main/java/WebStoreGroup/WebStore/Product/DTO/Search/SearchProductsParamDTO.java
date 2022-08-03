package WebStoreGroup.WebStore.Product.DTO.Search;

import WebStoreGroup.WebStore.Category.Category;
import WebStoreGroup.WebStore.Characteristic.Entities.*;
import WebStoreGroup.WebStore.Product.Product;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class SearchProductsParamDTO {
    private Characteristic characteristic;
    private String name;
    private SearchValueDTO<BigDecimal> decimalValue;
    private SearchValueDTO<Integer> intValue;
    private DateParam dateValue;
    private Set<Long> singleStringValues;
    private Set<Long> multipleStringValues;
    private Set<Long> brandIds;
    private SearchValueDTO<BigDecimal> price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Characteristic getCharacteristic() {
        return characteristic;
    }

    public void setCharacteristic(Characteristic characteristic) {
        this.characteristic = characteristic;
    }

    public SearchValueDTO<BigDecimal> getDecimalValue() {
        return decimalValue;
    }

    public void setDecimalValue(SearchValueDTO<BigDecimal> decimalValue) {
        this.decimalValue = decimalValue;
    }

    public SearchValueDTO<Integer> getIntValue() {
        return intValue;
    }

    public void setIntValue(SearchValueDTO<Integer> intValue) {
        this.intValue = intValue;
    }

    public DateParam getDateValue() {
        return dateValue;
    }

    public void setDateValue(DateParam dateValue) {
        this.dateValue = dateValue;
    }

    public Set<Long> getSingleStringValues() {
        return singleStringValues;
    }

    public void setSingleStringValues(Set<Long> singleStringValues) {
        this.singleStringValues = singleStringValues;
    }

    public Set<Long> getMultipleStringValues() {
        return multipleStringValues;
    }

    public void setMultipleStringValues(Set<Long> multipleStringValues) {
        this.multipleStringValues = multipleStringValues;
    }

    public Set<Long> getBrandIds() {
        return brandIds;
    }

    public void setBrandIds(Set<Long> brandIds) {
        this.brandIds = brandIds;
    }

    public SearchValueDTO<BigDecimal> getPrice() {
        return price;
    }

    public void setPrice(SearchValueDTO<BigDecimal> price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "SearchProductsParamDTO{" +
                "characteristic=" + characteristic +
                ", decimalValue=" + decimalValue +
                ", intValue=" + intValue +
                ", dateValue=" + dateValue +
                ", singleStringValues=" + singleStringValues +
                ", multipleStringValues=" + multipleStringValues +
                '}';
    }
}
