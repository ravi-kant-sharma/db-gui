package main.java.core;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import main.java.core.factory.DatabaseTabPanelFactory;
import main.java.data.DbData;
import main.java.data.DbList;
import main.java.util.PropertyFileLoader;
import main.ui.ClosableTabbedPane;
import main.ui.DatabaseQueryFrame;
import main.ui.DatabaseTabPanel;
import main.ui.MenuToolBar;
import main.ui.SQLExecutionResultTable;

import com.thoughtworks.xstream.XStream;


public class DatabaseQueryToolUI {
	
	public DatabaseQueryToolUI(){
		initComponents();
		
	}
	
	private void initComponents(){
		mainFrame = new DatabaseQueryFrame();
		SQLExecutionResultTable = new SQLExecutionResultTable();
		sqlQueryTabbedPane = new ClosableTabbedPane();
		tabMap = new HashMap<DatabaseTabPanel, DbData>();
		sqlExceptionTextArea = new JTextArea();
		Font f = new Font("Courier New",Font.PLAIN, 16);
		sqlExceptionTextArea.setForeground(Color.RED);
		sqlExceptionTextArea.setFont(f);
		try {
			replacePropertiesFile = PropertyFileLoader.getUserProperties();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		upperPanel = new JPanel();
		upperPanel.setLayout(null);
		bottomPanel = new JPanel();
		bottomPanel.setLayout(new GridLayout(1,1));
		sidePanel = new JPanel();
		sidePanel.setLayout(null);
		
		upperTextArea = new JTextArea();
		upperTextArea.setSize(upperPanel.getSize());
		bottomTextArea = new JTextArea();
		bottomTextArea.setSize(bottomPanel.getSize());
		
		upperScrollPane = new JScrollPane();
		
		bottomScrollPane = new JScrollPane();
		bottomScrollPane.setSize(bottomPanel.getSize());
	
		MenuToolBar menuToolBar = new MenuToolBar(this);
//		upperScrollPane.setViewportView(sqlQueryTabbedPane);
		bottomScrollPane.setViewportView(SQLExecutionResultTable);
//		this.SQLExecutionResultTable.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
		
		bottomPanel.add(bottomScrollPane);
//		upperPanel.add(upperScrollPane);
//		upperPanel.add(sqlQueryTabbedPane);
		
		mainSplittedPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, sqlQueryTabbedPane, bottomPanel);
        mainSplittedPane.setOneTouchExpandable(true);
        mainSplittedPane.setDividerLocation(0.5);
        
        verticalSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sidePanel, mainSplittedPane);
        verticalSplitPane.setOneTouchExpandable(true);
        verticalSplitPane.setDividerLocation(0.5);

        mainFrame.add(verticalSplitPane);
        mainFrame.getContentPane().add(menuToolBar, BorderLayout.NORTH);
        buildDatabaseTree();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle rect = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        mainFrame.setPreferredSize(new Dimension(rect.width,rect.height));
//        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setResizable(true);
        mainFrame.setVisible(true);
        mainFrame.pack();
        restoreDefaults();
        
