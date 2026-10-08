package jp.go.aist.rtm.rtcbuilder.container.param.setting;

import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MiddlewareDetail {
public List<Version> versions;
    
    @JsonProperty("workspace_presets")
    public List<String> workspacePresets;
    
    @JsonProperty("default_libs")
    public List<String> defaultLibs;
    
    @JsonProperty("functional_presets")
    public Map<String, List<String>> functionalPresets;
}
