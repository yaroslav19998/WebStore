package WebStoreGroup.WebStore.Product.Specification;

import WebStoreGroup.WebStore.Characteristic.Entities.ProductCharacteristicDateValue;
import WebStoreGroup.WebStore.Characteristic.Entities.ProductCharacteristicDecimalValue;
import WebStoreGroup.WebStore.Characteristic.Entities.ProductCharacteristicIntValue;
import WebStoreGroup.WebStore.Product.DTO.Search.SearchProductsParamDTO;
import WebStoreGroup.WebStore.Product.Product;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.List;
import java.util.Set;

public class SearchProductSpecification {
    public static Specification<Product> getProductsBySingleStringsValuesSpec(Set<Long> singleStringsIds) {
        return (root, query, criteriaBuilder) -> {
            Predicate equalPredicate = criteriaBuilder.in(root.join("singleStringCharacteristics")
                    .join("value").get("id")).value(singleStringsIds);
            return equalPredicate;
        };
    }
    public static Specification<Product> getProductsByMultipleStringsValuesSpec(Set<Long> multipleStringsIds) {
        return (root, query, criteriaBuilder) -> {
            Predicate equalPredicate = criteriaBuilder.in(root.join("multipleStringCharacteristics")
                    .get("id")).value(multipleStringsIds);
            return equalPredicate;
        };
    }
    public static Specification<Product> getProductsByIntValuesSpec(SearchProductsParamDTO param) {
        return (root, query, criteriaBuilder) -> {
            Subquery<Product> intValueSubquery = query.subquery(Product.class);
            Root<ProductCharacteristicIntValue> intValueRoot = intValueSubquery.from(ProductCharacteristicIntValue.class);

            Predicate eqCharacteristic = criteriaBuilder.equal(intValueRoot.get("characteristic"),param.getCharacteristic());

            if(param.getIntValue().getMaxValue()!=null){
                Predicate lessValue = criteriaBuilder.lessThan(intValueRoot.get("value"),param.getIntValue().getMaxValue());
                if(param.getIntValue().getMinValue()==null){
                    Predicate intValuePredicate1=criteriaBuilder.and(eqCharacteristic,lessValue);
                    intValueSubquery.select(intValueRoot.get("product")).where(intValuePredicate1);
                    return criteriaBuilder.in(root).value(intValueSubquery);
                }
            }
            if(param.getIntValue().getMinValue()!=null){
                Predicate greaterValue = criteriaBuilder.greaterThan(intValueRoot.get("value"),param.getIntValue().getMinValue());
                if(param.getIntValue().getMaxValue()==null){
                    Predicate intValuePredicate1=criteriaBuilder.and(eqCharacteristic,greaterValue);
                    intValueSubquery.select(intValueRoot.get("product")).where(intValuePredicate1);
                    return criteriaBuilder.in(root).value(intValueSubquery);
                }
            }
            Predicate lessValue = criteriaBuilder.lessThan(intValueRoot.get("value"),param.getIntValue().getMaxValue());
            Predicate greaterThen = criteriaBuilder.greaterThan(intValueRoot.get("value"),param.getIntValue().getMinValue());
            Predicate intValuePredicate1=criteriaBuilder.and(eqCharacteristic,lessValue);
            Predicate intValuePredicate2=criteriaBuilder.and(intValuePredicate1,greaterThen);
            intValueSubquery.select(intValueRoot.get("product")).where(intValuePredicate2);
            Predicate returnedPredicate = criteriaBuilder.in(root).value(intValueSubquery);

            return returnedPredicate;
        };
    }
    public static Specification<Product> getProductsByDateValuesSpec(SearchProductsParamDTO param) {
        return (root, query, criteriaBuilder) -> {
            Subquery<Product> dateValueSubquery = query.subquery(Product.class);
            Root<ProductCharacteristicDateValue> dateValueRoot = dateValueSubquery.from(ProductCharacteristicDateValue.class);
            Predicate eqCharacteristic = criteriaBuilder.equal(dateValueRoot.get("characteristic"),param.getCharacteristic());

            if(param.getDateValue().getMaxValue()!=null){
                Predicate lessValue = criteriaBuilder.lessThan(dateValueRoot.get("value"),param.getDateValue().getMaxValue());
                if(param.getDateValue().getMinValue()==null){
                    Predicate dateValuePredicate1=criteriaBuilder.and(eqCharacteristic,lessValue);
                    dateValueSubquery.select(dateValueRoot.get("product")).where(dateValuePredicate1);
                    return criteriaBuilder.in(root).value(dateValueSubquery);
                }
            }
            if(param.getDateValue().getMinValue()!=null){
                Predicate greaterValue = criteriaBuilder.greaterThan(dateValueRoot.get("value"),param.getDateValue().getMinValue());
                if(param.getDateValue().getMaxValue()==null){
                    Predicate dateValuePredicate1=criteriaBuilder.and(eqCharacteristic,greaterValue);
                    dateValueSubquery.select(dateValueRoot.get("product")).where(dateValuePredicate1);
                    return criteriaBuilder.in(root).value(dateValueSubquery);
                }
            }

            Predicate lessValue = criteriaBuilder.lessThan(dateValueRoot.get("value"),param.getDateValue().getMaxValue());
            Predicate greaterThen = criteriaBuilder.greaterThan(dateValueRoot.get("value"),param.getDateValue().getMinValue());
            Predicate dateValuePredicate1=criteriaBuilder.and(eqCharacteristic,lessValue);
            Predicate dateValuePredicate2=criteriaBuilder.and(dateValuePredicate1,greaterThen);

            dateValueSubquery.select(dateValueRoot.get("product")).where(dateValuePredicate2);
            Predicate returnedPredicate = criteriaBuilder.in(root).value(dateValueSubquery);

            return returnedPredicate;
        };
    }
    public static Specification<Product> getProductsByPriceSpec(SearchProductsParamDTO param) {
        return (root, query, criteriaBuilder) ->
        {
            if(param.getPrice().getMaxValue()!=null){
                if(param.getPrice().getMinValue()==null){
                  return criteriaBuilder.lessThan(root.get("price"),param.getPrice().getMaxValue());
                }
            }
            if(param.getPrice().getMinValue()!=null){
                if(param.getPrice().getMaxValue()==null){
                    return criteriaBuilder.greaterThan(root.get("price"),param.getPrice().getMinValue());
                }
            }

            Predicate lessValue = criteriaBuilder.lessThan(root.get("price"),param.getPrice().getMaxValue());
            Predicate greaterThen = criteriaBuilder.greaterThan(root.get("price"),param.getPrice().getMinValue());

            Predicate pricePredicate=criteriaBuilder.and(greaterThen,lessValue);

            return pricePredicate;
        };
    }
    public static Specification<Product> getProductsByBrandsSpec(Set<Long> brandIds) {
        return (root, query, criteriaBuilder) -> {
            Predicate brandsPredicate = criteriaBuilder.in(root.join("brand").get("id")).value(brandIds);
            return brandsPredicate;
        };
    }
    public static Specification<Product> getProductsByNameSpec(String name) {
        return (root, query, criteriaBuilder) -> {
            Predicate namePredicate = criteriaBuilder.like(root.get("name"),"%"+name+"%");
            return namePredicate;
        };
    }
    public static Specification<Product> getProductsByDecimalValuesSpec(SearchProductsParamDTO param) {
        return (root, query, criteriaBuilder) -> {
            Subquery<Product> decimalValueSubquery = query.subquery(Product.class);
            Root<ProductCharacteristicDecimalValue> decimalValueRoot =decimalValueSubquery.from(ProductCharacteristicDecimalValue.class);
            Predicate eqCharacteristic = criteriaBuilder.equal(decimalValueRoot.get("characteristic"),param.getCharacteristic());

            if(param.getDecimalValue().getMaxValue()!=null){
                Predicate lessValue = criteriaBuilder.lessThan(decimalValueRoot.get("value"),param.getDecimalValue().getMaxValue());
                if(param.getDecimalValue().getMinValue()==null){
                    Predicate decimalValuePredicate1=criteriaBuilder.and(eqCharacteristic,lessValue);
                    decimalValueSubquery.select(decimalValueRoot.get("product")).where(decimalValuePredicate1);
                    return criteriaBuilder.in(root).value(decimalValueSubquery);
                }
            }
            if(param.getDecimalValue().getMinValue()!=null){
                Predicate greaterValue = criteriaBuilder.greaterThan(decimalValueRoot.get("value"),param.getDecimalValue().getMinValue());
                if(param.getDecimalValue().getMaxValue()==null){
                    Predicate decimalValuePredicate1=criteriaBuilder.and(eqCharacteristic,greaterValue);
                    decimalValueSubquery.select(decimalValueRoot.get("product")).where(decimalValuePredicate1);
                    return criteriaBuilder.in(root).value(decimalValueSubquery);
                }
            }


            Predicate lessValue = criteriaBuilder.lessThan(decimalValueRoot.get("value"),param.getDecimalValue().getMaxValue());
            Predicate greaterThen = criteriaBuilder.greaterThan(decimalValueRoot.get("value"),param.getDecimalValue().getMinValue());

            Predicate decimalValuePredicate1=criteriaBuilder.and(eqCharacteristic,lessValue);
            Predicate decimalValuePredicate2=criteriaBuilder.and(decimalValuePredicate1,greaterThen);

            decimalValueSubquery.select(decimalValueRoot.get("product")).where(decimalValuePredicate2);
            Predicate returnedPredicate = criteriaBuilder.in(root).value(decimalValueSubquery);

            return returnedPredicate;
        };
    }
    public static Specification<Product> getProductsByCategoryIdSpec(Long categoryId) {
        return (root, query, criteriaBuilder) -> {
           Predicate equalPredicate = criteriaBuilder.equal(root.join("category").get("id"),(categoryId));
           return equalPredicate;
        };
    }
}
