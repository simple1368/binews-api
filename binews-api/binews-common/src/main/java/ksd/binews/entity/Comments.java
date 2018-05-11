package ksd.binews.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class Comments implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String id;
	
	private String newsId;
	
	@Size(min=4,max=300,message="评论字数必须在4~300字之间")
	private String content;
	
	private String commentBy;
	
	private Timestamp commentTime;
	
	@Pattern(regexp="^[yn]{1}$")
	private String showFlag;
	
	private Integer auditStatus;
	
	private String auditContent;
	
	private Timestamp createTime;
	
	private Timestamp updateTime;
	
	@Pattern(regexp="^[yn]{1}$")
	private String deletedFlag;

	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNewsId() {
		return newsId;
	}

	public void setNewsId(String newsId) {
		this.newsId = newsId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCommentBy() {
		return commentBy;
	}

	public void setCommentBy(String commentBy) {
		this.commentBy = commentBy;
	}

	public Timestamp getCommentTime() {
		return commentTime;
	}

	public void setCommentTime(Timestamp commentTime) {
		this.commentTime = commentTime;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
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
	
	

	public Integer getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(Integer auditStatus) {
		this.auditStatus = auditStatus;
	}

	public String getAuditContent() {
		return auditContent;
	}

	public void setAuditContent(String auditContent) {
		this.auditContent = auditContent;
	}
	
	public String getShowFlag() {
		return showFlag;
	}

	public void setShowFlag(String showFlag) {
		this.showFlag = showFlag;
	}

	@Override
	public String toString() {
		return "Comments [id=" + id + ", newsId=" + newsId + ", content=" + content + ", commentBy=" + commentBy + ", commentTime=" + commentTime
				+ ", showFlag=" + showFlag + ", auditStatus=" + auditStatus + ", auditContent=" + auditContent + ", createTime=" + createTime + ", updateTime="
				+ updateTime + ", deletedFlag=" + deletedFlag + "]";
	}
	
}
