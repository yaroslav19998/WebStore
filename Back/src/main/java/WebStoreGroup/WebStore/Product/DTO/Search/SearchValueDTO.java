package WebStoreGroup.WebStore.Product.DTO.Search;

public class SearchValueDTO<T>{
    T minValue;
    T maxValue;

    public T getMinValue() {
        return minValue;
    }

    public void setMinValue(T minValue) {
        this.minValue = minValue;
    }

    public T getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(T maxValue) {
        this.maxValue = maxValue;
    }
    
}
