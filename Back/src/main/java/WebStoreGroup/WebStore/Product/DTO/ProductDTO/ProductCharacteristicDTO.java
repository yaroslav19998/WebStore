package WebStoreGroup.WebStore.Product.DTO.ProductDTO;

import WebStoreGroup.WebStore.Characteristic.Entities.Characteristic;
import WebStoreGroup.WebStore.Product.Product;

public class ProductCharacteristicDTO {
    private Product product;
    private Characteristic characteristic;
    public ProductCharacteristicDTO(Product product, Characteristic characteristic) {
        this.product = product;
        this.characteristic = characteristic;
    }
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Characteristic getCharacteristic() {
        return characteristic;
    }

    public void setCharacteristic(Characteristic characteristic) {
        this.characteristic = characteristic;
    }


}
