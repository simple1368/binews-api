package ksd.binews.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class CoinMarket implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
    private String id;

    private String platformCoinId;

    private BigDecimal lastPrice;

    private BigDecimal firstPrice;

    private BigDecimal totalDay;

    private BigDecimal flotRange;

    private BigDecimal marketPrice;

    private Date createTime;

    private Date updateTime;


    public CoinMarket() {
		super();
	}
    
	public CoinMarket(String id, String platformCoinId, BigDecimal lastPrice, BigDecimal firstPrice, BigDecimal totalDay, BigDecimal flotRange, BigDecimal marketPrice,
			Date createTime, Date updateTime) {
		super();
		this.id = id;
		this.platformCoinId = platformCoinId;
		this.lastPrice = lastPrice;
		this.firstPrice = firstPrice;
		this.totalDay = totalDay;
		this.flotRange = flotRange;
		this.marketPrice = marketPrice;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}



	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getPlatformCoinId() {
        return platformCoinId;
    }

    public void setPlatformCoinId(String platformCoinId) {
        this.platformCoinId = platformCoinId == null ? null : platformCoinId.trim();
    }

    public BigDecimal getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(BigDecimal lastPrice) {
        this.lastPrice = lastPrice;
    }

    public BigDecimal getFirstPrice() {
        return firstPrice;
    }

    public void setFirstPrice(BigDecimal firstPrice) {
        this.firstPrice = firstPrice;
    }

    public BigDecimal getTotalDay() {
        return totalDay;
    }

    public void setTotalDay(BigDecimal totalDay) {
        this.totalDay = totalDay;
    }

    public BigDecimal getFlotRange() {
        return flotRange;
    }

    public void setFlotRange(BigDecimal range) {
        this.flotRange = range;
    }

    public BigDecimal getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

	@Override
	public String toString() {
		return "CoinMarket [id=" + id + ", platformCoinId=" + platformCoinId + ", lastPrice=" + lastPrice + ", firstPrice=" + firstPrice + ", totalDay=" + totalDay + ", range="
				+ flotRange + ", marketPrice=" + marketPrice + ", createTime=" + createTime + ", updateTime=" + updateTime + "]";
	}
}