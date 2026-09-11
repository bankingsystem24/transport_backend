package transport_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import transport_backend.entity.PartyMaster;

import java.util.Optional;

public interface PartyMasterRepository extends JpaRepository<PartyMaster, Long> {

    Optional<PartyMaster> findByPartyNameIgnoreCase(String partyName);

    boolean existsByPartyNameIgnoreCase(String partyName);
}