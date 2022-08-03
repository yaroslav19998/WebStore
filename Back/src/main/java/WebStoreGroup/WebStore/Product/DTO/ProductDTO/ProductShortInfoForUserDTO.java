package WebStoreGroup.WebStore.Product.DTO.ProductDTO;

import WebStoreGroup.WebStore.Brand.DTO.BrandInfoDTO;
import WebStoreGroup.WebStore.Category.DTO.CategoryIdNameDTO;
import WebStoreGroup.WebStore.Product.DTO.ProductImageDTO.ProductImageDTO;
import WebStoreGroup.WebStore.Product.DTO.ProductReviewDTO.ProductReviewDTO;

import java.math.BigDecimal;
import java.util.Set;

public interface ProductShortInfoForUserDTO extends ProductInfoDTO {
    BrandInfoDTO getBrand();
    CategoryIdNameDTO getCategory();
    Set<ProductImageDTO> getImages();
    BigDecimal getAverageRate();
}
