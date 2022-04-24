package HomeDecor.product.rating.ratingproductdto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RatingProductDTO {
    private Integer productID;
    private Integer userID;
    private Double rateProduct;
}
