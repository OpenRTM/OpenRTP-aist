package jp.go.aist.rtm.rtcbuilder.fsm.editor.config;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the test package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the
 * Java representation for XML content. The Java representation of XML content
 * can consist of schema derived interfaces and classes representing the binding
 * of schema type definitions, element declarations and model groups. Factory
 * methods for each of these are provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

	/**
	 * Create a new ObjectFactory that can be used to create new instances of
	 * schema derived classes for package: test
	 * 
	 */
	public ObjectFactory() {
	}

	/**
	 * Create an instance of {@link SCXMLConstraints }
	 * 
	 */
	public SCXMLConstraints createSCXMLConstraints() {
		return new SCXMLConstraints();
	}

	/**
	 * Create an instance of {@link SCXMLConstraints.RestrictedState }
	 * 
	 */
	public SCXMLConstraints.RestrictedState createSCXMLConstraintsRestrictedState() {
		return new SCXMLConstraints.RestrictedState();
	}

	/**
	 * Create an instance of
	 * {@link SCXMLConstraints.RestrictedState.PossibleEvent }
	 * 
	 */
	public SCXMLConstraints.RestrictedState.PossibleEvent createSCXMLConstraintsRestrictedStatePossibleEvent() {
		return new SCXMLConstraints.RestrictedState.PossibleEvent();
	}

}