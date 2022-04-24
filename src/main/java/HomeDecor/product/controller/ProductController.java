package HomeDecor.product.controller;


import HomeDecor.product.Product;
import HomeDecor.product.ProductCategory;
import HomeDecor.product.dto.ProductDTO;
import HomeDecor.product.dto.ProductResponse;
import HomeDecor.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @PostMapping(value = "/add")
    public ResponseEntity<String> addProduct(@RequestParam("userId") Long userId,
                                             @RequestParam("productName") String name,
                                             @RequestParam("productDescription") String description,
                                             @RequestParam("file") MultipartFile file,
                                             @RequestParam("productCategory") ProductCategory category,
                                             @RequestParam("productPrice") Long price
                                             ) {
        ProductDTO productDTO = new ProductDTO(
                name,
                description,
                category,
                price,
                userId
        );
        return ResponseEntity.status(HttpStatus.OK).body(productService.addProduct((MultipartFile) file, productDTO));
    }

    //Get All Product
    @GetMapping(value = "/getProduct")
    public ResponseEntity<List<ProductResponse>> getProduct(){
        return new ResponseEntity<List<ProductResponse>>(productService.getProduct(), HttpStatus.OK);
    }

    //Search BY product Name
    @GetMapping("/{productName}")
    public ResponseEntity<List<ProductResponse>> findByProductName(@PathVariable("productName") String productName) {
        return new ResponseEntity<List<ProductResponse>>(productService.findByProductName(productName), HttpStatus.OK);
    }

    @GetMapping("productById/{productId}")
    public ResponseEntity<ProductResponse> findByProductId(@PathVariable("productId")Integer productId) {
        return new ResponseEntity<ProductResponse>(productService.findByProductId(productId), HttpStatus.OK);
    }

    //Update
    @PutMapping("{productName}")
    public ResponseEntity<Product> updateProduct(@PathVariable("productName") String productName,
                                                 @RequestBody Product product) {
        return new ResponseEntity<Product>(productService.updateProductDetail(product, productName), HttpStatus.OK);
    }

    //Delete Product Rest API
    @DeleteMapping("/delete/{productName}")
    public ResponseEntity<String> deleteProduct(@PathVariable("productName") String productName) {
        return  new ResponseEntity<String>(productService.deleteProduct(productName), HttpStatus.OK);
    }

}
