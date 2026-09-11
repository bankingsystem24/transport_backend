package transport_backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import transport_backend.dto.ProductMasterRequest;
import transport_backend.dto.ProductMasterResponse;
import transport_backend.entity.ProductMaster;
import transport_backend.entity.User;
import transport_backend.exception.ResourceNotFoundException;
import transport_backend.repository.ProductMasterRepository;
import transport_backend.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ProductMasterService {

    private final ProductMasterRepository productMasterRepository;
    private final UserRepository userRepository;

    public ProductMasterService(
            ProductMasterRepository productMasterRepository,
            UserRepository userRepository) {

        this.productMasterRepository = productMasterRepository;
        this.userRepository = userRepository;
    }

    // CREATE
    public ProductMasterResponse create(ProductMasterRequest request) {

        if (productMasterRepository
                .existsByProductNameIgnoreCase(request.getProductName().trim())) {

            throw new RuntimeException("Product already exists");
        }

        User user = userRepository.findById(request.getCreatedBy())
                .orElseThrow(() -> new RuntimeException("User not found"));

        ProductMaster product = new ProductMaster();

        product.setProductName(request.getProductName().trim());
        product.setCreatedBy(user);
        product.setCreatedDate(LocalDateTime.now());

        ProductMaster saved = productMasterRepository.save(product);

        return mapToResponse(saved);
    }

    // GET ALL
    @Transactional(readOnly = true)
    public List<ProductMasterResponse> getAll() {

        return productMasterRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // GET BY ID
    @Transactional(readOnly = true)
    public ProductMasterResponse getById(Long id) {

        ProductMaster product = productMasterRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with id: " + id
                        ));

        return mapToResponse(product);
    }

    // UPDATE
    public ProductMasterResponse update(
            Long id,
            ProductMasterRequest request) {

        ProductMaster product = productMasterRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Product not found with id: " + id));

        String productName = request.getProductName().trim();

        if (!product.getProductName().equalsIgnoreCase(productName)
                && productMasterRepository.existsByProductNameIgnoreCase(productName)) {

            throw new RuntimeException("Product already exists");
        }

        // User user = userRepository.findById(request.getCreatedBy())
        //         .orElseThrow(() -> new RuntimeException("User not found"));

        product.setProductName(productName);
        // product.setCreatedBy(user);

        ProductMaster updated = productMasterRepository.save(product);

        return mapToResponse(updated);
    }

    // DELETE
    public void delete(Long id) {

        ProductMaster product = productMasterRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Product not found with id: " + id));

        productMasterRepository.delete(product);
    }

    // ENTITY -> RESPONSE
private ProductMasterResponse mapToResponse(ProductMaster product) {

    return new ProductMasterResponse(
            product.getId(),
            product.getProductName(),
            product.getCreatedBy().getId(),
            product.getCreatedBy().getName(),
            product.getCreatedDate()
    );
}
}