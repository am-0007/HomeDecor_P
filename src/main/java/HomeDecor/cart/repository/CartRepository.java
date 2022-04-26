package HomeDecor.cart.repository;

import HomeDecor.cart.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    List<Cart> findAllByUserId(Long user_id);

    Optional<Cart> findByUserId(Long user_id);

    Optional<Cart> findByUserIdAndProductId(Long userId, Integer productId);
}
