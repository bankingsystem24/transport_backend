package transport_backend.dto;

import java.math.BigDecimal;

public class BillCompletedSmsRequest {

    private String ownerName;
    private String mobile;
    private String vehicleNumber;
    private BigDecimal totalAmount;
    private BigDecimal tripBalance;
    private String billNo;

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTripBalance() {
        return tripBalance;
    }

    public void setTripBalance(BigDecimal tripBalance) {
        this.tripBalance = tripBalance;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }
}