package HomeDecor.cart.service;

import HomeDecor.cart.Cart;
import HomeDecor.cart.cartDTO.CartDTO;
import HomeDecor.cart.cartDTO.CartReturnDTO;
import HomeDecor.cart.repository.CartRepository;
import HomeDecor.product.Product;
import HomeDecor.product.repository.ProductRepository;
import HomeDecor.user.User;
import HomeDecor.user.repository.UserInterface;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserInterface userInterface;

    public Object saveCart(List<CartDTO> cartDTO, Long userId) {
        Optional<User> getUserById = userInterface.findById(userId);
        if (getUserById.isPresent()) {
            User getUser = getUserById.get();

            List<Cart> carts = new ArrayList<>();
            List<Integer> productExists = new ArrayList<>();

            for (CartDTO i : cartDTO) {
                Optional<Cart> cartExistsInRepo = cartRepository.findByUserIdAndProductId(userId, i.getProductId());
                Optional<Product> getProductById = productRepository.findById(i.getProductId());
                if (getProductById.isPresent()) {
                    Product getProduct = getProductById.get();
                    carts.add(new Cart(getProduct,
                            getUser,
                            i.getQuantity(),
                            i.getPrice(),
                            i.getAddedDate())
                    );
                } else {
                    productExists.add(i.getProductId());
                }
            }
            //Wrong product Id will lead to failure in saving product
            if (!productExists.isEmpty()) {
                return "Invalid product ID: " + productExists;
            } else {
                for (Cart saveCart : carts) {
                    cartRepository.save(saveCart);
                }
                return "Cart added successfully";
            }
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
        for (Cart i : cartList) {
            returnDTOList.add(new CartReturnDTO(
                    i.getUser().getId(),
                    i.getQuantity(),
                    i.getPrice(),
                    i.getAddedDate(),
                    i.getProduct().getImage().getImagePath())
            );
        }
        return  returnDTOList;
    }

    public Object updateCart(List<CartDTO> cartDTO, Long userId) throws Exception {
        List<Cart> cartList = cartRepository.findAllByUserId(userId);
        if (cartList.isEmpty()) {
            throw new Exception("User haven't added anything in the cart");
        }

        for (CartDTO i : cartDTO) {
            for (Cart k : cartList) {
                if (!i.getProductId().equals(k.getProduct().getId())) {
                    continue;
                } else {
                    k.setPrice(i.getPrice());
                    k.setQuantity(i.getQuantity());
                }
            }
        }
        for (Cart updateCart : cartList) {
            cartRepository.save(updateCart);
        }
        return "Successfully Updated!";
    }

    public Object deleteCart(Integer productId, Long userId) {
        Optional<Cart> cart = cartRepository.findByUserIdAndProductId(userId, productId);
        if (cart.isPresent()) {
            Cart deleteCart = cart.get();
            cartRepository.delete(deleteCart);
            return deleteCart.getProduct().getProductName() + " Successfully removed from Cart";
        }
        return "Delete Operation Failed!";
    }
}
