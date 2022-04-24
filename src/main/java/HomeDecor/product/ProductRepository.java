package HomeDecor.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface ProductRepository extends JpaRepository<Product, String> {

    Optional<Product> findById(Integer id);

    Optional<Product> findByProductName(String productName);

    List<Product> findAllByProductName(String productName);


//    void delete(String productName);

    //Product updateProductDetail(Product product, String productName);


}
