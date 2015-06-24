package main.ui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public final class AutoSuggestDialog extends JDialog {

	private JPanel panel;
	private JScrollPane scrollPane;
	private JTable table;
	
	public AutoSuggestDialog(Point p){
//		this.setUndecorated(true);
//		this.addFocusListener(new FocusListener() {
//			@Override
//			public void focusLost(FocusEvent arg0) {
//				System.out.println("Dialog Closed");
//				dispose();
//			}
//			
//			@Override
//			public void focusGained(FocusEvent arg0) {
//				
//			}
//		});
//		
//		this.addKeyListener(new KeyListener() {
//            @Override
//            public void keyTyped(KeyEvent e) {
//            }
//
//            @Override
//            public void keyPressed(KeyEvent e) {
//            	System.out.println("Inside KeyPressed");
//                if ((e.getKeyCode() == KeyEvent.VK_ESCAPE)) {
//                	dispose();
//                }
//            }
//
//            @Override
//            public void keyReleased(KeyEvent e) {
//            }
//        });
		
		

		initComponents();
		this.setVisible(true);
		this.setSize(300,300);
		this.setMaximumSize(new Dimension(300,300));
		this.setLayout(new GridLayout(1,1));
		if(p!= null)
		this.setLocation(p);
		KeyboardFocusManager
        .getCurrentKeyboardFocusManager()
        .addKeyEventDispatcher(new KeyEventDispatcher() {
        public boolean dispatchKeyEvent(KeyEvent e) {
            boolean keyHandled = false;
            if (e.getID() == KeyEvent.KEY_PRESSED) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    keyHandled = true;
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                	System.out.println("Inside Escape");
                	 keyHandled = true;
                	 KeyListener[] keyListeners= getKeyListeners();
                	 for(KeyListener key :keyListeners){
                		 removeKeyListener(key);
                	 }
                	 AutoSuggestDialog.this.setVisible(false);
                	 dispose();
//                	hide();
//                	 setVisible(false);
                }
            }
            return keyHandled;
        }
        });
//		
//		WindowAdapter adapter = (WindowAdapter)this.getWindowListeners()[0];
//		adapter.windowClosing(new WindowEvent((Window)this, WindowEvent.WINDOW_CLOSING));
		
	}
	
	private void initComponents(){
		panel = new JPanel();
		scrollPane = new JScrollPane();
		table = new JTable();
		Container dialogContainer = this.getContentPane();
		dialogContainer.add(panel);
//		scrollPane.setLayout(null);
		panel.setLayout(new GridLayout(1,1));
		panel.add(scrollPane);
//		scrollPane.setSize(this.getSize());
		scrollPane.setViewportView(table);
		table.setTableHeader(null);
		table.setShowGrid(false);
		DefaultTableModel model = (DefaultTableModel)table.getModel();
		Object[][] data = {{"ABC"},{"BCD"},{"CDE"}};
		Object[] columns= {"a"};
		model.setDataVector(data, columns);
		table.setModel(model);
		model.fireTableStructureChanged();
		
	}
	
	public static void main(String[] args) {
		AutoSuggestDialog dialog = new AutoSuggestDialog(null);
	}
	
}
