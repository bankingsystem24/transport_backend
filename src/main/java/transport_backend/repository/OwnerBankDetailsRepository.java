package transport_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import transport_backend.entity.OwnerBankDetails;

public interface OwnerBankDetailsRepository
        extends JpaRepository<OwnerBankDetails, Long> {

    List<OwnerBankDetails> findByOwner_Id(Long ownerId);

    boolean existsByOwner_IdAndAccountNoIgnoreCase(
            Long ownerId, String accountNo);

    boolean existsByOwner_IdAndAccountNoIgnoreCaseAndIdNot(
            Long ownerId, String accountNo, Long id);
}