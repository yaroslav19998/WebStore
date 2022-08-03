package WebStoreGroup.WebStore.Product.DTO.ProductImageDTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface ProductImageDTO {
    Long getId();
    @JsonProperty(value="is_main")
    boolean getIs_main();
}
