package transport_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import transport_backend.entity.CompanyProductMapping;

import java.util.List;
import java.util.Optional;

public interface CompanyProductMappingRepository
        extends JpaRepository<CompanyProductMapping, Long> {

    Optional<CompanyProductMapping> findByCompanyIdAndProductId(
            Long companyId,
            Long productId
    );

    List<CompanyProductMapping> findByCompany_Id(Long companyId);

        Optional<CompanyProductMapping> findByIdAndCompanyId(
            Long id,
            Long companyId
    );
}