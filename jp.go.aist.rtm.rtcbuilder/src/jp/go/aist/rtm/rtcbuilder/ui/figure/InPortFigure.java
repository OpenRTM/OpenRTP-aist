package jp.go.aist.rtm.rtcbuilder.ui.figure;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

import jp.go.aist.rtm.rtcbuilder.model.component.DataInPort;
import jp.go.aist.rtm.rtcbuilder.model.component.PortDirection;

/**
 * InPortのFigure
 */
public class InPortFigure extends PortFigureBase {

	@SuppressWarnings("unused")
	private DataInPort inport;

	public InPortFigure(DataInPort inport, int direction, Color backColor) {
		Rectangle constraint = null;
		this.inport = inport;
		int sizeX, sizeY, offsetX, offsetY;

		portLabel.setText(inport.getInPort_Name());
		add(this.portLabel);

		if( direction == PortDirection.RIGHT ) {
			offsetX = LABELWIDTH + LABELMARGIN;
			addPolyPoint(offsetX + 0, 0);
			addPolyPoint(offsetX + 0, 10);
			addPolyPoint(offsetX + 10, 10);
			addPolyPoint(offsetX + 5, 5);
			addPolyPoint(offsetX + 10, 0);
			portLabel.setLabelAlignment(PositionConstants.RIGHT);
			constraint = new Rectangle(0, -3, LABELWIDTH, -1);
			sizeX = SIZE+LABELWIDTH+LABELMARGIN;
			sizeY = SIZE + 3;
		} else if( direction == PortDirection.TOP ) {
			offsetX = LABELWIDTH/2-SIZE/2;
			addPolyPoint(offsetX + 0, 0);
			addPolyPoint(offsetX + 0, 10);
			addPolyPoint(offsetX + 10, 10);
			addPolyPoint(offsetX + 10, 0);
			addPolyPoint(offsetX + 5, 5);
			portLabel.setLabelAlignment(PositionConstants.CENTER);
			constraint = new Rectangle(0, SIZE, LABELWIDTH, -1);
			sizeX = LABELWIDTH;
			sizeY = SIZE+SIZE+LABELMARGIN;
		} else if( direction == PortDirection.BOTTOM ) {
			offsetX = LABELWIDTH/2-SIZE/2;
			offsetY = SIZE + LABELMARGIN;
			addPolyPoint(offsetX + 0, offsetY + 0);
			addPolyPoint(offsetX + 0, offsetY + 10);
			addPolyPoint(offsetX + 5, offsetY + 5);
			addPolyPoint(offsetX + 10, offsetY + 10);
			addPolyPoint(offsetX + 10, offsetY + 0);
			portLabel.setLabelAlignment(PositionConstants.CENTER);
			constraint = new Rectangle(0, 0, LABELWIDTH, -1);
			sizeX = LABELWIDTH;
			sizeY = SIZE+SIZE+LABELMARGIN;
		} else {
			offsetX = 0;
			addPolyPoint(offsetX + 0, 0);
			addPolyPoint(offsetX + 5, 5);
			addPolyPoint(offsetX + 0, 10);
			addPolyPoint(offsetX + 10, 10);
			addPolyPoint(offsetX + 10, 0);
			constraint = new Rectangle(SIZE+LABELMARGIN, -3, -1, -1);
			sizeX = SIZE+LABELWIDTH+LABELMARGIN;
			sizeY = SIZE + 3;
		}

		this.setConstraint(portLabel, constraint);

		setSize(sizeX,sizeY);

		setBackgroundColor(backColor);
	}
}
