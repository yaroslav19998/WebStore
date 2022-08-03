package WebStoreGroup.WebStore.Category;

import WebStoreGroup.WebStore.Category.DTO.*;
import WebStoreGroup.WebStore.DTOUtils.ConverterDTO;
import WebStoreGroup.WebStore.Exceptions.NotFoundException;
import WebStoreGroup.WebStore.DTOUtils.PageDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@Transactional
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ConverterDTO converterDTO;

    @Value("${default-page-size}")
    private int pageSize;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, ConverterDTO converterDTO) {
        this.categoryRepository = categoryRepository;
        this.converterDTO = converterDTO;
    }

    public CategoryAdminDTO createCategory(Category category) {
        byte depth = 1;
        category.setDepth(calculateDepth(category, depth));
        return converterDTO.convert(CategoryAdminDTO.class, categoryRepository.save(category));
    }


    public byte calculateDepth(Category category, byte depth) {
        if (category.getParentCategory() != null) {
            Category parentCategory = categoryRepository.findById(category.getParentCategory().getId()).get();
            return calculateDepth(parentCategory, (byte) (depth + 1));
        } else {
            return depth;
        }
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    public CategoryAdminDTO updateCategory(Long id, Category category) {
        Category categoryFromDb = categoryRepository.getById(id);
        BeanUtils.copyProperties(category, categoryFromDb, "id", "created", "updated", "depth", "products");
        categoryFromDb.setDepth(calculateDepth(category, (byte) 1));
        return converterDTO.convert(CategoryAdminDTO.class, categoryRepository.save(categoryFromDb));
    }


    public <T extends CategoryInfoDTO> T getCategoryById(Long id, Class<T> type) {
        return categoryRepository.findById(id, type).orElseThrow(() -> new NotFoundException("Category: " + id + " not found"));
    }

    public PageDTO<CategoryAdminDTO> getCategoriesForAdmin(String name, Integer pageNumber) {
        Page<Long> categoryIdsPage = null;
        if (name != null) {
            categoryIdsPage = categoryRepository.findIdsByNameContainingIgnoreCase(name, PageRequest.of(pageNumber, pageSize, Sort.by("id").descending()));
        } else {
            categoryIdsPage = categoryRepository.findPageIds(PageRequest.of(pageNumber, pageSize, Sort.by("id").descending()));
        }
        if (categoryIdsPage != null && categoryIdsPage.hasContent()) {
            return new PageDTO<>(categoryRepository.findByIdIn(categoryIdsPage.getContent(), CategoryAdminDTO.class, Sort.by("id").descending()),
                    categoryIdsPage.getNumber(),
                    categoryIdsPage.getTotalPages());
        }
        return new PageDTO<>(Collections.emptyList(), 0, 0);
    }
    public PageDTO<CategoryIdNameDTO> getCategoriesForRelations(){
        return new PageDTO<>(categoryRepository.findCategoriesForRelations(), 0, 0);
    }
    public PageDTO<CategoryIdNameDTO> getCategoriesForParentRelations(){
        return new PageDTO<>(categoryRepository.findAllForParentRelations(), 0, 0);
    }
    public PageDTO<CategoryWithSubCategoriesDTO> getCategoriesForPage(){
        return new PageDTO<>(categoryRepository.findByDepth((byte) 1), 0, 0);
    }



}
