package WebStoreGroup.WebStore.Characteristic.Repositories;

import WebStoreGroup.WebStore.Brand.DTO.BrandAdminDTO;
import WebStoreGroup.WebStore.Brand.DTO.BrandInfoDTO;
import WebStoreGroup.WebStore.Characteristic.Entities.Characteristic;
import WebStoreGroup.WebStore.Characteristic.Entities.DTO.CharacteristicAdminDTO;
import WebStoreGroup.WebStore.Characteristic.Entities.DTO.CharacteristicWithValuesDTO;
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
public interface CharacteristicRepository extends JpaRepository<Characteristic,Long> {

    @EntityGraph(attributePaths = {"characteristicMultipleStringValues","characteristicSingleStringValues"})
    <T> List<T> findByIdIn(List<Long> ids, Class<T> type, Sort sort);

    @Query("select c.id from Characteristic c")
    Page<Long> findPageIds(Pageable page);

    @Query("select c.id from Characteristic c where UPPER(c.name) like UPPER(concat('%',?1,'%'))")
    Page<Long>  findIdsByNameContainingIgnoreCase(String name, Pageable page);

    @EntityGraph(attributePaths = {"characteristicMultipleStringValues","characteristicSingleStringValues"})
    <T> Optional<T> findById(Long id, Class<T> type);

    @EntityGraph(attributePaths = {"characteristicMultipleStringValues","characteristicSingleStringValues"})
    List<CharacteristicWithValuesDTO> findByOrderByName();
}
