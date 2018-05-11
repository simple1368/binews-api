package ksd.binews.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class FileRecord implements Serializable {
    private String id;

    private String fileName;

    private String suffix;

    private BigDecimal fileSize;

    private String uri;

    private String newFileName;

    private Date createTime;

    public FileRecord() {
		super();
	}

	public FileRecord(String id, String fileName, String suffix, BigDecimal fileSize, String uri, String newFileName) {
		super();
		this.id = id;
		this.fileName = fileName;
		this.suffix = suffix;
		this.fileSize = fileSize;
		this.uri = uri;
		this.newFileName = newFileName;
	}

	private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName == null ? null : fileName.trim();
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix == null ? null : suffix.trim();
    }

    public BigDecimal getFileSize() {
        return fileSize;
    }

    public void setFileSize(BigDecimal fileSize) {
        this.fileSize = fileSize;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri == null ? null : uri.trim();
    }

    public String getNewFileName() {
        return newFileName;
    }

    public void setNewFileName(String newFileName) {
        this.newFileName = newFileName == null ? null : newFileName.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}