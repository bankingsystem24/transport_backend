
package transport_backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import transport_backend.dto.PaymentTransactionRequest;
import transport_backend.dto.PaymentTransactionResponse;
import transport_backend.entity.OwnerMaster;
import transport_backend.entity.PaymentTransaction;
import transport_backend.entity.User;
import transport_backend.repository.OwnerMasterRepository;
import transport_backend.repository.PaymentTransactionRepository;
import transport_backend.repository.UserRepository;
import transport_backend.security.JwtUtil;

@Service
public class PaymentTransactionService {

    private final PaymentTransactionRepository paymentTransactionRepository;
    private final OwnerMasterRepository ownerMasterRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public PaymentTransactionService(
            PaymentTransactionRepository paymentTransactionRepository,
            OwnerMasterRepository ownerMasterRepository,
            UserRepository userRepository,
            JwtUtil jwtUtil
            ) {

        this.paymentTransactionRepository = paymentTransactionRepository;
        this.ownerMasterRepository = ownerMasterRepository;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }


    // =========================================================
    // CREATE
    // =========================================================

@Transactional
public PaymentTransactionResponse create(
        PaymentTransactionRequest request,
        String token) {

    OwnerMaster owner = ownerMasterRepository
            .findById(request.getOwnerId())
            .orElseThrow(() ->
                    new RuntimeException(
                            "Owner not found with id: "
                                    + request.getOwnerId()
                    ));

    // Get userId from JWT
    Long userId = jwtUtil.extractUserId(token);

    User user = userRepository.findById(userId)
            .orElseThrow(() ->
                    new RuntimeException(
                            "User not found with id: " + userId
                    ));

    PaymentTransaction payment = new PaymentTransaction();

    payment.setPaymentDate(request.getPaymentDate());

    payment.setPaymentMonth(request.getPaymentMonth());

    payment.setOwner(owner);

    payment.setBankName(request.getBankName());

    payment.setAccountNo(request.getAccountNo());

    payment.setChequeNo(request.getChequeNo());

    payment.setAmount(request.getAmount());

    payment.setRemarks(request.getRemarks());

    // Set logged-in user
    payment.setCreatedBy(user);

    PaymentTransaction saved =
            paymentTransactionRepository.save(payment);

    return convertToResponse(saved);
}


    // =========================================================
    // GET ALL
    // =========================================================

    @Transactional(readOnly = true)
    public List<PaymentTransactionResponse> getAll() {

        return paymentTransactionRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }


    // =========================================================
    // GET BY ID
    // =========================================================

    @Transactional(readOnly = true)
    public PaymentTransactionResponse getById(Long id) {

        PaymentTransaction payment =
                paymentTransactionRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Payment transaction not found with id: "
                                                + id
                                ));

        return convertToResponse(payment);
    }


    // =========================================================
    // UPDATE
    // =========================================================

    @Transactional
    public PaymentTransactionResponse update(
            Long id,
            PaymentTransactionRequest request) {

        PaymentTransaction payment =
                paymentTransactionRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Payment transaction not found with id: "
                                                + id
                                ));

        OwnerMaster owner = ownerMasterRepository
                .findById(request.getOwnerId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Owner not found with id: "
                                        + request.getOwnerId()
                        ));

        payment.setPaymentDate(
                request.getPaymentDate()
        );

        payment.setPaymentMonth(
                request.getPaymentMonth()
        );

        payment.setOwner(owner);

        payment.setBankName(
                request.getBankName()
        );

        payment.setAccountNo(
                request.getAccountNo()
        );

        payment.setChequeNo(
                request.getChequeNo()
        );

        payment.setAmount(
                request.getAmount()
        );

        payment.setRemarks(
                request.getRemarks()
        );

        /*
         * createdBy is intentionally NOT changed during update.
         */

        PaymentTransaction updated =
                paymentTransactionRepository.save(payment);

        return convertToResponse(updated);
    }


    // =========================================================
    // DELETE
    // =========================================================

    @Transactional
    public void delete(Long id) {

        PaymentTransaction payment =
                paymentTransactionRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Payment transaction not found with id: "
                                                + id
                                ));

        paymentTransactionRepository.delete(payment);
    }


    // =========================================================
    // ENTITY -> RESPONSE
    // =========================================================

    private PaymentTransactionResponse convertToResponse(
            PaymentTransaction payment) {

        PaymentTransactionResponse response =
                new PaymentTransactionResponse();

        response.setId(payment.getId());

        response.setPaymentDate(
                payment.getPaymentDate()
        );

        response.setPaymentMonth(
                payment.getPaymentMonth()
        );


        // Owner
        if (payment.getOwner() != null) {

            response.setOwnerId(
                    payment.getOwner().getId()
            );

            response.setOwnerName(
                    payment.getOwner().getOwnerName()
            );
        }


        response.setBankName(
                payment.getBankName()
        );

        response.setAccountNo(
                payment.getAccountNo()
        );

        response.setChequeNo(
                payment.getChequeNo()
        );

        response.setAmount(
                payment.getAmount()
        );

        response.setRemarks(
                payment.getRemarks()
        );


        // Created By
        if (payment.getCreatedBy() != null) {

            response.setCreatedBy(
                    payment.getCreatedBy().getId()
            );

            response.setCreatedByName(
                    payment.getCreatedBy().getUsername()
            );
        }


        response.setCreatedDate(
                payment.getCreatedDate()
        );

        return response;
    }
}
