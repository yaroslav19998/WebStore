package WebStoreGroup.WebStore.Product;

import WebStoreGroup.WebStore.Order.DTO.OrderInfoDTO;
import WebStoreGroup.WebStore.Product.DTO.ProductReviewDTO.ProductReviewDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductReviewRepository extends JpaRepository<ProductReview,Long> {
    <T> Optional<T> findById(Long id, Class<T> type);

    <T> Page<T> findAllBy(Class<T> type, Pageable page);

    Page<ProductReviewDTO> findByProduct_Id(Long productId, Pageable page);
    Page<ProductReviewDTO> findByUser_Id(Long userId, Pageable page);

    Long countByProduct_Id(Long productId);
}
