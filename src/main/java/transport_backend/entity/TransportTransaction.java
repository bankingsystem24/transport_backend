package transport_backend.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "transport_transactions")
public class TransportTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Mandatory
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductMaster product;

    // Mandatory
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private CompanyMaster company;

    // Optional
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private OwnerMaster owner;

    // Optional
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    private VehicleMaster vehicle;

    // Snapshot/display field
    @Column(name = "vehicle_name", length = 100)
    private String vehicleName;

    // Optional
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id")
    private PartyMaster party;

    // Snapshot/display field
    @Column(name = "party_name", length = 150)
    private String partyName;

    // Optional
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_id")
    private DestinationMaster destination;

    // Snapshot/display field
    @Column(name = "destination_name", length = 150)
    private String destinationName;

    private LocalDate diDate;

    @Column(name = "di_no", length = 100)
    private String diNo;

    @Column(name = "lr_no", length = 100)
    private String lrNo;

    @Column(name = "invoice_no", length = 100)
    private String invoiceNo;

    @Column(name = "invoice_no1", length = 100)
    private String invoiceNo1;

    @Column(name = "invoice_no2", length = 100)
    private String invoiceNo2;

    @Column(name = "loading_wt", precision = 15, scale = 3)
    private BigDecimal loadingWt;

    @Column(name = "unloading_wt", precision = 15, scale = 3)
    private BigDecimal unloadingWt;

    @Column(name = "loading_date")
    private LocalDate loadingDate;

    @Column(name = "unloading_date")
    private LocalDate unloadingDate;

    @Column(precision = 15, scale = 3)
    private BigDecimal shortage;

    @Column(name = "owner_rate", precision = 15, scale = 2)
    private BigDecimal ownerRate;

    @Column(name = "total_amount", precision = 15, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "jumbo_rate", precision = 15, scale = 2)
    private BigDecimal jumboRate;

    @Column(name = "ton_mt", precision = 15, scale = 3)
    private BigDecimal tonMt;

    @Column(name = "other_pay", precision = 15, scale = 2)
    private BigDecimal otherPay;

    @Column(name = "diesel_rq_no", length = 100)
    private String dieselRqNo;

    @Column(name = "diesel_rq_date")
    private LocalDate dieselRqDate;

    @Column(name = "diesel_rate", precision = 15, scale = 2)
    private BigDecimal dieselRate;

    @Column(name = "diesel_qty", precision = 15, scale = 3)
    private BigDecimal dieselQty;

    @Column(name = "diesel_amount", precision = 15, scale = 2)
    private BigDecimal dieselAmount;

    @Column(precision = 15, scale = 2)
    private BigDecimal advance;

    @Column(name = "parking_charges", precision = 15, scale = 2)
    private BigDecimal parkingCharges;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @Column(name = "trip_balance", precision = 15, scale = 2)
    private BigDecimal tripBalance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    public TransportTransaction() {
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductMaster getProduct() {
        return product;
    }

    public void setProduct(ProductMaster product) {
        this.product = product;
    }

    public CompanyMaster getCompany() {
        return company;
    }

    public void setCompany(CompanyMaster company) {
        this.company = company;
    }

    public OwnerMaster getOwner() {
        return owner;
    }

    public void setOwner(OwnerMaster owner) {
        this.owner = owner;
    }

    public VehicleMaster getVehicle() {
        return vehicle;
    }

    public void setVehicle(VehicleMaster vehicle) {
        this.vehicle = vehicle;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public PartyMaster getParty() {
        return party;
    }

    public void setParty(PartyMaster party) {
        this.party = party;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public DestinationMaster getDestination() {
        return destination;
    }

    public void setDestination(DestinationMaster destination) {
        this.destination = destination;
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

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}