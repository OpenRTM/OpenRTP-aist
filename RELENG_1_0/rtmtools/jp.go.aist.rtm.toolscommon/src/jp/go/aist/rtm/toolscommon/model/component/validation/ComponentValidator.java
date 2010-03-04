package jp.go.aist.rtm.toolscommon.model.component.validation;


import jp.go.aist.rtm.toolscommon.model.component.Component;
import jp.go.aist.rtm.toolscommon.model.component.ConfigurationSet;
import jp.go.aist.rtm.toolscommon.model.component.ExecutionContext;
import jp.go.aist.rtm.toolscommon.model.component.InPort;
import jp.go.aist.rtm.toolscommon.model.component.OutPort;
import jp.go.aist.rtm.toolscommon.model.component.Port;
import jp.go.aist.rtm.toolscommon.model.component.ServicePort;
import jp.go.aist.rtm.toolscommon.model.component.SystemDiagram;
import org.eclipse.emf.common.util.EList;

/**
 * A sample validator interface for {@link jp.go.aist.rtm.toolscommon.model.component.Component}.
 * This doesn't really do anything, and it's not a real EMF artifact.
 * It was generated by the org.eclipse.emf.examples.generator.validator plug-in to illustrate how EMF's code generator can be extended.
 * This can be disabled with -vmargs -Dorg.eclipse.emf.examples.generator.validator=false.
 */
public interface ComponentValidator {
	boolean validate();

	boolean validateConfigurationSets(EList<ConfigurationSet> value);
	boolean validateActiveConfigurationSet(ConfigurationSet value);
	boolean validatePorts(EList<Port> value);
	boolean validateInports(EList<InPort> value);
	boolean validateOutports(EList<OutPort> value);
	boolean validateServiceports(EList<ServicePort> value);
	boolean validateComponents(EList<Component> value);
	boolean validateExecutionContexts(EList<ExecutionContext> value);
	boolean validateChildSystemDiagram(SystemDiagram value);
	boolean validateInstanceNameL(String value);
	boolean validateVenderL(String value);
	boolean validateDescriptionL(String value);
	boolean validateCategoryL(String value);
	boolean validateTypeNameL(String value);
	boolean validateVersionL(String value);
	boolean validatePathId(String value);
	boolean validateOutportDirection(String value);
	boolean validateCompositeTypeL(String value);
	boolean validateComponentId(String value);
	boolean validateRequired(boolean value);
}