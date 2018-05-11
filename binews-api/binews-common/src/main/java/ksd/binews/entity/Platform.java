package ksd.binews.entity;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Pattern;

public class Platform implements Serializable {
    private String id;

    private String name;

    private String code;

    private String logo;

    private Double dayTradeAmount;

    private String tradeType;

    private String tradeBase;

    private Integer bitCount;

    private Integer tradePairCount;

    private String area;

    private String officalSite;

    private String remark;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;
    
    private String currencies;
    
    @Pattern(regexp="^[yn]{1}$")
    private String deletedFlag;

    private Integer rank;
    
    private String logoUrl;
    
    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo == null ? null : logo.trim();
    }

    public Double getDayTradeAmount() {
        return dayTradeAmount;
    }

    public void setDayTradeAmount(Double dayTradeAmount) {
        this.dayTradeAmount = dayTradeAmount;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType == null ? null : tradeType.trim();
    }

    public String getTradeBase() {
        return tradeBase;
    }

    public void setTradeBase(String tradeBase) {
        this.tradeBase = tradeBase == null ? null : tradeBase.trim();
    }

    public Integer getBitCount() {
        return bitCount;
    }

    public void setBitCount(Integer bitCount) {
        this.bitCount = bitCount;
    }

    public Integer getTradePairCount() {
        return tradePairCount;
    }

    public void setTradePairCount(Integer tradePairCount) {
        this.tradePairCount = tradePairCount;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area == null ? null : area.trim();
    }

    public String getOfficalSite() {
        return officalSite;
    }

    public void setOfficalSite(String officalSite) {
        this.officalSite = officalSite == null ? null : officalSite.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy == null ? null : updateBy.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getDeletedFlag() {
        return deletedFlag;
    }

    public void setDeletedFlag(String deletedFlag) {
        this.deletedFlag = deletedFlag == null ? null : deletedFlag.trim();
    }

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public String getCurrencies() {
		return currencies;
	}

	public void setCurrencies(String currencies) {
		this.currencies = currencies;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}
    
}