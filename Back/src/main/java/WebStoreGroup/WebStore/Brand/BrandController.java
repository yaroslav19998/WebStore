package WebStoreGroup.WebStore.Brand;

import WebStoreGroup.WebStore.Brand.DTO.BrandAdminDTO;
import WebStoreGroup.WebStore.Brand.DTO.BrandInfoDTO;
import WebStoreGroup.WebStore.DTOUtils.PageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("brands")
public class BrandController {

    private final BrandService brandService;

    @Autowired
    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }


    @GetMapping("")
    private ResponseEntity<PageDTO<? extends BrandInfoDTO>> getBrands(@RequestParam(name = "name", required = false) String name,
                                                                      @RequestParam(name = "page", required = false, defaultValue = "0") Integer pageNumber,
                                                                      @RequestHeader(value = "ResponseResourceFormat", required = false) String resourceFormat) throws Exception {
        PageDTO<? extends BrandInfoDTO> brands;
        if (name != null) {
            brands = brandService.getBrandsByName(name, pageNumber);
        }
        if (resourceFormat != null && resourceFormat.equals("admin")) {
            brands = brandService.getBrandsForAdmin(pageNumber);
        } else if (resourceFormat != null && resourceFormat.equals("relations")) {
            brands = brandService.getBrandsForRelations();
        } else {
            brands = brandService.getBrands(pageNumber);
        }
        return new ResponseEntity<>(brands, HttpStatus.OK);
    }

    @GetMapping("{id}")
    private ResponseEntity<? extends BrandInfoDTO> getBrandById(@PathVariable("id") Long id,
                                                                @RequestHeader(value = "ResponseResourceFormat", required = false) String resourceFormat) {
        Class<? extends BrandInfoDTO> type = resourceFormat.equals("admin") ? BrandAdminDTO.class : BrandInfoDTO.class;
        BrandInfoDTO brand = brandService.getBrandById(id, type);
        return new ResponseEntity<>(brand, HttpStatus.OK);
    }

    @PostMapping(" ")
    private ResponseEntity<BrandAdminDTO> createBrand(@Valid @RequestBody Brand brand) {
        BrandAdminDTO newBrand = brandService.createBrand(brand);
        return new ResponseEntity<>(newBrand, HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    private ResponseEntity<?> deleteBrand(@PathVariable("id") Long id) {
        brandService.deleteBrand(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("{id}")
    private ResponseEntity<BrandAdminDTO> updateBrand(@Valid @RequestBody Brand brand) {
        BrandAdminDTO updateBrand = brandService.updateBrand(brand);
        return new ResponseEntity<>(updateBrand, HttpStatus.OK);
    }

}
