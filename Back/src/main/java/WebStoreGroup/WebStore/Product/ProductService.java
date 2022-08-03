package WebStoreGroup.WebStore.Product;

import WebStoreGroup.WebStore.Characteristic.Entities.*;
import WebStoreGroup.WebStore.Characteristic.Entities.DTO.ProductCharacteristicsDTO.CharacteristicSingleStringValueDTO;
import WebStoreGroup.WebStore.Characteristic.Services.CharacteristicService;
import WebStoreGroup.WebStore.DTOUtils.ConverterDTO;
import WebStoreGroup.WebStore.DTOUtils.PageDTO;
import WebStoreGroup.WebStore.Exceptions.NotFoundException;
import WebStoreGroup.WebStore.Product.DTO.ProductDTO.*;
import WebStoreGroup.WebStore.Product.DTO.ProductReviewDTO.ProductReviewDTO;
import WebStoreGroup.WebStore.Product.DTO.Search.SearchProductsParamDTO;
import WebStoreGroup.WebStore.Product.Specification.SearchProductSpecification;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.hibernate.Hibernate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final ConverterDTO converterDTO;
    private final ProductImageRepository productImageRepository;
    private final ProductReviewRepository productReviewRepository;
    private final CharacteristicService characteristicService;
    @Value("${upload.path}")
    private String imagePath;
    @Value("${default-page-size}")
    private int pageSize;


    @Autowired
    public ProductService(ProductRepository productRepository,
                          ConverterDTO converterDTO,
                          ProductImageRepository productImageRepository,
                          ProductReviewRepository productReviewRepository,
                          CharacteristicService characteristicService) {
        this.productRepository = productRepository;
        this.converterDTO = converterDTO;
        this.productImageRepository = productImageRepository;
        this.productReviewRepository = productReviewRepository;
        this.characteristicService = characteristicService;
    }

    public ProductAdminDTO CreateProduct(InCreateUpdateProductDTO productDTO, MultipartFile[] images) throws IOException {

        Product product = productRepository.save(productDTO.getProduct());
        if (productDTO.getDecimalValues() != null
                || productDTO.getDateValues() != null
                || productDTO.getIntValues() != null
                || productDTO.getSingleStrings() != null
                || productDTO.getMultipleStrings() != null) {
            product = createProductWithCharacteristic(product, productDTO);

        }
        if (images != null) {
            addImages(product, images, true);
        }
        product = productRepository.save(product);
        return converterDTO.convert(ProductAdminDTO.class, product);
    }

    public void addImages(Product product, MultipartFile[] images, boolean updateIsMain) throws IOException {
        int index = 0;
        for (MultipartFile image : images) {
            String filename = image.getOriginalFilename();
            if (filename != null) {
                String path = imagePath + "products/" + product.getId();
                String resourcePath = path;
                File file1 = new File(resourcePath);
                if (!file1.exists()) {
                    file1.mkdirs();
                }
                image.transferTo(new File(file1.getAbsolutePath() + "/" + filename));
                String filepath = resourcePath + "/" + filename;

                if (index == 0 && updateIsMain) {
                    product.addImage(new ProductImage(filepath, true));
                } else {
                    product.addImage(new ProductImage(filepath, false));
                }
                index++;
            }

        }
    }



    public List<ProductShortInfoForUserDTO> getProductsByName(String name) {
        if (name != null) {
            return productRepository.findTop4ByNameContainingIgnoreCase(name);
        } else {
            return Collections.emptyList();
        }
    }

    public PageDTO<ProductReviewDTO> getProductReviewsById(Long id, Pageable page) {
        Page<ProductReviewDTO> reviewPage = productReviewRepository.findByProduct_Id(id, page);
        if (reviewPage != null && reviewPage.hasContent()) {
            return new PageDTO<>(reviewPage.getContent(), reviewPage.getNumber(), reviewPage.getTotalPages());
        }
        return new PageDTO<>(Collections.emptyList(), 0, 0);
    }

    public PageDTO<ProductAdminDTO> getProductsForAdmin(String name,Integer pageNumber) {

        long time = System.currentTimeMillis();
        Page<ProductIdDTO> productIdsPage = null;
        if (name != null) {
            Specification<Product> spec = Specification.where(SearchProductSpecification.getProductsByNameSpec(name));
            productIdsPage = productRepository.findAll(spec, ProductIdDTO.class, PageRequest.of(pageNumber, pageSize, Sort.by("id").descending()));
        }
        else {
            productIdsPage = productRepository.findAllBy(ProductIdDTO.class, PageRequest.of(pageNumber, pageSize, Sort.by("id").descending()));
        }
        List<Long> ids = productIdsPage.getContent().stream().map(ProductIdDTO::getId).collect(Collectors.toList());
        if (productIdsPage.hasContent()) {
            List<ProductAdminDTO> prAdmin=productRepository.findProductForAdminByIdIn(ids, Sort.by("id").descending());
            System.out.println(System.currentTimeMillis() - time);
            return new PageDTO<>(prAdmin,
                    productIdsPage.getNumber(),
                    productIdsPage.getTotalPages());

        }
        return new PageDTO<>(Collections.emptyList(), 0, 0);
    }

    public PageDTO<? extends ProductInfoDTO> getProducts(Map<String, String> params,
                                                         Integer pageNumber, String sortBy, Sort.Direction order) throws UnsupportedEncodingException, JsonProcessingException {

        Specification<Product> productSpecification = null;
        if (params != null) {
            productSpecification = createSpecification(params);
        }
        return getProductsBySpecification( productSpecification, pageNumber,sortBy,order);
    }

    public PageDTO<ProductShortInfoForUserDTO> getProductsCategory(Long categoryId, Map<String, String> params, Integer pageNumber, String sortBy, Sort.Direction order) throws UnsupportedEncodingException, JsonProcessingException {
        Specification<Product> productSpecification = createSpecification(params);
        productSpecification = productSpecification.and((SearchProductSpecification.getProductsByCategoryIdSpec(categoryId)));
        return getProductsBySpecification( productSpecification, pageNumber,sortBy,order);
    }
    public PageDTO<ProductShortInfoForUserDTO> getProductsBySpecification(Specification<Product> productSpecification,Integer pageNumber, String sortBy, Sort.Direction order){
        long time = System.currentTimeMillis();

        Page<ProductIdDTO> productIdsPage = productRepository.findAll(productSpecification, ProductIdDTO.class, PageRequest.of(pageNumber, pageSize, Sort.by(order, sortBy)));
        List<Long> ids = productIdsPage.getContent().stream().map(ProductIdDTO::getId).collect(Collectors.toList());

        if (productIdsPage.hasContent()) {
            List<ProductShortInfoForUserDTO> pr=productRepository.findProductForPageByIdIn(ids, Sort.by(order, sortBy));
            System.out.println(System.currentTimeMillis() - time);
            return new PageDTO<>(pr,
                    productIdsPage.getNumber(),
                    productIdsPage.getTotalPages());
        }
        return new PageDTO<>(Collections.emptyList(), 0, 0);
    }


    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public ProductAdminDTO updateProduct(Long id, InCreateUpdateProductDTO productDTO, MultipartFile[] images) throws IOException {
        Product productFromDb = productRepository.getById(id);
        if (productDTO.getDeletedImages() != null) {
            productDTO.getDeletedImages().forEach(image ->
                    productImageRepository.deleteById(image.getId()));
        }
        if (productDTO.getDeletedCharacteristics() != null && !productDTO.getDeletedCharacteristics().isEmpty()) {
            productDTO.getDeletedCharacteristics().forEach(delete ->
                    characteristicService.deleteProductCharacteristic(delete.getCharacteristic().getId(),
                            productFromDb,
                            delete.getCharacteristic().getValueType()));

        }



        BeanUtils.copyProperties(productDTO.getProduct(), productFromDb,
                "id", "images", "reviews","created", "updated", "orders", "sales");
        boolean[] newIsMain = {true};
        productDTO.getProduct().getImages().forEach(
                img -> {
                    img.setProduct(productFromDb);
                    if (img.getIs_main()) {
                        newIsMain[0] = false;
                    }
                }
        );
        productFromDb.setImages(productDTO.getProduct().getImages());
        if (images != null) {
            addImages(productFromDb, images, newIsMain[0]);
        }
        productFromDb.getIntCharacteristics().forEach(ch -> ch.setId(new ProductCharacteristicValueId(ch.getCharacteristic().getId()
                , ch.getProduct().getId())));
        productFromDb.getDecimalCharacteristics().forEach(ch -> ch.setId(new ProductCharacteristicValueId(ch.getCharacteristic().getId()
                , ch.getProduct().getId())));
        productFromDb.getDateCharacteristics().forEach(ch -> ch.setId(new ProductCharacteristicValueId(ch.getCharacteristic().getId()
                , ch.getProduct().getId())));
        productFromDb.getSingleStringCharacteristics().forEach(ch -> ch.setId(new ProductCharacteristicValueId(ch.getCharacteristic().getId()
                , ch.getProduct().getId())));
        //        Hibernate.initialize(updatedProduct.getSingleStringCharacteristics());
//        updatedProduct.getSingleStringCharacteristics().forEach(val->{Hibernate.initialize(val.getValue());System.out.println(val.getValue());});
        return converterDTO.convert(ProductAdminDTO.class, productRepository.saveAndFlush(productFromDb));
    }

    public  <T extends ProductInfoDTO> T getProductById(Long id, Class<T> type) {
        return productRepository.findById(id,  type).orElseThrow(() -> new NotFoundException("Product:" + id + " not found"));

    }

    public byte[] getProductImageById(Long id) throws IOException {
        ProductImage imageInfo = productImageRepository.findById(id).get();
        File imgPath = new File(imageInfo.getFilePath());
        return Files.readAllBytes(imgPath.toPath());
    }

    private static List<SearchProductsParamDTO> convertParams(Map<String, String> params) throws JsonProcessingException, UnsupportedEncodingException {
        List<SearchProductsParamDTO> paramsList = new ArrayList<>();

        if (params != null) {
            String value = params.get("params");
            if (value != null && !value.isEmpty() && !value.equals("0")) {
                String res = URLDecoder.decode(value, "UTF-8").toString();
                ObjectMapper mapper = new ObjectMapper();
                paramsList = mapper.readValue(res, new TypeReference<List<SearchProductsParamDTO>>() {
                });
            }

        }
        return paramsList;
    }

    private static Specification<Product> createSpecification(Map<String, String> params) throws UnsupportedEncodingException, JsonProcessingException {

        Specification<Product> spec = Specification.where(null);
        List<SearchProductsParamDTO> paramsList = convertParams(params);
        if (!paramsList.isEmpty()) {
            for (SearchProductsParamDTO param : paramsList) {
                if (param.getCharacteristic() == null) {
                    if (param.getName() != null) {
                        spec = spec.and(SearchProductSpecification.getProductsByNameSpec(param.getName()));
                    }
                    if (param.getPrice() != null) {
                        if (param.getPrice().getMinValue() != null || param.getPrice().getMaxValue() != null) {
                            spec = spec.and(SearchProductSpecification.getProductsByPriceSpec(param));
                        }

                    }
                    if (param.getBrandIds() != null && !param.getBrandIds().isEmpty()) {
                        spec = spec.and(SearchProductSpecification.getProductsByBrandsSpec(param.getBrandIds()));
                    }
                    continue;
                }
                if (param.getCharacteristic().getValueType() == CharacteristicValueType.SINGLESTRING && param.getSingleStringValues() != null
                        && !param.getSingleStringValues().isEmpty()) {
                    spec = spec.and(SearchProductSpecification.getProductsBySingleStringsValuesSpec(param.getSingleStringValues()));

                }
                if (param.getCharacteristic().getValueType() == CharacteristicValueType.INT && param.getIntValue() != null) {
                    if (param.getIntValue().getMinValue() != null || param.getIntValue().getMaxValue() != null) {
                        spec = spec.and(SearchProductSpecification.getProductsByIntValuesSpec(param));
                    }

                }
                if (param.getCharacteristic().getValueType() == CharacteristicValueType.MULTIPLESTRING && param.getMultipleStringValues() != null
                        && !param.getMultipleStringValues().isEmpty()) {
                    spec = spec.and(SearchProductSpecification.getProductsByMultipleStringsValuesSpec(param.getMultipleStringValues()));

                }
                if (param.getCharacteristic().getValueType() == CharacteristicValueType.DECIMAL && param.getDecimalValue() != null) {
                    if (param.getDecimalValue().getMinValue() != null || param.getDecimalValue().getMaxValue() != null) {
                        spec = spec.and(SearchProductSpecification.getProductsByDecimalValuesSpec(param));
                    }

                }
                if (param.getCharacteristic().getValueType() == CharacteristicValueType.DATE && param.getDateValue() != null) {
                    if (param.getDateValue().getMinValue() != null || param.getDateValue().getMaxValue() != null) {
                        spec = spec.and(SearchProductSpecification.getProductsByDateValuesSpec(param));
                    }

                }
            }

        }
        return spec;
    }

    public Product createProductWithCharacteristic(Product product, InCreateUpdateProductDTO productDTO) {
        if (productDTO.getDateValues() != null && !productDTO.getDateValues().isEmpty()) {
            Set<ProductCharacteristicDateValue> dateValues = productDTO.getDateValues().stream().map(
                    value -> new ProductCharacteristicDateValue(value.getCharacteristic(), product, value.getValue())
            ).collect(Collectors.toSet());
            product.setDateCharacteristics(dateValues);
        }
        if (productDTO.getDecimalValues() != null && !productDTO.getDecimalValues().isEmpty()) {
            Set<ProductCharacteristicDecimalValue> decimalValues = productDTO.getDecimalValues().stream().map(
                    value -> new ProductCharacteristicDecimalValue(value.getCharacteristic(), product, value.getValue())
            ).collect(Collectors.toSet());
            product.setDecimalCharacteristics(decimalValues);
        }
        if (productDTO.getIntValues() != null && !productDTO.getIntValues().isEmpty()) {
            Set<ProductCharacteristicIntValue> intValues = productDTO.getIntValues().stream().map(
                    value -> new ProductCharacteristicIntValue(value.getCharacteristic(), product, value.getValue())
            ).collect(Collectors.toSet());
            product.setIntCharacteristics(intValues);
        }
        if (productDTO.getSingleStrings() != null && !productDTO.getSingleStrings().isEmpty()) {
            Set<ProductCharacteristicSingleStringValue> singleStringValues = productDTO.getSingleStrings().stream().map(
                    value -> new ProductCharacteristicSingleStringValue(value.getCharacteristic(), product, value.getValue())
            ).collect(Collectors.toSet());
            product.setSingleStringCharacteristics(singleStringValues);
        }
        if (productDTO.getMultipleStrings() != null && !productDTO.getMultipleStrings().isEmpty()) {
            Set<CharacteristicMultipleStringValue> multipleStringValues = new HashSet<CharacteristicMultipleStringValue>(productDTO.getMultipleStrings());
            product.setMultipleStringCharacteristics(multipleStringValues);
        }
        return product;
    }


}
