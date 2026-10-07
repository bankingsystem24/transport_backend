package transport_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import transport_backend.dto.CompanyProductMappingRequest;
import transport_backend.entity.CompanyMaster;
import transport_backend.entity.CompanyProductMapping;
import transport_backend.entity.ProductMaster;
import transport_backend.repository.CompanyMasterRepository;
import transport_backend.repository.CompanyProductMappingRepository;
import transport_backend.repository.ProductMasterRepository;

@Service
@RequiredArgsConstructor
public class CompanyProductMappingService {

    private final CompanyProductMappingRepository mappingRepository;
    private final CompanyMasterRepository companyRepository;
    private final ProductMasterRepository productRepository;

    public CompanyProductMapping saveOrUpdate(
            CompanyProductMappingRequest request) {

        CompanyMaster company = companyRepository
                .findById(request.getCompanyId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Company not found: " + request.getCompanyId()
                        ));

        ProductMaster product = productRepository
                .findById(request.getProductId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Product not found: " + request.getProductId()
                        ));

        CompanyProductMapping mapping =
                mappingRepository
                        .findByCompanyIdAndProductId(
                                request.getCompanyId(),
                                request.getProductId()
                        )
                        .orElseGet(CompanyProductMapping::new);

        mapping.setCompany(company);
        mapping.setProduct(product);
        mapping.setActive(request.getActive());

        return mappingRepository.save(mapping);
    }
}