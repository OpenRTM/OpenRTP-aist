package jp.go.aist.rtm.rtcbuilder.container.param.setting;

import java.util.LinkedHashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnySetter;

public class PackageProvider {
	public String apt;
    public String pip;
    
    private Map<String, PackageProvider> children = new LinkedHashMap<>();

    @JsonAnySetter
    public void setChild(String key, PackageProvider value) {
        this.children.put(key, value);
    }

    public PackageProvider get(String key) {
        return this.children.get(key);
    }

    public Map<String, PackageProvider> getChildren() {
        return this.children;
    }
}
