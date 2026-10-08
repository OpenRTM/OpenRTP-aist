package jp.go.aist.rtm.toolscommon.profiles.util;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.openrtp.namespaces.ros.version01.RosProfile;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import jp.go.aist.rtm.toolscommon.profiles.nl.Messages;

public class XmlHandlerROS {
	public String convertToXmlROS(RosProfile profile) throws Exception {
		String xmlString = "";
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance("org.openrtp.namespaces.ros.version01");
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper",
					new NamespacePrefixMapperImpl("http://www.openrtp.org/namespaces/ros"));
			StringWriter xmlFileWriter = new StringWriter();
			marshaller.marshal(profile, xmlFileWriter);
			xmlString = xmlFileWriter.toString();
		} catch (JAXBException exception) {
			throw new Exception(Messages.getString("XmlHandler.25"), exception);
		}
		return xmlString;
	}
	
	public RosProfile restoreFromXmlROS(String targetXML) throws Exception {
		RosProfile result = null;
	    SAXParserFactory spfactory = SAXParserFactory.newInstance();
	    SAXParser parser = spfactory.newSAXParser();
	    ROSXMLParser xmlParser = new ROSXMLParser();
	    StringReader xmlReader = new StringReader(targetXML);
	    parser.parse(new InputSource(xmlReader), xmlParser);
	    String targetClass = "org.openrtp.namespaces.ros.version01";

		JAXBContext jc = JAXBContext.newInstance(targetClass);
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		unmarshaller.setEventHandler(new javax.xml.bind.helpers.DefaultValidationEventHandler());
		//SAXでの解析後にcloseしてしまうため，再読込
	    xmlReader = new StringReader(targetXML);
	    Object profile = ((JAXBElement<?>)unmarshaller.unmarshal(xmlReader)).getValue();
	    //
    	result = (RosProfile)profile;

	    return result;
	}
	
	public boolean validateXmlROSBySchema(String targetString) throws Exception {
		try {
		    SAXParserFactory spfactory = SAXParserFactory.newInstance();
		    SAXParser parser = spfactory.newSAXParser();
		    ROSXMLParser xmlParser = new ROSXMLParser();
		    StringReader xmlReader = new StringReader(targetString);
		    parser.parse(new InputSource(xmlReader), xmlParser);
		    String targetClass = "org.openrtp.namespaces.ros.version01";

			JAXBContext jc = JAXBContext.newInstance(targetClass);
			Unmarshaller unmarshaller = jc.createUnmarshaller();

			SchemaFactory sf = SchemaFactory.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = sf.newSchema(getClass().getResource("/ROSProfile_ext.xsd"));
			unmarshaller.setSchema(schema);

			((JAXBElement<?>)unmarshaller.unmarshal(new StringReader(targetString))).getValue();

		} catch (JAXBException e) {
			throw new JAXBException("XML Validation Error.", e);
		}
		return true;
	}

	private class ROSXMLParser extends DefaultHandler {
		private String version = "";

		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			if(qName.equals("ros:ROSProfile")) {
				for(int intIdx=0;intIdx<attributes.getLength();intIdx++) {
					if( attributes.getQName(intIdx).equals("ros:version") ) {
						version = attributes.getValue(intIdx);
						break;
					}
				}
			}
			super.startElement(uri, localName, qName, attributes);
		}

	}
	


}
