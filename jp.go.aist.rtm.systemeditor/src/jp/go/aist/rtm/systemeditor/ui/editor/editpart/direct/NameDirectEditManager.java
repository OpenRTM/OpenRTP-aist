package jp.go.aist.rtm.systemeditor.ui.editor.editpart.direct;

import jp.go.aist.rtm.toolscommon.model.component.Component;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.swt.widgets.Text;

public class NameDirectEditManager extends DirectEditManager {

	Object model;

	public NameDirectEditManager(GraphicalEditPart source, Class<?> editorType,
			CellEditorLocator locator) {
		super(source, (Class)editorType, locator);
		this.model = source.getModel();
	}

	@Override
	protected void initCellEditor() {
		Object value = null;
		if (model instanceof Component) {
			value = ((Component) model).getInstanceNameL();
		}
		if (value == null) {
			return;
		}
		getCellEditor().setValue(value);
		Text text = (Text) getCellEditor().getControl();
		text.selectAll();
	}

}
