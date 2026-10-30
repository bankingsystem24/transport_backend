
package transport_backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DashboardTransactionResponse {

    private Long companyId;
    private String companyName;
    private Long productId;
    private String productName;
    private Long ownerId;
    private String ownerName;
    private String partyName;
    private Long destinationId;
    private String destination;
    private LocalDate diDate;
    private BigDecimal totalAmount;
    private BigDecimal dieselAmount;
    private BigDecimal parkingCharges;
    private BigDecimal advance;
    private BigDecimal otherPay;
    private BigDecimal tripBalance;
    private BigDecimal profitAmount;
    private BigDecimal tonMt;

    public DashboardTransactionResponse(
            Long companyId,
            String companyName,
            Long productId,
            String productName,
            Long ownerId,
            String ownerName,
            String partyName,
            Long destinationId,
            String destination,
            LocalDate diDate,
            BigDecimal totalAmount,
            BigDecimal dieselAmount,
            BigDecimal parkingCharges,
            BigDecimal advance,
            BigDecimal otherPay,
            BigDecimal tripBalance,
            BigDecimal profitAmount,
            BigDecimal tonMt) {

        this.companyId = companyId;
        this.companyName = companyName;
        this.productId = productId;
        this.productName = productName;
        this.ownerId = ownerId;
        this.ownerName = ownerName;
        this.partyName = partyName;
        this.destinationId = destinationId;
        this.destination = destination;
        this.diDate = diDate;
        this.totalAmount = totalAmount;
        this.dieselAmount = dieselAmount;
        this.parkingCharges = parkingCharges;
        this.advance = advance;
        this.otherPay = otherPay;
        this.tripBalance = tripBalance;
        this.profitAmount = profitAmount;
        this.tonMt = tonMt;
    }

    public Long getCompanyId() { return companyId; }
    public String getCompanyName() { return companyName; }
    public Long getProductId() { return productId; }
    public String getProductName() { return productName; }
    public Long getOwnerId() { return ownerId; }
    public String getOwnerName() { return ownerName; }
    public String getPartyName() { return partyName; }
    public Long getDestinationId() { return destinationId; }
    public String getDestination() { return destination; }
    public LocalDate getDiDate() { return diDate; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public BigDecimal getDieselAmount() { return dieselAmount; }
    public BigDecimal getParkingCharges() { return parkingCharges; }
    public BigDecimal getAdvance() { return advance; }
    public BigDecimal getOtherPay() { return otherPay; }
    public BigDecimal getTripBalance() { return tripBalance; }
    public BigDecimal getProfitAmount() { return profitAmount; }
    public BigDecimal getTonMt(){ return tonMt;}
}
