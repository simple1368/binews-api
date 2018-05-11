package ksd.binews.dto;

import java.util.Date;

public class PriceDto {
	private String id;
	private String symbol;
	private double price;
	private Date startTime;
	private Date endTime;
	private int sTime;
	private int eTime;
	private String tableName;
	
	private Integer type;
	
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
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
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public int getsTime() {
		return sTime;
	}
	public void setsTime(int sTime) {
		this.sTime = sTime;
	}
	public int geteTime() {
		return eTime;
	}
	public void seteTime(int eTime) {
		this.eTime = eTime;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	
}
