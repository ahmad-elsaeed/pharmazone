package pharmazone.MR;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.asserts.SoftAssert;
import com.applitools.eyes.selenium.Eyes;
import Helper.Help;

public class TestBase {
	protected static WebDriver driver;
	protected static Eyes eyes;
	private static Properties prop;

	// soft assertion method
	protected SoftAssert softassert = new SoftAssert();

	// Initiate the Chrome browser
	@BeforeSuite
	public void openBrowser() throws InterruptedException, Exception {
		prop = new Properties();
		try {
			FileInputStream fis = new FileInputStream("resources\\test.properties");
			prop.load(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String chromeDriverPath = prop.getProperty("chromepath");
		System.setProperty("webdriver.chrome.driver", chromeDriverPath);
		//	System.setProperty("webdriver.chrome.driver", "resources\\ChromeDriver\\chromedriver1.exe");
		driver = new ChromeDriver();
		/*
		String firefoxDriverDriverPath = prop.getProperty("firefoxpath");
		System.setProperty("webdriver.gecko.driver", firefoxDriverDriverPath);
		driver = new FirefoxDriver();
		 System.setProperty("webdriver.chrome.driver",
		 "resources\\ChromeDriver\\chromedriver1.exe");
		driver = new ChromeDriver();
		 * String firefoxDriverDriverPath = prop.getProperty("firefoxpath");
		 * System.setProperty("webdriver.gecko.driver", firefoxDriverDriverPath); driver
		 * = new FirefoxDriver();
		 */
		driver.navigate().to("https://pharmazone.roqay.solutions/login");
		driver.manage().deleteAllCookies();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		initiEyes();
	}

	// Initiate Eye apptlitool for visual testing
	private static void initiEyes() {
		eyes = new Eyes();
		eyes.setApiKey(prop.getProperty("api"));

	}

	// validate window design and elements
	public void validateWindow() {
		eyes.open(driver, "pharmazone", Thread.currentThread().getStackTrace()[2].getMethodName());
		// get image for the whole screen
		eyes.setForceFullPageScreenshot(true);
		eyes.checkWindow();
		eyes.close();
	}


	//taking screenshot for failures 
	@AfterMethod
	public void screenshotOnFailure(ITestResult result) {
		if (result.getStatus() == ITestResult.FAILURE) {
			System.out.println("Failed!");
			System.out.println("Taking Screenshot....");
			Help.captureScreenshot(driver, result.getName());
		}
	}

	@AfterSuite
	public void quitBrowser() {
	driver.quit();
	//	eyes.abortIfNotClosed();
	}

}
