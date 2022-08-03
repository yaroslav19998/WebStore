package WebStoreGroup.WebStore.Product.DTO.ProductDTO;

import WebStoreGroup.WebStore.Brand.DTO.BrandInfoDTO;

import java.math.BigDecimal;

public interface ProductInfoDTO extends ProductIdNameDTO{
    String getDescription();
    BigDecimal getPrice();
    int getSales();
}
