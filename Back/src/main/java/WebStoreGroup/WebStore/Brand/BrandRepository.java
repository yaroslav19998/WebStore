package WebStoreGroup.WebStore.Brand;

import WebStoreGroup.WebStore.Brand.DTO.BrandAdminDTO;
import WebStoreGroup.WebStore.Brand.DTO.BrandInfoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {

    Page<BrandAdminDTO> findByNameContainingIgnoreCase(String name, Pageable page);

    <T> Optional<T> findById(Long id, Class<T> type);

    <T> Page<T> findAllBy(Class<T> type, Pageable page);

    List<BrandInfoDTO> findByOrderByName();
}
