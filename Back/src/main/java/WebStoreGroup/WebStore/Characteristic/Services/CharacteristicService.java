package WebStoreGroup.WebStore.Characteristic.Services;

import WebStoreGroup.WebStore.Characteristic.Entities.*;
import WebStoreGroup.WebStore.Characteristic.Entities.DTO.*;
import WebStoreGroup.WebStore.Characteristic.Repositories.*;
import WebStoreGroup.WebStore.DTOUtils.ConverterDTO;
import WebStoreGroup.WebStore.Exceptions.NotFoundException;
import WebStoreGroup.WebStore.DTOUtils.PageDTO;
import WebStoreGroup.WebStore.Product.Product;
import org.hibernate.Hibernate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Validated
@Transactional
public class CharacteristicService {
    private final CharacteristicRepository characteristicRepository;
    private final CharacteristicSingleStringValueRepository characteristicSingleStringValueRepository;
    private final CharacteristicMultipleStringValueRepository characteristicMultipleValueRepository;
    private final ProductCharacteristicSingleStringValueRepository productCharacteristicSingleStringValueRepository;
    private final ProductCharacteristicDecimalValueRepository productCharacteristicDecimalValueRepository;
    private final ProductCharacteristicIntValueRepository productCharacteristicIntValueRepository;
    private final ProductCharacteristicDateValueRepository productCharacteristicDateValueRepository;
    private final ConverterDTO converterDTO;

    @Value("${default-page-size}")
    private int pageSize;

    @Autowired
    public CharacteristicService(CharacteristicRepository characteristicRepository,
                                 CharacteristicSingleStringValueRepository characteristicSingleStringValueRepository,
                                 CharacteristicMultipleStringValueRepository characteristicMultipleValueRepository,
                                 ProductCharacteristicSingleStringValueRepository productCharacteristicSingleStringValueRepository,
                                 ProductCharacteristicDecimalValueRepository productCharacteristicDecimalValueRepository,
                                 ProductCharacteristicIntValueRepository productCharacteristicIntValueRepository,
                                 ProductCharacteristicDateValueRepository productCharacteristicDateValueRepository,
                                 ConverterDTO converterDTO) {
        this.characteristicRepository = characteristicRepository;
        this.characteristicSingleStringValueRepository = characteristicSingleStringValueRepository;
        this.characteristicMultipleValueRepository = characteristicMultipleValueRepository;
        this.productCharacteristicSingleStringValueRepository = productCharacteristicSingleStringValueRepository;
        this.productCharacteristicDecimalValueRepository = productCharacteristicDecimalValueRepository;
        this.productCharacteristicIntValueRepository = productCharacteristicIntValueRepository;
        this.productCharacteristicDateValueRepository = productCharacteristicDateValueRepository;
        this.converterDTO = converterDTO;
    }

    public CharacteristicAdminDTO createCharacteristic(InCharacteristicStringDTO inCharacteristicStringDTO) {
        Characteristic Characteristic = characteristicRepository.save(inCharacteristicStringDTO.getCharacteristic());

        switch (Characteristic.getValueType()) {
            case MULTIPLESTRING:
                Set<CharacteristicMultipleStringValue> multipleStringValues = new HashSet<CharacteristicMultipleStringValue>();
                inCharacteristicStringDTO.getStringList().forEach(value->multipleStringValues.add(new CharacteristicMultipleStringValue(value, Characteristic)));
                Characteristic.setCharacteristicMultipleStringValues(multipleStringValues);
                break;
            case SINGLESTRING:
                Set<CharacteristicSingleStringValue> singleStringValues = new HashSet<CharacteristicSingleStringValue>();
                inCharacteristicStringDTO.getStringList().forEach(value->singleStringValues.add(new CharacteristicSingleStringValue(value, Characteristic)));
                Characteristic.setCharacteristicSingleStringValues(singleStringValues);
                break;
        }
        return converterDTO.convert(CharacteristicAdminDTO.class, characteristicRepository.save(Characteristic));
    }

    public void deleteCharacteristic(Long id) {
        characteristicRepository.deleteById(id);
    }

