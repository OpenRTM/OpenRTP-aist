package jp.go.aist.rtm.systemeditor.ui.views.logview;

import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import jp.go.aist.rtm.systemeditor.nl.Messages;
import jp.go.aist.rtm.systemeditor.ui.views.logview.FilteringParam.FilteringKind;

public class LogFilterDialog extends Dialog {
	private FilteringParam rootParam = null;
	
	private Button btnAnd;
	private Button btnOr;
	private Button btnLevel;
	private Button btnTime;
	private Button btnManager;
	private Button btnIdentifier;
	private Button btnPID;
	private Button btnHost;
	private Button btnMessage;
	private Button btnEdit;
	private Button btnDelete;
	
	public FilteringParam getRootParam() {
		return rootParam;
	}
	public void setRootParam(FilteringParam rootParam) {
		this.rootParam = rootParam;
	}

	public LogFilterDialog(Shell parentShell) {
		super(parentShell);
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		GridLayout gridLayout = new GridLayout(2, false);
		Composite mainComposite = (Composite) super.createDialogArea(parent);
		mainComposite.setLayout(gridLayout);
		mainComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label lblHint = new Label(mainComposite, SWT.NONE);
		lblHint.setText(Messages.getString("LogView.hint"));
	    GridData gd = new GridData();
	    gd.horizontalSpan = 2;
	    lblHint.setLayoutData(gd);
		
		Tree conditionTree = new Tree(mainComposite, SWT.BORDER);
		conditionTree.addSelectionListener(new SelectionListener() {
		    @Override
		    public void widgetDefaultSelected(SelectionEvent e) {
		    }
		    @Override
		    public void widgetSelected(SelectionEvent event) {
		        TreeItem targetItem = (TreeItem) event.item;
	            FilteringParam targetParam = (FilteringParam)targetItem.getData();
	            
            	btnDelete.setEnabled(false);
            	btnEdit.setEnabled(false);
        		btnAnd.setEnabled(false);
        		btnOr.setEnabled(false);
            	btnLevel.setEnabled(false);
	            btnTime.setEnabled(false);
	            btnPID.setEnabled(false);
	            btnHost.setEnabled(false);
	            btnManager.setEnabled(false);
	            btnIdentifier.setEnabled(false);
	            btnMessage.setEnabled(false);
	            
	            if(targetParam.getKind()!=FilteringKind.ROOT) {
	            	btnDelete.setEnabled(true);
	            }
	            if(targetParam.getKind()!=FilteringKind.ROOT && targetParam.getKind()!=FilteringKind.AND && targetParam.getKind()!=FilteringKind.OR) {
	            	btnEdit.setEnabled(true);
	            }
	            if(targetParam.getKind()==FilteringKind.ROOT) {
	            	if(targetParam.getChildParams().size()==0) {
	            		btnAnd.setEnabled(true);
	            		btnOr.setEnabled(true);
	            	}
	            }
	            if(targetParam.getKind()==FilteringKind.AND || targetParam.getKind()==FilteringKind.OR) {
            		btnAnd.setEnabled(true);
            		btnOr.setEnabled(true);
	        		
		            boolean existLevel = false;
		            for(FilteringParam child : targetParam.getChildParams()) {
		            	if(child.getKind()==FilteringKind.LEVEL) {
		            		existLevel = true;
		            		break;
		            	}
		            }
		            if(existLevel==false) {
		            	btnLevel.setEnabled(true);
		            }
		            btnTime.setEnabled(true);
		            btnManager.setEnabled(true);
		            btnIdentifier.setEnabled(true);
		            btnPID.setEnabled(true);
		            btnHost.setEnabled(true);
		            btnMessage.setEnabled(true);
	            }
		    }
		});
		
		gd = new GridData();
		gd.verticalAlignment = SWT.FILL;
		gd.horizontalAlignment = SWT.FILL;
		gd.grabExcessVerticalSpace = true;
		gd.grabExcessHorizontalSpace = true;
		conditionTree.setLayoutData(gd);
		
		TreeItem root = new TreeItem(conditionTree, SWT.NULL);
		if(rootParam==null) {
			rootParam = new FilteringParam(FilteringKind.ROOT, null);
		    root.setData(rootParam);
		    root.setText(rootParam.toString());
		} else {
		    root.setData(rootParam);
		    root.setText(rootParam.toString());
		    buildTree(rootParam, root);
		}
	    root.setExpanded(true);
		buildMenu(conditionTree);		
		/////
		Composite btnComposite = new Composite(mainComposite, SWT.FILL);
		gd = new GridData();
		gd.horizontalAlignment = SWT.FILL;
		gd.horizontalSpan = 1;
		btnComposite.setLayoutData(gd);
		
		gridLayout = new GridLayout();
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		gridLayout.numColumns = 1;
		btnComposite.setLayout(gridLayout);
		
		btnDelete = createButton(btnComposite, Messages.getString("LogView.menuDelete"));
		btnDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
	            TreeItem targetItem = conditionTree.getSelection()[0];
	            FilteringParam targetParam = (FilteringParam)targetItem.getData();
	            deleteItem(targetItem, targetParam);
			}
		});
		
		btnEdit = createButton(btnComposite, Messages.getString("LogView.menuEdit"));
		btnEdit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
	            TreeItem targetItem = conditionTree.getSelection()[0];
	            FilteringParam targetParam = (FilteringParam)targetItem.getData();
	            editItem(targetItem, targetParam);
			}
		});
		
		Label lblDummuy02 = new Label(btnComposite, SWT.NONE);
		gd = new GridData();
		gd.grabExcessVerticalSpace = true;
		lblDummuy02.setLayoutData(gd);
		
		btnAnd = createButton(btnComposite, Messages.getString("LogView.menuAnd"));
		btnAnd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
	            TreeItem targetItem = conditionTree.getSelection()[0];
	            FilteringParam targetParam = (FilteringParam)targetItem.getData();
				addAnd(targetItem, targetParam);
			}
		});
		
		btnOr = createButton(btnComposite, Messages.getString("LogView.menuOr"));
		btnOr.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
	            TreeItem targetItem = conditionTree.getSelection()[0];
	            FilteringParam targetParam = (FilteringParam)targetItem.getData();
	            addOr(targetItem, targetParam);
			}
		});
		
		Label lblDummuy01 = new Label(btnComposite, SWT.NONE);
		gd = new GridData();
		gd.grabExcessVerticalSpace = true;
		lblDummuy01.setLayoutData(gd);

		btnLevel = createButton(btnComposite, Messages.getString("LogView.menuLevel"));
		btnLevel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
	            TreeItem targetItem = conditionTree.getSelection()[0];
	            FilteringParam targetParam = (FilteringParam)targetItem.getData();
	            addLevel(targetItem, targetParam);
			}
		});
		
		btnTime = createButton(btnComposite, Messages.getString("LogView.menuTime"));
		btnTime.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
	            TreeItem targetItem = conditionTree.getSelection()[0];
	            FilteringParam targetParam = (FilteringParam)targetItem.getData();
	            addTime(targetItem, targetParam);
			}
		});
		
		btnManager = createButton(btnComposite, Messages.getString("LogView.menuManager"));
		btnManager.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
	            TreeItem targetItem = conditionTree.getSelection()[0];
	            FilteringParam targetParam = (FilteringParam)targetItem.getData();
	            addString(targetItem, targetParam, FilteringKind.MANAGER);
			}
		});
		
		btnIdentifier = createButton(btnComposite, Messages.getString("LogView.menuIdentifier"));
		btnIdentifier.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
	            TreeItem targetItem = conditionTree.getSelection()[0];
	            FilteringParam targetParam = (FilteringParam)targetItem.getData();
	            addString(targetItem, targetParam, FilteringKind.IDENTIFIER);
			}
		});
		
		btnPID = createButton(btnComposite, Messages.getString("LogView.menuPID"));
		btnPID.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
	            TreeItem targetItem = conditionTree.getSelection()[0];
	            FilteringParam targetParam = (FilteringParam)targetItem.getData();
	            addString(targetItem, targetParam, FilteringKind.PID);
			}
		});
		
		btnHost = createButton(btnComposite, Messages.getString("LogView.menuHost"));
		btnHost.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
	            TreeItem targetItem = conditionTree.getSelection()[0];
	            FilteringParam targetParam = (FilteringParam)targetItem.getData();
	            addString(targetItem, targetParam, FilteringKind.HOST);
			}
		});
		
		btnMessage = createButton(btnComposite, Messages.getString("LogView.menuMessage"));
		btnMessage.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
	            TreeItem targetItem = conditionTree.getSelection()[0];
	            FilteringParam targetParam = (FilteringParam)targetItem.getData();
	            addString(targetItem, targetParam, FilteringKind.MESSAGE);
			}
		});
		
		return mainComposite;
	}
	
	private void buildTree(FilteringParam targetParam, TreeItem targetItem) {
		List<FilteringParam> elems = targetParam.getChildParams();
		for(FilteringParam eachElem : elems) {
			TreeItem treeElem = new TreeItem(targetItem, SWT.NULL);
		    treeElem.setData(eachElem);
		    treeElem.setText(eachElem.toString());
		    targetItem.setExpanded(true);
		    
			if(0<eachElem.getChildParams().size()) {
				buildTree(eachElem, treeElem);
			}
		}
	}

	private void buildMenu(Tree conditionTree) {
		final Menu menu = new Menu(conditionTree);
		conditionTree.setMenu(menu);
	    menu.addMenuListener(new MenuAdapter() {
	        public void menuShown(MenuEvent e) {
	            for (MenuItem item :  menu.getItems()) {
	                item.dispose();
	            }
	            //
	            TreeItem targetItem = conditionTree.getSelection()[0];
	            FilteringParam targetParam = (FilteringParam)targetItem.getData();
	            if(targetParam.getKind()!=FilteringKind.ROOT) {
	            	buildDeleteMenu(menu, targetItem, targetParam);
	            }
	            //
	            if(targetParam.getKind()!=FilteringKind.ROOT && targetParam.getKind()!=FilteringKind.AND && targetParam.getKind()!=FilteringKind.OR) {
		            buildEditMenu(menu, targetItem, targetParam);
	            }
	            if(targetParam.getKind()==FilteringKind.ROOT) {
	            	if(targetParam.getChildParams().size()==0) {
			            buildAndMenu(menu, targetItem, targetParam);
			            buildOrMenu(menu, targetItem, targetParam);
	            	}
	            }
	            if(targetParam.getKind()==FilteringKind.AND || targetParam.getKind()==FilteringKind.OR) {
	        		new MenuItem(menu, SWT.SEPARATOR);
		            buildAndMenu(menu, targetItem, targetParam);
		            buildOrMenu(menu, targetItem, targetParam);
	        		new MenuItem(menu, SWT.SEPARATOR);
	        		
		            boolean existLevel = false;
		            for(FilteringParam child : targetParam.getChildParams()) {
		            	if(child.getKind()==FilteringKind.LEVEL) {
		            		existLevel = true;
		            		break;
		            	}
		            }
		            if(existLevel==false) {
		            	buildLevelMenu(menu, targetItem, targetParam);
		            }
	            	buildTimeMenu(menu, targetItem, targetParam);
	            	buildStringMenu(menu, targetItem, targetParam, FilteringKind.MANAGER);
	            	buildStringMenu(menu, targetItem, targetParam, FilteringKind.IDENTIFIER);
	            	buildStringMenu(menu, targetItem, targetParam, FilteringKind.PID);
	            	buildStringMenu(menu, targetItem, targetParam, FilteringKind.HOST);
	            	buildStringMenu(menu, targetItem, targetParam, FilteringKind.MESSAGE);
	            }
	        }
	    });
	}

	private void buildAndMenu(final Menu menu, TreeItem targetItem, FilteringParam targetParam) {
		MenuItem andItem = new MenuItem(menu, SWT.PUSH);
        andItem.setText(Messages.getString("LogView.menuAnd"));
        andItem.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addAnd(targetItem, targetParam);
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
	}

	private void addAnd(TreeItem targetItem, FilteringParam targetParam) {
		TreeItem andElem = new TreeItem(targetItem, SWT.NULL);
	    FilteringParam andParam = new FilteringParam(FilteringKind.AND, targetParam);
	    andElem.setData(andParam);
	    andElem.setText(andParam.toString());
	    targetParam.getChildParams().add(andParam);
	    targetItem.setExpanded(true);
	}
	
	private void buildOrMenu(final Menu menu, TreeItem targetItem, FilteringParam targetParam) {
		MenuItem item = new MenuItem(menu, SWT.PUSH);
		item.setText(Messages.getString("LogView.menuOr"));
		item.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addOr(targetItem, targetParam);
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
	}
	private void addOr(TreeItem targetItem, FilteringParam targetParam) {
		TreeItem elem = new TreeItem(targetItem, SWT.NULL);
	    FilteringParam param = new FilteringParam(FilteringKind.OR, targetParam);
	    elem.setData(param);
	    elem.setText(param.toString());
	    targetParam.getChildParams().add(param);
	    targetItem.setExpanded(true);
	}
	
	private void buildDeleteMenu(final Menu menu, TreeItem targetItem, FilteringParam targetParam) {
		MenuItem deleteItem = new MenuItem(menu, SWT.PUSH);
		deleteItem.setText(Messages.getString("LogView.menuDelete"));
		deleteItem.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				deleteItem(targetItem, targetParam);
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
	}
	private void deleteItem(TreeItem targetItem, FilteringParam targetParam) {
		targetItem.dispose();
		FilteringParam parentParam = targetParam.getParentParam();
		parentParam.getChildParams().remove(targetParam);
	}
	
	private void buildEditMenu(final Menu menu, TreeItem targetItem, FilteringParam targetParam) {
		MenuItem menuItem = new MenuItem(menu, SWT.PUSH);
		menuItem.setText(Messages.getString("LogView.menuEdit"));
		menuItem.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				editItem(targetItem, targetParam);
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
	}
	
	private void editItem(TreeItem targetItem, FilteringParam targetParam) {
		if(targetParam.kind==FilteringKind.LEVEL) {
			LogLevelDialog dialog = new LogLevelDialog(getShell());
			dialog.setTargetParam(targetParam);
			int open = dialog.open();
			if (open != IDialogConstants.OK_ID) {
				return;
			}
			targetItem.setText(targetParam.toString());
		    targetItem.setExpanded(true);
		    
		} else if(targetParam.kind==FilteringKind.TIME) {
			TimeRangeDialog dialog = new TimeRangeDialog(getShell());
			dialog.setTargetParam(targetParam);
			int open = dialog.open();
			if (open != IDialogConstants.OK_ID) {
				return;
			}
			targetItem.setText(targetParam.toString());
		    targetItem.setExpanded(true);
		    
		} else {
		    StringDialog dialog = new StringDialog(getShell());
			dialog.setTargetParam(targetParam);
			dialog.setKind(targetParam.kind);
			int open = dialog.open();
			if (open != IDialogConstants.OK_ID) {
				return;
			}
			targetItem.setText(targetParam.toString());
		    targetItem.setExpanded(true);
			
		}
	}
	
	private void buildLevelMenu(final Menu menu, TreeItem targetItem, FilteringParam targetParam) {
		MenuItem item = new MenuItem(menu, SWT.PUSH);
		item.setText(Messages.getString("LogView.menuLevel"));
		item.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			    addLevel(targetItem, targetParam);
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
	}
	private void addLevel(TreeItem targetItem, FilteringParam targetParam) {
		FilteringParam param = new FilteringParam(FilteringKind.LEVEL, targetParam);
		LogLevelDialog dialog = new LogLevelDialog(getShell());
		dialog.setTargetParam(param);
		int open = dialog.open();
		if (open != IDialogConstants.OK_ID) {
			return;
		}
		TreeItem elem = new TreeItem(targetItem, SWT.NULL);
	    elem.setData(param);
	    elem.setText(param.toString());
	    targetParam.getChildParams().add(param);
	    targetItem.setExpanded(true);
	}
	
	private void buildTimeMenu(final Menu menu, TreeItem targetItem, FilteringParam targetParam) {
		MenuItem item = new MenuItem(menu, SWT.PUSH);
		item.setText(Messages.getString("LogView.menuTime"));
		item.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			    addTime(targetItem, targetParam);
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
	}
	private void addTime(TreeItem targetItem, FilteringParam targetParam) {
		FilteringParam param = new FilteringParam(FilteringKind.TIME, targetParam);
		TimeRangeDialog dialog = new TimeRangeDialog(getShell());
		dialog.setTargetParam(param);
		int open = dialog.open();
		if (open != IDialogConstants.OK_ID) {
			return;
		}
		TreeItem elem = new TreeItem(targetItem, SWT.NULL);
	    elem.setData(param);
	    elem.setText(param.toString());
	    targetParam.getChildParams().add(param);
	    targetItem.setExpanded(true);
	}
	
	private void buildStringMenu(final Menu menu, TreeItem targetItem, FilteringParam targetParam, FilteringKind type) {
		MenuItem item = new MenuItem(menu, SWT.PUSH);
		switch(type) {
		case MANAGER:
			item.setText(Messages.getString("LogView.menuManager"));
			break;
		case IDENTIFIER:
			item.setText(Messages.getString("LogView.menuIdentifier"));
			break;
		case PID:
			item.setText(Messages.getString("LogView.columnPid"));
			break;
		case HOST:
			item.setText(Messages.getString("LogView.columnHost"));
			break;
		case MESSAGE:
			item.setText(Messages.getString("LogView.menuMessage"));
			break;
		default:
			return;
		}
		item.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			    addString(targetItem, targetParam, type);
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
	}
	private void addString(TreeItem targetItem, FilteringParam targetParam, FilteringKind type) {
		FilteringParam param = new FilteringParam(type, targetParam);
	    StringDialog dialog = new StringDialog(getShell());
		dialog.setTargetParam(param);
		dialog.setKind(type);
		int open = dialog.open();
		if (open != IDialogConstants.OK_ID) {
			return;
		}
		TreeItem elem = new TreeItem(targetItem, SWT.NULL);
	    elem.setData(param);
	    elem.setText(param.toString());
	    targetParam.getChildParams().add(param);
	    targetItem.setExpanded(true);
	}
	
	private Button createButton(Composite parent, String text) {
		Button result = new Button(parent, SWT.NONE);
		GridData gd = new GridData();
		gd.widthHint = 150;
		result.setLayoutData(gd);
		result.setText(text);
		return result;
	}
	
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("Log Filtering Condition"); //$NON-NLS-1$
	}
	
	protected Point getInitialSize() {
		return new Point(600, 500);
	}
	
	@Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
    }
	
	@Override
	protected void okPressed() {
		super.okPressed();
	}
}
