import jp.go.aist.rtm.RTC.jfsm.DataType;
import jp.go.aist.rtm.RTC.jfsm.State;
import jp.go.aist.rtm.RTC.jfsm.StateDef;

import RTC.TimedLong;
import RTC.TimedString;

/**
 * 
 */
@DataType(Top.Data.class)
public class Top extends StateDef implements ModuleNameProtocol {

    public static class Data {
    }
    
     @Override
    public void onInit() {
        setState(new State(State01.class));
    }

    @Override
    public void onEntry() {
    }

    @Override
    public void onExit() {
    }
    
    @Override
    public void Event01_02(TimedLong data) {
    }

    @Override
    public void Event02_Final(TimedString data) {
    }

}