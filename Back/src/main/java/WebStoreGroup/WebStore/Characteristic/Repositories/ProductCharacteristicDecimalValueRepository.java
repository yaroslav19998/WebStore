package WebStoreGroup.WebStore.Characteristic.Repositories;

import WebStoreGroup.WebStore.Characteristic.Entities.ProductCharacteristicDecimalValue;
import WebStoreGroup.WebStore.Characteristic.Entities.ProductCharacteristicIntValue;
import WebStoreGroup.WebStore.Characteristic.Entities.ProductCharacteristicSingleStringValue;
import WebStoreGroup.WebStore.Characteristic.Entities.ProductCharacteristicValueId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface ProductCharacteristicDecimalValueRepository extends JpaRepository<ProductCharacteristicDecimalValue, ProductCharacteristicValueId> {
    @Transactional
    Optional<ProductCharacteristicDecimalValue> findByCharacteristic_IdAndProduct_Id(@Param("characteristicId")Long characteristicId,
                                                                                 @Param("productId")Long product_id);
    @Transactional
    void deleteByCharacteristic_IdAndProduct_Id(@Param("characteristicId")Long characteristicId, @Param("productId")Long product_id);
}
