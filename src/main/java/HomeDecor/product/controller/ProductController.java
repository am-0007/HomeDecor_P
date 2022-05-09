package HomeDecor.product.controller;


import HomeDecor.product.Product;
import HomeDecor.product.ProductCategory;
import HomeDecor.product.dto.ProductDTO;
import HomeDecor.product.dto.ProductResponse;
import HomeDecor.product.productStatus.Status;
import HomeDecor.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    //Get All Product
    @GetMapping(value = "/getProduct")
    public ResponseEntity<List<ProductResponse>> getProduct(){
        return new ResponseEntity<List<ProductResponse>>(productService.getProductByStatus(Status.APPROVED), HttpStatus.OK);
    }

    //Search BY product Name
    @GetMapping("/getProductByName/{productId}")
    public ResponseEntity<List<ProductResponse>> findByProductName(@PathVariable("productId") String productId) {
        return new ResponseEntity<List<ProductResponse>>(productService.findByProductName(productId), HttpStatus.OK);
    }

    //search by product Id
    @GetMapping("/getProductById/{productId}")
    public ResponseEntity<ProductResponse> findByProductId(@PathVariable("productId")Integer productId) {
        return new ResponseEntity<ProductResponse>(productService.findByProductId(productId), HttpStatus.OK);
    }

}
