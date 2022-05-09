package HomeDecor.product;

import HomeDecor.product.image.Image;
import HomeDecor.product.productStatus.Status;
import HomeDecor.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Getter
@Setter
@Component
@NoArgsConstructor
@Entity(name = "tb_product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    private String productName;
    private String productDescription;
    @Enumerated(EnumType.STRING)
    private ProductCategory productCategory;
    private Long price;
    /*@Lob
    private byte[] image;*/
    //To distinguish user

    @JoinColumn(name = "userId", nullable = false)
    @ManyToOne
    private User user;

    @JoinColumn(name = "imgId", nullable = false)
    @ManyToOne
    private Image image;

    @Enumerated(EnumType.STRING)
    private Status status;

    public Product(String productName,
                   String productDescription,
                   ProductCategory productCategory,
                   Long price,
                   User user,
                   Image image,
                   Status status) {
        this.productName = productName;
        this.productDescription = productDescription;
        this.productCategory = productCategory;
        this.price = price;
        this.user = user;
        this.image = image;
        this.status = status;
    }

}
