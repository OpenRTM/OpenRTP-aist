package jp.go.aist.rtm.systemeditor.ui.views.logview;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jp.go.aist.rtm.systemeditor.nl.Messages;

public class FilteringParam {
	public enum FilteringKind {
		ROOT,
		AND,
		OR,
		LEVEL,
		TIME,
		MANAGER,
		IDENTIFIER,
		PID,
		HOST,
		MESSAGE
	}
	protected FilteringKind kind;
	private FilteringParam parentParam;
	private List<FilteringParam> childParams = new ArrayList<FilteringParam>();
	
	private List<String> levelList = new ArrayList<String>();
	
	private boolean isFrom;
	private Calendar fromCal;
	private boolean isTo;
	private Calendar toCal;
	
	private String managerCond;
	private boolean regexpManager;	
	
	private String identifierCond;
	private boolean regexpIdentifier;
	
	private String pidCond;
	private boolean regexpPid;

	private String hostCond;
	private boolean regexpHost;

	private String messageCond;
	private boolean regexpMessage;	
	
	public FilteringParam(FilteringKind kind, FilteringParam parent) {
		this.kind = kind;
		this.parentParam = parent;
	}
	
	public FilteringKind getKind() {
		return kind;
	}

	public FilteringParam getParentParam() {
		return parentParam;
	}
	public void setParentParam(FilteringParam parentParam) {
		this.parentParam = parentParam;
	}

	public void addChild(FilteringParam child) {
		this.childParams.add(child);
	}

	public List<FilteringParam> getChildParams() {
		return childParams;
	}
	/////
	public List<String> getLevelList() {
		return levelList;
	}
	/////
	public boolean isFrom() {
		return isFrom;
	}
	public void setFrom(boolean isFrom) {
		this.isFrom = isFrom;
	}

	public Calendar getFromCal() {
		return fromCal;
	}
	public void setFromCal(Calendar fromCal) {
		this.fromCal = fromCal;
	}

	public boolean isTo() {
		return isTo;
	}
	public void setTo(boolean isTo) {
		this.isTo = isTo;
	}

	public Calendar getToCal() {
		return toCal;
	}
	public void setToCal(Calendar toCal) {
		this.toCal = toCal;
	}
	//////////
	public String getManagerCond() {
		return managerCond;
	}
	public void setManagerCond(String managerCond) {
		this.managerCond = managerCond;
	}
	public boolean isRegexpManager() {
		return regexpManager;
	}
	public void setRegexpManager(boolean regexpManager) {
		this.regexpManager = regexpManager;
	}

	public String getIdentifierCond() {
		return identifierCond;
	}
	public void setIdentifierCond(String identifierCond) {
		this.identifierCond = identifierCond;
	}
	public boolean isRegexpIdentifier() {
		return regexpIdentifier;
	}
	public void setRegexpIdentifier(boolean regexpIdentifier) {
		this.regexpIdentifier = regexpIdentifier;
	}

	public String getPidCond() {
		return pidCond;
	}
	public void setPidCond(String pidCond) {
		this.pidCond = pidCond;
	}
	public boolean isRegexpPid() {
		return regexpPid;
	}
	public void setRegexpPid(boolean regexpPid) {
		this.regexpPid = regexpPid;
	}

	public String getHostCond() {
		return hostCond;
	}
	public void setHostCond(String hostCond) {
		this.hostCond = hostCond;
	}
	public boolean isRegexpHost() {
		return regexpHost;
	}
	public void setRegexpHost(boolean regexpHost) {
		this.regexpHost = regexpHost;
	}

	public String getMessageCond() {
		return messageCond;
	}
	public void setMessageCond(String messageCond) {
		this.messageCond = messageCond;
	}
	public boolean isRegexpMessage() {
		return regexpMessage;
	}
	public void setRegexpMessage(boolean regexpMessage) {
		this.regexpMessage = regexpMessage;
	}
	

	@Override
	public String toString() {
		switch(this.kind) {
		case ROOT:
			return "Filtering Condition";
		case AND:
			return "And";
		case OR:
			return "Or";
		case LEVEL:
		{
			StringBuilder builder = new StringBuilder();
			builder.append(Messages.getString("LogView.columnLevel"));
			builder.append("=");
			for(int index=0; index<levelList.size(); index++) {
				if(0<index) builder.append(", ");
				builder.append(levelList.get(index));
			}
			return builder.toString();
		}
		case TIME: {
			StringBuilder builder = new StringBuilder();
			builder.append(Messages.getString("LogView.columnTime"));
			builder.append("=");
			if(isFrom) {
				String fromDate = String.format("%04d",fromCal.get(Calendar.YEAR)) + "/"
									+ String.format("%02d", fromCal.get(Calendar.MONTH)) + "/"
									+ String.format("%02d",fromCal.get(Calendar.DATE));
				String fromTime = String.format("%02d",fromCal.get(Calendar.HOUR)) + ":"
						+ String.format("%02d",fromCal.get(Calendar.MINUTE)) + ":"
						+ String.format("%02d",fromCal.get(Calendar.SECOND));
				builder.append("From:").append(fromDate).append(" ").append(fromTime);
			}
			if(isTo) {
				builder.append(" - ");
				String toDate = String.format("%04d",toCal.get(Calendar.YEAR)) + "/"
								+ String.format("%02d", toCal.get(Calendar.MONTH)) + "/"
								+ String.format("%02d",toCal.get(Calendar.DATE));
				String toTime = String.format("%02d",toCal.get(Calendar.HOUR)) + ":"
								+ String.format("%02d",toCal.get(Calendar.MINUTE)) + ":"
								+ String.format("%02d",toCal.get(Calendar.SECOND));
				builder.append("To:").append(toDate).append(" ").append(toTime);
			}
			return builder.toString();
		}
		case MANAGER: {
			StringBuilder builder = new StringBuilder();
			builder.append(Messages.getString("LogView.columnManager"));
			builder.append("=");
			builder.append(managerCond);
			if(regexpManager) {
				builder.append(" (");
				builder.append(Messages.getString("LogView.regexp"));
				builder.append(")");
			}
			return builder.toString();
		}
		case IDENTIFIER: {
			StringBuilder builder = new StringBuilder();
			builder.append(Messages.getString("LogView.columnID"));
			builder.append("=");
			builder.append(identifierCond);
			if(regexpIdentifier) {
				builder.append(" (");
				builder.append(Messages.getString("LogView.regexp"));
				builder.append(")");
			}
			return builder.toString();
		}
		case PID: {
			StringBuilder builder = new StringBuilder();
			builder.append(Messages.getString("LogView.columnPid"));
			builder.append("=");
			builder.append(pidCond);
			if(regexpPid) {
				builder.append(" (");
				builder.append(Messages.getString("LogView.regexp"));
				builder.append(")");
			}
			return builder.toString();
		}
		case HOST: {
			StringBuilder builder = new StringBuilder();
			builder.append(Messages.getString("LogView.columnHost"));
			builder.append("=");
			builder.append(hostCond);
			if(regexpHost) {
				builder.append(" (");
				builder.append(Messages.getString("LogView.regexp"));
				builder.append(")");
			}
			return builder.toString();
		}
		case MESSAGE: {
			StringBuilder builder = new StringBuilder();
			builder.append(Messages.getString("LogView.columnMessage"));
			builder.append("=");
			builder.append(messageCond);
			if(regexpMessage) {
				builder.append(" (");
				builder.append(Messages.getString("LogView.regexp"));
				builder.append(")");
			}
			return builder.toString();
		}
		default:
			return super.toString();
		}
	}
}
