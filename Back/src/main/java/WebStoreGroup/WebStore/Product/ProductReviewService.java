package WebStoreGroup.WebStore.Product;

import WebStoreGroup.WebStore.DTOUtils.ConverterDTO;
import WebStoreGroup.WebStore.Product.DTO.ProductReviewDTO.ProductReviewDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@Transactional
public class ProductReviewService {

    private final ProductReviewRepository productReviewRepository;
    private final ConverterDTO converterDTO;
    private final ProductRepository productRepository;


    @Autowired
    public ProductReviewService(ProductReviewRepository productReviewRepository, ConverterDTO converterDTO, ProductRepository productRepository) {
        this.productReviewRepository = productReviewRepository;
        this.converterDTO = converterDTO;
        this.productRepository = productRepository;
    }


    public ProductReviewDTO createReview(ProductReview review) {
        Product pr= productRepository.findById(review.getProduct().getId()).get();
        pr.addReview(review);
        Product product=productRepository.save(pr);
        ProductReview newReview=product.getReviews().stream().reduce((start,lastReview)->lastReview).get();
        return converterDTO.convert(ProductReviewDTO.class,newReview);
    }

    public void deleteReview(Long id) {
        productReviewRepository.deleteById(id);
    }

    public ProductReviewDTO updateReview(ProductReview review) {
        return converterDTO.convert(ProductReviewDTO.class, productReviewRepository.save(review));
    }
    public Long getProductReviewsCount(Long productId) {
        return productReviewRepository.countByProduct_Id(productId);
    }
}
