package main.java.core;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.spec.OAEPParameterSpec;

import main.java.data.QueryParameterData;

public class TableAndParamterFinder {
	private static List<String> parameterList;
	
	public static List<QueryParameterData>  findParameters(String query){
		Pattern parameterPattern = Pattern.compile("[a-zA-Z_0-9]*.[a-zA-Z_]*(\\s+)?<?>?=?(LIKE)?(\\s+)?:[a-zA-Z\\._0-9]*");
		Matcher matcher = parameterPattern.matcher(query);
		List<QueryParameterData> queryParameterData = new ArrayList<QueryParameterData>();
		String[] strArr = new String[1];
		String str = new String();
		while(matcher.find())
	     {
			str = matcher.group();
			if(str.contains("=")){
				strArr = matcher.group().split("=");
				queryParameterData.add(new QueryParameterData(strArr[1].trim(), strArr[0].trim().split("\\.")[0], null));
			}else{
				strArr = matcher.group().split("[\\s+]");
				queryParameterData.add(new QueryParameterData(strArr[2].trim(), strArr[0].trim().split("\\.")[0], null));
			}
	     }
		queryParameterData = findParametersTables(query,queryParameterData);
		return queryParameterData;
	}
	
	public static List<QueryParameterData> findParametersTables(String query,List<QueryParameterData> queryParameterData){
		List<String> tableKeywords = new ArrayList<String>();
		tableKeywords.add("FROM");
		tableKeywords.add("JOIN");
		parameterList = new ArrayList<String>();
		List<QueryParameterData> newParameterData = new ArrayList<QueryParameterData>();
		for(QueryParameterData parameterData : queryParameterData){
			if(!parameterList.contains(parameterData.getQueryParameter())){
				if(!(parameterData.getTableAlias().trim().indexOf(' ') >0)){
				Pattern parameterPattern = Pattern.compile("(\\s+)[a-zA-Z_]*(\\s+)("+parameterData.getTableAlias()+")(\\s+)");
				Matcher matcher = parameterPattern.matcher(query);
				while(matcher.find())
			      {
					String[] tableNames = matcher.group().trim().split("\\s+");
					if(tableKeywords.contains(tableNames[0].trim().toUpperCase())){
						parameterData.setTableName(parameterData.getTableAlias());
					}else{
						parameterData.setTableName(tableNames[0]);
					}
			      }
				}else{
					Pattern parameterPattern = Pattern.compile("(FROM)(\\s+)[a-zA-Z0-9_]*");
					Matcher matcher = parameterPattern.matcher(query);
					while(matcher.find())
				      {
						System.out.println(matcher.group());
				      }
				}
				newParameterData.add(parameterData);
				parameterList.add(parameterData.getQueryParameter());
				System.out.println(parameterData.getQueryParameter());
			}
		}
		return newParameterData;
	}
//	
	public static void main(String[] args) {
		String str = "SELECT LLCC.LOSS_LOC_CITY_NM value "+			    
			"FROM CLAIM CLAIM "+
		    "JOIN CLAIM_PARTICIPATION CCPART "+
		    "WHERE "+
				"CLAIM.CLM_ID = :claimId	";
		List<QueryParameterData> queryParameterData = findParameters(str);
//		for(QueryParameterData parameterData : queryParameterData){
//			System.out.println(parameterData.getQueryParameter()+" "+parameterData.getTableAlias());
//		}
		findParametersTables(str,queryParameterData);
		
		
		for(QueryParameterData parameterData : queryParameterData){
			System.out.println(parameterData.getQueryParameter()+" "+parameterData.getTableAlias() +" "+ parameterData.getTableName());
		}
	}
}
