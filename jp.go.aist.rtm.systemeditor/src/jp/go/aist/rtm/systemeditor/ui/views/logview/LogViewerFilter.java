package jp.go.aist.rtm.systemeditor.ui.views.logview;

import java.util.List;
import java.util.regex.PatternSyntaxException;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import jp.go.aist.rtm.systemeditor.ui.views.logview.FilteringParam.FilteringKind;

public class LogViewerFilter extends ViewerFilter {
	private FilteringParam condition = null;
	
	public void setCondition(FilteringParam condition) {
		this.condition = condition;
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if(condition==null) return true;
		
		boolean result = true;
		LogParam entry = (LogParam) element;
		
		//条件チェック
		List<FilteringParam> elems = condition.getChildParams();
		if(elems.size()!=1) return true;
		
		FilteringParam rootCond = elems.get(0);
		if(rootCond.getKind()==FilteringKind.AND) {
			result = checkAndCondition(rootCond, entry);
		} else if(rootCond.getKind()==FilteringKind.OR) {
			result = checkOrCondition(rootCond, entry);
		} 
		return result;
	}
	
	private boolean checkAndCondition(FilteringParam target, LogParam logParam) {
		List<FilteringParam> elems = target.getChildParams();
		for(FilteringParam elem : elems) {
			if(elem.getKind()==FilteringKind.AND) {
				if(checkAndCondition(elem, logParam)==false) return false;
				
			} else if(elem.getKind()==FilteringKind.OR) {
				if(checkOrCondition(elem, logParam)==false) return false;
				
			} else if(elem.getKind()==FilteringKind.LEVEL) {
				if(checkLevelCondition(elem, logParam)==false) return false;
				
			} else if(elem.getKind()==FilteringKind.TIME) {
				if(checkTimeCondition(elem, logParam)==false) return false;
				
			} else if(elem.getKind()==FilteringKind.MANAGER) {
				if(checkStringCondition(logParam.getManager(), elem.getManagerCond(), elem.isRegexpManager())==false) return false;
				
			} else if(elem.getKind()==FilteringKind.IDENTIFIER) {
				if(checkStringCondition(logParam.getName(), elem.getIdentifierCond(), elem.isRegexpIdentifier())==false) return false;
				
			} else if(elem.getKind()==FilteringKind.PID) {
				if(checkStringCondition(logParam.getPid(), elem.getPidCond(), elem.isRegexpPid())==false) return false;

			} else if(elem.getKind()==FilteringKind.HOST) {
				if(checkStringCondition(logParam.getHost(), elem.getHostCond(), elem.isRegexpHost())==false) return false;

			} else if(elem.getKind()==FilteringKind.MESSAGE) {
				if(checkStringCondition(logParam.getMessage(), elem.getMessageCond(), elem.isRegexpMessage())==false) return false;
			}
		}
		return true;
	}
	
	private boolean checkOrCondition(FilteringParam target, LogParam logParam) {
		List<FilteringParam> elems = target.getChildParams();
		if(elems.size()==0) return true;
		for(FilteringParam elem : elems) {
			if(elem.getKind()==FilteringKind.AND) {
				if(checkAndCondition(elem, logParam)) return true;
				
			} else if(elem.getKind()==FilteringKind.OR) {
				if(checkOrCondition(elem, logParam)) return true;
				
			} else if(elem.getKind()==FilteringKind.LEVEL) {
				if(checkLevelCondition(elem, logParam)) return true;
				
			} else if(elem.getKind()==FilteringKind.TIME) {
				if(checkTimeCondition(elem, logParam)) return true;
				
			} else if(elem.getKind()==FilteringKind.MANAGER) {
				if(checkStringCondition(logParam.getManager(), elem.getManagerCond(), elem.isRegexpManager())) return true;
				
			} else if(elem.getKind()==FilteringKind.IDENTIFIER) {
				if(checkStringCondition(logParam.getName(), elem.getIdentifierCond(), elem.isRegexpIdentifier())) return true;
				
			} else if(elem.getKind()==FilteringKind.PID) {
				if(checkStringCondition(logParam.getPid(), elem.getPidCond(), elem.isRegexpPid())) return true;
				
			} else if(elem.getKind()==FilteringKind.HOST) {
				if(checkStringCondition(logParam.getHost(), elem.getHostCond(), elem.isRegexpHost())) return true;
				
			} else if(elem.getKind()==FilteringKind.MESSAGE) {
				if(checkStringCondition(logParam.getMessage(), elem.getMessageCond(), elem.isRegexpMessage())) return true;
			}
		}
		return false;
	}
	
	private boolean checkLevelCondition(FilteringParam target, LogParam logParam) {
		List<String> levelList = target.getLevelList();
		if(levelList.contains(logParam.getLevel())) {
			return true;
		}
		return false;
	}
	
	private boolean checkTimeCondition(FilteringParam target, LogParam logParam) {
		if(target.isFrom()) {
			int diff = logParam.getCal().compareTo(target.getFromCal());
			if(diff<0) return false;
		}
		if(target.isTo()) {
			int diff = target.getToCal().compareTo(logParam.getCal());
			if(diff<0) return false;
		}
		return true;
	}
	
	private boolean checkStringCondition(String target, String filter, boolean isRegexp) {
		if(filter==null || filter.trim().length()==0) return true;
		if(isRegexp) {
			try {
				return target.matches(filter);
			} catch(PatternSyntaxException ex) {
				return false;
			}
		} else {
			return target.contains(filter);
		}
	}
}
