package WebStoreGroup.WebStore.Brand;

import WebStoreGroup.WebStore.Brand.DTO.BrandAdminDTO;
import WebStoreGroup.WebStore.Brand.DTO.BrandInfoDTO;
import WebStoreGroup.WebStore.DTOUtils.ConverterDTO;
import WebStoreGroup.WebStore.Exceptions.NotFoundException;
import WebStoreGroup.WebStore.DTOUtils.PageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@Transactional
public class BrandService {

    private final BrandRepository brandRepository;
    private final ConverterDTO converterDTO;
    @Value("${default-page-size}")
    private int pageSize;

    @Autowired
    public BrandService(BrandRepository brandRepository, ConverterDTO converterDTO) {
        this.brandRepository = brandRepository;
        this.converterDTO = converterDTO;
    }

    public BrandAdminDTO createBrand(Brand brand) {
        return converterDTO.convert(BrandAdminDTO.class, brandRepository.save(brand));
    }

    public void deleteBrand(Long id) {
        brandRepository.deleteById(id);
    }

    public BrandAdminDTO updateBrand(Brand brand) {
        return converterDTO.convert(BrandAdminDTO.class, brandRepository.save(brand));
    }

    public <T extends BrandInfoDTO> T getBrandById(Long id, Class<T> type) {

        return brandRepository.findById(id, type).orElseThrow(() -> new NotFoundException("Brand:" + id + " not found"));

    }

    public PageDTO<BrandAdminDTO> getBrandsForAdmin(Integer pageNumber) {
        Page<BrandAdminDTO> adminPage = brandRepository.findAllBy(BrandAdminDTO.class, PageRequest.of(pageNumber, pageSize));
        if (adminPage != null && adminPage.hasContent()) {
            return new PageDTO<>(adminPage.getContent(), adminPage.getNumber(), adminPage.getTotalPages());
        }
        return new PageDTO<>(Collections.emptyList(), 0, 0);
    }

    public PageDTO<BrandInfoDTO> getBrandsForRelations() {
        return new PageDTO<>(brandRepository.findByOrderByName(), 0, 0);
    }

    public PageDTO<BrandInfoDTO> getBrands(Integer pageNumber) {
        Page<BrandInfoDTO> brandDTOPage = brandRepository.findAllBy(BrandInfoDTO.class, PageRequest.of(pageNumber, pageSize));
        if (brandDTOPage != null && brandDTOPage.hasContent()) {
            return new PageDTO<>(brandDTOPage.getContent(), brandDTOPage.getNumber(), brandDTOPage.getTotalPages());
        }
        return new PageDTO<>(Collections.emptyList(), 0, 0);
    }

    public PageDTO<BrandAdminDTO> getBrandsByName(String name, Integer pageNumber) {
        Page<BrandAdminDTO> brandPage = brandRepository.findByNameContainingIgnoreCase(name, PageRequest.of(pageNumber, pageSize));
        return new PageDTO<>(brandPage.getContent(), brandPage.getNumber(), brandPage.getTotalPages());
    }

}
