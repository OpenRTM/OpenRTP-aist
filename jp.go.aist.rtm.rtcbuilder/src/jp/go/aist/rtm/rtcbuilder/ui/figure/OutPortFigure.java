package jp.go.aist.rtm.rtcbuilder.ui.figure;

import jp.go.aist.rtm.rtcbuilder.model.component.DataOutPort;
import jp.go.aist.rtm.rtcbuilder.model.component.PortDirection;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

public class OutPortFigure extends PortFigureBase {

	@SuppressWarnings("unused")
	private DataOutPort outport;

	public OutPortFigure(DataOutPort outport, int direction, Color backColor) {

		Rectangle constraint = null;
		this.outport = outport;
		int sizeX, sizeY, offsetX, offsetY;

		portLabel.setText(outport.getOutPort_Name());
		add(this.portLabel);

		if( direction == PortDirection.RIGHT ) {
			offsetX = LABELWIDTH + LABELMARGIN;
			addPolyPoint(offsetX + 0, 0);
			addPolyPoint(offsetX + 5, 0);
			addPolyPoint(offsetX + 10, 5);
			addPolyPoint(offsetX + 5, 10);
			addPolyPoint(offsetX + 0, 10);
			portLabel.setLabelAlignment(PositionConstants.RIGHT);
			constraint = new Rectangle(0, -3, LABELWIDTH, -1);
			sizeX = SIZE+LABELWIDTH+LABELMARGIN;
			sizeY = SIZE + 3;
		} else if( direction == PortDirection.TOP ) {
			offsetX = LABELWIDTH/2-SIZE/2;
			addPolyPoint(offsetX + 5, 0);
			addPolyPoint(offsetX + 10, 5);
			addPolyPoint(offsetX + 10, 10);
			addPolyPoint(offsetX + 0, 10);
			addPolyPoint(offsetX + 0, 5);
			portLabel.setLabelAlignment(PositionConstants.CENTER);
			constraint = new Rectangle(0, SIZE, LABELWIDTH, -1);
			sizeX = LABELWIDTH;
			sizeY = SIZE+SIZE+LABELMARGIN;
		} else if( direction == PortDirection.BOTTOM ) {
			offsetX = LABELWIDTH/2-SIZE/2;
			offsetY = SIZE + LABELMARGIN;
			addPolyPoint(offsetX + 0, offsetY + 0);
			addPolyPoint(offsetX + 10, offsetY + 0);
			addPolyPoint(offsetX + 10, offsetY + 5);
			addPolyPoint(offsetX + 5, offsetY + 10);
			addPolyPoint(offsetX + 0, offsetY + 5);
			portLabel.setLabelAlignment(PositionConstants.CENTER);
			constraint = new Rectangle(0, 0, LABELWIDTH, -1);
			sizeX = LABELWIDTH;
			sizeY = SIZE+SIZE+LABELMARGIN;
		} else {
			offsetX = 0;
			addPolyPoint(offsetX + 5, 0);
			addPolyPoint(offsetX + 10, 0);
			addPolyPoint(offsetX + 10, 10);
			addPolyPoint(offsetX + 5, 10);
			addPolyPoint(offsetX + 0, 5);
			constraint = new Rectangle(SIZE+LABELMARGIN, -3, -1, -1);
			sizeX = SIZE+LABELWIDTH+LABELMARGIN;
			sizeY = SIZE + 3;
		}

		this.setConstraint(portLabel, constraint);

		setSize(sizeX,sizeY);

		setBackgroundColor(backColor);
	}

}
