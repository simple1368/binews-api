package ksd.binews.entity;

import java.io.Serializable;
import java.util.Date;

public class Rates implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String id;

    private String code;

    private String name;
    
    private Double rates;

    private Date createTime;

    private Date updateTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getRates() {
		return rates;
	}

	public void setRates(Double rates) {
		this.rates = rates;
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
}