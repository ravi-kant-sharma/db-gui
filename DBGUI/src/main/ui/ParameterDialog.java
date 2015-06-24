package main.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

import main.java.core.DBConnect;
import main.java.core.DatabaseQueryToolUI;
import main.java.data.DbData;
import main.java.data.ParameterTableModel;
import main.java.data.QueryParameterData;

public class ParameterDialog extends JDialog{

	private JPanel parameterPanel;
	private JPanel bottomPanel;
	private JPanel upperPanel;
	private JScrollPane parameterScrollPane;
	private JTable parameterTable;
	private JButton replaceButton;
	private JButton cancelButton;
	private DatabaseTabPanel dbPanel;
	private Map<String,QueryParameterData> paramDataMap = new HashMap<String,QueryParameterData>();
	List<QueryParameterData> queryParamData;
	DatabaseQueryToolUI splitPanelImpl;
	public ParameterDialog(DatabaseTabPanel dbPanel,DatabaseQueryToolUI splitPanelImpl,List<QueryParameterData> queryParameterData){
		this.queryParamData = queryParameterData;
		this.dbPanel = dbPanel;
		this.splitPanelImpl = splitPanelImpl;
		initComponents();
		this.setLocation(splitPanelImpl.mainFrame.getX()+100,splitPanelImpl.mainFrame.getY()+50);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setAlwaysOnTop(true);
		this.setVisible(true);
	}
	
