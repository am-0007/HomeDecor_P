package HomeDecor.cart.controller;

import HomeDecor.cart.cartDTO.CartDTO;
import HomeDecor.cart.cartDTO.CartReturnDTO;
import HomeDecor.cart.service.CartService;
import lombok.AllArgsConstructor;
import org.apache.hadoop.yarn.webapp.hamlet.HamletSpec;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.PathParam;
import java.util.List;


@CrossOrigin
@RestController
@RequestMapping("/api/cart")
@AllArgsConstructor
public class CartController {

    private final CartService cartService;

    //get cart by id
    @PreAuthorize("hasAuthority('user:readCart')")
    @GetMapping("/getCart/{user_id}")
    public ResponseEntity<List<CartReturnDTO>> getCartById(@PathVariable(value = "user_id") Long user_id) {
        return new ResponseEntity<List<CartReturnDTO>>(cartService.getCartById(user_id), HttpStatus.OK);
    }

    //save cartDTO
    @PreAuthorize("hasAuthority('user:saveCart')")
    @PostMapping(value = "/saveCart/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveCart(@PathVariable(value = "userId") Long userId, @RequestBody List<CartDTO> cartDTO){
        return new ResponseEntity<>(cartService.saveCart(cartDTO, userId), HttpStatus.OK );
    }

    //update
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping(value = "/updateCart/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateCart(@PathVariable(value = "userId") Long userId, @RequestBody List<CartDTO> cartDTO) {
        try {
            return new ResponseEntity<>(cartService.updateCart(cartDTO, userId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.EXPECTATION_FAILED);
        }
    }

    //delete
    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping(value = "/deleteCart/{productId}")
    public ResponseEntity<?> deleteCart(@PathVariable("productId") Integer productId, @RequestParam("userId") Long userId) {
        return new ResponseEntity<>(cartService.deleteCart(productId, userId), HttpStatus.OK);
    }


}
