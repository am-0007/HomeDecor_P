package HomeDecor.product.rating.controller;

import HomeDecor.product.rating.ratingproductdto.RatingProductDTO;
import HomeDecor.product.rating.ratingservice.RatingService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/product")
@AllArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    //rateProduct
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(value = "/rateproduct")
    public ResponseEntity<?> rateProduct(@RequestBody RatingProductDTO ratingProductDTO) {
        return new ResponseEntity<>(ratingService.rateProduct(ratingProductDTO), HttpStatus.OK);
    }

    //updateProduct



}
