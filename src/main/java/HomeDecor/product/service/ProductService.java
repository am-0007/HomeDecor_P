package HomeDecor.product.service;

import HomeDecor.product.Product;
import HomeDecor.product.ProductRepository;
import HomeDecor.product.dto.ProductDTO;
import HomeDecor.product.dto.ProductResponse;
import HomeDecor.product.image.Image;
import HomeDecor.product.image.ImageRepository;
import HomeDecor.product.image.service.ImageService;
import HomeDecor.user.User;
import HomeDecor.user.UserInterface;
import HomeDecor.user.UserService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductService {

    private final ImageService imageService;
    private final ProductRepository productRepository;

    @Autowired
    private final UserInterface userInterface;

    @Autowired
    private final Product product;


    @SneakyThrows
    public String addProduct(MultipartFile multipartFile,
                             @RequestBody ProductDTO productDTO) {
        Optional<User> user = userInterface.findById(productDTO.getUserId());
        if (user.isEmpty()) {
            return "User Don't Exists!";
        }
        User getUser = user.get();
        Image image = imageService.saveImage(multipartFile, getUser);

        List<Product> productNameExists = productRepository.findAllByProductName(product.getProductName());

        for (Product i : productNameExists) {
            if (i.getUser().getUsername().equals(product.getUser().getUsername())) {
                throw new IllegalStateException("You have product with same name, please update the the existing product details!");
            }
        }
        Product product = new Product(
                productDTO.getProductName(),
                productDTO.getProductDescription(),
                productDTO.getProductCategory(),
                productDTO.getPrice(),
                getUser,
                image
        );
        productRepository.save(product);
        return product.getProductName() + " Successfully Added";

    }

    @SneakyThrows
    public String updateProductDetail(MultipartFile multipartFile,
                                      ProductDTO productDTO,
                                      Integer productId) {
        Optional<User> user = userInterface.findById(productDTO.getUserId());
        if (user.isEmpty()) {
            return "User Don't Exists!";
        }

        User getUser = user.get();
        Product productExist = productRepository.findById(productId)
                .orElseThrow(
                        () -> new IllegalStateException("Sorry, Not Avail!")
                );
        if (productDTO.getUserId().equals(productExist.getUser().getId())) {

            Integer imageId = productExist.getImage().getImgId();
            String formerImageName = productExist.getImage().getName();
            Image image = imageService.updateImage(multipartFile, getUser, imageId, formerImageName);

            productExist.setId(productId);
            productExist.setProductName(productDTO.getProductName());
            productExist.setProductDescription(productDTO.getProductDescription());
            productExist.setProductCategory(productDTO.getProductCategory());
            productExist.setPrice(productDTO.getPrice());
            productExist.setUser(getUser);
            productExist.setImage(image);

            productRepository.save(productExist);
            return "product Id: " + productExist.getId() + " Successfully Updated";
        } else {
            return "Unavailable to Update Product";
        }

    }

    public List<ProductResponse> getProduct() {
        List<Product> getProduct = productRepository.findAll();
        List<ProductResponse> productResponse = new ArrayList<>();
        for (Product i : getProduct) {
            productResponse.add(new ProductResponse(
                    i.getId(),
                    i.getProductName(),
                    i.getProductDescription(),
                    i.getProductCategory(),
                    i.getPrice(),
                    i.getUser().getId(),
                    i.getImage().getImagePath()
            ));
        }
        return productResponse;
    }

    public List<ProductResponse> findByProductName(String productName) {
        List<Product> getProductListByProductName = productRepository.findAllByProductName(productName);
        List<ProductResponse> productResponse = new ArrayList<>();
        for (Product i : getProductListByProductName) {
            productResponse.add(new ProductResponse(
                    i.getId(),
                    i.getProductName(),
                    i.getProductDescription(),
                    i.getProductCategory(),
                    i.getPrice(),
                    i.getUser().getId(),
                    i.getImage().getImagePath()
            ));
        }
        return productResponse;
    }

    public String deleteProduct(Integer productId) {
        Product deleteProduct = productRepository.findById(productId).orElseThrow(
                () -> new IllegalStateException("The product you searched for doesn't exists")
        );
        System.out.println(deleteProduct.getProductName());
        System.out.println("User ID: " + deleteProduct.getImage().getUser().getId());
        productRepository.delete(deleteProduct);
        imageService.deleteImage(deleteProduct.getImage());
        return "product Id: " + productId + " Successfully Deleted!!";
    }

    public ProductResponse findByProductId(Integer productId) {
        Optional<Product> productIdExists = productRepository.findById(productId);
        if (productIdExists.isEmpty()) {
            throw new IllegalStateException("Not Found!");
        }
        Product getProductById = productIdExists.get();
        ProductResponse productResponse = new ProductResponse(
                getProductById.getId(),
                getProductById.getProductName(),
                getProductById.getProductDescription(),
                getProductById.getProductCategory(),
                getProductById.getPrice(),
                getProductById.getUser().getId(),
                getProductById.getImage().getImagePath()
        );
        return productResponse;
    }
}
