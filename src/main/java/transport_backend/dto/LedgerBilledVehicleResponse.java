package transport_backend.dto;

import java.math.BigDecimal;

public class LedgerBilledVehicleResponse {

    private Long vehicleId;
    private String vehicleName;

    private BigDecimal balanceAmount;
    private BigDecimal dieselAmount;
    private BigDecimal advance;
    private BigDecimal billedAmount;
    private Long companyId;
    private String companyName;

    public LedgerBilledVehicleResponse() {
    }

    public LedgerBilledVehicleResponse(
            Long vehicleId,
            String vehicleName,
            BigDecimal balanceAmount,
            BigDecimal dieselAmount,
            BigDecimal advance,
            BigDecimal billedAmount,
            Long companyId,
            String companyName) {

        this.vehicleId = vehicleId;
        this.vehicleName = vehicleName;
        this.balanceAmount = balanceAmount;
        this.dieselAmount = dieselAmount;
        this.advance = advance;
        this.billedAmount = billedAmount;
        this.companyId = companyId;
        this.companyName = companyName;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public BigDecimal getBalanceAmount() {
        return balanceAmount;
    }

    public void setBalanceAmount(BigDecimal balanceAmount) {
        this.balanceAmount = balanceAmount;
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

    public BigDecimal getBilledAmount() {
        return billedAmount;
    }

    public void setBilledAmount(BigDecimal billedAmount) {
        this.billedAmount = billedAmount;
    }

    public Long getCompanyId(){
        return companyId;
    }

    public void setCompanyId(Long companyId){
        this.companyId=companyId;
    }

    public String getCompanyName(){
        return companyName;
    }

    public void setCompanyName(String companyName){
        this.companyName = companyName;
    }
} 
