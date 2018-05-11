package ksd.binews.entity;

import java.io.Serializable;
import java.util.Date;

public class LoggerInfo implements Serializable {
    private Long id;

    private String clientIp;

    private String uri;

    private String method;

    private Date createTime;

    private String paramData;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp == null ? null : clientIp.trim();
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri == null ? null : uri.trim();
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method == null ? null : method.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getParamData() {
        return paramData;
    }

    public void setParamData(String paramData) {
        this.paramData = paramData == null ? null : paramData.trim();
    }

	@Override
	public String toString() {
		return "LoggerInfo [id=" + id + ", clientIp=" + clientIp + ", uri=" + uri + ", method=" + method + ", createTime=" + createTime + ", paramData="
				+ paramData + "]";
	}
    
    
}