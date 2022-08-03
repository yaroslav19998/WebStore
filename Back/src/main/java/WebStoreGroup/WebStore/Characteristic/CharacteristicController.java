package WebStoreGroup.WebStore.Characteristic;

import WebStoreGroup.WebStore.Characteristic.Entities.DTO.CharacteristicAdminDTO;
import WebStoreGroup.WebStore.Characteristic.Entities.DTO.InCharacteristicStringDTO;
import WebStoreGroup.WebStore.Characteristic.Entities.DTO.CharacteristicWithValuesDTO;
import WebStoreGroup.WebStore.Characteristic.Entities.DTO.UpdateCharacteristicDTO;
import WebStoreGroup.WebStore.Characteristic.Services.CharacteristicService;
import WebStoreGroup.WebStore.DTOUtils.PageDTO;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("characteristics")

public class CharacteristicController {
    private final CharacteristicService characteristicService;
    @Autowired
    public CharacteristicController(CharacteristicService characteristicService) {
        this.characteristicService = characteristicService;
    }

    @GetMapping("")
    private ResponseEntity<PageDTO<? extends CharacteristicWithValuesDTO>> getCharacteristics(
            @RequestParam(name = "name",required = false) String name,
            @RequestParam(name = "page",required = false,defaultValue = "0") Integer pageNumber,
            @RequestHeader(value = "ResponseResourceFormat", required = false) String resourceFormat)  {

        if (resourceFormat != null && resourceFormat.equals("relations")) {
            return new ResponseEntity<>(characteristicService.getCharacteristicsForRelation(), HttpStatus.OK);
        }
        return new ResponseEntity<>(characteristicService.getCharacteristicsForAdmin(name,pageNumber), HttpStatus.OK);
    }

    @GetMapping("{id}")
    private ResponseEntity<CharacteristicAdminDTO> getCharacteristicById(@PathVariable("id") Long id)

    {
        return new ResponseEntity<>(characteristicService.getCharacteristicById(id), HttpStatus.OK);
    }
    @PostMapping(" ")
    private ResponseEntity<CharacteristicAdminDTO> createCharacteristic(@Valid  @RequestBody InCharacteristicStringDTO inCharacteristicStringDTO){
        CharacteristicAdminDTO ch=characteristicService.createCharacteristic(inCharacteristicStringDTO);
        System.out.println("crate");
        System.out.println(ch.getCharacteristicSingleStringValues());
        return new ResponseEntity<>(ch,HttpStatus.CREATED);
    }
    @DeleteMapping("{id}")
    private ResponseEntity<?> deleteCharacteristic(@PathVariable("id") Long id)
    {
        characteristicService.deleteCharacteristic(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PutMapping("{id}")
    private ResponseEntity<CharacteristicAdminDTO> updateCharacteristic(@RequestBody UpdateCharacteristicDTO updateCharacteristicDTO, @PathVariable Long id)
    {
        CharacteristicAdminDTO ch=characteristicService.updateCharacteristic(id,updateCharacteristicDTO);

        return new ResponseEntity<>(ch,HttpStatus.OK);
    }
}
