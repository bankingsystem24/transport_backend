package transport_backend.service;

import transport_backend.entity.CompanyMaster;
import transport_backend.repository.CompanyMasterRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyMasterService {

    private final CompanyMasterRepository repository;

    public CompanyMasterService(CompanyMasterRepository repository) {
        this.repository = repository;
    }

    public List<CompanyMaster> getAll() {
        return repository.findAll();
    }

    public CompanyMaster getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Company not found with id: " + id));
    }

    public CompanyMaster create(CompanyMaster company) {

        if (repository.existsByCompanyNameIgnoreCase(
                company.getCompanyName())) {
            throw new RuntimeException("Company name already exists");
        }

        return repository.save(company);
    }

    public CompanyMaster update(Long id, CompanyMaster request) {

        CompanyMaster existing = getById(id);

        existing.setCompanyName(request.getCompanyName());
        existing.setAddress(request.getAddress());
        existing.setPhone(request.getPhone());
        existing.setEmail(request.getEmail());

        return repository.save(existing);
    }

    public void delete(Long id) {

        CompanyMaster existing = getById(id);

        repository.delete(existing);
    }
}
