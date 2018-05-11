package ksd.binews.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class CoinPrice implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
    private String id;

    private String name;

    private BigDecimal price;

    private Date pTime;

    private Date createTime;


    public CoinPrice() {
		super();
	}
    
	public CoinPrice(String id, String name, BigDecimal price, Date pTime, Date createTime) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.pTime = pTime;
		this.createTime = createTime;
	}

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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Date getpTime() {
        return pTime;
    }

    public void setpTime(Date pTime) {
        this.pTime = pTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

	@Override
	public String toString() {
		return "CoinPrice [id=" + id + ", name=" + name + ", price=" + price + ", pTime=" + pTime + ", createTime=" + createTime + "]";
	}
}