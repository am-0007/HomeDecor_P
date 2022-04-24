package HomeDecor.cart.cartDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {

    private Integer productId;
    private Long userId;
    private Integer quantity;
    private Double price;
    private Date addedDate;
    
}