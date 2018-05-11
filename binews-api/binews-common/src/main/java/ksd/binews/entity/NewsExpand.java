package ksd.binews.entity;

import java.io.Serializable;

public class NewsExpand extends News implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private String commented;
	
	private String shared;
	
	private String liked;
	

	public String getCommented() {
		return commented;
	}

	public void setCommented(String commented) {
		this.commented = commented;
	}

	public String getShared() {
		return shared;
	}

	public void setShared(String shared) {
		this.shared = shared;
	}

	public String getLiked() {
		return liked;
	}

	public void setLiked(String liked) {
		this.liked = liked;
	}
	
}
