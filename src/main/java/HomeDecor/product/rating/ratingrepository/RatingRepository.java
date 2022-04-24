package HomeDecor.product.rating.ratingrepository;

import HomeDecor.product.Product;
import HomeDecor.product.rating.RatingProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<RatingProduct, Integer> {
    //Optional<RatingProduct> findByProduct(Product product);

    List<RatingProduct> findAllById(Integer productId);
    List<RatingProduct> findByProduct(Product product);

    /*List<RatingProduct> fillItemsUserBased(Integer userId);*/
//    Optional<RatingProduct> findByProduct(Integer productID);
}
