package jp.go.aist.rtm.rtcbuilder.ui.figure;

import org.eclipse.draw2d.XYLayout;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.PlatformUI;

import jp.go.aist.rtm.rtcbuilder.IRtcBuilderConstants;
import jp.go.aist.rtm.rtcbuilder.model.component.DataOutPort;
import jp.go.aist.rtm.rtcbuilder.util.RTCUtil;

public class OutPortBaseFigure extends PortFigureBase {
	private OutPortFigure outPortFig;

	public OutPortBaseFigure(DataOutPort outPort, int direction) {
		int portType = outPort.getPort_Type();
		RGB color = null;
		if(portType==IRtcBuilderConstants.Type_Event) {
			color = RTCUtil.defaultRGBMap.get(RTCUtil.COLOR_EVENTPORT);
		} else {
			color = RTCUtil.defaultRGBMap.get(RTCUtil.COLOR_DATAPORT);
		}

		outPortFig = new OutPortFigure(outPort, direction,
							new Color(PlatformUI.getWorkbench().getDisplay(), color));
		setLayoutManager(new XYLayout());
		add(outPortFig);
	}
	
	public OutPortFigure getInnerFigure() {
		return this.outPortFig;
	}

	public void setInnerFigure(OutPortFigure figure) {
		this.outPortFig = figure;
	}

}
