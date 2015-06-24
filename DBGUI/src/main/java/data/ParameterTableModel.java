package main.java.data;

import java.util.List;

import javax.swing.table.AbstractTableModel;

public class ParameterTableModel extends AbstractTableModel{

	private List<QueryParameterData> queryParamData;
	
	public final String[] columnNames =new String[]{"Parameter","Value","Search","Table"};
	
	public ParameterTableModel(List<QueryParameterData> queryParamData){
		this.queryParamData = queryParamData;
	}
	@Override
	public int getRowCount() {
		return queryParamData.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}
	
	public String getColumnName(int column) {
		return columnNames[column];
	}
    
	@Override
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
		if(columnIndex == 0){
			return false;
		}
		return true;
    }
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		QueryParameterData data = queryParamData.get(rowIndex);
		if(columnIndex==0){
			return data.getQueryParameter();
		}
		if(columnIndex==1){
			return data.getValue();
		}
		if(columnIndex==2){
			return "Search";
		}
		if(columnIndex==3){
			return data.getTableName();
		}
		if(columnIndex == -1){
			return data.getTableName();
		}
		return null;
	}
	
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		QueryParameterData data = queryParamData.get(rowIndex);
		if(columnIndex==1){
			data.setValue((String)aValue);
			fireTableCellUpdated(rowIndex, 1);
		}
		if(columnIndex ==3){
			data.setTableName((String)aValue);
			fireTableCellUpdated(rowIndex, 3);
		}
		
    }

}
