package main.java.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.table.DefaultTableModel;

import main.java.data.DbData;
import main.ui.SQLExecutionResultTable;

public class DBConnect
{
	Statement stmt;
	static Connection con;
	static String url;
	static String username;
	static String password;
	static DatabaseQueryToolUI client;
	static public Map<DbData,Connection> dbConnectionMap = new HashMap<DbData,Connection>();
	
	public static void main(String[] args)
	{

		try{

	        UIManager.setLookAndFeel("main.ui.lookandfeel.com.jtattoo.plaf.texture.TextureLookAndFeel");
			
		}catch (Exception e) {
		}
		client = new DatabaseQueryToolUI();
		return;
	}
	
	/**
	 * Handles SQL commands. Type 1 = Update. Type 2 = Query
	 */
	public static boolean sqlCmd(int type,Connection con,String selectQuery ,String[] query)
	{
		boolean result = false;
		StringBuilder str = new StringBuilder();
		if(type == 1)
		{
			Statement stmt;
			try
			{
				for(String q : query){
					if(q.trim().length() >0){
						stmt = con.createStatement();
						stmt.executeUpdate(q);
						str.append("1 record updated\r\n");
					}
				}
				client.setSqlResultView(str.toString());
			}
			catch (SQLException e)
			{
				client.setSqlResultView(str.append(e.getLocalizedMessage()).toString());
				e.printStackTrace();
			}
		}
		else if(type == 2)
		{

			Statement stmt;
			try
			{
				stmt = con.createStatement();
				ResultSet resultSet = stmt.executeQuery(selectQuery);
				ResultSetMetaData md = resultSet.getMetaData();
				int columnCount = md.getColumnCount();
				
				Vector<String> columns = new Vector<String>(columnCount);
				
				// store column names
				for(int i = 1; i <= columnCount; i++)
					columns.add(md.getColumnName(i));
				
				Vector<Vector> data = new Vector<Vector>();
				
				// store row data
				while(resultSet.next())
				{
					Vector<String> row = new Vector<String>(columnCount);
					
					for(int i = 1; i <= columnCount; i++)
					{
						row.add(resultSet.getString(i));
					}
					data.add(row);
				}
				
				DefaultTableModel tableModel = (DefaultTableModel) client.getSQLExecutionResultTable().getModel();
				tableModel.setDataVector(data, columns);
				client.getSQLExecutionResultTable().setModel(tableModel);
				TableColumnAdjuster tca = new TableColumnAdjuster(client.getSQLExecutionResultTable());
				tableModel.fireTableStructureChanged();
				tca.adjustColumns();
				client.setSqlResultView(null);
			}
			catch (SQLException e)
			{
				client.setSqlResultView(e.getLocalizedMessage());
				e.printStackTrace();
			}
		}
		
		return(result);
	}
	
	public static boolean sqlCmdForSearch(Connection con,String query,SQLExecutionResultTable SQLExecutionResultTable)
	{
		boolean result = false;

			Statement stmt;
			try
			{
				stmt = con.createStatement();
				ResultSet resultSet = stmt.executeQuery(query);
				ResultSetMetaData md = resultSet.getMetaData();
				int columnCount = md.getColumnCount();
				
				Vector<String> columns = new Vector<String>(columnCount);
				
				// store column names
				for(int i = 1; i <= columnCount; i++)
					columns.add(md.getColumnName(i));
				
				Vector<Vector> data = new Vector<Vector>();
				
				// store row data
				while(resultSet.next())
				{
					Vector<String> row = new Vector<String>(columnCount);
					
					for(int i = 1; i <= columnCount; i++)
					{
						row.add(resultSet.getString(i));
					}
					data.add(row);
				}
				
				DefaultTableModel tableModel = (DefaultTableModel) SQLExecutionResultTable.getModel();
				tableModel.setDataVector(data, columns);
				SQLExecutionResultTable.setModel(tableModel);
				TableColumnAdjuster tca = new TableColumnAdjuster(SQLExecutionResultTable);
				tableModel.fireTableStructureChanged();
				tca.adjustColumns();
			}
			catch (SQLException e)
			{
				client.setSqlResultView(e.getLocalizedMessage());
				e.printStackTrace();
			}
		return(result);
	}
	
	public static boolean initDB(DbData dbToConnect)
	{

		try
		{
			Class.forName(dbToConnect.getDbDriver());
			url = dbToConnect.getDbUrl().toString();
			username = dbToConnect.getDbUserName();
			password = dbToConnect.getDbPassword();
			con = DriverManager.getConnection(url, username, password);
			
			System.out.println("URL: " + url);
			System.out.println("Connection: " + con);
			dbConnectionMap.put(dbToConnect, con);
			return(true);
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return(false);
	}
}
