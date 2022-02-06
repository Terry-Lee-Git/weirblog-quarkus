package com.weirblog.vo;

public class JsonVO<T> {

	public Integer code = 200;
	
	public Boolean success = true;

	public String msg = "操作成功";

	public T data;

	public JsonVO() {
	}

	public JsonVO(Integer code, Boolean success, String msg) {
		this.code = code;
		this.success = success;
		this.msg = msg;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "JsonVO [code=" + code + ", success=" + success + ", msg=" + msg + ", data=" + data + "]";
	}

}
