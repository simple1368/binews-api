package ksd.binews.entity;

import java.io.Serializable;
import java.util.Date;

public class NavCurrency implements Serializable {
    private String id;

    private String navId;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

    private String deletedFlag;

    private String digitCurrencyId;

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getNavId() {
        return navId;
    }

    public void setNavId(String navId) {
        this.navId = navId == null ? null : navId.trim();
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

    public String getDigitCurrencyId() {
        return digitCurrencyId;
    }

    public void setDigitCurrencyId(String digitCurrencyId) {
        this.digitCurrencyId = digitCurrencyId == null ? null : digitCurrencyId.trim();
    }
}