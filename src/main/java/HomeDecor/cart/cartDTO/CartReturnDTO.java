package HomeDecor.cart.cartDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Base64;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class CartReturnDTO {

    private Long userId;
    private Integer quantity;
    private Double price;
    private Date addedDate;
    /*private Product product;*/
    private String imageUrl;



    public CartReturnDTO(Long userId,
                         Integer quantity,
                         Double price,
                         Date addedDate,
                         /*Product product,*/
                         String imageUrl) {
        this.userId = userId;
        this.quantity = quantity;
        this.price = price;
        this.addedDate = addedDate;
        /*this.product = product;*/
        this.imageUrl = imageUrl;
    }

    public byte[] decodeImage(byte[] image) {
        if (image != null) {
            image = Base64.getDecoder().decode(image);
            return image;
        }
        throw new IllegalStateException("No image!");
    }
}
