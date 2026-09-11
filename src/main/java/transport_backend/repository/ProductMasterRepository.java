package transport_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import transport_backend.entity.ProductMaster;

import java.util.Optional;

public interface ProductMasterRepository extends JpaRepository<ProductMaster, Long> {

    Optional<ProductMaster> findByProductNameIgnoreCase(String productName);

    boolean existsByProductNameIgnoreCase(String productName);
}