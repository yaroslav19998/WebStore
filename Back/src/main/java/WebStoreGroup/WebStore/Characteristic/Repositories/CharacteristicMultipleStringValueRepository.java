package WebStoreGroup.WebStore.Characteristic.Repositories;

import WebStoreGroup.WebStore.Characteristic.Entities.CharacteristicMultipleStringValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface CharacteristicMultipleStringValueRepository extends JpaRepository<CharacteristicMultipleStringValue,Long> {
}
