package main.ui;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Caret;

import main.java.core.DatabaseQueryToolUI;
import main.java.core.TableAndParamterFinder;
import main.java.data.QueryParameterData;


public class DatabaseTabPanel extends JPanel{
	 JScrollPane tabScrollPane;
	 JTextArea tabTextArea;
	 JToolBar toolbar;
	 JButton runSqlCommandButton;
	 boolean textPasted;
	 DatabaseQueryToolUI splitPanelImpl;
	 
	public DatabaseTabPanel(final DatabaseQueryToolUI splitPanelImpl){
		this.splitPanelImpl = splitPanelImpl;
		 this.tabScrollPane = new JScrollPane();
//		 this.toolbar = new JToolBar();
		 this.runSqlCommandButton = new JButton("Run Query");
//		 toolbar.add(this.runSqlCommandButton);
//		 this.toolbar.setSize(this.getWidth(), 40);
//		 this.add(toolbar,BorderLayout.PAGE_START);
//		 this.toolbar.addSeparator(new Dimension(150, 0));
		 createTextAreaWithProperties(tabTextArea,"");
		 add(tabScrollPane);
		 setLayout(new GridLayout(1,1));
	}
	
	private void onTextPasted(){
		String text = new String(tabTextArea.getSelectedText() != null ?  tabTextArea.getSelectedText() : tabTextArea.getText());
		Set<Entry<Object, Object>> entrySet = splitPanelImpl.getReplacePropertiesFile().entrySet();
		Iterator enrtySetItr = entrySet.iterator();
		while(enrtySetItr.hasNext()){
			Entry<Object,Object>  entry = (Entry<Object, Object>) enrtySetItr.next();
			text= text.replaceAll((String)entry.getKey(),(String)entry.getValue());
		}
		createTextAreaWithProperties(tabTextArea, text);
		tabTextArea.setCaretPosition(tabTextArea.getText().length());
		this.revalidate();
		this.repaint();
	}
	
	private void openParameterWindow(){
		String query = tabTextArea.getSelectedText() != null ?  tabTextArea.getSelectedText() : tabTextArea.getText();
		List<QueryParameterData> queryParamData = TableAndParamterFinder.findParameters(query);
		for(QueryParameterData data : queryParamData){
			System.out.println(data.getQueryParameter()+" "+data.getTableAlias() +" = "+ data.getTableName());
		}
		ParameterDialog parameterDialog = new ParameterDialog(this,splitPanelImpl, queryParamData);
//		replaceParameters(query,queryParamData);
	}
	
	private void openAutoSuggestDialog(){
		Caret caret = tabTextArea.getCaret();
		Point p = caret.getMagicCaretPosition();
		if(p != null){
			p.x += tabTextArea.getLocationOnScreen().x;
			p.y += tabTextArea.getLocationOnScreen().y;
		}else{
			p = new Point();
			p.x = tabTextArea.getLocationOnScreen().x;
			p.y = tabTextArea.getLocationOnScreen().y;
		}
		
		AutoSuggestDialog dialog = new AutoSuggestDialog(p);
	}
	
	public void replaceParameters(Collection<QueryParameterData> queryParamData){
		String query = tabTextArea.getSelectedText() != null ?  tabTextArea.getSelectedText() : tabTextArea.getText();
		if(queryParamData != null){
			for(QueryParameterData queryParam : queryParamData){
				if(queryParam.getValue() != null && queryParam.getValue().length() > 0){
					query = query.replaceAll(queryParam.getQueryParameter(), queryParam.getValue());
				}
			}
		}
		createTextAreaWithProperties(tabTextArea, query);
		this.revalidate();
		this.repaint();
	}
	
	public void replaceText(String sourceText,String replaceText){
		String query = tabTextArea.getSelectedText() != null ?  tabTextArea.getSelectedText() : tabTextArea.getText();
		if(sourceText != null && replaceText != null){
			query = query.replaceAll(sourceText, replaceText);
		}
		createTextAreaWithProperties(tabTextArea,query);
		this.revalidate();
		this.repaint();
	}
	
	private void openFindFrame(){
		Find findFrame = new Find(this,splitPanelImpl);
	}
	
	public void replaceSymbols(){
		onTextPasted();
	}
	public void reverseSymbols(){
		String text = new String(tabTextArea.getSelectedText() != null ?  tabTextArea.getSelectedText() : tabTextArea.getText());
		Set<Entry<Object, Object>> entrySet = splitPanelImpl.getReplacePropertiesFile().entrySet();
		Iterator enrtySetItr = entrySet.iterator();
		while(enrtySetItr.hasNext()){
			Entry<Object,Object>  entry = (Entry<Object, Object>) enrtySetItr.next();
//			System.out.println((String)entry.getValue());
			if(((String)entry.getValue()).equals("<") || ((String)entry.getValue()).equals(">")){
				System.out.println("Inside < || >");
				text = text.replaceAll((String)entry.getValue(),(String)entry.getKey());
			}else if(((String)entry.getValue()).equals("||")){
				System.out.println(" || ");
				text = text.replaceAll("\\|\\|",(String)entry.getKey());
			}
			else{
				text= text.replaceAll("(\\s+)"+(String)entry.getValue()+"(\\s+)"," "+(String)entry.getKey()+" ");
			}
			
//			System.out.println(text);
		}
		createTextAreaWithProperties(tabTextArea, text);
		tabTextArea.setCaretPosition(tabTextArea.getText().length());
		this.revalidate();
		this.repaint();
	}
	
	private void createTextAreaWithProperties(JTextArea textArea,String text){
		 tabTextArea = new JTextArea(text);
		 Font f = new Font("Courier New",Font.PLAIN, 14);
		 tabTextArea.setFont(f);
		 tabTextArea.getDocument().addDocumentListener( new DocumentListener() {
			  public void changedUpdate( DocumentEvent e )
			  {
			  }
			  public void insertUpdate( DocumentEvent e )
			  {
			    if(textPasted){
			    	onTextPasted();
			    }
			  }
			  public void removeUpdate( DocumentEvent e )
			  {
				  
			  }
			});
		 tabTextArea.addKeyListener(new KeyListener() {
	            @Override
	            public void keyTyped(KeyEvent e) {
	            }

	            @Override
	            public void keyPressed(KeyEvent e) {
	                if ((e.getKeyCode() == KeyEvent.VK_V) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
	                	textPasted = true;
	                }
	                
	                if ((e.getKeyCode() == KeyEvent.VK_R) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
	                	openParameterWindow();
	                }
	                if ((e.getKeyCode() == KeyEvent.VK_SPACE) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
//	                	openAutoSuggestDialog();
	                }
	                if ((e.getKeyCode() == KeyEvent.VK_F) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
	                	openFindFrame();
	                }
	            }

	            @Override
	            public void keyReleased(KeyEvent e) {
	            }
	        });
		tabScrollPane.setViewportView(this.tabTextArea);
		textPasted = false;
	}
}
