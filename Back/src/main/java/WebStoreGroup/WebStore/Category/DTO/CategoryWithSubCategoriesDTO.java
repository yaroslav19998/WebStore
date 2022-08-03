package WebStoreGroup.WebStore.Category.DTO;

import java.util.Set;

public interface CategoryWithSubCategoriesDTO extends CategoryInfoDTO {
    Set<SubCategoryDepthTwoDTO> getSubCategories();
    public interface SubCategoryDepthTwoDTO extends CategoryInfoDTO{
        Set<CategoryInfoDTO> getSubCategories();
    }
}
