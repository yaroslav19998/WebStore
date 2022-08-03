package WebStoreGroup.WebStore.Characteristic.Repositories;

import WebStoreGroup.WebStore.Characteristic.Entities.Characteristic;
import WebStoreGroup.WebStore.Characteristic.Entities.CharacteristicSingleStringValue;
import WebStoreGroup.WebStore.Product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface CharacteristicSingleStringValueRepository extends JpaRepository<CharacteristicSingleStringValue,Long> {

}
