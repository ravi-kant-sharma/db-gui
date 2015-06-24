package main.ui;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import main.java.core.TableColumnAdjuster;

public class SQLExecutionResultTable extends JTable{
	
	public SQLExecutionResultTable(){
		initTable();
	}
	
	private void initTable(){
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        setAutoscrolls(false);
        getTableHeader().setReorderingAllowed(false);
        setColumnSelectionAllowed(true);
        setRowSelectionAllowed(true);
//        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
//        for (int column = 0; column < getColumnCount(); column++)
//        {
//            TableColumn tableColumn = getColumnModel().getColumn(column);
//            int preferredWidth = tableColumn.getMinWidth();
//            int maxWidth = tableColumn.getMaxWidth();
//         
//            for (int row = 0; row < getRowCount(); row++)
//            {
//                TableCellRenderer cellRenderer = getCellRenderer(row, column);
//                Component c = prepareRenderer(cellRenderer, row, column);
//                int width = c.getPreferredSize().width + getIntercellSpacing().width;
//                preferredWidth = Math.max(preferredWidth, width);
//                //  We've exceeded the maximum width, no need to check other rows
//                if (preferredWidth >= maxWidth)
//                {
//                    preferredWidth = maxWidth;
//                    break;
//                }
//            }
//            tableColumn.setPreferredWidth( preferredWidth );
//        }
        setShowGrid(true);
	}

}
