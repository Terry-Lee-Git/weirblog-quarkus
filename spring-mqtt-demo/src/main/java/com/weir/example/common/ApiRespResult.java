package com.weir.example.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 通用返回对象
 * @author weir
 *
 * 2019年5月7日 下午2:22:33
 */
@JsonIgnoreProperties
public class ApiRespResult<T> {

    @JsonIgnore
    private static final String SUCCESS_MSG = "请求成功";
    private static final String MYSQL_ERROR_MSG = "保存数据库失败，请联系管理员";

//    @ApiModelProperty("错误码")
    @JsonProperty("code")
    private int code = HttpStatus.SC_OK;

//    @ApiModelProperty("错误信息")
    @JsonProperty("msg")
    private String msg = SUCCESS_MSG;

//    @ApiModelProperty("请求非法的字段")
    @JsonProperty("field_name")
    private String fieldName;

//    @ApiModelProperty("响应数据")
    @JsonProperty("data")
    private T data;
    
    private String message;

    public ApiRespResult() {
    }

    public ApiRespResult(T data) {
        this.data = data;
    }

    public ApiRespResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ApiRespResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ApiRespResult(int code, String msg, String fieldName) {
		this.code = code;
		this.msg = msg;
		this.fieldName = fieldName;
	}
    
    public static ApiRespResult<Object> error() {
		return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, MYSQL_ERROR_MSG);
	}

	private static ApiRespResult<Object> error(int code, String msg) {
		ApiRespResult<Object> result = new ApiRespResult<>(code, msg);
		return result;
	}
	
	public static ApiRespResult<Object> error(String msg) {
		return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, msg);
	}
	
	public static ApiRespResult<Object> ok() {
		ApiRespResult<Object> result = new ApiRespResult<>();
		return result;
	}

	public int getCode() {
        return code;
    }

    public void setCode(int code) {
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

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "ApiRespResult [code=" + code + ", msg=" + msg + ", fieldName=" + fieldName
				+ ", data=" + data + "]";
	}
}
