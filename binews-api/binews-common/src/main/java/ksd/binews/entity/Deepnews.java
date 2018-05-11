package ksd.binews.entity;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.ws.rs.DefaultValue;

public class Deepnews extends BaseDBEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
    private String id;

    @Size(min=1,max=100,message="标题字数必须在1~100字之间")
    private String title;

    private String pic;

    @DefaultValue("0")
    private Integer browserCount;

    private String publishStatus;

    private String publishBy;

    private String publishLogo;

    //@JsonDeserialize(using=CustomJsonDateDeserializer.class)
    private Date publishTime;

    @Size(min=0,max=20,message="字数不能大于20字")
    private String snFrom;
    
    @Size(min=0,max=20,message="字数不能大于20字")
    private String snAuthor;
    
    @Size(min=0,max=20,message="字数不能大于20字")
    private String snCompile;

    @Pattern(regexp="^[yn]{1}$")
    private String topFlag;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

    @Pattern(regexp="^[yn]{1}$")
    private String deletedFlag;

    private String content;
    
    private Integer rank;
    
    private Deepnews next;
    
    private String picUrl;
    
    private String logoUrl;
    
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

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic == null ? null : pic.trim();
    }

    public Integer getBrowserCount() {
        return browserCount;
    }

    public void setBrowserCount(Integer browserCount) {
        this.browserCount = browserCount;
    }

    public String getPublishStatus() {
        return publishStatus;
    }

    public void setPublishStatus(String publishStatus) {
        this.publishStatus = publishStatus == null ? null : publishStatus.trim();
    }

    public String getPublishBy() {
        return publishBy;
    }

    public void setPublishBy(String publishBy) {
        this.publishBy = publishBy == null ? null : publishBy.trim();
    }

    public String getPublishLogo() {
        return publishLogo;
    }

    public void setPublishLogo(String publishLogo) {
        this.publishLogo = publishLogo == null ? null : publishLogo.trim();
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }
    
    public String getSnFrom() {
		return snFrom;
	}

	public void setSnFrom(String snFrom) {
		this.snFrom = snFrom;
	}

	public String getSnAuthor() {
		return snAuthor;
	}

	public void setSnAuthor(String snAuthor) {
		this.snAuthor = snAuthor;
	}

	public String getSnCompile() {
		return snCompile;
	}

	public void setSnCompile(String snCompile) {
		this.snCompile = snCompile;
	}

	public String getTopFlag() {
        return topFlag;
    }

    public void setTopFlag(String topFlag) {
        this.topFlag = topFlag == null ? null : topFlag.trim();
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

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public Deepnews getNext() {
		return next;
	}

	public void setNext(Deepnews next) {
		this.next = next;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	@Override
	public String toString() {
		return "Deepnews [id=" + id + ", title=" + title + ", pic=" + pic + ", browserCount=" + browserCount + ", publishStatus=" + publishStatus
				+ ", publishBy=" + publishBy + ", publishLogo=" + publishLogo + ", publishTime=" + publishTime + ", snFrom=" + snFrom + ", snAuthor=" + snAuthor
				+ ", snCompile=" + snCompile + ", topFlag=" + topFlag + ", createBy=" + createBy + ", createTime=" + createTime + ", updateBy=" + updateBy
				+ ", updateTime=" + updateTime + ", deletedFlag=" + deletedFlag + ", content=" + content + "]";
	}
    
}