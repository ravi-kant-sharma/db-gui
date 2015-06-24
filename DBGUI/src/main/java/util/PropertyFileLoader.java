package main.java.util;
import java.io.FileInputStream;
import java.util.Properties;

public class PropertyFileLoader {
	  public static Properties getUserProperties()
	    throws Exception
	  {
	    String str = System.getProperty("user.home");
	    Properties localProperties = new Properties();
	    FileInputStream localFileInputStream = null;
	    try
	    {
	      localFileInputStream = new FileInputStream(str + "/replaceList.properties");
	      localProperties.load(localFileInputStream);
	      if (localProperties.size() == 0) {
	        throw new RuntimeException("Unable to load code_review_upload.properties");
	      }
	    }
	    finally
	    {
	      if (localFileInputStream != null) {
	        localFileInputStream.close();
	      }
	    }
	    return localProperties;
	  }

}
