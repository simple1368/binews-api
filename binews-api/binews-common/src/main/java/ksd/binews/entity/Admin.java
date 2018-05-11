package ksd.binews.entity;

import java.io.Serializable;
import java.util.Date;

public class Admin implements Serializable {
    private String id;

    private String name;

    private String password;

    private Date createTime;

    public Admin() {
		super();
	}

	public Admin(String id, String name, String password) {
		super();
		this.id = id;
		this.name = name;
		this.password = password;
	}
    
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}