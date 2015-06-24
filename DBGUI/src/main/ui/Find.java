package main.ui;

import static javax.swing.GroupLayout.Alignment.BASELINE;
import static javax.swing.GroupLayout.Alignment.LEADING;

import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import com.ibm.db2.jcc.b.db;

import main.java.core.DatabaseQueryToolUI;
 
public class Find extends JDialog {
    public Find(DatabaseTabPanel dbPanel,DatabaseQueryToolUI splitPanelImpl) {
    	this.setLocation(splitPanelImpl.mainFrame.getX()+100,splitPanelImpl.mainFrame.getY()+50);
    	this.setVisible(true);
    	this.dbPanel = dbPanel;
    	
        JLabel label = new JLabel("Find What:");;
        JLabel replaceLabel = new JLabel("Replace With");
        findTextField = new JTextField();
        replaceTextField = new JTextField();
        JCheckBox caseCheckBox = new JCheckBox("Match Case");
        JCheckBox wrapCheckBox = new JCheckBox("Wrap Around");
        JCheckBox wholeCheckBox = new JCheckBox("Whole Words");
        JCheckBox backCheckBox = new JCheckBox("Search Backwards");
        JButton findButton = new JButton("Find");
        JButton replaceButton = new JButton("Repalace All");
        JButton cancelButton = new JButton("Cancel");
 
        // remove redundant default border of check boxes - they would hinder
        // correct spacing and aligning (maybe not needed on some look and feels)
        caseCheckBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        wrapCheckBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        wholeCheckBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        backCheckBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        
        cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
			}
		});
        
        replaceButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			replaceText();
			}
		});
 
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
 
        layout.setHorizontalGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup()
                .addComponent(label)
                .addComponent(replaceLabel))
                .addGroup(layout.createParallelGroup(LEADING)
                    .addComponent(findTextField)
                    .addComponent(replaceTextField)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(LEADING)
                            .addComponent(caseCheckBox)
                            .addComponent(wholeCheckBox))
                        .addGroup(layout.createParallelGroup(LEADING)
                            .addComponent(wrapCheckBox)
                            .addComponent(backCheckBox))))
                .addGroup(layout.createParallelGroup(LEADING)
                    .addComponent(findButton)
                    .addComponent(replaceButton)
                    .addComponent(cancelButton))
            );
        
        layout.linkSize(SwingConstants.HORIZONTAL, findButton, cancelButton);
       
        layout.setVerticalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(BASELINE)
                .addComponent(label)
                .addComponent(findTextField)
                .addComponent(findButton))
                .addGroup(layout.createParallelGroup(BASELINE)
                .addComponent(replaceLabel)
                .addComponent(replaceTextField)
                .addComponent(replaceButton))
            .addGroup(layout.createParallelGroup(LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(BASELINE)
                        .addComponent(caseCheckBox)
                        .addComponent(wrapCheckBox))
                    .addGroup(layout.createParallelGroup(BASELINE)
                        .addComponent(wholeCheckBox)
                        .addComponent(backCheckBox)))
                .addComponent(cancelButton))
        );
 
        setTitle("Find");
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
    
    private void replaceText(){
    	String findText = findTextField.getText();
    	String replaceText = replaceTextField.getText();
 
    	dbPanel.replaceText(findText, replaceText);
    }
    
    JTextField findTextField; 
    JTextField replaceTextField;
    DatabaseTabPanel dbPanel;
    
}