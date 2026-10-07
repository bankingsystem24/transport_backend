package transport_backend.service;

import lombok.RequiredArgsConstructor;

import java.util.List;

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
            CompanyProductMappingRequest request, Long companyId) {

        CompanyMaster company = companyRepository
                .findById(companyId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Company not found: " + companyId
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
                                companyId,
                                request.getProductId()
                        )
                        .orElseGet(CompanyProductMapping::new);

        mapping.setCompany(company);
        mapping.setProduct(product);
        mapping.setActive(request.getActive());

        return mappingRepository.save(mapping);
    }


    public List<CompanyProductMapping> getAllByCompanyId(Long companyId) {
    return mappingRepository.findByCompany_Id(companyId);
}
}