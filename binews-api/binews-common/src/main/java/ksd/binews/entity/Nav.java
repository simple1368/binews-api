package ksd.binews.entity;

import java.io.Serializable;
import java.util.Date;

public class Nav implements Serializable {
    private String id;

    private String title;

    private String logo;
    
    private String logoUrl;

    private String country;
    
    private String tradeType;
    
    private String tradeMade;
    
    private String identityType;
    
    private String techType;

    private Integer supportNum;

    private String supportLang;

    private Integer type;
    
    private String url;

    private String mark;

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
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getTradeMade() {
		return tradeMade;
	}

	public void setTradeMade(String tradeMade) {
		this.tradeMade = tradeMade;
	}

	public String getIdentityType() {
		return identityType;
	}

	public void setIdentityType(String identityType) {
		this.identityType = identityType;
	}

	public String getTechType() {
		return techType;
	}

	public void setTechType(String techType) {
		this.techType = techType;
	}

	public Integer getSupportNum() {
		return supportNum;
	}

	public void setSupportNum(Integer supportNum) {
		this.supportNum = supportNum;
	}

	public String getSupportLang() {
		return supportLang;
	}

	public void setSupportLang(String supportLang) {
		this.supportLang = supportLang;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
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
		this.updateBy = updateBy;
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
		this.deletedFlag = deletedFlag;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

}