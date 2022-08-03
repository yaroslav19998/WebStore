package WebStoreGroup.WebStore.Product.DTO.ProductDTO;

import WebStoreGroup.WebStore.Characteristic.Entities.*;
import WebStoreGroup.WebStore.Product.Product;
import WebStoreGroup.WebStore.Product.ProductImage;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.*;

public class InCreateUpdateProductDTO {
    @Valid
    private Product product;
    private List<ProductCharacteristicSingleStringValue> singleStrings;
    private List<ProductCharacteristicDecimalValue> decimalValues;
    private List<CharacteristicMultipleStringValue> multipleStrings;
    private List<ProductCharacteristicIntValue> intValues;
    private List<ProductCharacteristicDateValue> dateValues;
    private Set<ProductCharacteristicDTO> deletedCharacteristics;
    private Set<ProductImage> deletedImages;
    private boolean updatedImageIsMain;
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<ProductCharacteristicSingleStringValue> getSingleStrings() {
        return singleStrings;
    }

    public void setSingleStrings(List<ProductCharacteristicSingleStringValue> singleStrings) {
        this.singleStrings = singleStrings;
    }

    public List<ProductCharacteristicDecimalValue> getDecimalValues() {
        return decimalValues;
    }

    public void setDecimalValues(List<ProductCharacteristicDecimalValue> decimalValues) {
        this.decimalValues = decimalValues;
    }

    public List<CharacteristicMultipleStringValue> getMultipleStrings() {
        return multipleStrings;
    }

    public void setMultipleStrings(List<CharacteristicMultipleStringValue> multipleStrings) {
        this.multipleStrings = multipleStrings;
    }

    public List<ProductCharacteristicIntValue> getIntValues() {
        return intValues;
    }

    public void setIntValues(List<ProductCharacteristicIntValue> intValues) {
        this.intValues = intValues;
    }

    public List<ProductCharacteristicDateValue> getDateValues() {

            return dateValues;
    }

    public void setDateValues(List<ProductCharacteristicDateValue> dateValues) {
        this.dateValues = dateValues;
    }

    public Set<ProductCharacteristicDTO> getDeletedCharacteristics() {
        return deletedCharacteristics;
    }

    public void setDeletedCharacteristics(Set<ProductCharacteristicDTO> deletedCharacteristics) {
        this.deletedCharacteristics = deletedCharacteristics;
    }

    public Set<ProductImage> getDeletedImages() {
        return deletedImages;
    }

    public void setDeletedImages(Set<ProductImage> deletedImages) {
        this.deletedImages = deletedImages;
    }

    public boolean getUpdatedImageIsMain() {
        return updatedImageIsMain;
    }

    public void setUpdatedImageIsMain(boolean updatedImageIsMain) {
        this.updatedImageIsMain = updatedImageIsMain;
    }
}
