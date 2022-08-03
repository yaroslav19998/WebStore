package WebStoreGroup.WebStore.Product.DTO.ProductReviewDTO;

import WebStoreGroup.WebStore.Product.DTO.ProductDTO.ProductIdNameDTO;
import WebStoreGroup.WebStore.User.DTO.UserIdNameDTO;

import java.time.LocalDateTime;

public interface ProductReviewDTO {
     Long getId();
     byte getRate();
     String getReview();
     UserIdNameDTO getUser();
     ProductIdNameDTO getProduct();
     LocalDateTime getCreated();
     LocalDateTime getUpdated();
     String getPros();
     String getCons();


}
