package jp.go.aist.rtm.systemeditor.ui.editor.editpart;

import jp.go.aist.rtm.systemeditor.manager.SystemEditorPreferenceManager;
import jp.go.aist.rtm.systemeditor.ui.editor.figure.ExportedInPortFigure;
import jp.go.aist.rtm.systemeditor.ui.editor.figure.InPortFigure;
import jp.go.aist.rtm.toolscommon.model.component.InPort;
import jp.go.aist.rtm.toolscommon.model.component.Port;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.Panel;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.PlatformUI;

/**
 * InPort��EditPart�N���X
 */
public class InPortEditPart extends PortEditPart {

	/**
	 * �R���X�g���N�^
	 * 
	 * @param actionRegistry
	 *            ActionRegistry
	 */
	public InPortEditPart(ActionRegistry actionRegistry) {
		super(actionRegistry);
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public InPort getModel() {
		return (InPort) super.getModel();
	}

	/**
	 * {@inheritDoc}
	 */
	public void notifyChanged(Notification notification) {
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			public void run() {
				if (isActive()) {
					refresh();
					refreshVisuals();
					refreshTargetConnections();
				}
			}
		});
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	protected IFigure createFigure() {
		IFigure result =  isExported() ? new ExportedInPortFigure(getModel())
				: new InPortFigure(getModel());
		result.setLocation(new Point(0, 0));

		OutPortEditPart.supportAutoCreateConnectorToolMode(getViewer(), result);

		return result;
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	protected void refreshVisuals() {
		Color color = SystemEditorPreferenceManager.getInstance().getColor(
				SystemEditorPreferenceManager.COLOR_DATAPORT_NO_CONNECT);
		if (isConnected()) {
			color = SystemEditorPreferenceManager.getInstance().getColor(
					SystemEditorPreferenceManager.COLOR_DATAPORT_CONNECTED);
		}

		getFigure().setBackgroundColor(color);

		getFigure().setToolTip(getDataPortToolTip(getModel()));

		if (getFigure().getParent() != null)
			((GraphicalEditPart) getParent()).setLayoutConstraint(this,
					getFigure(), getFigure().getBounds());
	}

	/**
	 * �f�[�^�|�[�g�̃c�[���`�b�v���擾����
	 * 
	 * @param profile
	 *            ���f��
	 * @return �c�[���`�b�v
	 */
	public static Panel getDataPortToolTip(Port port) {
		Panel tooltip = new Panel();
		tooltip.setLayoutManager(new StackLayout());

		String labelString = "";
		try {
			labelString = labelString
					+ (port.getNameL() == null ? "<unknown>" : port.getNameL()) + "\r\n";
			labelString = labelString
					+ (port.getDataTypes() == null ? "<unknown>"
							: port.getDataTypes().toString()) + "\r\n";
			labelString = labelString
					+ (port.getInterfaceTypes().size() == 0 ? "<unknown>"
							: port.getInterfaceTypes()) + "\r\n";
			labelString = labelString
					+ (port.getDataflowTypes().size() == 0 ? "<unknown>"
							: port.getDataflowTypes()) + "\r\n";
			labelString = labelString
					+ (port.getSubscriptionTypes().size() == 0 ? "<unknown>"
							: port.getSubscriptionTypes()) + ""; // \r\n�͍Ō�͂���Ȃ�
		} catch (RuntimeException e) {
			// void
		}

		Label label1 = new Label(labelString);
		tooltip.add(label1);
		return tooltip;
	}

}