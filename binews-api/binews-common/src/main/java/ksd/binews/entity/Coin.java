package ksd.binews.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Pattern;

import ksd.binews.entity.price.PriceEntity;

public class Coin implements Serializable {
    private String id;

    private String enName;

    private String cnName;

    private String code;

    private Double price;

    private Float dayRise;

    private Double dayTradeAmount;

    private Double marketValue;

    private String remark;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;
    
    private List<PriceEntity> marketTrend;
    
    public List<PriceEntity> getMarketTrend() {
		return marketTrend;
	}

	public void setMarketTrend(List<PriceEntity> marketTrend) {
		this.marketTrend = marketTrend;
	}

	@Pattern(regexp="^[yn]{1}$")
    private String deletedFlag;
    
    private Integer rank;

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName == null ? null : enName.trim();
    }

    public String getCnName() {
        return cnName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName == null ? null : cnName.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Float getDayRise() {
        return dayRise;
    }

    public void setDayRise(Float dayRise) {
        this.dayRise = dayRise;
    }

    public Double getDayTradeAmount() {
        return dayTradeAmount;
    }

    public void setDayTradeAmount(Double dayTradeAmount) {
        this.dayTradeAmount = dayTradeAmount;
    }

    public Double getMarketValue() {
        return marketValue;
    }

    public void setMarketValue(Double marketValue) {
        this.marketValue = marketValue;
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
}