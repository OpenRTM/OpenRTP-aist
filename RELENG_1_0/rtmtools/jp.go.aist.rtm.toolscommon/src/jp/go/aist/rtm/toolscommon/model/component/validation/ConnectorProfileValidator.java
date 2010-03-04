package jp.go.aist.rtm.toolscommon.model.component.validation;


/**
 * A sample validator interface for {@link jp.go.aist.rtm.toolscommon.model.component.ConnectorProfile}.
 * This doesn't really do anything, and it's not a real EMF artifact.
 * It was generated by the org.eclipse.emf.examples.generator.validator plug-in to illustrate how EMF's code generator can be extended.
 * This can be disabled with -vmargs -Dorg.eclipse.emf.examples.generator.validator=false.
 */
public interface ConnectorProfileValidator {
	boolean validate();

	boolean validateDataflowType(String value);
	boolean validateSubscriptionType(String value);
	boolean validateSubscriptionTypeAvailable(boolean value);
	boolean validatePushIntervalAvailable(boolean value);
	boolean validateName(String value);
	boolean validateConnectorId(String value);
	boolean validateDataType(String value);
	boolean validateInterfaceType(String value);
	boolean validatePushRate(Double value);
	boolean validateSourceString(String value);
	boolean validateTargetString(String value);
}