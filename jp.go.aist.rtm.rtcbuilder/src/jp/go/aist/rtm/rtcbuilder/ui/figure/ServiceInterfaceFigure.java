package jp.go.aist.rtm.rtcbuilder.ui.figure;

import jp.go.aist.rtm.rtcbuilder.model.component.InterfaceDirection;
import jp.go.aist.rtm.rtcbuilder.model.component.PortDirection;
import jp.go.aist.rtm.rtcbuilder.model.component.ServiceInterface;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

public class ServiceInterfaceFigure extends PortFigureBase {

	public static int OFFSET_X = 50; 
	public static int OFFSET_TOP = 40; 
	public static int OFFSET_BOTTOM = 20; 
	public static int SPACE_X = 70; 
	public static int SPACE_Y = 20; 
	@SuppressWarnings("unused")
	private ServiceInterface serviceIF;

	/**
	 * コンストラクタ
	 * 
	 * @param servicePort
	 *            モデル
	 */
	public ServiceInterfaceFigure(ServiceInterface serviceIF, int portDirection, int ifDirection, Color backColor) {
		this.serviceIF = serviceIF;

		Rectangle constraint = null;
		int sizeX, sizeY, offsetX, offsetY;

		portLabel.setText(serviceIF.getServiceInterface_Name());
		add(this.portLabel);

		if( portDirection == PortDirection.RIGHT ) {
			offsetX = 0;
			if( ifDirection == InterfaceDirection.PROVIDED) {
				addOvalPoint(offsetX + 0, 0, 10, 10);
			} else {
				setLineWidth(2);
				addArcPoint(offsetX, 0, 10, 10, -90, -180);
			}
			portLabel.setLabelAlignment(PositionConstants.LEFT);
			constraint = new Rectangle(SIZE+LABELMARGIN, -3, LABELWIDTH, -1);
			sizeX = SIZE+LABELWIDTH+LABELMARGIN;
			sizeY = SIZE;
		} else if( portDirection == PortDirection.TOP ) {
			offsetX = LABELWIDTH/2-SIZE/2;
			offsetY = SIZE + LABELMARGIN;
			if( ifDirection == InterfaceDirection.PROVIDED) {
				addOvalPoint(offsetX+0, offsetY+0, 10, 10);
			} else {
				setLineWidth(2);
				addArcPoint(offsetX, offsetY+0, 10, 10, -180, 180);
			}
			portLabel.setLabelAlignment(PositionConstants.CENTER);
			constraint = new Rectangle(0, 0, LABELWIDTH, -1);
			sizeX = LABELWIDTH;
			sizeY = SIZE+SIZE+LABELMARGIN;
		} else if( portDirection == PortDirection.BOTTOM ) {
			offsetX = LABELWIDTH/2-SIZE/2;
			offsetY = 0;
			if( ifDirection == InterfaceDirection.PROVIDED) {
				addOvalPoint(offsetX+0, offsetY+0, 10, 10);
			} else {
				setLineWidth(2);
				addArcPoint(offsetX, offsetY+0, 10, 10, 0, 180);
			}
			portLabel.setLabelAlignment(PositionConstants.CENTER);
			constraint = new Rectangle(0, SIZE, LABELWIDTH, -1);
			sizeX = LABELWIDTH;
			sizeY = SIZE+SIZE+LABELMARGIN;
		} else {
			offsetX = LABELWIDTH+LABELMARGIN;
			if( ifDirection == InterfaceDirection.PROVIDED) {
				addOvalPoint(offsetX + 0, 0, 10, 10);
			} else {
				setLineWidth(2);
				addArcPoint(offsetX, 0, 10, 10, -90, 180);
			}
			portLabel.setLabelAlignment(PositionConstants.RIGHT);
			constraint = new Rectangle(0, -3, LABELWIDTH, -1);
			sizeX = SIZE+LABELWIDTH+LABELMARGIN;
			sizeY = SIZE;
		}

		this.setConstraint(portLabel, constraint);

		setSize(sizeX,sizeY);

		setBackgroundColor(backColor);
	}
}
