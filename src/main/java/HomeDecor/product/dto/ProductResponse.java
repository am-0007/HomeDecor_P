package HomeDecor.product.dto;

import HomeDecor.product.ProductCategory;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
public class ProductResponse {
    private Integer id;
    private String productName;
    private String productDescription;
    @Enumerated(EnumType.STRING)
    private ProductCategory productCategory;
    private Long price;
    private Long userId;
    private String imageUrl;

    public ProductResponse(Integer id,
                           String productName,
                           String productDescription,
                           ProductCategory productCategory,
                           Long price,
                           Long userId,
                           String imageUrl) {
        this.id = id;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productCategory = productCategory;
        this.price = price;
        this.userId = userId;
        this.imageUrl = imageUrl;
    }
}
