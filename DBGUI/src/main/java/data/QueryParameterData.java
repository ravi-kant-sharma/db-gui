package main.java.data;

public class QueryParameterData {
	
	private String queryParameter;
	private String value;
	private String tableAlias;
	private String tableName;
	
	public QueryParameterData(String queryParam,String tableAlias,String tableName){
		this.queryParameter = queryParam;
		this.tableAlias = tableAlias;
		this.tableName = tableName;
	}
	public String getQueryParameter() {
		return queryParameter;
	}
	
	public void setQueryParameter(String queryParameter) {
		this.queryParameter = queryParameter;
	}
	
	public String getTableAlias() {
		return tableAlias;
	}
	
	public void setTableAlias(String tableAlias) {
		this.tableAlias = tableAlias;
	}
	
	public String getTableName() {
		return tableName;
	}
	
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	
}
