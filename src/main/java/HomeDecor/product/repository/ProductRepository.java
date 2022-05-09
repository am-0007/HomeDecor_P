package HomeDecor.product.repository;

import HomeDecor.product.Product;
import HomeDecor.product.productStatus.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import static HomeDecor.product.productStatus.Status.*;

@Repository
@Transactional
public interface ProductRepository extends JpaRepository<Product, String> {

    @Query("select t from tb_product t")
    Page<Product> findAllByProductName(Pageable pageable);

    @Query("select t from tb_product t" +
            " where t.id = ?1 " +
            "AND t.status = ?2")
    Optional<Product> findById(Integer id, Status status);

    Optional<Product> findById(Integer id);

    Optional<Product> findByProductName(String productName);

    @Query("select t from tb_product t where " +
            "t.productName = :productName AND " +
            "t.status = :status")
    List<Product> findAllByProductName(@Param("productName") String productName, @Param("status") Status status);

    @Query("UPDATE tb_product p" +
            " SET p.status = HomeDecor.product.productStatus.Status.APPROVED" +
            " Where p.id = ?1")
    @Modifying
    int approveStatus(Integer productId);

    @Query("UPDATE tb_product p" +
            " SET p.status = HomeDecor.product.productStatus.Status.APPROVED" +
            " Where p.id = ?1")
    @Modifying
    int rejectStatus(Integer productId);

    @Query("select p from tb_product p where " +
            "p.status = ?2 and p.user.id = ?1")
    Optional<List<Product>> findAllByStatus(Long userId, Status approved);

    Optional<List<Product>> findAllByStatus(Status approved);


//    void delete(String productName);

    //Product updateProductDetail(Product product, String productName);


}
