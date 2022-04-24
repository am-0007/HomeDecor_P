package HomeDecor.cart;

import HomeDecor.product.Product;
import HomeDecor.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@AllArgsConstructor
@Table(name = "cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JoinColumn(name = "product_id", nullable = false)
    @ManyToOne
    private Product product;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne
    private User user;

    @Column (name = "quantity")
    private int quantity;

    @Column (name = "price")
    private double price;

    @Column (name = "added_date")
    private Date addedDate;

    public Cart(){}

    public Cart(Product product,
                User user,
                int quantity,
                double price,
                Date addedDate) {
        this.product = product;
        this.user = user;
        this.quantity = quantity;
        this.price = price;
        this.addedDate = addedDate;
    }
}
