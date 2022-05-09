package HomeDecor.product.recommendation.controller;

import HomeDecor.product.Product;
import HomeDecor.product.recommendation.service.RecommendService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@AllArgsConstructor
@RequestMapping("/api/recommendProduct")
public class RecommendationController {

    private final RecommendService recommendService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(value = "/userBased/{userId}")
    public ResponseEntity<List<Product>> listAllItemUserBased(@PathVariable("userId") long userId) {
        List<Product> recommendProduct = recommendService.recommendAllItemUserBased(userId);
        if (recommendProduct.isEmpty()) {
            return new ResponseEntity<List<Product>>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<Product>>(recommendProduct, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(value = "/itemBased/{id}")
    public ResponseEntity<List<Product>> ListAllItemsItemBased(@PathVariable("id") Integer userId) {
        List<Product> recommendProduct = recommendService.recommendAllItemsItemBased(userId);
        if (recommendProduct.isEmpty()) {
            return new ResponseEntity<List<Product>>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<Product>>(recommendProduct, HttpStatus.OK);

    }

}
