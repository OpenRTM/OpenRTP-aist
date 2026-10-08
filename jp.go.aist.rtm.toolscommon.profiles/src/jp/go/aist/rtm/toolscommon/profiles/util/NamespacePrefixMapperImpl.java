package jp.go.aist.rtm.toolscommon.profiles.util;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

public class NamespacePrefixMapperImpl extends NamespacePrefixMapper {
//    private String rootNamespace;

    public NamespacePrefixMapperImpl(String rootNamespace) {
        super();
//        this.rootNamespace = rootNamespace;
    }

    public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
        if (namespaceUri == null || namespaceUri.equals("")) {
            return "";
        }
        //
        if (namespaceUri.equalsIgnoreCase("http://www.openrtp.org/namespaces/rts")){
            return "rts";
        }
        if (namespaceUri.equalsIgnoreCase("http://www.openrtp.org/namespaces/rts_ext")){
            return "rtsExt";
        }
        //
        if (namespaceUri.equalsIgnoreCase("http://www.openrtp.org/namespaces/rtc")){
            return "rtc";
        }
        if (namespaceUri.equalsIgnoreCase("http://www.openrtp.org/namespaces/rtc_doc")){
            return "rtcDoc";
        }
        if (namespaceUri.equalsIgnoreCase("http://www.openrtp.org/namespaces/rtc_ext")){
            return "rtcExt";
        }
        if (namespaceUri.equalsIgnoreCase("http://www.openrtp.org/namespaces/ros")){
            return "ros";
        }
        if (namespaceUri.equalsIgnoreCase("http://www.openrtp.org/namespaces/ros_doc")){
            return "rosDoc";
        }
        if (namespaceUri.equalsIgnoreCase("http://www.openrtp.org/namespaces/ros_ext")){
            return "rosExt";
        }
        if (namespaceUri.equalsIgnoreCase("http://www.w3.org/2001/XMLSchema-instance") ){
	      return "xsi";
	    }
        return suggestion;
    }

    @Override
    public String[] getPreDeclaredNamespaceUris() {
        return new String[] {"http://www.w3.org/2001/XMLSchema-instance"};
    }
}
