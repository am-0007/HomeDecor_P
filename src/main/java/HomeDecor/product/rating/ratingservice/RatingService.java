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

    @Autowired
    private final RatingRepository ratingRepository;

    @Autowired
    private final ProductRepository productRepository;



    public Object rateProduct(RatingProductDTO ratingProductDTO) {
        RatingProduct ratingProduct = new RatingProduct();
        Optional<Product> productExist = productRepository.findById(ratingProductDTO.getProductID());
        if (productExist.isPresent()) {
            Integer setRatingId = null;
            Product product = productExist.get();
            List<RatingProduct> getProduct = ratingRepository.findByProduct(product);
            for (RatingProduct k : getProduct) {
                //check which userId rated the product and Update
                if (k.getUserID().equals(ratingProductDTO.getUserID())) {
                    setRatingId = k.getId();
                    /*
                    k.setRatingProduct(ratingProductDTO.getRateProduct());
                    ratingRepository.save(k);
                    */
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
            throw  new IllegalStateException("Product Doesn't Exists!");

       /* Optional<Product> productExists = productRepository.findById(ratingProductDTO.getProductID());
        if (productExists.isPresent()) {
            Product product = productExists.get();
            RatingProduct ratingProduct = new RatingProduct();
            Optional<RatingProduct> productIDExistsInRatingProduct = ratingRepository.findByProduct(product);
            if (productIDExistsInRatingProduct.isEmpty()) {
                ratingProduct.setUserID(ratingProductDTO.getUserID());
                ratingProduct.setRatingProduct(ratingProductDTO.getRateProduct());
                ratingProduct.setProduct(product);
                ratingRepository.save(ratingProduct);
                return ratingProduct;
            }
            ratingProduct.setRatingProduct(ratingProductDTO.getRateProduct());
            return ratingProduct;
            *//*RatingProduct updatingRatingProduct = productIDExistsInRatingProduct.get();
            double updateRating = (updatingRatingProduct.getRatingProduct() +
                    ratingProductDTO.getRateProduct()) / 2;
            updatingRatingProduct.setRatingProduct(updateRating);
            ratingRepository.save(updatingRatingProduct);
            return updatingRatingProduct;*//*
        }
        else {
            throw new IllegalStateException(ratingProductDTO.getProductID() + " Doesn't Exists!");
        }*/



        //Bro's code
        /*Optional<RatingProduct> ratingProductOptional = ratingRepository.findByProduct(ratingProduct.getProduct());
        if(ratingProductOptional.isEmpty()) {
            ratingRepository.save(ratingProduct);
            return ratingProduct;
        }
        RatingProduct updatingRatingProduct = ratingProductOptional.get();
        double updateRating = (updatingRatingProduct.getRatingProduct() +
                ratingProduct.getRatingProduct()) / 2;
        updatingRatingProduct.setRatingProduct(updateRating);
        ratingRepository.save(updatingRatingProduct);
        return updatingRatingProduct;*/
    }




}
