package ksd.binews.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ChangeRank implements Serializable {
    private String id;

    private String name;

    private String code;

    private BigDecimal dayTradeAmount;

    private BigDecimal price;

    private BigDecimal changeValue;

    private Integer type;

    private Date createTime;

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

    public BigDecimal getDayTradeAmount() {
        return dayTradeAmount;
    }

    public void setDayTradeAmount(BigDecimal dayTradeAmount) {
        this.dayTradeAmount = dayTradeAmount;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getChangeValue() {
        return changeValue;
    }

    public void setChangeValue(BigDecimal changeValue) {
        this.changeValue = changeValue;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}