        mainSplittedPane.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, new PropertyChangeListener() {
        	@Override
        	public void propertyChange(PropertyChangeEvent pce) {
        	   changeDimesions();
        	}
        	});
        
        verticalSplitPane.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, new PropertyChangeListener() {
        	@Override
        	public void propertyChange(PropertyChangeEvent pce) {
        	   changeDimesions();
        	}
        	});
		
	}
	
	 private void restoreDefaults() {
	        SwingUtilities.invokeLater(new Runnable() {

	            @Override
	            public void run() {
	                mainSplittedPane.setDividerLocation(mainSplittedPane.getSize().height /2);
	                verticalSplitPane.setDividerLocation(verticalSplitPane.getSize().width /6);
	                Dimension dim = new Dimension(mainSplittedPane.getSize().width,mainSplittedPane.getSize().height /2);
	                upperScrollPane.setSize(dim);
	                upperTextArea.setSize(dim);
	                
	                bottomScrollPane.setSize(dim);
	                bottomTextArea.setSize(dim);
	                
	                sidePanel.setSize(new Dimension(200,mainFrame.getHeight()));
	                
	            }
	        });
	 }
	 
	 private void changeDimesions(){
		 int divider = mainSplittedPane.getDividerLocation();
		 Dimension dim = new Dimension(verticalSplitPane.getSize().width,mainSplittedPane.getSize().height*divider);
         upperScrollPane.setSize(dim);
         upperTextArea.setSize(dim);
         
         bottomScrollPane.setSize(dim);
         bottomTextArea.setSize(dim);
	 }
	 
	 public void addNewTab(DbData dbToConnect){
		 DatabaseTabPanel databaseTabPanel = DatabaseTabPanelFactory.getDatabaseTabPanelInstance(this);
//		 DatabaseTabPanel databaseTabPanel = new DatabaseTabPanel(this);
		 sqlQueryTabbedPane.add(" "+dbToConnect.getDbName()+"   ",databaseTabPanel);
		 sqlQueryTabbedPane.setSelectedIndex(sqlQueryTabbedPane.getTabCount()-1);
		 tabMap.put(databaseTabPanel, dbToConnect);
	 }
	 
	 private void buildDatabaseTree(){
		DefaultMutableTreeNode databases = new DefaultMutableTreeNode("Databases");
		loadDatabase();
		dbMap= new HashMap<Integer,DbData>();
		int i = 1;
		for(DbData dbData : dbList.getDatabase()){
			DefaultMutableTreeNode db = new DefaultMutableTreeNode(dbData.getDbName());
			databases.add(db);
			dbMap.put(i, dbData);
			i++;
		}
		 databaseTree = new JTree(databases);
		 databaseTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		 databaseTree.setShowsRootHandles(true);
		 JScrollPane treeScrollPane = new JScrollPane();
		 treeScrollPane.getViewport().add(databaseTree);
		 sidePanel.setLayout(new GridLayout(1,1));
		 sidePanel.add( treeScrollPane, BorderLayout.CENTER );
	 }
	 
	 private void loadDatabase(){
		try{
			String userHome = System.getProperty("user.home");
			String filePath = userHome+"\\db.xml";
			XStream xstream = new XStream();
			xstream.alias("dbdataList",DbList.class);
			xstream.alias("dbdata",DbData.class);
			InputStream is = new FileInputStream(filePath);
			dbList = (DbList) xstream.fromXML(is);
			System.out.println(((DbData)dbList.getDatabase().get(1)).getDbDriver());
			}catch(Exception f){
				f.printStackTrace();
		}
	 }
	 
	 public void setSqlResultView(String text){
		if(text == null){
			 bottomScrollPane.setViewportView(this.SQLExecutionResultTable);
		}else{
		 bottomScrollPane.setViewportView(this.sqlExceptionTextArea);
		 sqlExceptionTextArea.setText(text);
		}
	 }
	 
	public DatabaseQueryFrame mainFrame;
	private JSplitPane mainSplittedPane;
	private JSplitPane verticalSplitPane;
	private JPanel upperPanel;
	private JPanel bottomPanel;
	private JPanel sidePanel;
	private JScrollPane bottomScrollPane;
	private JScrollPane upperScrollPane;
	private JTextArea upperTextArea;
	private JTextArea bottomTextArea;
	private SQLExecutionResultTable SQLExecutionResultTable;
	private ClosableTabbedPane sqlQueryTabbedPane;
	private JToolBar toolBar;
	private JButton connectButton;
	private JTree databaseTree;
	private DbList dbList;
	private Map<Integer,DbData> dbMap;
	private Map<DatabaseTabPanel,DbData> tabMap;
	Properties replacePropertiesFile;
	private JTextArea sqlExceptionTextArea;
	
	public JTree getDatabaseTree(){
		return this.databaseTree;
	}
	
	public Map<Integer,DbData> getDbMap(){
		return this.dbMap;
	}
	
	public Map<DatabaseTabPanel,DbData> getTabMap(){
		return this.tabMap;
	}
	
	public ClosableTabbedPane getSqlQueryTabbedPane(){
		return this.sqlQueryTabbedPane;
	}
	
	public JTable getSQLExecutionResultTable(){
		return this.SQLExecutionResultTable;
	}
	
	public Properties getReplacePropertiesFile(){
		return this.replacePropertiesFile;
	}
	public JTextArea getSqlExceptionTextArea(){
		return this.sqlExceptionTextArea;
	}
}
