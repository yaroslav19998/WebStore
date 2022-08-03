package WebStoreGroup.WebStore.Product;

import WebStoreGroup.WebStore.Product.DTO.ProductDTO.ProductAdminDTO;
import WebStoreGroup.WebStore.Product.DTO.ProductDTO.ProductIdDTO;
import WebStoreGroup.WebStore.Product.DTO.ProductDTO.ProductInfoForUserDTO;
import WebStoreGroup.WebStore.Product.DTO.ProductDTO.ProductShortInfoForUserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import th.co.geniustree.springdata.jpa.repository.JpaSpecificationExecutorWithProjection;

import org.springframework.data.domain.Pageable;
import th.co.geniustree.springdata.jpa.repository.support.JpaSpecificationExecutorWithProjectionImpl;

import java.util.List;
import java.util.Optional;

//,
@Repository
public interface ProductRepository extends JpaRepository<Product,Long>, JpaSpecificationExecutorWithProjection<Product> {


   <T> Page<T> findAll(Specification<Product> spec,Class<T> type,Pageable page);

   <T> Page<T> findAllBy(Class<T> type, Pageable pageable);


   @Query("select p from Product p where p.id in :ids")


   @EntityGraph(attributePaths = {"images","category","brand",
           "intCharacteristics", "dateCharacteristics", "decimalCharacteristics", "singleStringCharacteristics", "multipleStringCharacteristics",
           "intCharacteristics.characteristic", "dateCharacteristics.characteristic", "decimalCharacteristics.characteristic",
           "singleStringCharacteristics.characteristic", "singleStringCharacteristics.value","multipleStringCharacteristics.characteristic"})
   List<ProductAdminDTO> findProductForAdminByIdIn(List<Long> ids, Sort sort);

   @Query("select p from Product p where p.id in :ids")
   @EntityGraph(attributePaths = {"images","category","brand"})
   @Transactional(readOnly=true)
   List<ProductShortInfoForUserDTO> findProductForPageByIdIn(List<Long> ids, Sort sort);

   @EntityGraph(attributePaths = {"images","category","brand",
           "intCharacteristics", "dateCharacteristics", "decimalCharacteristics", "singleStringCharacteristics", "multipleStringCharacteristics",
           "intCharacteristics.characteristic", "dateCharacteristics.characteristic", "decimalCharacteristics.characteristic", "singleStringCharacteristics.characteristic",
           "singleStringCharacteristics.value","multipleStringCharacteristics.characteristic"})
   <T> Optional<T> findById(Long id, Class<T> type);

   @EntityGraph(attributePaths = {"images","category","brand"})
   List<ProductShortInfoForUserDTO> findTop4ByNameContainingIgnoreCase(String name);

}
