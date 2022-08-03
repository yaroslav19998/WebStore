package WebStoreGroup.WebStore.Order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ProductInOrderRepository extends JpaRepository<ProductInOrder,Long> {
    @Transactional
    void deleteByOrder_IdAndProduct_Id(@Param("orderId")Long characteristicId, @Param("productId")Long product_id);
}
