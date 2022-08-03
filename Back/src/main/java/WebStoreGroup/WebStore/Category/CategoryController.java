package WebStoreGroup.WebStore.Category;

import WebStoreGroup.WebStore.Category.DTO.CategoryAdminDTO;
import WebStoreGroup.WebStore.Category.DTO.CategoryCharacteristicsBrandsDTO;
import WebStoreGroup.WebStore.Category.DTO.CategoryIdNameDTO;
import WebStoreGroup.WebStore.Category.DTO.CategoryInfoDTO;
import WebStoreGroup.WebStore.DTOUtils.PageDTO;
import WebStoreGroup.WebStore.Product.DTO.ProductDTO.ProductInfoForUserDTO;
import WebStoreGroup.WebStore.Product.DTO.ProductDTO.ProductShortInfoForUserDTO;
import WebStoreGroup.WebStore.Product.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.Map;

@RestController
@RequestMapping("categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final ProductService productService;

    @Autowired
    public CategoryController(CategoryService categoryService, ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }


    @GetMapping("")
    private ResponseEntity<PageDTO<? extends CategoryIdNameDTO>> getCategories(@RequestParam(name = "name", required = false) String name,
                                                                     @RequestParam(name = "page", required = false, defaultValue = "0") Integer pageNumber,
                                                                     @RequestHeader(value = "ResponseResourceFormat", required = false) String resourceFormat) throws Exception {

        if (resourceFormat != null && resourceFormat.equals("relations")) {
            return  new ResponseEntity<>(categoryService.getCategoriesForRelations(), HttpStatus.OK);

        } else if (resourceFormat != null && resourceFormat.equals("parent relations")) {
            return  new ResponseEntity<>(categoryService.getCategoriesForParentRelations(), HttpStatus.OK);
        }
        if (resourceFormat != null && resourceFormat.equals("admin")) {
            return  new ResponseEntity<>(categoryService.getCategoriesForAdmin(name,pageNumber), HttpStatus.OK);
        }
        return new ResponseEntity<>(categoryService.getCategoriesForPage(), HttpStatus.OK);
    }

    @GetMapping("{id}")
    private ResponseEntity<? extends CategoryInfoDTO> getCategoryById(@PathVariable("id") Long id,
                                                              @RequestHeader(value = "ResponseResourceFormat", required = false) String resourceFormat) {
        Class<? extends CategoryInfoDTO> type = resourceFormat.equals("admin") ? CategoryAdminDTO.class : CategoryCharacteristicsBrandsDTO.class;
        System.out.println("getcategory");
        return new ResponseEntity<>(categoryService.getCategoryById(id, type), HttpStatus.OK);
    }

    @GetMapping("{id}/products")
    private ResponseEntity<PageDTO<ProductShortInfoForUserDTO>> getProductsByCategoryId(@PathVariable("id") Long id,
                                                                                   @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                                                                                   @RequestParam(required = false) Map<String, String> params,
                                                                                   @RequestParam(name = "sort_by", required = false, defaultValue = "sales") String sortBy,
                                                                                   @RequestParam(name = "order", required = false, defaultValue = "DESC") Sort.Direction order) throws UnsupportedEncodingException, JsonProcessingException {
        PageDTO<ProductShortInfoForUserDTO> products = productService.getProductsCategory(id, params, page, sortBy, order);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PostMapping("")
    private ResponseEntity<CategoryAdminDTO> createCategory(@Valid @RequestBody Category category) {
        return new ResponseEntity<>(categoryService.createCategory(category), HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    private ResponseEntity<?> deleteCategory(@PathVariable("id") Long id) {
        categoryService.deleteCategory(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("{id}")
    private ResponseEntity<CategoryAdminDTO> updateCategory(@PathVariable("id") Long id, @Valid @RequestBody Category category) {
        return new ResponseEntity<>(categoryService.updateCategory(id, category), HttpStatus.OK);
    }

}
