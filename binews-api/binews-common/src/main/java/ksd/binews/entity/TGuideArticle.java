package ksd.binews.entity;

import java.util.Date;

import javax.validation.constraints.Pattern;

public class TGuideArticle {
	private static final long serialVersionUID = 1L;
		
    private String id;

    private String title;

    private String subTitle;

    private String publishBy;

    private Date publishTime;

    @Pattern(regexp="^[HC]{1}$")
    private String type;
    
    private Integer articleType;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

    private String deletedFlag;

    private String content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public String getPublishBy() {
        return publishBy;
    }

    public void setPublishBy(String publishBy) {
        this.publishBy = publishBy == null ? null : publishBy.trim();
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

	public Integer getArticleType() {
		return articleType;
	}

	public void setArticleType(Integer articleType) {
		this.articleType = articleType;
	}
    
    
}