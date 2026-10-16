package transport_backend.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import transport_backend.dto.OwnerBankDetailsDTO;
import transport_backend.entity.OwnerBankDetails;
import transport_backend.entity.OwnerMaster;
import transport_backend.repository.OwnerBankDetailsRepository;
import transport_backend.repository.OwnerMasterRepository;

@Service
public class OwnerBankDetailsService {

    private final OwnerBankDetailsRepository bankRepository;
    private final OwnerMasterRepository ownerRepository;

    public OwnerBankDetailsService(
            OwnerBankDetailsRepository bankRepository,
            OwnerMasterRepository ownerRepository) {
        this.bankRepository = bankRepository;
        this.ownerRepository = ownerRepository;
    }

    @Transactional
    public OwnerBankDetailsDTO create(OwnerBankDetailsDTO dto) {

        validate(dto);

        OwnerMaster owner = getOwner(dto.getOwnerId());

        checkDuplicate(dto.getOwnerId(), dto.getAccountNo(), null);

        OwnerBankDetails bank = new OwnerBankDetails();
        bank.setOwner(owner);

        copyFields(dto, bank);

        return toDTO(bankRepository.save(bank));
    }

    @Transactional(readOnly = true)
    public List<OwnerBankDetailsDTO> getAll() {
        return bankRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public OwnerBankDetailsDTO getById(Long id) {

        OwnerBankDetails bank = bankRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Bank details not found with ID: " + id));

        return toDTO(bank);
    }

    @Transactional(readOnly = true)
    public List<OwnerBankDetailsDTO> getByOwnerId(Long ownerId) {

        if (!ownerRepository.existsById(ownerId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Owner not found with ID: " + ownerId);
        }

        return bankRepository.findByOwner_Id(ownerId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional
    public OwnerBankDetailsDTO update(
            Long id, OwnerBankDetailsDTO dto) {

        validate(dto);

        OwnerBankDetails bank = bankRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Bank details not found with ID: " + id));

        OwnerMaster owner = getOwner(dto.getOwnerId());

        checkDuplicate(dto.getOwnerId(), dto.getAccountNo(), id);

        bank.setOwner(owner);
        copyFields(dto, bank);

        return toDTO(bankRepository.save(bank));
    }

    @Transactional
    public void delete(Long id) {

        OwnerBankDetails bank = bankRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Bank details not found with ID: " + id));

        bankRepository.delete(bank);
    }

    private OwnerMaster getOwner(Long ownerId) {

        return ownerRepository.findById(ownerId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Owner not found with ID: " + ownerId));
    }

    private void checkDuplicate(
            Long ownerId, String accountNo, Long excludeId) {

        boolean exists;

        if (excludeId == null) {
            exists = bankRepository
                    .existsByOwner_IdAndAccountNoIgnoreCase(
                            ownerId, accountNo);
        } else {
            exists = bankRepository
                    .existsByOwner_IdAndAccountNoIgnoreCaseAndIdNot(
                            ownerId, accountNo, excludeId);
        }

        if (exists) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "This account number already exists for this owner");
        }
    }

    private void validate(OwnerBankDetailsDTO dto) {

        if (dto == null || dto.getOwnerId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "ownerId is required");
        }

        if (dto.getAccountNo() == null
                || dto.getAccountNo().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "accountNo is required");
        }

        if (dto.getBankName() == null
                || dto.getBankName().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "bankName is required");
        }

        if (dto.getIfscCode() == null
                || !dto.getIfscCode().trim()
                        .matches("(?i)[A-Z]{4}0[A-Z0-9]{6}")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A valid IFSC code is required");
        }

        dto.setAccountNo(dto.getAccountNo().trim());
        dto.setBankName(dto.getBankName().trim());
        dto.setIfscCode(dto.getIfscCode().trim().toUpperCase());

        if (dto.getBranch() != null) {
            dto.setBranch(dto.getBranch().trim());
        }
    }

    private void copyFields(
            OwnerBankDetailsDTO dto, OwnerBankDetails bank) {

        bank.setAccountNo(dto.getAccountNo());
        bank.setBankName(dto.getBankName());
        bank.setIfscCode(dto.getIfscCode());
        bank.setBranch(dto.getBranch());
    }

    private OwnerBankDetailsDTO toDTO(OwnerBankDetails bank) {

        OwnerBankDetailsDTO dto = new OwnerBankDetailsDTO();

        dto.setId(bank.getId());
        dto.setOwnerId(bank.getOwner().getId());
        dto.setOwnerName(bank.getOwner().getOwnerName());
        dto.setAccountNo(bank.getAccountNo());
        dto.setBankName(bank.getBankName());
        dto.setIfscCode(bank.getIfscCode());
        dto.setBranch(bank.getBranch());

        return dto;
    }
}