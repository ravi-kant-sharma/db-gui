package main.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class Main {
    public static void main(String[] args) {
        new Main().start();
    }

    private void start() {
        JFrame f = new JFrame();
        f.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        initTable();

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }

        JPanel calendar = new JPanel();
        calendar.setLayout(new GridBagLayout());
        calendar.setBorder(BorderFactory.createTitledBorder("Calendar"));
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 0;
        gbc.weightx = 0;
        calendar.add(scrollPane, gbc);

        f.add(calendar,gbc);

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

    JTable table;
    JScrollPane scrollPane;
    CalendarTableModel tableModel;

    private void initTable() {
        setTable(new JTable());
        getTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        getTable().setAutoscrolls(false);
        getTable().getTableHeader().setResizingAllowed(false);
        getTable().getTableHeader().setReorderingAllowed(false);
        getTable().setColumnSelectionAllowed(true);
        getTable().setRowSelectionAllowed(true);
        getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tableModel = new CalendarTableModel();  //my class extended from AbstractTableModel
        getTable().setModel(tableModel);
        scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setAutoscrolls(false);

        //GET DIMENSION
        int width = 0;
        int height = 0;
        int allWidth, allHeight;
        int padx = 2,pady = 2;
        for (int col = 0; col < table.getColumnCount(); col++) {
            TableColumn tableColumn = table.getTableHeader().getColumnModel().getColumn(col);
            TableCellRenderer renderer = tableColumn.getHeaderRenderer();
            if (renderer == null) {
                renderer = table.getTableHeader().getDefaultRenderer();
            }
            Component component = renderer.getTableCellRendererComponent(table,
                    tableColumn.getHeaderValue(), false, false, -1, col);
            width = Math.max(component.getPreferredSize().width, width);
            table.getColumnModel().getColumn(col).setPreferredWidth(width);
        }
        allWidth = (table.getColumnCount()+padx) * width;
        for (int row = 0; row < table.getRowCount(); row++) {
            TableCellRenderer renderer = table.getCellRenderer(row, 0);
            Component comp = table.prepareRenderer(renderer, row, 0);
            height = Math.max(comp.getMinimumSize().height, height);
        }
        allHeight = (1+table.getRowCount()+pady) * height;

//HERE I SET WIDTHS AND HEIGHTS
        table.setRowHeight(height);
        table.setMinimumSize(new Dimension(allWidth, allHeight));
        scrollPane.setMinimumSize(new Dimension(allWidth, allHeight));
    }

    private void setTable(JTable jTable) {
        this.table = jTable;
    }

    private JTable getTable() {
        return this.table;
    }

    private class CalendarTableModel extends AbstractTableModel {
        private String[] daysData = {"Po", "Út", "St", "Čt", "Pá", "So", "Ne"};
        private int[][] values;

        public CalendarTableModel() {
            values = new int[7][6];
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 7; j++) {
                    values[j][i] = 7;
                }
            }
        }

        @Override
        public int getRowCount() {
            return 6;
        }

        @Override
        public int getColumnCount() {
            return 7;
        }

        @Override
        public String getColumnName(int column) {
            return daysData[column];
        }

        @Override
        public Object getValueAt(int row, int column) {
            return this.values[column][row];
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }
}