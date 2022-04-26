package HomeDecor.product.rating.ratingservice;

import HomeDecor.product.Product;
import HomeDecor.product.ProductRepository;
import HomeDecor.product.rating.RatingProduct;
import HomeDecor.product.rating.ratingproductdto.RatingProductDTO;
import HomeDecor.product.rating.ratingrepository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class RatingService {

    private final RatingRepository ratingRepository;
    private final ProductRepository productRepository;

    public Object rateProduct(RatingProductDTO ratingProductDTO) {
        RatingProduct ratingProduct = new RatingProduct();
        Optional<Product> productExist = productRepository.findById(ratingProductDTO.getProductID());
        if (productExist.isPresent()) {
            Product product = productExist.get();
            Integer setRatingId = null;
            List<RatingProduct> getProduct = ratingRepository.findByProduct(product);
            for (RatingProduct k : getProduct) {
                //check which userId rated the product and Update
                if (k.getUserID().equals(ratingProductDTO.getUserID())) {
                    setRatingId = k.getId();
                    break;

                }
            }
            if (setRatingId != null) {
                RatingProduct updateRateExistingProduct = ratingRepository.getById(setRatingId);
                updateRateExistingProduct.setRatingProduct(ratingProductDTO.getRateProduct());
                ratingRepository.save(updateRateExistingProduct);
                return "Data is Updated!";
            }
            //ratingProductDTO.userId != userID and save
            ratingProduct.setUserID(ratingProductDTO.getUserID());
            ratingProduct.setRatingProduct(ratingProductDTO.getRateProduct());
            ratingProduct.setProduct(product);
            ratingRepository.save(ratingProduct);
            return ratingProduct;
        }
        else
            return "Product does not exists!";
    }




}
