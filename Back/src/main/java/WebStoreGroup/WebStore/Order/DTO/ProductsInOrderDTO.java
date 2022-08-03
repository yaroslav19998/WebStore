package WebStoreGroup.WebStore.Order.DTO;

import WebStoreGroup.WebStore.Product.DTO.ProductDTO.ProductInfoDTO;
import WebStoreGroup.WebStore.Product.DTO.ProductDTO.ProductInfoForUserDTO;

import java.math.BigDecimal;

public interface ProductsInOrderDTO {
    ProductInfoDTO getProduct();
    int getQuantity();
    BigDecimal getPrice();
 }
