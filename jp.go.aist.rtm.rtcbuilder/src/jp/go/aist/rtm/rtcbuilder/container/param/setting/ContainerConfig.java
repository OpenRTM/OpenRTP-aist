package jp.go.aist.rtm.rtcbuilder.container.param.setting;

import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ContainerConfig {
	private Map<String, MiddlewareDetail> middlewares = new LinkedHashMap<>();

    @JsonProperty("mapping_db")
    public MappingDb mappingDb;

    @JsonAnySetter
    public void setMiddleware(String key, MiddlewareDetail value) {
        this.middlewares.put(key, value);
    }

    public Map<String, MiddlewareDetail> getMiddlewares() {
        return this.middlewares;
    }
}
