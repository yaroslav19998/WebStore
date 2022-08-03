package WebStoreGroup.WebStore.Characteristic.Repositories;

import WebStoreGroup.WebStore.Characteristic.Entities.ProductCharacteristicSingleStringValue;
import WebStoreGroup.WebStore.Characteristic.Entities.ProductCharacteristicValueId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Repository
public interface ProductCharacteristicSingleStringValueRepository  extends JpaRepository<ProductCharacteristicSingleStringValue, ProductCharacteristicValueId> {

    @Transactional
    Optional<ProductCharacteristicSingleStringValueRepository> findByCharacteristic_IdAndProduct_Id(@Param("characteristicId")Long characteristicId, @Param("productId")Long product_id);
    @Transactional
    void deleteByCharacteristic_IdAndProduct_Id(@Param("characteristicId")Long characteristicId, @Param("productId")Long product_id);
}
