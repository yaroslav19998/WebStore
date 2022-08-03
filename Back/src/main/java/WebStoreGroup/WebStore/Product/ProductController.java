package WebStoreGroup.WebStore.Product;

import WebStoreGroup.WebStore.DTOUtils.PageDTO;
import WebStoreGroup.WebStore.Product.DTO.ProductDTO.*;
import WebStoreGroup.WebStore.Product.DTO.ProductReviewDTO.ProductReviewDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

@RestController
@RequestMapping("products")

public class ProductController {
    private final ProductService productService;
    private final ProductReviewService productReviewService;
    @Autowired
    public ProductController(ProductService productService, ProductReviewService productReviewService) {
        this.productService = productService;
        this.productReviewService = productReviewService;
    }

    @GetMapping("")
    private ResponseEntity<PageDTO<? extends ProductInfoDTO>> getProducts(@RequestParam(name = "name",required = false) String name,
                                                                  @RequestParam(name = "page",required = false,defaultValue = "0") Integer pageNumber,
                                                                  @RequestHeader(value = "ResponseResourceFormat", required = false) String resourceFormat,
                                                                  @RequestParam(required = false) Map<String,String> params,
                                                                  @RequestParam(name = "sort_by",required = false,defaultValue = "sales") String sortBy,
                                                                  @RequestParam(name = "order",required = false,defaultValue = "DESC") Sort.Direction order) throws UnsupportedEncodingException, JsonProcessingException {


        if(resourceFormat!=null&& resourceFormat.equals("usersearch")){
            PageDTO<ProductShortInfoForUserDTO>products=new PageDTO<>(productService.getProductsByName(name),0,0);
            return new ResponseEntity<>(products, HttpStatus.OK);
        }
        if (resourceFormat != null && resourceFormat.equals("admin")) {
            return new ResponseEntity<>(productService.getProductsForAdmin(name,pageNumber), HttpStatus.OK);
        }
        PageDTO<? extends ProductInfoDTO> products=productService.getProducts(params,pageNumber,sortBy,order);

        return new ResponseEntity<>(products, HttpStatus.OK);
    }
    @GetMapping("{id}")
    private ResponseEntity<? extends ProductInfoDTO> getProductById(@PathVariable("id") Long id,
                                            @RequestHeader(value = "ResponseResourceFormat", required = false) String resourceFormat)

    {

        if (resourceFormat != null && resourceFormat.equals("admin")) {
           return new ResponseEntity<>(productService.getProductById(id,ProductAdminDTO.class), HttpStatus.OK);
        }
        else if (resourceFormat != null && resourceFormat.equals("orderrelation")) {
            return new ResponseEntity<>(productService.getProductById(id, ProductInfoDTO.class), HttpStatus.OK);
        }
        return new ResponseEntity<>(productService.getProductById(id, ProductInfoForUserDTO.class), HttpStatus.OK);

    }
    @PostMapping(path=" ", consumes = {MediaType.APPLICATION_JSON_VALUE ,MediaType.MULTIPART_FORM_DATA_VALUE })
    private ResponseEntity<ProductAdminDTO> createProduct(@Valid @RequestPart("productDTO") InCreateUpdateProductDTO productDTO,
                                                @RequestPart(required = false) MultipartFile[] images ) throws IOException {

        return new ResponseEntity<>(productService.CreateProduct (productDTO,images),HttpStatus.CREATED);
    }

    @PutMapping(path="{id}", consumes = {MediaType.APPLICATION_JSON_VALUE ,MediaType.MULTIPART_FORM_DATA_VALUE })
    private ResponseEntity<ProductAdminDTO> updateProduct(@PathVariable Long id,
                                                   @Valid  @RequestPart("productDTO") InCreateUpdateProductDTO productDTO,
                                                   @RequestPart(required = false) MultipartFile[] images ) throws IOException {
        return new ResponseEntity<>(productService.updateProduct(id,productDTO,images),HttpStatus.OK);
    }
    @GetMapping("{id}/reviews")
    private ResponseEntity<PageDTO<ProductReviewDTO>> getProductReviews(@PathVariable("id") Long id,
                                                              @RequestParam(name = "page",required = false,defaultValue = "0") Integer page)
    {
        return new ResponseEntity<>(productService.getProductReviewsById(id,PageRequest.of(page,20)), HttpStatus.OK);
    }

    @PostMapping("{id}/reviews")
    private ResponseEntity<ProductReviewDTO> createReview(@PathVariable("id") Long id,
                                                       @Valid @RequestBody ProductReview review ){

        return new ResponseEntity<>(productReviewService.createReview (review),HttpStatus.CREATED);
    }
    @GetMapping("{id}/reviews/count")
    private ResponseEntity<Long> countReview(@PathVariable("id") Long id){

        return new ResponseEntity<>(productReviewService.getProductReviewsCount(id),HttpStatus.OK);
    }
    @DeleteMapping("reviews/{reviewId}")
    public ResponseEntity<?> getUserReviewsById(@PathVariable Long reviewId) {
        productReviewService.deleteReview(reviewId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PutMapping("reviews/{reviewId}")
    private ResponseEntity<ProductReviewDTO> update(@Valid @RequestBody ProductReview productReview, @PathVariable String reviewId)
    {
        return new ResponseEntity<>(productReviewService.updateReview(productReview),HttpStatus.OK);
    }
    @DeleteMapping("{id}")
    private ResponseEntity<?> delete(@PathVariable("id") Long id)
    {
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/images/{id}")
    private ResponseEntity<byte[]> getImage(@PathVariable("id") Long id) throws IOException {

        byte[] image =  productService.getProductImageById(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentLength(image.length);
        return new ResponseEntity<>(image, headers, HttpStatus.OK);
    }
}
