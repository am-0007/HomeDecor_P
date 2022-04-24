package HomeDecor.product.rating;

import HomeDecor.product.Product;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "rating_product")
@Getter
@Setter
public class RatingProduct {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false, name = "user_id")
    private Integer userID;

    @Column(nullable = false, name = "rating")
    private double ratingProduct;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public RatingProduct() {}

    public RatingProduct(Integer userID,
                         double ratingProduct,
                         Product product) {
        this.userID = userID;
        this.ratingProduct = ratingProduct;
        this.product = product;
    }
}
