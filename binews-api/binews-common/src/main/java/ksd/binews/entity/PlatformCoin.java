package ksd.binews.entity;

import java.io.Serializable;
import java.util.Date;

public class PlatformCoin implements Serializable {
    private String id;

    private String platformId;

    private String coinId;

    private String quoteCurrencyId;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

    private String deletedFlag;

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId == null ? null : platformId.trim();
    }

    public String getCoinId() {
        return coinId;
    }

    public void setCoinId(String coinId) {
        this.coinId = coinId == null ? null : coinId.trim();
    }

    public String getQuoteCurrencyId() {
        return quoteCurrencyId;
    }

    public void setQuoteCurrencyId(String quoteCurrencyId) {
        this.quoteCurrencyId = quoteCurrencyId == null ? null : quoteCurrencyId.trim();
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
}