package ksd.binews.dto;

import java.util.Map;

public class BaseResponse {

	private int code;
	private String message;
	private Map<String, Object> data;
	
	public BaseResponse(int code, String message, Map<String, Object> data) {
		this.setCode(code);
		this.setMessage(message);
		this.setData(data);
	}
	
	public static BaseResponse success() {
		return new BaseResponse(200, "success", null);
	}
	public static BaseResponse success(String message) {
		return new BaseResponse(200, message, null);
	}
	public static BaseResponse success(Map<String, Object> data) {
		return new BaseResponse(200, "success", data);
	}
	public static BaseResponse success(String message, Map<String, Object> data) {
		return new BaseResponse(200, message, data);
	}
	
	public static BaseResponse error() {
		return new BaseResponse(500, "fail", null);
	}
	public static BaseResponse error(String message) {
		return new BaseResponse(500, message, null);
	}
	public static BaseResponse error(Map<String, Object> data) {
		return new BaseResponse(500, "fail", data);
	}
	public static BaseResponse error(String message, Map<String, Object> data) {
		return new BaseResponse(500, message, data);
	}
	
	public static BaseResponse badrequest() {
		return new BaseResponse(500, "no identifier arguments", null);
	}
	public static BaseResponse badrequest(String message) {
		return new BaseResponse(500, message, null);
	}
	public static BaseResponse badrequest(Map<String, Object> data) {
		return new BaseResponse(500, "no identifier arguments", data);
	}
	public static BaseResponse badrequest(String message, Map<String, Object> data) {
		return new BaseResponse(500, message, data);
	}
	
	public static BaseResponse noLogin(String message) {
		return new BaseResponse(300, message, null);
	}
	
	public int getCode() {
		return code;
	}
	private void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	private void setMessage(String message) {
		this.message = message;
	}
	public Map<String, Object> getData() {
		return data;
	}
	private void setData(Map<String, Object> data) {
		this.data = data;
	}
	
	
}
