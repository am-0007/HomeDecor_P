package HomeDecor.user.repository;

import HomeDecor.user.User;
import HomeDecor.user.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface UserInterface extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE tb_user u " +
            "SET u.enabled = TRUE WHERE u.username = ?1 OR u.email = ?1")
    int enableUser(String username);

    @Query("select u from tb_user u where " +
            "u.userRole = ?1")
    List<User> findAllByUserRole(UserRole userRole);

    @Query("select COUNT(u) from tb_user u where " +
            "u.userRole = HomeDecor.user.UserRole.ADMIN OR " +
            "u.userRole = HomeDecor.user.UserRole.USER")
    Long countUser();

    Optional<User> findByUsernameAndEmail(String username, String email);

}
