import jp.go.aist.rtm.RTC.Manager;
import OpenRTM.ExtTrigExecutionContextService;
import jp.go.aist.rtm.RTC.port.CorbaConsumer;
import RTC.ExecutionContext;

Manager manager;
ExtTrigExecutionContextService ec0Ref;

public void setup() {
  String[] args = {"processing", "-o", "exec_cxt.periodic.type:jp.go.aist.rtm.RTC.executionContext.OpenHRPExecutionContext"};
  //String[] args = {"processing"};
  manager = Manager.init(args);
  ProcessingExampleComp init = new ProcessingExampleComp();
  manager.setModuleInitProc(init);
  manager.activateManager();
  
  //manager.runManager();
  
  
  manager.runManager(true);
  
  CorbaConsumer<ExtTrigExecutionContextService> ec0c = new CorbaConsumer<ExtTrigExecutionContextService>(ExtTrigExecutionContextService.class);
  ProcessingExampleImpl comp = (ProcessingExampleImpl)manager.getComponent("ProcessingExample0");
  ExecutionContext ec0 =  comp.getExecutionContext(0);
  ec0c.setObject(ec0);
  ec0Ref = ec0c._ptr();
  comp.setPApplet(this);
}


public void draw() {
  ec0Ref.tick();
}
