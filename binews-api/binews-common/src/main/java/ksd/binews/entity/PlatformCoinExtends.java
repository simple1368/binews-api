package ksd.binews.entity;

import java.io.Serializable;

public class PlatformCoinExtends extends PlatformCoin implements Serializable{

	private static final long serialVersionUID = 1L;

	private String baseCurrency;
	
	private String quoteCurrency;

	
	public String getBaseCurrency() {
		return baseCurrency;
	}

	public void setBaseCurrency(String baseCurrency) {
		this.baseCurrency = baseCurrency;
	}

	public String getQuoteCurrency() {
		return quoteCurrency;
	}

	public void setQuoteCurrency(String quoteCurrency) {
		this.quoteCurrency = quoteCurrency;
	}
	
	
}