package main.java.data;



import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;



//@XmlAccessorType(XmlAccessType.FIELD)

//@XmlRootElement(name="dblist")
public class DbList {
	
//	@XmlElement(name = "db",required = true)
	protected List<DbData> dbList;
	
	public List<DbData> getDatabase() {
        if (dbList == null) {
        	dbList = new ArrayList<DbData>();
        }
        return this.dbList;
    }
	
	public DbList(List<DbData> dbData){
		this.dbList = dbData;
	}
	
	public DbList(){
		
	}

}
