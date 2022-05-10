package HomeDecor.product.controller;

import HomeDecor.product.ProductCategory;
import HomeDecor.product.dto.ProductDTO;
import HomeDecor.product.productStatus.Status;
import HomeDecor.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static HomeDecor.product.productStatus.Status.*;

@RestController
@RequestMapping("/api/product")
public class ProductAdminController {

    private final ProductService productService;

    @Autowired
    public ProductAdminController(ProductService productService) {
        this.productService = productService;
    }

    @PreAuthorize("hasAnyAuthority('admin:getProduct')")
    @GetMapping("/getConfirmedProduct/{userId}")
    public ResponseEntity<?> getApprovedProduct(@PathVariable("userId") Long userId) {
        return new ResponseEntity<>(productService.getProductByStatus(userId, APPROVED), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('admin:getProduct')")
    @GetMapping("/getRejectedProduct/{userId}")
    public ResponseEntity<?> getRejectedProduct(@PathVariable("userId") Long userId) {
        return new ResponseEntity<>(productService.getProductByStatus(userId, REJECTED), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('admin:getProduct')")
    @GetMapping("/getPendingProduct/{userId}")
    public ResponseEntity<?> getPendingProduct(@PathVariable("userId") Long userId) {
        return new ResponseEntity<>(productService.getProductByStatus(userId, PENDING), HttpStatus.OK);
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

    //Update
    @PreAuthorize("hasAuthority('admin:editProduct')")
    @PutMapping("/admin/editProduct/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable("productId") Integer productId,
                                           @RequestParam("userId") Long userId,
                                           @RequestParam("productName") String name,
                                           @RequestParam("productDescription") String description,
                                           @RequestParam("file") MultipartFile file,
                                           @RequestParam("productCategory") ProductCategory category,
                                           @RequestParam("productPrice") Long price,
                                           @RequestParam("status")Status status) {
        ProductDTO productDTO = new ProductDTO(
                name,
                description,
                category,
                price,
                userId
        );
        return new ResponseEntity<>(productService.updateProductDetail(file, productDTO, productId, status), HttpStatus.OK);
    }

    //Delete Product Rest API
    @PreAuthorize("hasAuthority('admin:removeProduct')")
    @DeleteMapping("/admin/delete/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable("productId") Integer productId) {
        return  new ResponseEntity<String>(productService.deleteProduct(productId), HttpStatus.OK);
    }

    //returnTotal number of product added by user
    @PreAuthorize("hasAuthority('admin:countProduct')")
    @GetMapping("/admin/countTotalProduct/{userId}")
    public ResponseEntity<?> countProduct(@PathVariable("userId") Long userId) {
        return  new ResponseEntity<>(productService.countProductByUserId(userId), HttpStatus.OK);
    }


}
