package transport_backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class TransportTransactionResponse {

    private Long id;

    private Long productId;
    private String productName;

    private Long companyId;
    private String companyName;

    private Long ownerId;
    private String ownerName;

    private Long vehicleId;
    private String vehicleNumber;

    private String vehicleName;

    private Long partyId;
    private String partyName;

    private Long destinationId;
    private String destinationName;

    private LocalDate diDate;
    private String diNo;
    private String lrNo;

    private String invoiceNo;
    private String invoiceNo1;
    private String invoiceNo2;

    private BigDecimal loadingWt;
    private BigDecimal unloadingWt;

    private LocalDate loadingDate;
    private LocalDate unloadingDate;

    private BigDecimal shortage;
    private BigDecimal ownerRate;
    private BigDecimal totalAmount;
    private BigDecimal jumboRate;
    private BigDecimal tonMt;
    private BigDecimal otherPay;

    private String dieselRqNo;
    private LocalDate dieselRqDate;
    private BigDecimal dieselRate;
    private BigDecimal dieselQty;
    private BigDecimal dieselAmount;

    private BigDecimal advance;
    private BigDecimal parkingCharges;

    private LocalDate paymentDate;

    private BigDecimal tripBalance;

    private Long createdById;
    private LocalDateTime createdDate;


    // Constructor

    public TransportTransactionResponse() {
    }


    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public Long getPartyId() {
        return partyId;
    }

    public void setPartyId(Long partyId) {
        this.partyId = partyId;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public Long getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(Long destinationId) {
        this.destinationId = destinationId;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public LocalDate getDiDate() {
        return diDate;
    }

    public void setDiDate(LocalDate diDate) {
        this.diDate = diDate;
    }

    public String getDiNo() {
        return diNo;
    }

    public void setDiNo(String diNo) {
        this.diNo = diNo;
    }

    public String getLrNo() {
        return lrNo;
    }

    public void setLrNo(String lrNo) {
        this.lrNo = lrNo;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getInvoiceNo1() {
        return invoiceNo1;
    }

    public void setInvoiceNo1(String invoiceNo1) {
        this.invoiceNo1 = invoiceNo1;
    }

    public String getInvoiceNo2() {
        return invoiceNo2;
    }

    public void setInvoiceNo2(String invoiceNo2) {
        this.invoiceNo2 = invoiceNo2;
    }

    public BigDecimal getLoadingWt() {
        return loadingWt;
    }

    public void setLoadingWt(BigDecimal loadingWt) {
        this.loadingWt = loadingWt;
    }

    public BigDecimal getUnloadingWt() {
        return unloadingWt;
    }

    public void setUnloadingWt(BigDecimal unloadingWt) {
        this.unloadingWt = unloadingWt;
    }

    public LocalDate getLoadingDate() {
        return loadingDate;
    }

    public void setLoadingDate(LocalDate loadingDate) {
        this.loadingDate = loadingDate;
    }

    public LocalDate getUnloadingDate() {
        return unloadingDate;
    }

    public void setUnloadingDate(LocalDate unloadingDate) {
        this.unloadingDate = unloadingDate;
    }

    public BigDecimal getShortage() {
        return shortage;
    }

    public void setShortage(BigDecimal shortage) {
        this.shortage = shortage;
    }

    public BigDecimal getOwnerRate() {
        return ownerRate;
    }

    public void setOwnerRate(BigDecimal ownerRate) {
        this.ownerRate = ownerRate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getJumboRate() {
        return jumboRate;
    }

    public void setJumboRate(BigDecimal jumboRate) {
        this.jumboRate = jumboRate;
    }

    public BigDecimal getTonMt() {
        return tonMt;
    }

    public void setTonMt(BigDecimal tonMt) {
        this.tonMt = tonMt;
    }

    public BigDecimal getOtherPay() {
        return otherPay;
    }

    public void setOtherPay(BigDecimal otherPay) {
        this.otherPay = otherPay;
    }

    public String getDieselRqNo() {
        return dieselRqNo;
    }

    public void setDieselRqNo(String dieselRqNo) {
        this.dieselRqNo = dieselRqNo;
    }

    public LocalDate getDieselRqDate() {
        return dieselRqDate;
    }

    public void setDieselRqDate(LocalDate dieselRqDate) {
        this.dieselRqDate = dieselRqDate;
    }

    public BigDecimal getDieselRate() {
        return dieselRate;
    }

    public void setDieselRate(BigDecimal dieselRate) {
        this.dieselRate = dieselRate;
    }

    public BigDecimal getDieselQty() {
        return dieselQty;
    }

    public void setDieselQty(BigDecimal dieselQty) {
        this.dieselQty = dieselQty;
    }

    public BigDecimal getDieselAmount() {
        return dieselAmount;
    }

    public void setDieselAmount(BigDecimal dieselAmount) {
        this.dieselAmount = dieselAmount;
    }

    public BigDecimal getAdvance() {
        return advance;
    }

    public void setAdvance(BigDecimal advance) {
        this.advance = advance;
    }

    public BigDecimal getParkingCharges() {
        return parkingCharges;
    }

    public void setParkingCharges(BigDecimal parkingCharges) {
        this.parkingCharges = parkingCharges;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public BigDecimal getTripBalance() {
        return tripBalance;
    }

    public void setTripBalance(BigDecimal tripBalance) {
        this.tripBalance = tripBalance;
    }

    public Long getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Long createdById) {
        this.createdById = createdById;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}
