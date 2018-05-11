package ksd.binews.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.validation.constraints.Max;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class News implements Serializable {
	private static final long serialVersionUID = 1L;
	
    private String id;
    
    @Size(min=4,max=120)
    private String title;
    
    @Size(min=4,max=2000)
    private String content;
    
    @Max(value=5)
    private Integer publishStatus;
    
    private Timestamp publishTime;
    
    private String publishBy;
    
    private String likedFlag;
    
    @Pattern(regexp="^[yn]{1}$")
    private String commentFlag;
    
    @Pattern(regexp="^[yn]{1}$")
    private String shareFlag;
    
    private Integer likedCount;
    
    private Integer commentCount;
    
    private Integer shareCount;
    
    private Integer voteUpCount;
    
    private Integer voteDownCount;
    
    @Pattern(regexp="^[yn]{1}$")
    private String importantFlag;
    
    private String createBy;
    
    private Timestamp createTime;
    
    private String updateBy;
    
    private Timestamp updateTime;
    
    @Pattern(regexp="^[yn]{1}$")
    private String deletedFlag;

    
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getPublishStatus() {
		return publishStatus;
	}

	public void setPublishStatus(Integer publishStatus) {
		this.publishStatus = publishStatus;
	}

	public Timestamp getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Timestamp publishTime) {
		this.publishTime = publishTime;
	}

	public String getPublishBy() {
		return publishBy;
	}

	public void setPublishBy(String publishBy) {
		this.publishBy = publishBy;
	}

	public String getLikedFlag() {
		return likedFlag;
	}

	public void setLikedFlag(String likedFlag) {
		this.likedFlag = likedFlag;
	}

	public String getCommentFlag() {
		return commentFlag;
	}

	public void setCommentFlag(String commentFlag) {
		this.commentFlag = commentFlag;
	}

	public String getShareFlag() {
		return shareFlag;
	}

	public void setShareFlag(String shareFlag) {
		this.shareFlag = shareFlag;
	}

	public Integer getLikedCount() {
		return likedCount;
	}

	public void setLikedCount(Integer likedCount) {
		this.likedCount = likedCount;
	}

	public Integer getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(Integer commentCount) {
		this.commentCount = commentCount;
	}

	public Integer getShareCount() {
		return shareCount;
	}

	public void setShareCount(Integer shareCount) {
		this.shareCount = shareCount;
	}

	public String getImportantFlag() {
		return importantFlag;
	}

	public void setImportantFlag(String importantFlag) {
		this.importantFlag = importantFlag;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public String getDeletedFlag() {
		return deletedFlag;
	}

	public void setDeletedFlag(String deletedFlag) {
		this.deletedFlag = deletedFlag;
	}

	public Integer getVoteUpCount() {
		return voteUpCount;
	}

	public void setVoteUpCount(Integer voteUpCount) {
		this.voteUpCount = voteUpCount;
	}

	public Integer getVoteDownCount() {
		return voteDownCount;
	}

	public void setVoteDownCount(Integer voteDownCount) {
		this.voteDownCount = voteDownCount;
	}

	@Override
	public String toString() {
		return "News [id=" + id + ", title=" + title + ", content=" + content + ", publishStatus=" + publishStatus + ", publishTime=" + publishTime
				+ ", publishBy=" + publishBy + ", likedFlag=" + likedFlag + ", commentFlag=" + commentFlag + ", shareFlag=" + shareFlag + ", likedCount="
				+ likedCount + ", commentCount=" + commentCount + ", shareCount=" + shareCount + ", voteUpCount=" + voteUpCount + ", voteDownCount="
				+ voteDownCount + ", importantFlag=" + importantFlag + ", createBy=" + createBy + ", createTime=" + createTime + ", updateBy=" + updateBy
				+ ", updateTime=" + updateTime + ", deletedFlag=" + deletedFlag + "]";
	}
   
}