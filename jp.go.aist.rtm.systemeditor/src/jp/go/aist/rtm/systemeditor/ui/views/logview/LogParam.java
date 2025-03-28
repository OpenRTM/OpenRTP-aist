package jp.go.aist.rtm.systemeditor.ui.views.logview;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"time",
"name",
"manager",
"level",
"message"
})

public class LogParam {
	@JsonProperty("time")
	private String time;
	
	@JsonProperty("name")
	private String name;
	
	@JsonProperty("manager")
	private String manager;
	
	@JsonProperty("level")
	private String level;
	
	@JsonProperty("message")
	private String message;
	
	@JsonProperty("pid")
	private String pid;

	@JsonProperty("host")
	private String host;

	private Calendar cal;
	
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();
	
	@JsonProperty("time")
	public String getTime() {
		return time;
	}
	@JsonProperty("time")
	public void setTime(String time) {
		this.time = time;
		//
		this.cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
        try{
        	this.cal.setTime(sdf.parse(this.time));
//        	String str1 = sdf.format(this.cal.getTime());
//        	System.out.println(str1);
        } catch(ParseException ex) {
        }
	}
	
	public Calendar getCal() {
		return cal;
	}
	@JsonProperty("name")
	public String getName() {
		return name;
	}
	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}
	
	@JsonProperty("manager")
	public String getManager() {
		return manager;
	}
	@JsonProperty("manager")
	public void setManager(String manager) {
		this.manager = manager;
	}
	
	@JsonProperty("level")
	public String getLevel() {
		return level;
	}
	@JsonProperty("level")
	public void setLevel(String level) {
		this.level = level;
	}
	
	@JsonProperty("message")
	public String getMessage() {
		return message;
	}
	
	@JsonProperty("message")
	public void setMessage(String message) {
		this.message = message;
	}
	
	@JsonProperty("pid")
	public String getPid() {
		return pid;
	}
	
	@JsonProperty("pid")
	public void setPid(String pid) {
		this.pid = pid;
	}

	@JsonProperty("host")
	public String getHost() {
		return host;
	}
	
	@JsonProperty("host")
	public void setHost(String host) {
		this.host = host;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}
	
	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}
}
