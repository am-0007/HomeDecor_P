package HomeDecor.product.controller;


import HomeDecor.product.Product;
import HomeDecor.product.ProductCategory;
import HomeDecor.product.dto.ProductDTO;
import HomeDecor.product.dto.ProductResponse;
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


    @PreAuthorize("hasAuthority('admin:addProduct')")
    @PostMapping(value = "/admin/add")
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
    @GetMapping("getProductByName/{productId}")
    public ResponseEntity<List<ProductResponse>> findByProductName(@PathVariable("productId") String productId) {
        return new ResponseEntity<List<ProductResponse>>(productService.findByProductName(productId), HttpStatus.OK);
    }

    //search by product Id
    @GetMapping("getProductById/{productId}")
    public ResponseEntity<ProductResponse> findByProductId(@PathVariable("productId")Integer productId) {
        return new ResponseEntity<ProductResponse>(productService.findByProductId(productId), HttpStatus.OK);
    }

    //Update
    @PreAuthorize("hasAuthority('admin:editProduct')")
    @PutMapping("/admin/editProduct/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable("productId") Integer productId,
                                                 @RequestParam("userId") Long userId,
                                                 @RequestParam("productName") String name,
                                                 @RequestParam("productDescription") String description,
                                                 @RequestParam("file") MultipartFile file,
                                                 @RequestParam("productCategory") ProductCategory category,
                                                 @RequestParam("productPrice") Long price) {
        ProductDTO productDTO = new ProductDTO(
                name,
                description,
                category,
                price,
                userId
        );
        return new ResponseEntity<>(productService.updateProductDetail(file, productDTO, productId), HttpStatus.OK);
    }

    //Delete Product Rest API
    @PreAuthorize("hasAuthority('admin:removeProduct')")
    @DeleteMapping("/admin/delete/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable("productId") Integer productId) {
        return  new ResponseEntity<String>(productService.deleteProduct(productId), HttpStatus.OK);
    }

}
