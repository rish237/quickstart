package rish.maven.quickstart;




import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;



/**
 * Hello world!
 *
 */
@Test
public class App 
{

	public WebDriver driver;
	public static Properties propPaths,propTestcase;

@BeforeTest
public void beforeTest() throws InterruptedException
{
		
	//Setting the path for the driver so that system can load it....very important
	System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+File.separator+"drivers"+File.separator+"chromedriver.exe");
	System.out.println("Path " + System.getProperty("user.dir")+File.separator+"drivers"+File.separator+"chromedriver.exe");
	
	
	Map<String,Object> prefs = new HashMap<String,Object>();
	
	//removing popups
	prefs.put("profile.default_content_settings.popups", 0);
	prefs.put("download.prompt_for_download", "false");
	
	ChromeOptions options = new ChromeOptions();
	options.setExperimentalOption("prefs",prefs);
	
	
	//options.addArguments("test-type");
	//making sure the window fully opens
	options.addArguments("--start-maximized");
	
	
	DesiredCapabilities caps = DesiredCapabilities.chrome();
	caps.setCapability(ChromeOptions.CAPABILITY,options);
	
	
	setPropertyFiles();
	
	driver = new ChromeDriver(caps);
	Thread.sleep(2000);
	
	DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	Date date = new Date();
	
	
	String dateString=dateFormat.format(date);
			
	
	
	System.out.println("FolderName "+dateString);
	
	
	File BuildDirectory = new File(System.getProperty("user.dir")+File.separator+"Builds"+File.separator+dateString);
    if (!BuildDirectory.exists()) {
        if (BuildDirectory.mkdir()) {
            System.out.println("Directory is created!");
        } else {
            System.out.println("Failed to create directory!");
        }
    }
	
	
    String currentBuildFile=System.getProperty("user.dir")+File.separator+"Builds"+File.separator+"CurrentBuild.txt";
    
    modifyFile(currentBuildFile, dateString);	
	
//	ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
//	
//	driver.close();
//	driver.switchTo().window(tabs.get(1));
}


public void setPropertyFiles()
{
	
	propPaths = new Properties();
	propTestcase = new Properties();
	try {
		
	propPaths.load(new FileInputStream(System.getProperty("user.dir")+File.separator+"src"+File.separator+"main"+File.separator+"resources"+File.separator+"Path.properties"));
	propTestcase.load(new FileInputStream(System.getProperty("user.dir")+File.separator+"src"+File.separator+"main"+File.separator+"resources"+File.separator+"TestCase.properties"));
	System.out.println(System.getProperty("user.dir")+File.separator+"src"+File.separator+"main"+File.separator+"resources"+File.separator+"TestCase.properties");
	}  catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
 	
	
}

/**
 * Purpose : To get the Properties object for the "Paths.properties" Properties File
 * @return propPaths
 * @author rish
 */
public static Properties getPaths(){
	  return propPaths;
}

/**
 * Purpose : To get the Properties object for the "Testcase.properties" Properties File
 * @return propTestcase
 * @author rish
 */
public static Properties getTestcase(){
	  return propTestcase;
}




public void openApp() throws Exception 
{

	try
	{
	System.out.println("Testing");
	Thread.sleep(3000);
	driver.get("https://www.google.com");
	Thread.sleep(3000);
	
	
	
	
	WebDriverWait wait = new WebDriverWait(driver,30);
	
	

	takeScreenShot(driver,"GoogleScreen");
	
	String enteredValue =getTestcase().getProperty("FirstTeam");
	
	System.out.println("enteredValue "+enteredValue);
	
	String urlField=getPaths().getProperty("googlePathTextField");
	String googleSearchbutton=getPaths().getProperty("googleSearchbutton");
	
	
	
	wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(urlField)));
	
	Thread.sleep(3000);
	
	
	
	driver.findElement(By.xpath(urlField)).clear();

	driver.findElement(By.xpath(urlField)).sendKeys(enteredValue);
	
	Thread.sleep(3000);
	
	wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(googleSearchbutton)));
	
	driver.findElement(By.xpath(googleSearchbutton)).click();

	Thread.sleep(3000);
	

	takeScreenShot(driver,"Google Searched "+enteredValue);
	
	String googleIconButton=getPaths().getProperty("googleIconButton");
	
	driver.findElement(By.xpath(googleIconButton)).click();

	
	enteredValue =getTestcase().getProperty("SecondTeam");
	
	System.out.println("enteredValue "+enteredValue);
	
	wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(urlField)));
	
	Thread.sleep(3000);
	
	
	
	driver.findElement(By.xpath(urlField)).clear();

	driver.findElement(By.xpath(urlField)).sendKeys(enteredValue);
	
	Thread.sleep(3000);
	
	wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(googleSearchbutton)));
	
	driver.findElement(By.xpath(googleSearchbutton)).click();

	Thread.sleep(3000);
	
	takeScreenShot(driver,"Google Searched "+enteredValue);
	
	Thread.sleep(3000);
	
	
}
	catch(AssertionError|Exception e)
	{
		//String path=Commons.takeScreenShot(driver,methodName);
		//failResult(methodName, methodName+".png", sb);			
		throw e;
	}
	finally
	{
		//takeScreenShot(driver,"");
	}
	
	
}

public static String takeScreenShot(WebDriver driver, String name) throws Exception
{
	
		System.out.println("Taking Screenshot");
		
		
		 String currentBuildFile=System.getProperty("user.dir")+File.separator+"Builds"+File.separator+"CurrentBuild.txt";
		   
		
		String buildNumber=readFile(currentBuildFile);
		System.out.println("buildNumber "+buildNumber);
		

File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
String fileLocation= System.getProperty("user.dir")+File.separator+"Builds"+File.separator+buildNumber+File.separator;

	FileUtils.copyFile(scrFile, new File(fileLocation+name+".png"));
	return fileLocation+name+".png";

	}


static void modifyFile(String filePath, String newString)
{
    File fileToBeModified = new File(filePath);    
    FileWriter writer = null;
     
    try
    {
    	if(fileToBeModified.delete()){
    		fileToBeModified.createNewFile();
    	}else{
    	    //throw an exception indicating that the file could not be cleared
    	}
        writer = new FileWriter(fileToBeModified);
        writer.write(newString);
    }
    catch (IOException e)
    {
        e.printStackTrace();
    }
    finally
    {
        try
        {
        writer.close();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }
}


public static String readFile(String path)throws Exception
{

File file = new File(path);

BufferedReader br = new BufferedReader(new FileReader(file));

String st;
String buildNumber=null;
while ((st = br.readLine()) != null)
{
	//System.out.println(st);
buildNumber=st;
}


return buildNumber;
}



@AfterTest
public void afterTest() throws InterruptedException {
	System.out.println("Closing!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
	
driver.close();
}



}