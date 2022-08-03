package WebStoreGroup.WebStore.Product.DTO.Search;

import java.util.List;

public class SearchParamsList {
    List<SearchProductsParamDTO> params;

    public List<SearchProductsParamDTO> getParams() {
        return params;
    }

    public void setParams(List<SearchProductsParamDTO> params) {
        this.params = params;
    }
}
