package WebStoreGroup.WebStore.Category;

import WebStoreGroup.WebStore.Category.DTO.CategoryIdNameDTO;
import WebStoreGroup.WebStore.Category.DTO.CategoryInfoDTO;
import WebStoreGroup.WebStore.Category.DTO.CategoryWithSubCategoriesDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {

    @Query("select c.id from Category c where UPPER(c.name) like UPPER(concat('%',?1,'%'))")
    Page<Long>  findIdsByNameContainingIgnoreCase(String name, Pageable page);

    @Query("select c.id from Category c")
    Page<Long> findPageIds(Pageable page);

    @EntityGraph(attributePaths = {"subCategories","brands","characteristics","parentCategory"})
    <T> List<T> findByIdIn(List<Long> ids, Class<T> type, Sort sort);

    @EntityGraph(attributePaths = {"subCategories","brands","characteristics.characteristicMultipleStringValues","characteristics.characteristicSingleStringValues","parentCategory","characteristics"})
    <T> Optional<T> findById(Long id, Class<T> type);



    @Query("select c.id as id,c.name as name from Category c")
    List<CategoryIdNameDTO> findAllForParentRelations();

    @EntityGraph(attributePaths = {"subCategories","subCategories.subCategories"})
    List<CategoryWithSubCategoriesDTO> findByDepth(byte depth);

    @Query("select c.id as id,c.name as name from Category c where c.depth>=2 ORDER BY c.name ASC")
    List<CategoryIdNameDTO> findCategoriesForRelations();

}
