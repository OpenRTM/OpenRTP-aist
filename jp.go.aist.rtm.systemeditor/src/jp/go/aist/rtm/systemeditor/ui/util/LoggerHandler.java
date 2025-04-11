package jp.go.aist.rtm.systemeditor.ui.util;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.ui.PlatformUI;

import com.fasterxml.jackson.databind.ObjectMapper;

import influent.EventEntry;
import influent.forward.ForwardCallback;
import influent.forward.ForwardServer;
import jp.go.aist.rtm.systemeditor.ui.views.logview.LogParam;

public class LoggerHandler {
	private ForwardServer logServer;
	private List<LogParam> logList;
	private TableViewer logTable;
	private boolean autoScroll = true;
	
	ForwardCallback callback = ForwardCallback.ofSyncConsumer(
	  stream -> {
    	  ObjectMapper mapper = new ObjectMapper();
		  for (EventEntry entry : stream.getEntries()) {
			  String rawData = entry.getRecord().toString();
	          try {
	        	  LogParam info = mapper.readValue(rawData, LogParam.class);
			      PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
						public void run() {
							logList.add(info);
							logTable.refresh();
							if(autoScroll) {
								int itemNum = logTable.getTable().getItemCount();
								if(0 < itemNum) {
									logTable.getTable().setTopIndex(itemNum-1);
								}
							}
						}
					});
	          } catch (IOException e) {
	              e.printStackTrace();
	          }		    	  
		    }				  
	  },
	  Executors.newFixedThreadPool(1)
	);
	
	public void startServer(int portNo, TableViewer logTable) {
		this.logTable = logTable;
		this.logList = (List<LogParam>)logTable.getInput();
		logServer = new ForwardServer
				  .Builder(callback)
				  .localAddress(portNo)
				  .build();
		logServer.start();
	}
	
	public void stopServer() {
		CompletableFuture<Void> stopping = logServer.shutdown();
		try {
			stopping.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		logServer = null;
	}
	
	public void setAutoScroll(boolean source) {
		this.autoScroll = source;
	}
}
