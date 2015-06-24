package main.java.core.factory;

import main.java.core.DatabaseQueryToolUI;
import main.ui.DatabaseTabPanel;

public final class DatabaseTabPanelFactory {
	
	public static DatabaseTabPanel getDatabaseTabPanelInstance(DatabaseQueryToolUI splitPanelImpl){
		return new DatabaseTabPanel(splitPanelImpl);
	}

}
