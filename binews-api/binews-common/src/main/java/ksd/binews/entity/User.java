package ksd.binews.entity;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Pattern;

public class User implements Serializable {
    private String id;

    private String name;

    @Pattern(regexp="^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,20}$",message="密码必须为8-20位数字和字母的组合")
    private String password;
    
    private String rePassword;

    @Pattern(regexp="^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$",message="手机号格式不正确")
    private String mobile;

    private Date registerTime;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

    @Pattern(regexp="^[yn]{1}$")
    private String deletedFlag;
    
    private String checkCode;

    private static final long serialVersionUID = 1L;
    
    public User() {
		super();
	}

	public User(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

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
    
    public String getRePassword() {
        return rePassword;
    }

    public void setRePassword(String rePassword) {
        this.rePassword = rePassword == null ? null : rePassword.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
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

	public String getCheckCode() {
		return checkCode;
	}

	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", password=" + password + ", rePassword=" + rePassword + ", mobile=" + mobile + ", registerTime="
				+ registerTime + ", createTime=" + createTime + ", updateBy=" + updateBy + ", updateTime=" + updateTime + ", deletedFlag=" + deletedFlag + "]";
	}
    
    
}