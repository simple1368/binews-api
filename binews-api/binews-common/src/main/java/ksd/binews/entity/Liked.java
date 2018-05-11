package ksd.binews.entity;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Pattern;

public class Liked implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
    private String id;

    private String newsId;

    private String likedBy;

    private Date likedTime;

    private Date createTme;

    private Date updateTime;

    @Pattern(regexp="^[yn]{1}$")
    private String deletedFlag;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId == null ? null : newsId.trim();
    }

    public String getLikedBy() {
        return likedBy;
    }

    public void setLikedBy(String likedBy) {
        this.likedBy = likedBy == null ? null : likedBy.trim();
    }

    public Date getLikedTime() {
        return likedTime;
    }

    public void setLikedTime(Date likedTime) {
        this.likedTime = likedTime;
    }

    public Date getCreateTme() {
        return createTme;
    }

    public void setCreateTme(Date createTme) {
        this.createTme = createTme;
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