	private void initComponents(){
		Container infoDialogContainer = this.getContentPane();
		this.setSize(600, 280);
		this.setResizable(false);
		
		parameterScrollPane = new JScrollPane();
		parameterPanel = new JPanel();
		bottomPanel = new JPanel();
		upperPanel = new JPanel();
		replaceButton = new JButton("Replace");
		cancelButton = new JButton("Cancel");
		
		
//		{
//			public boolean isCellEditable(int row,int column){
//				if(column == 0 || column == 2){
//					return false;
//				}
//				return true;
//			}
//			
//			public void setValueAt(Object value,int row,int column){
//				((DefaultTableModel)getModel()).fireTableCellUpdated(row, column);
//			}
//		};
		
		infoDialogContainer.add(parameterPanel);

		{
			replaceButton.setSize(50, 20);
			replaceButton.setLocation(450, 200);
			cancelButton.setSize(50, 20);
			cancelButton.setLocation(450, 250);
		}
		
		parameterPanel.setLayout(new GridBagLayout());
		
		GridBagConstraints gBC = new GridBagConstraints();
		 gBC.fill = GridBagConstraints.HORIZONTAL;
	     gBC.weightx = 0.5;
	     gBC.gridx = 0;
	     gBC.gridy = 0;
		parameterPanel.add(upperPanel,gBC);
	     gBC.fill = GridBagConstraints.HORIZONTAL;
	     gBC.weightx = 0.5;
	     gBC.gridx = 0;
	     gBC.gridy = 1;
		parameterPanel.add(bottomPanel,gBC);
		
		parameterScrollPane.setPreferredSize(new Dimension(550,200));
		Vector data = new Vector();
		Vector column = new Vector();
		column.add("Parameter");
		column.add("Value");
		column.add("Search");
		
		for(QueryParameterData queryParam: queryParamData){
			Vector row = new Vector(2);
			row.add(queryParam.getQueryParameter());
			row.add("");
			row.add("Search");
			data.add(row);
			paramDataMap.put(queryParam.getQueryParameter(), queryParam);
		}
//		for(int i=0;i<20;i++){
//		data.add(addNewRow());
//		}
		ParameterTableModel parameterModel = new ParameterTableModel(queryParamData);
		parameterTable = new JTable(parameterModel);
//		DefaultTableModel tableModel = (DefaultTableModel) parameterTable.getModel();
//		tableModel.setDataVector(data,column);
		
		
//		parameterTable.setModel(tableModel);
		parameterScrollPane.setViewportView(parameterTable);
		upperPanel.add(parameterScrollPane);
		bottomPanel.add(replaceButton);
		bottomPanel.add(cancelButton);
//		tableModel.fireTableStructureChanged();
		
		parameterTable.setRowHeight(25);
		parameterTable.getColumn("Search").setCellRenderer(new ButtonRenderer());
		parameterTable.getColumn("Search").setPreferredWidth(3);
		parameterTable.getColumn("Search").setCellEditor(new ButtonEditor(new JCheckBox()));
//		parameterTable.getColumn("Value").setCellRenderer(new TextFieldRenderer());
		parameterTable.getColumn("Parameter").setCellRenderer(new LabelRenderer());
		parameterTable.getColumn("Parameter").setPreferredWidth(70);
		parameterTable.getColumn("Parameter").setCellEditor(null);
		
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		replaceButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setQueryParameterValues();
				dbPanel.replaceParameters(queryParamData);
			}
		});
	}
	
	private void setQueryParameterValues(){
		int cloumnCount = parameterTable.getModel().getColumnCount();
		ParameterTableModel par = (ParameterTableModel)parameterTable.getModel();
		for(int i=0;i<cloumnCount;i++){
			String param = (String)parameterTable.getModel().getValueAt(0, i);
			if(paramDataMap.get(param) != null){
				((QueryParameterData)paramDataMap.get(param)).setValue((String)parameterTable.getModel().getValueAt(i, 1));
			}
		}
		dispose();
	}
	
	public static void main(String[] args) {
		new ParameterDialog(null,null, null);
	}
	
	class ButtonRenderer extends JButton implements TableCellRenderer {

	    public ButtonRenderer() {
	        setOpaque(true);
	    }

	    @Override
	    public Component getTableCellRendererComponent(JTable table, Object value,
	            boolean isSelected, boolean hasFocus, int row, int column) {
	        if (isSelected) {
	            setForeground(table.getSelectionForeground());
	            setBackground(table.getSelectionBackground());
	        } else {
	            setForeground(table.getForeground());
	            setBackground(UIManager.getColor("Button.background"));
	        }
	        setText((value == null) ? "" : value.toString());
	        return this;
	    }
	    
	}
	
	class TextFieldRenderer extends JTextField implements TableCellRenderer {

	    public TextFieldRenderer() {
	        setOpaque(true);
	    }

	    @Override
	    public Component getTableCellRendererComponent(JTable table, Object value,
	            boolean isSelected, boolean hasFocus, int row, int column) {
	    	setText((value == null) ? "" : value.toString());
	        return this;
	    }
	}
	
	class LabelRenderer extends JLabel implements TableCellRenderer {

	    public LabelRenderer() {
	        setOpaque(true);
	    }

	    @Override
	    public Component getTableCellRendererComponent(JTable table, Object value,
	            boolean isSelected, boolean hasFocus, int row, int column) {
	    	setText((value == null) ? "" : value.toString());
	        return this;
	    }
	}
	
	class ButtonEditor extends DefaultCellEditor {

	    protected JButton button;
	    private String label;
	    private boolean isPushed;
	    private int row;
	    private int column;

	    public ButtonEditor(JCheckBox checkBox) {
	        super(checkBox);
	        button = new JButton();
	        button.setOpaque(true);
	        button.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                fireEditingStopped();
	                if(isPushed){
	    	        	openSearchDialog(row,column);
	    	        }
	            }
	        });
	    }

	    @Override
	    public Component getTableCellEditorComponent(JTable table, Object value,
	            boolean isSelected, int row, int column) {
	        if (isSelected) {
	            button.setForeground(table.getSelectionForeground());
	            button.setBackground(table.getSelectionBackground());
	        } else {
	            button.setForeground(table.getForeground());
	            button.setBackground(table.getBackground());
	        }
	        label = (value == null) ? "" : value.toString();
	        button.setText(label);
	        isPushed = true;
	        this.row = row;
	        this.column = column;
	        return button;
	    }

	    @Override
	    public Object getCellEditorValue() {
	        
	        return label;
	    }

	    @Override
	    public boolean stopCellEditing() {
	        isPushed = false;
	        return super.stopCellEditing();
	    }

	    @Override
	    protected void fireEditingStopped() {
	        super.fireEditingStopped();
	    }
	}
	
	private void openSearchDialog(int row,int column){
		ParameterTableModel par = (ParameterTableModel)parameterTable.getModel();
		String tableName= (String) ((ParameterTableModel)parameterTable.getModel()).getValueAt(row, 3);
		if(tableName != null && tableName.length() > 0){
			String query = "select * from " + tableName;
			DatabaseTabPanel selectedPanel = (DatabaseTabPanel) splitPanelImpl.getSqlQueryTabbedPane().getSelectedComponent();
			DbData dbData = (DbData) splitPanelImpl.getTabMap().get(selectedPanel);
			Connection con = DBConnect.dbConnectionMap.get(dbData);
			SearchQueryDialog searchDialog = new SearchQueryDialog(parameterTable,con,query,row);
			
		}
	}
	
	class LabelEditor extends DefaultCellEditor {
		protected JButton button;
	    protected JLabel jLabel;
	    private String label;
	    private boolean isPushed;

	    public LabelEditor(JCheckBox checkBox) {
	        super(checkBox);
	        button = new JButton();
	        jLabel = new JLabel();
	        jLabel.setOpaque(true);
	        button.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                fireEditingStopped();
	            }
	        });
	    }

	    @Override
	    public Component getTableCellEditorComponent(JTable table, Object value,
	            boolean isSelected, int row, int column) {
	        if (isSelected) {
	            button.setForeground(table.getSelectionForeground());
	            button.setBackground(table.getSelectionBackground());
	        } else {
	            button.setForeground(table.getForeground());
	            button.setBackground(table.getBackground());
	        }
	        label = (value == null) ? "" : value.toString();
	        button.setText(label);
	        isPushed = true;
	        return button;
	    }

	    @Override
	    public Object getCellEditorValue() {
	        return label;
	    }

	    @Override
	    public boolean stopCellEditing() {
	        isPushed = false;
	        return super.stopCellEditing();
	    }

	    @Override
	    protected void fireEditingStopped() {
	        super.fireEditingStopped();
	    }
	}
}
