package HomeDecor.product.controller;


import HomeDecor.product.productStatus.Status;
import HomeDecor.product.service.ProductService;
import HomeDecor.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static HomeDecor.product.productStatus.Status.*;

@CrossOrigin
@RestController
@RequestMapping("/api/superAdmin")
public class ProductSuperAdminController {

    private final ProductService productService;
    private final UserService userService;

    @Autowired
    public ProductSuperAdminController(ProductService productService, UserService userService) {
        this.productService = productService;
        this.userService = userService;
    }

    @PreAuthorize("hasAnyAuthority('superAdmin:getProduct')")
    @GetMapping("/getConfirmedProduct")
    public ResponseEntity<?> getApprovedProduct() {
        return new ResponseEntity<>(productService.getProductByStatus(APPROVED), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('superAdmin:getProduct')")
    @GetMapping("/getRejectedProduct")
    public ResponseEntity<?> getRejectedProduct() {
        return new ResponseEntity<>(productService.getProductByStatus(REJECTED), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('superAdmin:getProduct')")
    @GetMapping("/getPendingProduct")
    public ResponseEntity<?> getPendingProduct() {
        return new ResponseEntity<>(productService.getProductByStatus(PENDING), HttpStatus.OK);
    }


    //approve product
    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN')")
    @PutMapping("/approveProduct/{productId}")
    public ResponseEntity<?> approveProduct(@PathVariable("productId") Integer productId, @RequestParam("status") Status status) {
        return new ResponseEntity<>(productService.approveProduct(productId, status), HttpStatus.OK);
    }

    //reject product
    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN')")
    @PutMapping("/rejectProduct/{productId}")
    public ResponseEntity<?> rejectProduct(@PathVariable("productId") Integer productId) {
        return new ResponseEntity<>(productService.rejectProduct(productId), HttpStatus.OK);
    }

    //returnTotal number of product
    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN')")
    @GetMapping("/countProduct")
    public ResponseEntity<?> countProduct() {
        return new ResponseEntity<>(productService.countAllProduct(), HttpStatus.OK);
    }

}
