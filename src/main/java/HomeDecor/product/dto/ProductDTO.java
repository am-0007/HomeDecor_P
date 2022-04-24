package HomeDecor.product.dto;

import HomeDecor.product.ProductCategory;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
public class ProductDTO {
    private String productName;
    private String productDescription;
    @Enumerated(EnumType.STRING)
    private ProductCategory productCategory;
    private Long price;
    private Long userId;

    public ProductDTO() {}

    public ProductDTO(String productName,
                      String productDescription,
                      ProductCategory productCategory,
                      Long price,
                      Long userId) {
        this.productName = productName;
        this.productDescription = productDescription;
        this.productCategory = productCategory;
        this.price = price;
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public Long getPrice() {
        return price;
    }


}
