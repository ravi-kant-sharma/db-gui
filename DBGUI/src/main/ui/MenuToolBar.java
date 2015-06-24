package main.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.border.EtchedBorder;

import main.java.core.DBConnect;
import main.java.core.DatabaseQueryToolUI;
import main.java.data.DbData;



public class MenuToolBar extends JToolBar{

	private JButton connectButton;
	private JButton runSqlCommandButton;
	private JButton replaceSymbols;
	private JButton reverseSymbols;
	
	public MenuToolBar(final DatabaseQueryToolUI splitPanelImpl){
		setBorder(new EtchedBorder());
		
		this.connectButton = new JButton("Connect");
		this.connectButton.setBorderPainted(true);
		this.runSqlCommandButton = new JButton("Run Query");
		this.replaceSymbols = new JButton("Replace Symbols");
		this.reverseSymbols  = new JButton("Reverse Symbols");

		add(this.connectButton);
		add(this.runSqlCommandButton);
		add(this.replaceSymbols);
		add(this.reverseSymbols);
		
		 connectButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int selectedRow = splitPanelImpl.getDatabaseTree().getSelectionRows()[0];
					if(selectedRow > 0){
						DbData dbToConnect = (DbData) splitPanelImpl.getDbMap().get(selectedRow);
						if(DBConnect.initDB(dbToConnect)){
							splitPanelImpl.addNewTab(dbToConnect);
						}else{
							System.out.println("Connection Failed");
						}
					}
				}
			});
		 
		 runSqlCommandButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					DatabaseTabPanel selectedPanel = (DatabaseTabPanel) splitPanelImpl.getSqlQueryTabbedPane().getSelectedComponent();
					DbData dbData = (DbData) splitPanelImpl.getTabMap().get(selectedPanel);
					Connection con = DBConnect.dbConnectionMap.get(dbData);
					String query = selectedPanel.tabTextArea.getSelectedText() != null ?  selectedPanel.tabTextArea.getSelectedText() :selectedPanel.tabTextArea.getText();
					query = query.replaceAll(";", "");
					if(query.toUpperCase().contains("INSERT") || query.toUpperCase().contains("UPDATE")){
						String[] multipleQuery = query.split("((?i)GO)");
						if(multipleQuery != null && multipleQuery.length > 0){
							DBConnect.sqlCmd(1, con,null,multipleQuery);
						}
					}
					else
						DBConnect.sqlCmd(2, con,query,null);
				}
			});
		 replaceSymbols.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				DatabaseTabPanel selectedPanel = (DatabaseTabPanel) splitPanelImpl.getSqlQueryTabbedPane().getSelectedComponent();
				selectedPanel.replaceSymbols();
			}
		});
		 
		 reverseSymbols.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DatabaseTabPanel selectedPanel = (DatabaseTabPanel) splitPanelImpl.getSqlQueryTabbedPane().getSelectedComponent();
				selectedPanel.reverseSymbols();
			}
		});
	}
}
