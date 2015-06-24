package main.java.data;

public class DbData {
		
		private String dbName;
		
		private String dbDriver;
		
		private String dbUrl;
		
		private String dbUserName;
		
		private String dbPassword;
		
		public DbData(){
			
		}
		
		public String getDbName() {
			return dbName;
		}
		
		public void setDbName(String dbName) {
			this.dbName = dbName;
		}
		
		public String getDbDriver() {
			return dbDriver;
		}
		
		public void setDbDriver(String dbDriver) {
			this.dbDriver = dbDriver;
		}
		
		public String getDbUrl() {
			return dbUrl;
		}
		
		public void setDbUrl(String dbUrl) {
			this.dbUrl = dbUrl;
		}
		
		public String getDbUserName() {
			return dbUserName;
		}
		
		public void setDbUserName(String dbUserName) {
			this.dbUserName = dbUserName;
		}
		
		public String getDbPassword() {
			return dbPassword;
		}
		
		public void setDbPassword(String dbPassword) {
			this.dbPassword = dbPassword;
		}
		
		public boolean equals(Object obj){
			if(this.getDbName() == ((DbData)obj).getDbName()){
				System.out.println("this.getDbName()="+this.getDbName()+" ((DbData)obj).getDbName()="+((DbData)obj).getDbName());
				return true;
			}
			return false;
			
		}
		
		public int hashCode(){
			return this.getDbName().hashCode();
		}
		
}
