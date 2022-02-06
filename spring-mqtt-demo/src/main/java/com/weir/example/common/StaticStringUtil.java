package com.weir.example.common;

import java.util.Arrays;
import java.util.List;

/**
 * @author weir
 *
 * 2019年6月19日 上午11:16:27
 */
public class StaticStringUtil {

	public static final String VALUE = "value";
	public static final String FORWARD_SLASH = "/";
	public static final String UNDERLINE = "_";
	public static final String HYPHEN = "-";
	public static final String TILDE = "~";
	public static final String COMMA = ",";
	public static final String LEFT_BRACKET = "[";
	public static final String RIGHT_BRACKET = "]";
	public static final String BLANK = "";
	public static final int SHUZI_1 = 1;
	public static final int SHUZI_2 = 2;
	public static final int SHUZI_3 = 3;
	public static final int SHUZI_4 = 4;
	public static final String TOPIC = "topic";
	
	public static final String TIME_SYNS = "时间校准";
	public static final String VARIABLE_CONTROL = "变量控制";
	public static final String DATA = "data";
	public static final String PLC_STATUS = "plcStatus";
	public static final String COLLECTOR_INFO = "collectorInfo";
	public static final String ALARM_INFO = "alarmInfo";
	public static final String CONTROL_REPLY = "controlReply";
	public static final String DOWNLOAD_REPLY = "downloadReply";
	public static final String TIME = "time";
	
	public static final String USER_NOT_EXIST = "该用户不存在";
	public static final String USER_CANCEL = "该用户已注销";
	public static final String USER_PWD_ERROR = "用户名或密码错误";
	public static final String USER_TOKEN_ERROR = "用户token关系错误";
	public static final String INSTALLATION_INFOR = "installation_infor";
	public static final String INSTALLATION_INFOR_ID = "installation_infor_id";
	public static final String NO_PERSON_IN_CHARGE = "没有找到设备负责人";
	public static final String MESSAGE_USER = "message_user";
	public static final String EFFLUENT_DETECTION = "出水检测";
	public static final String EXCEPTION_MESSAGE = "设备异常消息";
	public static final String APPROVAL_PASS = "审批通过：";
	public static final String APPROVAL_NO_PASS = "未审批通过：";
	public static final String APPROVAL_RESULT = "审批结果消息";
	public static final String ORG_ID = "org_id";
	public static final String USER_ID = "user_id";
	public static final String CODE = "code";
	
	public static final String TOPIC_MQTT = "/dtu/%s/data";
	public static final String TOPIC_MQTT_CONTROL = "/dtu/%s/control";
	public static final String TOPIC_MQTT_CONTROLREPLY = "/dtu/%s/controlReply";
	public static final String TOPIC_MQTT_TIME = "/dtu/%s/time";
	public static final String TOPIC_MQTT_COLLECTORINFO = "/dtu/%s/collectorInfo";
	public static final String YES = "是";
	public static final String OUTFLOW_ALARM = "出水报警";
	public static final String INFLUENT_ALARM = "进水报警";
	public static final String ROUTINE_ALARM = "常规报警";
	public static final String STR_0 = "0";
	public static final String STR_1 = "1";
	public static final String PARAMETER_GROUP_TYPE = "parameter_group_type";
	public static final String ALARM_GROUP = "alarm_group";
	public static final String CREATE_TIME = "create_time";
	public static final String CLIENT_ID = "client_id";
	public static final String ID = "id";
	public static final String STATUS = "status";
	public static final String OPEN_COMMAND = "open_command";
	public static final String SHUT_COMMAND = "shut_command";
	public static final String COMMAND_RECORD_ID = "command_record_id";
	public static final int SHUZI_20 = 20;
	public static final String ASC = "asc";
	public static final int SHUZI_1000 = 1000;
	public static final long SHUZI_F_1 = -1;
	public static final String OUTPUT_RANGE = "(%s)";
	public static final String PLC_VARIABLE_GROUP = "plc_variable_group";
	public static final String CLOSE_OUT = "远程锁定出水";
	public static final int SHUZI_5 = 5;
	public static final String IS_MSG = "is_msg";
	public static final String TOPIC_DATA_ID = "topic_data_id";
	public static final String MODEL_TEMPLATE_ID = "model_template_id";
	public static final String PLC_ID = "plc_id";
	public static final String DICT_NAME = "dict_name";
	public static final String PARM_TYPE = "参数类型";
	public static final String PARAMETER_UNIT = "parameter_unit";
	public static final String PARM_UNIT = "参数单位";
	public static final int SHUZI_6 = 6;
	public static final int SHUZI_7 = 7;
	public static final int SHUZI_8 = 8;
	public static final int SHUZI_9 = 9;
	public static final int SHUZI_10 = 10;
	public static final int SHUZI_11 = 11;
	public static final int SHUZI_12 = 12;
	public static final int SHUZI_13 = 13;
	public static final String MODEL_ID = "model_id";
	public static final String UPDATE_TIME = "update_time";
	public static final String EQUIPMENT_ID = "equipment_id";
	public static final String PLC_VARIABLE_VALUE = "plc_variable_value";
	
	public static final List<String> ALARMS_LIST = Arrays.asList(OUTFLOW_ALARM,INFLUENT_ALARM,ROUTINE_ALARM);
	public static final int SHUZI_15 = 15;
	
	public static final String CLOSE_WATER = "出水关停";
	public static final String OPEN_WATER = "出水开启";
	public static final String INSTALL_PARAMETER_ID = "install_parameter_id";
}
