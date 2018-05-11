package ksd.binews.entity;

import java.io.Serializable;
import java.util.List;

public class CoinExpand extends Coin implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String refreshTime;
	
	private String pId;
	
	private String pName;
	
	private Integer rank;
	
	private String quoteCurrency;
	
	private List<CoinPrice> marketTrend;


	public String getRefreshTime() {
		return refreshTime;
	}

	public void setRefreshTime(String refreshTime) {
		this.refreshTime = refreshTime;
	}

	public String getpName() {
		return pName;
	}

	public void setpName(String pName) {
		this.pName = pName;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public String getQuoteCurrency() {
		return quoteCurrency;
	}

	public void setQuoteCurrency(String quoteCurrency) {
		this.quoteCurrency = quoteCurrency;
	}

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}
}