    public CharacteristicAdminDTO updateCharacteristic(Long id, UpdateCharacteristicDTO characteristicDTO) {
        Characteristic characteristicFromDb = characteristicRepository.getById(id);

        BeanUtils.copyProperties(characteristicDTO.getCharacteristic(), characteristicFromDb, "id", "created", "updated",
                "categories", "characteristicMultipleStringValues", "characteristicSingleStringValues");
     //   DeleteUpdatedCharacteristicValue(characteristicFromDb,characteristicDTO);
        switch (characteristicDTO.getCharacteristic().getValueType()) {
            case MULTIPLESTRING:
                if (characteristicDTO.getDeleted() != null) {

                    for (UpdateStringCharacteristicValueDTO deleted : characteristicDTO.getDeleted()) {
                        System.out.println("deleted");
                        System.out.println( deleted.getId());

                        characteristicMultipleValueRepository.deleteById(deleted.getId());
                    }
                }
                if (characteristicDTO.getAdded() != null) {
                    characteristicFromDb.getCharacteristicMultipleStringValues().addAll(
                            characteristicDTO.getAdded()
                                    .stream()
                                    .map((added) -> new CharacteristicMultipleStringValue(added.getValue(), characteristicFromDb))
                                    .collect(Collectors.toSet()));
                }
                break;
            case SINGLESTRING:
                if (characteristicDTO.getDeleted() != null ) {

                    for (UpdateStringCharacteristicValueDTO deleted : characteristicDTO.getDeleted()) {
                        characteristicSingleStringValueRepository.deleteById(deleted.getId());
                    }
                }
                if (characteristicDTO.getAdded() != null) {
                    characteristicFromDb.getCharacteristicSingleStringValues().addAll(
                            characteristicDTO.getAdded()
                                    .stream()
                                    .map((added) -> new CharacteristicSingleStringValue(added.getValue(), characteristicFromDb))
                                    .collect(Collectors.toSet()));
                }
                break;

        }
        Characteristic ch=characteristicRepository.saveAndFlush(characteristicFromDb);
        CharacteristicAdminDTO ch2= converterDTO.convert(CharacteristicAdminDTO.class, ch);
        Hibernate.initialize(ch2.getCharacteristicSingleStringValues());
        Hibernate.initialize(ch2.getCharacteristicMultipleStringValues());
        return   ch2;
    }

//    public  void DeleteUpdatedCharacteristicValue(Characteristic updatedCharacteristic,UpdateCharacteristicDTO characteristicDTO){
//        switch (updatedCharacteristic.getValueType()) {
//            case MULTIPLESTRING:
//                if (characteristicDTO.getDeleted() != null) {
//
//                    for (UpdateStringCharacteristicValueDTO deleted : characteristicDTO.getDeleted()) {
//                        System.out.println("deleted");
//                        System.out.println( deleted.getId());
//
//                        characteristicMultipleValueRepository.deleteById(deleted.getId());
//                    }
//                }
//                if (characteristicDTO.getAdded() != null) {
//                    updatedCharacteristic.getCharacteristicMultipleStringValues().addAll(
//                            characteristicDTO.getAdded()
//                                    .stream()
//                                    .map((added) -> new CharacteristicMultipleStringValue(added.getValue(), updatedCharacteristic))
//                                    .collect(Collectors.toSet()));
//                }
//                break;
//            case SINGLESTRING:
//                if (characteristicDTO.getDeleted() != null ) {
//
//                    for (UpdateStringCharacteristicValueDTO deleted : characteristicDTO.getDeleted()) {
//                        characteristicSingleStringValueRepository.deleteById(deleted.getId());
//                    }
//                }
//                if (characteristicDTO.getAdded() != null) {
//                    updatedCharacteristic.getCharacteristicSingleStringValues().addAll(
//                            characteristicDTO.getAdded()
//                                    .stream()
//                                    .map((added) -> new CharacteristicSingleStringValue(added.getValue(), updatedCharacteristic))
//                                    .collect(Collectors.toSet()));
//                }
//                break;
//
//        }
//    }
    public PageDTO<CharacteristicWithValuesDTO> getCharacteristicsForRelation(){
        return new PageDTO<>(characteristicRepository.findByOrderByName(),0,0);
    }
    public PageDTO<CharacteristicAdminDTO> getCharacteristicsForAdmin(String name, Integer pageNumber){
        Page<Long> characteristicIdsPage;
        if(name!=null){
            characteristicIdsPage = getCharacteristicsIdsByName(name, pageNumber);
        }
        else {
            characteristicIdsPage = characteristicRepository.findPageIds(PageRequest.of(pageNumber, pageSize, Sort.by("id").descending()));
        }
        if (characteristicIdsPage != null && characteristicIdsPage.hasContent()) {
             return new PageDTO<>(getCharacteristicsByIds(characteristicIdsPage.getContent(),CharacteristicAdminDTO.class),
                  characteristicIdsPage.getNumber(),characteristicIdsPage.getTotalPages());
        }
        return new PageDTO<>(Collections.emptyList(), 0, 0);
    }
    public Page<Long> getCharacteristicsIdsByName(String name, Integer pageNumber){
       return characteristicRepository.findIdsByNameContainingIgnoreCase(name,PageRequest.of(pageNumber, pageSize, Sort.by("id").descending()));
    }
    private  <T extends CharacteristicInfoDTO> List<T> getCharacteristicsByIds(List<Long> ids,Class<T> type){
        return characteristicRepository.findByIdIn(ids, type,Sort.by("id").descending());
    }

    public CharacteristicAdminDTO getCharacteristicById(Long id) {
        return characteristicRepository.findById(id, CharacteristicAdminDTO.class).orElseThrow(()-> new NotFoundException("Characteristic: "+id+ " not found"));
    }


    public void deleteProductCharacteristic(Long characteristicId, Product product, CharacteristicValueType valueType) {
        Long productId = product.getId();
        switch (valueType) {
            case SINGLESTRING:
                productCharacteristicSingleStringValueRepository.deleteByCharacteristic_IdAndProduct_Id(characteristicId, productId);
                break;
            case DATE:
                productCharacteristicDateValueRepository.deleteByCharacteristic_IdAndProduct_Id(characteristicId, productId);
                break;
            case DECIMAL:
                productCharacteristicDecimalValueRepository.deleteByCharacteristic_IdAndProduct_Id(characteristicId, productId);
                break;
            case INT:
                productCharacteristicIntValueRepository.deleteByCharacteristic_IdAndProduct_Id(characteristicId, productId);
                break;
            case MULTIPLESTRING:
                Characteristic characteristic = characteristicRepository.getById(characteristicId);
                characteristic.getCharacteristicMultipleStringValues().stream().forEach(multipleStringValue -> multipleStringValue.removeProduct(product));
                characteristicRepository.save(characteristic);
                break;
        }
    }
}
