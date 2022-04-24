package HomeDecor.cart.controller;

import HomeDecor.cart.cartDTO.CartDTO;
import HomeDecor.cart.cartDTO.CartReturnDTO;
import HomeDecor.cart.repository.CartRepository;
import HomeDecor.cart.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin
@RestController
@RequestMapping("/api/cart")
@AllArgsConstructor
public class CartController {

    private final CartService cartService;

    //get cart by id
    @GetMapping("/getCart/{user_id}")
    public ResponseEntity<List<CartReturnDTO>> getCartById(@PathVariable(value = "user_id") Long user_id) {
        return new ResponseEntity<List<CartReturnDTO>>(cartService.getCartById(user_id), HttpStatus.OK);
    }

    //save cartDTO
    @PostMapping("/saveCart")
    public ResponseEntity<?> createCart(@RequestBody CartDTO cartDTO){
        return new ResponseEntity<>(cartService.createCart(cartDTO), HttpStatus.OK );
    }
}
