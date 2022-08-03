package WebStoreGroup.WebStore.Order;

import WebStoreGroup.WebStore.Order.DTO.OrderInfoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {

    @EntityGraph(attributePaths = {"products","products.product","user"})
    <T> Optional<T> findById(Long id, Class<T> type);

    @Query("select o.id from Order o")
    Page<Long> findAllBy(Pageable page);

    @Query("select o.id from Order o where o.status=:status")
    Page<Long> findByStatus(OrderStatus status,Pageable page);

    @EntityGraph(attributePaths = {"products","products.product","user"})
    <T> List<T> findByIdIn(List<Long> ids, Class<T> type, Sort sort);

    @Query("select o.id from Order o where o.user.id=:userId")
    Page<Long> findByUser_Id(@Param("userId") Long userId,Pageable page);
}
