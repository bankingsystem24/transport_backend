package transport_backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class LedgerPaymentResponse {

    private Long id;
    private LocalDate paymentDate;
    private LocalDate paymentMonth;

    private String bankName;
    private String accountNo;
    private String chequeNo;
    private Long companyId;

    private BigDecimal amount;
    private String remarks;

    public LedgerPaymentResponse() {
    }

    public LedgerPaymentResponse(
            Long id,
            LocalDate paymentDate,
            LocalDate paymentMonth,
            String bankName,
            String accountNo,
            String chequeNo,
            BigDecimal amount,
            Long companyId,
            String remarks) {

        this.id = id;
        this.paymentDate = paymentDate;
        this.paymentMonth = paymentMonth;
        this.bankName = bankName;
        this.accountNo = accountNo;
        this.chequeNo = chequeNo;
        this.amount = amount;
        this.companyId = companyId;
        this.remarks = remarks;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public LocalDate getPaymentMonth() {
        return paymentMonth;
    }

    public void setPaymentMonth(LocalDate paymentMonth) {
        this.paymentMonth = paymentMonth;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getChequeNo() {
        return chequeNo;
    }

    public void setChequeNo(String chequeNo) {
        this.chequeNo = chequeNo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Long getCompanyId(){
        return companyId;
    }

    public void setCompanyId(Long companyId){
        this.companyId= companyId;
    }
}