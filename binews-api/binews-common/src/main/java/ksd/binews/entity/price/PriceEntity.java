package ksd.binews.entity.price;

import java.io.Serializable;

public class PriceEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 669896495323575924L;
	
	private String id;
	private String symbol;
	private double price;
	private long mTime;
	private long createTime;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public long getmTime() {
		return mTime;
	}
	public void setmTime(long mTime) {
		this.mTime = mTime;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
}
