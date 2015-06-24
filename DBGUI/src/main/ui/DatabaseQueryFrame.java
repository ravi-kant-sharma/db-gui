package main.ui;

import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Rectangle;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

import javax.swing.JFrame;

import main.java.core.DBConnect;

public class DatabaseQueryFrame extends JFrame{

	private Rectangle maxBounds;

	public DatabaseQueryFrame()
	{
	    super();        
	    maxBounds = null;
	}

	//Full implementation has other JFrame constructors

	public Rectangle getMaximizedBounds()
	{
	    return(maxBounds);
	}

	public synchronized void setMaximizedBounds(Rectangle maxBounds)
	{
	    this.maxBounds = maxBounds;
	    super.setMaximizedBounds(maxBounds);
	}

	public synchronized void setExtendedState(int state)
	{       
	    if (maxBounds == null &&
	        (state & Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH)
	    {
	        Insets screenInsets = getToolkit().getScreenInsets(getGraphicsConfiguration());         
	        Rectangle screenSize = getGraphicsConfiguration().getBounds();
	        Rectangle maxBounds = new Rectangle(screenInsets.left + screenSize.x, 
	                                    screenInsets.top + screenSize.y, 
	                                    screenSize.x + screenSize.width - screenInsets.right - screenInsets.left,
	                                    screenSize.y + screenSize.height - screenInsets.bottom - screenInsets.top);
	        super.setMaximizedBounds(maxBounds);
	    }
	    super.setMaximizedBounds(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds());

	    super.setExtendedState(state);
	}
	
	@Override
	public void setDefaultCloseOperation(int closeOperation) {
		Collection<Connection> dbConnections = DBConnect.dbConnectionMap.values();
		for(Connection dbConnection : dbConnections){
			try {
				dbConnection.close();
				System.out.println("Connection Closed");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		super.setDefaultCloseOperation(closeOperation);
	}
}
