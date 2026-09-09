package transport_backend.repository;


import transport_backend.entity.CompanyMaster;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyMasterRepository
        extends JpaRepository<CompanyMaster, Long> {

    boolean existsByCompanyNameIgnoreCase(String companyName);
}
