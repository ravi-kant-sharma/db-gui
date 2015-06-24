package main.ui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import main.java.core.DBConnect;
import main.java.data.DbData;

public class SearchQueryDialog extends JDialog{
	private JScrollPane scrollPane;
	private SQLExecutionResultTable SQLExecutionResultTable;
	private JPanel panel;
	private JPanel bottomPanel;
	private JPanel upperPanel;
	private JButton selectButton;
	private JButton cancelButton;
	private Connection con;
	private String query;
	private JTable parameterTable;
	int rowCount;
	
	public SearchQueryDialog(JTable parameterTable,Connection con,String query,int row){
		  this.con= con;
		  this.query = query;
		  this.parameterTable = parameterTable;
		  this.rowCount = row;
		  this.setTitle(query);
		  this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		  this.setAlwaysOnTop(true);
		  this.setVisible(true);
		  initComponents();
	}
	
	private void initComponents(){
		Container dialogContainer = this.getContentPane();
		this.setSize(500, 280);
		this.setMinimumSize(new Dimension(500, 280));
		scrollPane = new JScrollPane();
		panel = new JPanel();
		bottomPanel = new JPanel();
		upperPanel = new JPanel();
		selectButton = new JButton("Select");
		cancelButton = new JButton("Cancel");
		SQLExecutionResultTable = new SQLExecutionResultTable();
		scrollPane.setViewportView(SQLExecutionResultTable);
		dialogContainer.add(panel);
		panel.setLayout(new GridBagLayout());
		
		GridBagConstraints gBC = new GridBagConstraints();
		 gBC.fill = GridBagConstraints.HORIZONTAL;
	     gBC.weightx = 0.5;
	     gBC.gridx = 0;
	     gBC.gridy = 0;
	     panel.add(upperPanel,gBC);
	     gBC.fill = GridBagConstraints.HORIZONTAL;
	     gBC.weightx = 0.5;
	     gBC.gridx = 0;
	     gBC.gridy = 1;
	     panel.add(bottomPanel,gBC);
	     
	    upperPanel.add(scrollPane);
	    upperPanel.setPreferredSize(new Dimension(450,200));
	    scrollPane.setSize(upperPanel.getSize());
	    upperPanel.setLayout(new GridLayout(1,1));
		bottomPanel.add(selectButton);
		bottomPanel.add(cancelButton);
		
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		selectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onSelectButtonClicked();
				dispose();
			}
		});
		setResultData();
	}
	
	private void onSelectButtonClicked(){
		int row = SQLExecutionResultTable.getSelectedRow();
		int column = SQLExecutionResultTable.getSelectedColumn();
		String value = (String)((DefaultTableModel)SQLExecutionResultTable.getModel()).getValueAt(row, column);
		if(!value.matches("\\d*")){
			value = "'"+value+"'";
		}
		parameterTable.getModel().setValueAt(value, rowCount, 1);
	}
//	
//	public static void main(String[] args) {
//		SearchQueryDialog dialog = new SearchQueryDialog();
//	}
	
	private void setResultData(){
			DBConnect.sqlCmdForSearch(con,query,SQLExecutionResultTable);
		
	}
}
