package transport_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import transport_backend.entity.CompanyProductMapping;

import java.util.Optional;

public interface CompanyProductMappingRepository
        extends JpaRepository<CompanyProductMapping, Long> {

    Optional<CompanyProductMapping> findByCompanyIdAndProductId(
            Long companyId,
            Long productId
    );
}