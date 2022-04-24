package HomeDecor.cart.service;

import HomeDecor.cart.Cart;
import HomeDecor.cart.cartDTO.CartDTO;
import HomeDecor.cart.cartDTO.CartReturnDTO;
import HomeDecor.cart.repository.CartRepository;
import HomeDecor.product.Product;
import HomeDecor.product.ProductRepository;
import HomeDecor.user.User;
import HomeDecor.user.UserInterface;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CartService {

    @Autowired
    private final CartRepository cartRepository;

    @Autowired
    private final ProductRepository productRepository;

    @Autowired
    private final UserInterface userInterface;


    public Object createCart(CartDTO cartDTO) {
        Optional<Product> getProductById = productRepository.findById(cartDTO.getProductId());
        Optional<User> getUserById = userInterface.findById(cartDTO.getUserId());
        if (getUserById.isPresent()) {
            User getUser = getUserById.get();
            if (getProductById.isPresent()) {
                Product getProduct = getProductById.get();
                cartRepository.save(
                        new Cart(
                                getProduct,
                                getUser,
                                cartDTO.getQuantity(),
                                cartDTO.getPrice(),
                                cartDTO.getAddedDate()
                        )
                );
                return "Cart added successfully";
            }
            return "Product Doesn't Exists!";
        }
        return "User Doesn't Exists!";
    }

    @Transactional
    public List<CartReturnDTO> getCartById(Long user_id) {
        List<Cart> cartList = cartRepository.findAllByUserId(user_id);
        List<CartReturnDTO> returnDTOList = new ArrayList<>();
        if (cartList.isEmpty()) {
            throw new IllegalStateException("User haven't added anything in the cart");
        }
        for (Cart i : cartList){
            System.out.println(i.getId());
        }
        for (Cart i : cartList) {
            returnDTOList.add(new CartReturnDTO(
                    i.getUser().getId(),
                    i.getQuantity(),
                    i.getPrice(),
                    i.getAddedDate(),
                    /*i.getProduct(),*/
                    i.getProduct().getImage().getImagePath())
            );
        }

        /*for (CartReturnDTO i : returnDTOList) {
            i.getProduct().setImage(i.decodeImage(i.getProduct().getImage()));
        }*/
        return  returnDTOList;
    }
}
