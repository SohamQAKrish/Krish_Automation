package common;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.assertj.core.api.Assertions;
import org.json.simple.JSONObject;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.TimeoutException;
import org.testng.ITestClass;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import org.yaml.snakeyaml.Yaml;
import com.google.common.collect.Ordering;
import com.opencsv.CSVReader;

import enums.bateel.BateelLoginCheckoutEnum;
import enums.bateel.BateelPLPPageEnum;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;

/**
 * This class will contain all the common utility methods
 * 
 * @author spandit
 * @lastmodifiedby spandit
 */
public class UtilitiesCommon {
	private static final String DEFAULT_ENVIRONMENT = "QA";
	private static final String DEFAULT_BROWSER = "chrome";
	private static final String DEFAULT_TIMEOUT = "30";
	private static final String USER_DIR_CONSTANT = "user.dir";
	private static final String ATTRIBUTE_APPLICATION = "ApplicationURL";
	private static final String JAVASCRIPT_BORDER = "arguments[0].style.border='3px solid green'";
	private static Logger logger = null;
	private static Map<String, HashMap<String, String>> testCasesData;
	private static Map<String, String> testCaseAttributes;
	private static Map<String, String> envAttributes;
	private static Map<String, String> testrailAttributes;
	private static WebDriver driver;
	private static String applicationUrl;
	private static File fileObj;
	private static String environment;
	private static final String WARNING = "**********************WARNING*********************";
	private static Long waitTime;
	private static String defaultTimeout;
	private static ChromeOptions chromeOptions;
	private static String browser;
	public static WebDriverWait wait;
	public static JavascriptExecutor jsExecutor;
	private static Actions builder;
	private static Robot robot;
	private static SoftAssert softAssert;
	private static String parentWindowHandle;
	private static String currentMethodName;
	private static boolean isLocalSuite;
	private static String suiteName;
	public static final String HUB_URL = "http://hub:4444/wd/hub";
	private static boolean remoteWebDriver = false;
	public static boolean isUserLoggedIn = false;

	public static boolean isRemoteWebDriver() {
		return remoteWebDriver;
	}

	/**
	 * This method will initialize the objects for Automation suite.
	 * 
	 * @param context Context
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void init(ITestContext context) {
		setupLogger();
		initWebDriver(context);
		setupDefaultTimeout(context);
		setupSoftAssert();
		validateSuite(context);
	}

	/**
	 * This method will set up the default timeout in seconds for wait operations.
	 * 
	 * @param context Context
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void setupDefaultTimeout(ITestContext context) {
		defaultTimeout = context.getCurrentXmlTest().getParameter("defaultTimeout");
		if (defaultTimeout == null || defaultTimeout.equals("${defaultTimeout}")) {
			defaultTimeout = DEFAULT_TIMEOUT;
		} else {
			defaultTimeout = context.getCurrentXmlTest().getParameter(("defaultTimeout"));
		}
		log("The default timeout is set for waiting operations : " + defaultTimeout);
		waitTime = Long.parseLong(defaultTimeout);
	}

	/**
	 * This method is used to setup the webdriver wait instance.
	 * 
	 * @author spandit
	 * @lastmodifiedby kdave
	 */
	public static void setupWebdriverWait(int waitTimeInSeconds) {
	    wait = new WebDriverWait(driver, waitTimeInSeconds);
	}

	/**
	 * This method is used to setup the JavaScript Executor Instance.
	 * 
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void setupJavaScriptExecutor() {
		jsExecutor = (JavascriptExecutor) driver;
	}
	 static {
	        setupLogger();
	    }
	/**
	 * This method will set up the log4j logger
	 * 
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void setupLogger() {
        logger = LogManager.getLogger(UtilitiesCommon.class);
	}
		/**
	 * This method is used to initialize the object for Actions class
	 * 
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void setupActionsBuilder() {
		builder = new Actions(driver);
	}
	
	 public static void clickWithMouseHover(WebElement element) {
	        Actions actions = new Actions(driver);
	        actions.moveToElement(element).click().perform();
	    }
	/**	
	 * This method is used to initialize the object for SoftAssert class
	 * 
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void setupSoftAssert() {
		softAssert = new SoftAssert();
	}
	/**
	 * This method will log the message in console as well as in allure report.
	 * 
	 * @param message Message
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	@Step("LOG : {0}")
	public static void log(String message) {
		logger.info(message);
	}

	/**
	 * This method will return the decrypted password
	 * 
	 * @param encryptedPassword Encrypted Password
	 * @return Decrypted Password
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static String getDecryptedPassword(String encryptedPassword) {
		return EncryptDecrypt.decryptPassword(encryptedPassword);
	}

	public static void scrolltillpageend() {
		JavascriptExecutor js1 = (JavascriptExecutor) driver;
		js1.executeScript("window.scrollTo(0, document.body.scrollHeight)");
	}
	
	/**
	 * This method is used to read .yaml files
	 * 
	 * @param fileName Name of the yaml file
	 * @return File content in Key and Value pair
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	private static HashMap<String, Object> readYamlFile(String fileName) {
		Yaml yaml = new Yaml();
		String filePath = System.getProperty(USER_DIR_CONSTANT) + File.separator + "src" + File.separator + "test"
				+ File.separator + "resources" + File.separator + "TestData" + File.separator + fileName;
		InputStream inputStream;
		try {
			inputStream = new FileInputStream(filePath);
		} catch (FileNotFoundException e) {
			throw new CustomExceptions(
					"The Specified file : " + fileName + " is not present in specified file Path : " + filePath);
		}
		return yaml.load(inputStream);
	}

	/**
	 * This method is used to read Environment details from Environments.yaml and
	 * store it in map
	 * 
	 * @param context Context
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void readEnvironmentData(ITestContext context) {
		HashMap<String, Object> result = readYamlFile("Environments.yaml");
		envAttributes = new HashMap<>();
		environment = context.getCurrentXmlTest().getParameter("environment");
		if (environment == null || environment.equals("${environment}")) {
			environment = DEFAULT_ENVIRONMENT.toUpperCase();
		} else {
			environment = context.getCurrentXmlTest().getParameter("environment").toUpperCase();
		}
		log("Reading the Environment Details for : " + environment);
		if (result.containsKey(environment)) {
			HashMap<String, String> envDetails = (HashMap<String, String>) result.get(environment);
			for (Entry<String, String> entry : envDetails.entrySet()) {
				envAttributes.put(entry.getKey(), entry.getValue());
			}
		} else {
			throw new CustomExceptions(
					"Invalid Environment detail is present in testng xml file or Environments.yaml file : "
							+ environment);
		}
	}	
	/**
	 * This method is used to read Test Class Data from TestData.yaml and store it in map
	 * @param testClass Test Class
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void loadTestClassData(ITestClass testClass) {
		String className = testClass.getRealClass().getSimpleName();
		log("Loading Test data for Class : " + className);
		testCasesData = new HashMap<>();
		Map<String, Object> classData = readYamlFile("TestData.yaml");
		List<HashMap<String, HashMap<String, String>>> testCaseDataList = (List<HashMap<String, HashMap<String, String>>>) classData
				.get(className);
		if (testCaseDataList != null && !testCaseDataList.isEmpty()) {
			for (HashMap<String, HashMap<String, String>> testCase : testCaseDataList) {
				for (Entry<String, HashMap<String, String>> entry : testCase.entrySet()) {
					testCasesData.put(entry.getKey(), entry.getValue());
				}
			}
		} else {
			throw new CustomExceptions("Test Data is not present in TestData.yaml for Class : " + className);
		}
	}
	/**
	 * This method is used to read Test Case Data from TestData.yaml and store it in map
	 * @param result ITestResult
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void loadTestCaseData(ITestResult result) {
		currentMethodName = result.getMethod().getMethodName();
		log("Loading Test data for Method : " + currentMethodName);
		testCaseAttributes = new HashMap<>();
		if (testCasesData.containsKey(currentMethodName)) {
			if (testCasesData.get(currentMethodName) != null) {
				for (Entry<String, String> entry : testCasesData.get(currentMethodName).entrySet()) {
					testCaseAttributes.put(entry.getKey(), entry.getValue());
				}
			}
		} else {
			log("Terminating the Screen Recording");
			ScriptExecutionRecorder.setScreenRecorderStatus(false);
			throw new CustomExceptions(
					"Test Case Data is not present in TestData.yaml for Method : " + currentMethodName);
		}
	}

	/**
	 * This method is used to set up the web driver using Bonigarcia.
	 * @param context Context
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void initWebDriver(ITestContext context) {
		browser = context.getCurrentXmlTest().getParameter("browser");
		if (browser == null || browser.equals("${browser}")) {
			browser = DEFAULT_BROWSER;
		} else {
			browser = context.getCurrentXmlTest().getParameter("browser");
		}
		if ("firefox".equalsIgnoreCase(browser)) {
			WebDriverManager.firefoxdriver().setup();
		}
	}

	/**
	 * This method is used to add arguments for chrome options
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	private static void createChromeOptions() {
		chromeOptions = new ChromeOptions();
		if (SystemUtils.IS_OS_MAC) {
			chromeOptions.addArguments("start-fullscreen");
		} else {
			chromeOptions.addArguments("start-maximized");
		}
		try {
			HttpURLConnection con = (HttpURLConnection) new URL(HUB_URL + "/status").openConnection();
			try {
				con.setRequestMethod("GET");
				remoteWebDriver = con.getResponseCode() == HttpURLConnection.HTTP_OK;
				UtilitiesCommon.log("Connection status of HUB: " + remoteWebDriver);
			} finally {
				con.disconnect();
			}
		} catch (IOException ignore) {
		}

		if (!remoteWebDriver) {
			UtilitiesCommon.log("Initializing Web Driver for Local Execution .....");
			WebDriverManager.chromedriver().setup();
			Map<String, Object> preferences = new HashMap<String, Object>();
			preferences.put("download.default_directory",
					System.getProperty(USER_DIR_CONSTANT) + File.separator + "src" + File.separator + "test"
							+ File.separator + "resources" + File.separator + "TestData" + File.separator
							+ "TestDataDownload");
			chromeOptions.setExperimentalOption("prefs", preferences);
		  //run test with headless mode for git actions
			//chromeOptions.addArguments("--headless");

			driver = new ChromeDriver(chromeOptions);
		}

		if (remoteWebDriver) {
			UtilitiesCommon.log("Initializing Test case in docker container .....");
			chromeOptions.addArguments("--disable-gpu");
			chromeOptions.addArguments("--headless");
			chromeOptions.addArguments("--no-sandbox");
			chromeOptions.addArguments("--disable-setuid-sandbox");
			chromeOptions.addArguments("--disable-dev-shm-usage");
			chromeOptions.addArguments("--disable-extensions");
			if (SystemUtils.IS_OS_MAC) {
				chromeOptions.addArguments("start-fullscreen");
			} else {
				chromeOptions.addArguments("--window-size=1400,900");
			}
			chromeOptions.addArguments("disable-infobars");

			try {
				driver = new RemoteWebDriver(new URL(HUB_URL), chromeOptions);
			} catch (MalformedURLException e) {
				throw new CustomExceptions("URL is bad: " + e.getStackTrace());
			}
		}
		chromeOptions.addArguments("incognito");
	}

	/**
	 * This method is used to get the web driver
	 * @return driver
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static WebDriver getDriver() {
		return driver;
	}

	/**
	 * This method is used to get the Javascript Executor
	 * @return javascript executor
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static JavascriptExecutor getJavascriptExecutor() {
		return jsExecutor;
	}

	/**
	 * This method is used to open browser and initialize webdriver wait, javascript executor, Action builder and then launch the application and starts screen recording.
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void launchApplication() {
		log(browser + " browser is initialized");
		if ("chrome".equalsIgnoreCase(browser)) {
			createChromeOptions();
		} else {
			driver = new FirefoxDriver();
			driver.manage().window().maximize();
		}
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		//setupWebdriverWait(60);
		setupJavaScriptExecutor();
		setupActionsBuilder();
		applicationUrl = UtilitiesCommon.getEnvironmentData(ATTRIBUTE_APPLICATION);
		log("Launching Application URL : " + applicationUrl);
		driver.get(applicationUrl);
		if (!remoteWebDriver) {
			startScreenRecording(currentMethodName);
		}
	}

	/**
	 * This method is used to open url on same browser.
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void openUrl() {
		driver.get(applicationUrl);
	}

	/**
	 * This method is used to Logout from the application.
	 * 
	 * @author spandit
	 * @lastmodifiedby kdave
	 */
	public static void applicationLogout() {
		// navigateToPage("https://staging2.bateel.com/en_in/");
		// HomePage.logout();
	}

	/**
	 * This method is used to close the web browser
	 * 
	 * @author spandit
	 * @lastmodifiedby kdave
	 */
	public static void closeDriver() {
//		if (driver != null) {
//			log("Closing Browser");
//			driver.quit();
//			driver = null;
//		} else {
//			log("Web Driver is NULL and it has not initialized properly");
//		}
	}

	/**
	 * This method is used to verify that the attribute is present in Test Case Data map and then returns its value.
	 * @param attributeKey Attribute Key
	 * @return attributeValue Attribute Value
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static String getTestData(String attributeKey) {
		verifyAttribute(testCaseAttributes, attributeKey,
				"Attribute is not present in Test case data in TestData.yaml : ");
		return testCaseAttributes.get(attributeKey);
	}

	/**
	 * This method is used to set the Test Data to map
	 * @param attributeKey Attribute Key
	 * @param value        Value
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void setTestData(String attributeKey, String value) {
		testCaseAttributes.put(attributeKey, value);
	}

	/**
	 * This method is used to verify that the attribute is present Environment Data map and then returns its value.
	 * @param attributeKey Attribute Key
	 * @return attributeValue Attribute Value
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static String getEnvironmentData(String attributeKey) {
		verifyAttribute(envAttributes, attributeKey, "Environment Attribute is not present for Environment - "
				+ environment + " in Environment.yaml file : " + attributeKey);
		return envAttributes.get(attributeKey);
	}

	/**
	 * This method is used to set the Environment Data to map
	 * @param attributeKey Attribute Key
	 * @param value Value
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void setEnvironmentData(String attributeKey, String value) {
		envAttributes.put(attributeKey, value);
	}

	/**
	 * This method is used to verify the specified attribute is present in given map.
	 * @param dataMap Data Map
	 * @param attributeKey Attribute Key
	 * @param message Error Message
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void verifyAttribute(Map<String, String> dataMap, String attributeKey, String message) {
		if (!dataMap.containsKey(attributeKey)) {
			throw new CustomExceptions(message + attributeKey);
		}
	}

	/**
	 * This method is used to navigate to the specified page
	 * @param pageUrl Page URL
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void navigateToPage(String pageUrl) {
		driver.get(pageUrl);
	}

	/**
	 * This method is used to get the Application Url
	 * @return applicationUrl
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static String getApplicationUrl() {
		return applicationUrl;
	}

	/**
	 * This method will return locator of the web element.
	 * @param enumValue Enum Value
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static By getLocator(Enum<?> enumValue) {
		By locator;
		String enumKey = enumValue.name();
		String locatorType = enumKey.substring(enumKey.lastIndexOf("_") + 1);

		switch (locatorType) {
		case "ID":
			locator = By.id(enumValue.toString());
			return locator;
		case "NAME":
			locator = By.name(enumValue.toString());
			return locator;
		case "CLASSNAME":
			locator = By.className(enumValue.toString());
			return locator;
		case "LINKTEXT":
			locator = By.linkText(enumValue.toString());
			return locator;
		case "PARTIALLINKTEXT":
			locator = By.partialLinkText(enumValue.toString());
			return locator;
		case "XPATH":
			locator = By.xpath(enumValue.toString());
			return locator;
		case "CSS":
			locator = By.cssSelector(enumValue.toString());
			return locator;
		case "TAGNAME":
			locator = By.tagName(enumValue.toString());
			return locator;
		default:
			throw new CustomExceptions(
					"Locator " + enumValue.name() + " is not correct at " + enumValue.getDeclaringClass());
		}
	}

	/**
	 * This method will return web element available on the web page.
	 * @param enumValue Enum Value
	 * @return webElement
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static WebElement getElement(Enum<?> enumValue) {
	    By locator = getLocator(enumValue);
	    if (locator == null) {
	        throw new IllegalArgumentException("Locator is null for enum value: " + enumValue);
	    }
	    waitForElementIsVisible(locator);
	    return driver.findElement(locator);
	}

	/**
	 * This method will return list of web elements available on the web page.
	 * @param enumValue Enum Value
	 * @return WebElementList
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static List<WebElement> getElements(Enum<?> enumValue) {
	    By locator = getLocator(enumValue);
	    if (locator == null) {
	        throw new IllegalArgumentException("Locator is null for enum value: " + enumValue);
	    }
	    return driver.findElements(locator);
	}

	/**
	 * This method will return web element text
	 * @param enumValue Enum Value
	 * @return ElementText
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static String getElementText(Enum<?> enumValue) {
	    try {
	        WebElement element = getElement(enumValue);
	        return element.getText();
	    } catch (NoSuchElementException e) {
	        System.out.println("Element not found for enum value: " + enumValue);
	        return null; // or you can return an empty string or handle it as per your requirement
	    }
	}

	/**
	 * This method will return web elements text
	 * @param elementList Element List
	 * @return ElementTextList
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static List<String> getElementsText(List<WebElement> elementList) {
		List<String> elementTextsList = new LinkedList<>();
		for (WebElement element : elementList) {
			elementTextsList.add(element.getText());
		}
		return elementTextsList;
	}

	/**
	 * This method will return specified web element attribute value.
	 * @param enumValue Enum Value
	 * @param attributeName Attribute Name
	 * @return Attribute Value
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static String getElementAttribute(Enum<?> enumValue, String attributeName) {
		WebElement element = getElement(enumValue);
		executeJS(JAVASCRIPT_BORDER, element);
		return element.getAttribute(attributeName);
	}

	/**
	 * This method will return web element dynamically using specified dynamicValue.
	 * @param enumValue    Enum Value
	 * @param dynamicValue Dynamic Value
	 * @return webElement
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static WebElement getDynamicElement(Enum<?> enumValue, String dynamicValue) {
		String xpathExpression = generateDynamicXpath(enumValue.toString(), dynamicValue);
		wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(xpathExpression)));
		return driver.findElement(By.xpath(xpathExpression));
	}

	/**
	 * This method will return List of web elements dynamically using specified dynamicValue.
	 * @param enumValue    Enum Value
	 * @param dynamicValue Dynamic Value
	 * @return webElementList
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static List<WebElement> getDynamicElements(Enum<?> enumValue, String dynamicValue) {
		String xpathExpression = generateDynamicXpath(enumValue.toString(), dynamicValue);
		wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(xpathExpression)));
		return driver.findElements(By.xpath(xpathExpression));
	}
	
	public static void waitForElementIsClickable(Enum<?> enumValue) {
	    By locator = getLocator(enumValue);
	    if (locator == null) {
	        throw new IllegalArgumentException("Locator is null for enum value: " + enumValue);
	    }
	    long timeoutSeconds = Long.parseLong(DEFAULT_TIMEOUT);
	    WebDriverWait wait = new WebDriverWait(driver, timeoutSeconds);
	    wait.until(ExpectedConditions.elementToBeClickable(locator));
	}
	
	/**
	 * This method is used to generate the Dynamic Xpath
	 * @param xpath        XPATH
	 * @param dynamicValue Dynamic Value
	 * @return String xpathExpression
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static String generateDynamicXpath(String xpath, String dynamicValue) {
		return String.format(xpath, dynamicValue);
	}

	/**
	 * This method will perform click operation on the element available on the web
	 * page.
	 * 
	 * @param enumValue Enum Value
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void click(Enum<?> enumValue) {
		WebElement element = getElement(enumValue);
		waitForElementIsClickable(element);
		executeJS(JAVASCRIPT_BORDER, element);
		element.click();
	}
	public static boolean clickAndVerifyClickable(BateelLoginCheckoutEnum enumValue) {
        try {
            click(enumValue);
            return true;
        } catch (AssertionError e) {
            return false;
        }
    }

	/**
	 * This method will perform click operation on the element available on the web
	 * page by using dynamic xpath.
	 * 
	 * @param enumValue    Enum Value
	 * @param dynamicValue Dynamic Value
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void clickOnDynamicElement(Enum<?> enumValue, String dynamicValue) {
		WebElement element = getDynamicElement(enumValue, dynamicValue);
		waitForElementIsClickable(element);
		executeJS(JAVASCRIPT_BORDER, element);
		element.click();
	}

	/**
	 * This method will enter the specified value in the text field.
	 * 
	 * @param enumValue  Enum Value
	 * @param inputValue Input Value
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void enterValue(Enum<?> enumValue, String inputValue) {
		WebElement element = getElement(enumValue);
		waitForElementIsClickable(element);
		executeJS(JAVASCRIPT_BORDER, element);
		element.clear();
		element.sendKeys(inputValue);
	}

	/**
	 * This method will enter the specified value in the text field by using dynamic
	 * xpath.
	 * 
	 * @param enumValue    Enum Value
	 * @param inputValue   Input Value
	 * @param dynamicValue Dynamic Value
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void enterValueForDynamicElement(Enum<?> enumValue, String inputValue, String dynamicValue) {
		WebElement element = getDynamicElement(enumValue, dynamicValue);
		waitForElementIsClickable(element);
		executeJS(JAVASCRIPT_BORDER, element);
		element.clear();
		element.sendKeys(inputValue);
	}

	/**
	 * This method is used to select dropdown value by Text - This will used for
	 * select tags
	 * 
	 * @param enumValue    Enum Value
	 * @param selectOption Select Options
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void selectByVisibleText(Enum<?> enumValue, String selectOption) {
		WebElement element = getElement(enumValue);
		Select select = new Select(element);
		select.selectByVisibleText(selectOption);
	}

	/**
	 * This method is used to get All drowpdown options Text - This will used for
	 * select tags
	 * 
	 * @param enumValue Enum Value
	 * @return SelectOptionsText
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static List<String> getSelectOptionsText(Enum<?> enumValue) {
		WebElement element = getElement(enumValue);
		Select select = new Select(element);
		return getElementsText(select.getOptions());
	}

	/**
	 * This method is used to wait until web element is clickable
	 * 
	 * @param element Web Element
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void waitForElementIsClickable(WebElement element) {
		wait.until(ExpectedConditions.elementToBeClickable(element));
	}

	/**
	 * This method is used to wait until web element is located on DOM and visible
	 * 
	 * @param locator Locator
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	private static void waitForElementIsVisible(By locator) {
		wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}

	/**
	 * This is used to check the web element is enabled or not
	 * 
	 * @param enumValue Enum Value
	 * @return boolean
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static boolean isElementEnabled(Enum<?> enumValue) {
		WebElement element = getElement(enumValue);
		return element.isEnabled();
	}

	/**
	 * This is used to check the web element is present or not
	 * 
	 * @param enumValue Enum Value
	 * @return boolean
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static boolean isElementPresent(Enum<?> enumValue) {
		WebElement element = getElement(enumValue);
		return element.isDisplayed();
	}
	
	public static boolean isElementPresent(By locator) {
	    try {
	        driver.findElement(locator);
	        return true;
	    } catch (NoSuchElementException e) {
	        return false;
	    }
	}

	/**
	 * This is used to check the web element is selected or not
	 * 
	 * @param enumValue Enum Value
	 * @return boolean
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static boolean isElementSelected(Enum<?> enumValue) {
		WebElement element = getElement(enumValue);
		return element.isSelected();
	}

	/**
	 * This method will perform click operation on the element available on the web
	 * page using javascript executor.
	 * 
	 * @param enumValue Enum Value
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void javaScriptClick(Enum<?> enumValue) {
	    WebElement element = getElement(enumValue);
	    executeJS("arguments[0].click();", element);
	}
	/**
	 * This method will enter the specified value in the text field using javascript
	 * executor.
	 * 
	 * @param enumValue  Enum Value
	 * @param inputValue Input Value
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void javaScriptEnterValue(Enum<?> enumValue, String inputValue) {
		WebElement element = getElement(enumValue);
		waitForElementIsClickable(element);
		element.click();
		executeJS("arguments[0].setAttribute('value', '" + inputValue + "')", element);
	}

	/**
	 * This method will scroll to the specified web element using javascript
	 * executor.
	 * 
	 * @param enumValue Enum Value
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void javaScriptWaitAndScroll(Enum<?> enumValue) {
		WebElement element = getElement(enumValue);
		executeJS("arguments[0].scrollIntoView(true);", element);
	}

	/**
	 * This method will scroll to the Dynamically generated web element using
	 * javascript executor.
	 * 
	 * @param enumValue    Enum Value
	 * @param dynamicValue Dynamic Value
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void javaScriptScrollDynamicElement(Enum<?> enumValue, String dynamicValue) {
		WebElement element = getDynamicElement(enumValue, dynamicValue);
		executeJS("arguments[0].scrollIntoView(true);", element);
	}

	/**
	 * This method will Click to the Dynamically generated web element using
	 * javascript executor.
	 * 
	 * @param enumValue    Enum Value
	 * @param dynamicValue Dynamic Value
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void javaScriptClickDynamicElement(Enum<?> enumValue, String dynamicValue) {
		WebElement element = getDynamicElement(enumValue, dynamicValue);
		executeJS("arguments[0].click();", element);
	}

	/**
	 * This method will execute the java scripts.
	 * 
	 * @param script  Java Script
	 * @param element Web Element
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void executeJS(String script, WebElement element) {
		try {
			jsExecutor.executeScript(script, element);
		} catch (JavascriptException e) {
			throw new CustomExceptions("Failed in the JavaScript execution : " + e.getMessage());
		}
	}

	/**
	 * This method will perform mouse hover action on an element
	 * 
	 * @param enumValue Enum Value
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void hoverOverElement(Enum<?> enumValue) {
		WebElement elementTobeHovered = getElement(enumValue);
		builder.moveToElement(elementTobeHovered).build().perform();
	}

	/**
	 * This method will perform mouse hover action on an dynamically generated
	 * element.
	 * 
	 * @param enumValue    Enum Value
	 * @param dynamicValue Dynamic Value
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void hoverOverDynamicElement(Enum<?> enumValue, String dynamicValue) {
		WebElement elementTobeHovered = getDynamicElement(enumValue, dynamicValue);
		builder.moveToElement(elementTobeHovered).build().perform();
	}

	/**
	 * This method will perform double click on an element.
	 * 
	 * @param enumValue Enum Value
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void doubleClick(Enum<?> enumValue) {
		WebElement elementTobeDoubleClicked = getElement(enumValue);
		builder.doubleClick(elementTobeDoubleClicked).perform();
	}

	/**
	 * This method will perform right click action on an element.
	 * 
	 * @param enumValue Enum Value
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void rightClick(Enum<?> enumValue) {
		WebElement elementTobeRightClicked = getElement(enumValue);
		builder.contextClick(elementTobeRightClicked).perform();
	}

	/**
	 * This method will perform drag and drop action on an element.
	 * 
	 * @param sourceEnumValue      Enum Value
	 * @param destinationEnumValue Enum Value
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void dragAndDropToElement(Enum<?> sourceEnumValue, Enum<?> destinationEnumValue) {
		WebElement source = getElement(sourceEnumValue);
		WebElement destination = getElement(destinationEnumValue);
		builder.dragAndDrop(source, destination).build().perform();
	}

	/**
	 * This method will perform drag and drop action on an dynamically generated
	 * element.
	 * 
	 * @param sourceEnumValue         Enum Value
	 * @param dynamicSourceValue      Dynamic Value
	 * @param destinationEnumValue    Enum Value
	 * @param dynamicDestinationValue Dynamic Value
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void dragAndDropToDynamicElement(Enum<?> sourceEnumValue, String dynamicSourceValue,
			Enum<?> destinationEnumValue, String dynamicDestinationValue) {
		WebElement source = getDynamicElement(sourceEnumValue, dynamicSourceValue);
		WebElement destination = getDynamicElement(destinationEnumValue, dynamicDestinationValue);
		builder.dragAndDrop(source, destination).build().perform();
	}

	/**
	 * This method will perform browser search for specified String.
	 * 
	 * @param searchText Search Text
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void searchInBrowser(String searchText) {
		try {
			robot = new Robot();
			robot.delay(3000);
			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_F);
			robot.keyRelease(KeyEvent.VK_F);
			robot.keyRelease(KeyEvent.VK_CONTROL);
			robot.delay(1000);
			StringSelection stringSelection = new StringSelection(searchText);
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(stringSelection, stringSelection);
			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);
			robot.delay(1000);
		} catch (AWTException e) {
			throw new CustomExceptions("Failed to Search the text using Browser Search : " + e.getMessage());
		}
	}

	/**
	 * This method will compare and assert the expected and actual text
	 * 
	 * @param enumValue    Enum Value
	 * @param expectedText Expected Text
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void verifyText(Enum<?> enumValue, String expectedText) {
		String actualText = getElementAttribute(enumValue, "innerText");
		Assertions.assertThat(actualText.trim()).isEqualTo(expectedText);
	}

	/**
	 * This method will compare and Soft assert the expected and actual text
	 * 
	 * @param enumValue    Enum Value
	 * @param expectedText Expected Text
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void softAssertText(Enum<?> enumValue, String expectedText) {
		String actualText = getElementText(enumValue);
		softAssert.assertEquals(actualText, expectedText);
	}

	/**
	 * This method will trigger the exception for failed assertion and needs to be
	 * used at the end of the test
	 * 
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void softAssertAll() {
		softAssert.assertAll();
	}

	/**
	 * This method will wait until the presence of element is located
	 * 
	 * @param enumValue Enum Value
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void waitForElementIsPresent(Enum<?> enumValue) {
		wait.until(ExpectedConditions.presenceOfElementLocated(getLocator(enumValue)));
	}

	/**
	 * This method will check if the element is visible
	 * 
	 * @param enumValue Enum Value
	 * @return Element is visible or not
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static boolean verifyElementIsVisible(Enum<?> enumValue) {
		boolean isElementVisible;
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(getLocator(enumValue)));
			isElementVisible = true;
		} catch (ElementNotVisibleException | TimeoutException exception) {
			isElementVisible = false;
		}
		return isElementVisible;
	}
	public static boolean waitForElementIsNotVisible(By locator) {
	    boolean isElementNotVisible;
	    try {
	        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
	        isElementNotVisible = true;
	    } catch (TimeoutException exception) {
	        isElementNotVisible = false;
	    }
	    return isElementNotVisible;
	}
	/**
	 * This method will check if the dynamically generated element is displayed.
	 * 
	 * @param enumValue    Enum Value
	 * @param dynamicValue Dynamic Value
	 * @return boolean
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static boolean verifyDynamicElementIsDisplayed(Enum<?> enumValue, String dynamicValue) {
		String xpathExpression = generateDynamicXpath(enumValue.toString(), dynamicValue);
		boolean isElementDisplayed = false;
		if (driver.findElements(By.xpath(xpathExpression)).size() > 0) {
			isElementDisplayed = true;
		}
		return isElementDisplayed;
	}

	/**
	 * This method will check if the element is displayed.
	 * 
	 * @param enumValue Enum Value
	 * @return boolean
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static boolean verifyElementIsDisplayed(Enum<?> enumValue) {
		List<WebElement> element = getElements(enumValue);
		boolean isElementDisplayed = false;
		if ((element).size() > 0) {
			isElementDisplayed = true;
		}
		return isElementDisplayed;
	}

	/**
	 * This method will check if the element is not visible.
	 * 
	 * @param enumValue Enum Value
	 * @return boolean
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static boolean verifyElementIsNotVisible(Enum<?> enumValue) {
		return wait.until(ExpectedConditions.invisibilityOfElementLocated(getLocator(enumValue)));
	}

	/**
	 * This method will check if the dynamically generated element is visible.
	 * 
	 * @param enumValue    Enum Value
	 * @param dynamicValue Dynamic Value
	 * @return boolean
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static boolean verifyDynamicElementIsVisible(Enum<?> enumValue, String dynamicValue) {
		String xpathExpression = generateDynamicXpath(enumValue.toString(), dynamicValue);
		boolean isElementVisible;
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathExpression)));
			isElementVisible = true;
		} catch (ElementNotVisibleException | TimeoutException exception) {
			isElementVisible = false;
		}
		return isElementVisible;
	}

	/**
	 * This method will check if the dynamically generated element is not visible.
	 * 
	 * @param enumValue    Enum Value
	 * @param dynamicValue Dynamic Value
	 * @return boolean
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static boolean verifyDynamicElementIsNotVisible(Enum<?> enumValue, String dynamicValue) {
		String xpathExpression = generateDynamicXpath(enumValue.toString(), dynamicValue);
		boolean isDynamicElementNotVisible;
		try {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(xpathExpression)));
			isDynamicElementNotVisible = true;
		} catch (Exception exception) {
			isDynamicElementNotVisible = false;
		}
		return isDynamicElementNotVisible;
	}

	/**
	 * This method will check if the Text Field currently has Focus.
	 * 
	 * @param enumValue Enum Value
	 * @return boolean
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static boolean verifyTextFieldFocus(Enum<?> enumValue) {
		WebElement actual = (WebElement) jsExecutor.executeScript("return document.activeElement;");
		WebElement expected = getElement(enumValue);
		return actual.equals(expected);
	}

	/**
	 * This method will switch the control to the child window
	 * 
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void switchToChildWindow() {
		parentWindowHandle = getCurrentWindowHandle();
		for (String winHandle : getChildWindowHandles()) {
			driver.switchTo().window(winHandle);
		}
	}

	/**
	 * This method will switch the control back to the parent window
	 * 
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void switchToParentWindow() {
		driver.switchTo().window(parentWindowHandle);
	}

	/**
	 * This method will fetch handle of the current window
	 * 
	 * @return Current Window Handle
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static String getCurrentWindowHandle() {
		return driver.getWindowHandle();
	}

	/**
	 * This method will fetch handles of the child windows
	 * 
	 * @return Child Window Handles
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static Set<String> getChildWindowHandles() {
		return driver.getWindowHandles();
	}

	/**
	 * This method will switch the control to a specific frame
	 * 
	 * @param enumValue Enum Value
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void switchToFrame(Enum<?> enumValue) {
		WebElement frameElement = getElement(enumValue);
		driver.switchTo().frame(frameElement);
	}

	/**
	 * This method will switch the control back to default content from iframe
	 * 
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void switchToDefault() {
		driver.switchTo().defaultContent();
	}

	/**
	 * This method is used to get current date and time in dd-MM-yyyy HH-mm-ss
	 * format
	 * 
	 * @return current Date and Time
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static String getCurrentDateTimeStamp() {
		SimpleDateFormat formDate = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
		return formDate.format(new Date());
	}

	/**
	 * This method is used to Generate 7 Digit Random number
	 * 
	 * @return RandomNumber
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static int generateRandomNumber() {
		Random rand = new Random();
		return rand.nextInt(9999999);
	}

	/**
	 * This method is used to Generate Random string data
	 * 
	 * @param value Integer Value
	 * @return RandomText
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static String generateRandomText(int value) {
		return RandomStringUtils.randomAlphabetic(value);
	}

	/**
	 * This method is used to capture a screenshot for Testrail.
	 * 
	 * @param methodName Method Name
	 * @return file
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static File captureScreenshot(String methodName) {
		fileObj = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(fileObj, new File(System.getProperty(USER_DIR_CONSTANT) + "/screenshotsAndRecordings/"
					+ methodName + "_" + getCurrentDateTimeStamp() + ".png"));
		} catch (IOException e) {
			log(WARNING);
			log("Unable to copy screenshot file to screenshotsAndRecordings : " + e.getMessage());
			log(WARNING);
		}
		return fileObj;
	}

	/**
	 * This method is used to delete the allure reports and attachments like
	 * screenshots and screen recordings.
	 * 
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void deleteAllureReportsAndAttachments() {
		deleteExistingAllureReports();
		deleteExistingAttachments();
	}

	/**
	 * This method is used to delete the old allure reports.
	 * 
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	private static void deleteExistingAllureReports() {
		File allureResults = new File(System.getProperty(USER_DIR_CONSTANT) + File.separator + "allure-results");
		if (allureResults.exists() && allureResults.isDirectory()) {
			try {
				for (File f : allureResults.listFiles()) {
					if (!f.getName().equalsIgnoreCase("environment.properties")
							&& !f.getName().equalsIgnoreCase("categories.json")) {
						FileUtils.forceDelete(f);
					}
				}
				log("Cleaned up the old allure reports !!");
			} catch (Exception e) {
				throw new CustomExceptions(
						"There is problem while deleting the old allure reports from allure-results folder."
								+ e.getMessage());
			}
		}
	}

	/**
	 * This method is used to delete the old Screen Recordings and Screen shots.
	 * 
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	private static void deleteExistingAttachments() {
		File scriptRecordings = new File(
				System.getProperty(USER_DIR_CONSTANT) + File.separator + "screenshotsAndRecordings");
		if (scriptRecordings.exists() && scriptRecordings.isDirectory()) {
			try {
				for (File f : scriptRecordings.listFiles()) {
					FileUtils.forceDelete(f);
				}
				log("Cleaned up the old Screen Recordings and Screenshots !!");
			} catch (Exception e) {
				throw new CustomExceptions(
						"There is problem while deleting the old screen recordings and screenshots from screenshotsAndRecordings folder."
								+ e.getMessage());
			}
		}
	}

	/**
	 * This method is used to delete the Screen Recording of Passed Test case.
	 * 
	 * @param result ITestResult
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void deletePassedTestCaseRecording(ITestResult result) {
		File scriptRecordings = new File(
				System.getProperty(USER_DIR_CONSTANT) + File.separator + "screenshotsAndRecordings");
		if (scriptRecordings.exists() && scriptRecordings.isDirectory()) {
			try {
				for (File f : scriptRecordings.listFiles()) {
					if (f.getName().contains(result.getName())) {
						FileUtils.forceDelete(f);
						log("Successfully Deleted Screen Recording of Passed Test Case : " + result.getName());
					}
				}
			} catch (Exception e) {
				throw new CustomExceptions(
						"There is a problem while deleting screen recording of passed test case from screenshotsAndRecordings folder."
								+ e.getMessage());
			}
		}
	}

	/**
	 * This method is used to set the environment details in allure report.
	 * 
	 * @param context ITestContext
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void setAllureEnvironment(ITestContext context) {
		suiteName = context.getCurrentXmlTest().getSuite().getName();
		if (!"Default Suite".equalsIgnoreCase(suiteName)) {
			browser = context.getCurrentXmlTest().getParameter("browser");
			environment = context.getCurrentXmlTest().getParameter("environment");
			if (browser == null || browser.equals("${browser}")) {
				browser = DEFAULT_BROWSER;
			}
			if (environment == null || environment.equals("${environment}")) {
				environment = DEFAULT_ENVIRONMENT;
			}
			browser = browser.toLowerCase();
			environment = environment.toLowerCase();
			String filePath = System.getProperty(USER_DIR_CONSTANT) + File.separator + "allure-results" + File.separator
					+ "environment.properties";
			Properties props = new Properties();
			try (FileInputStream in = new FileInputStream(filePath)) {
				props.load(in);
			} catch (FileNotFoundException e1) {
				throw new CustomExceptions("Allure Environment property file is not present in allure-results folder.");
			} catch (IOException e1) {
				throw new CustomExceptions("There is problem while loading the environment.properties file.");
			}
			try (FileOutputStream out = new FileOutputStream(filePath)) {
				props.setProperty("Browser", browser.substring(0, 1).toUpperCase() + browser.substring(1));
				props.setProperty("Environment", environment.substring(0, 1).toUpperCase() + environment.substring(1));
				props.setProperty(ATTRIBUTE_APPLICATION, getEnvironmentData(ATTRIBUTE_APPLICATION));
				props.setProperty("OperatingSystem", System.getProperty("os.name"));
				props.store(out, null);
			} catch (FileNotFoundException e1) {
				throw new CustomExceptions("Allure Environment property file is not present in allure-results folder.");
			} catch (IOException e1) {
				throw new CustomExceptions("There is problem while loading the environment.properties file.");
			}
		}
	}

	/**
	 * This method is used to take a screenshot and attach it to allure report.
	 * 
	 * @param methodName Method Name
	 * @return Byte array
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	@Attachment(value = "{0}", type = "image/png")
	public static byte[] captureAllureScreenshot(String methodName) {
		if (driver != null) {
			return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
		} else {
			return new byte[0];
		}
	}

	/**
	 * This method is used to attach the recording of execution flow of test case to
	 * allure report.
	 * 
	 * @param result ITestResult
	 * @return Byte array
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	@Attachment(value = "Script Recording", type = "video/avi")
	@Step("Screen recording of Test case flow")
	public static byte[] attachScreenRecording(ITestResult result) {
		if (ScriptExecutionRecorder.isRecordingCompleted()) {
			String filePath = System.getProperty(USER_DIR_CONSTANT) + File.separator + "screenshotsAndRecordings"
					+ File.separator + result.getMethod().getMethodName() + ".avi";
			try {
				return FileUtils.readFileToByteArray(new File(filePath));
			} catch (IOException e) {
				log("Error while attaching screen recording: " + e.getMessage());
				return new byte[0];
			}
		} else {
			return new byte[0];
		}
	}

	/**
	 * This method is used to start the recording of execution flow of test case.
	 * 
	 * @param methodName Method Name
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void startScreenRecording(String methodName) {
		try {
			ScriptExecutionRecorder.startRecord(methodName);
		} catch (IOException | AWTException e) {
			log("Error in starting recording: " + e.getMessage());
		}
	}

	/**
	 * This method is used to stop the recording of execution flow of test case.
	 * 
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void stopScreenRecording() {
		if (ScriptExecutionRecorder.getScreenRecorder() != null) {
			try {
				ScriptExecutionRecorder.stopRecord();
			} catch (IOException e) {
				log("Error in stopping recording: " + e.getMessage());
			}
		}
	}

	/**
	 * This method is used to set the Testrail Test Case name corresponding to the
	 * currently executing Test Method in Allure Report.
	 * 
	 * @param result ITestResult
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	@Test
	public static void setTestCaseNameInAllure(ITestResult result) {
		Method method = result.getMethod().getConstructorOrMethod().getMethod();
		if (method.isAnnotationPresent(Test.class) && !method.getAnnotation(Test.class).testName().isBlank()) {
			Test test = method.getAnnotation(Test.class);
			AllureLifecycle lifecycle = Allure.getLifecycle();
			lifecycle.updateTestCase(testResult -> testResult.setName(test.testName()));
		} else {
			log("testName property is missing in @Test annotation of method : " + result.getName() + "in Test Class "
					+ result.getTestClass().getName());
		}
	}

	/**
	 * This method will validate if the Suite is Local, Sanity or Regression Suite
	 * 
	 * @param context ITestContext
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void validateSuite(ITestContext context) {
		suiteName = context.getCurrentXmlTest().getSuite().getName();
		log("Suite Name = " + suiteName);
		if (suiteName.equals("Sanity Test Suite") || suiteName.equals("Regression Test Suite")) {
			isLocalSuite = false;
		} else {
			isLocalSuite = true;
		}
	}

	/**
	 * This method is used to generate allure report automatically for windows,
	 * Linux OS and Mac OS.
	 * 
	 * @param context ITestContext
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void generateAllureReport(ITestContext context) {
		if (!"Default Suite".equalsIgnoreCase(context.getSuite().getName()) && !remoteWebDriver) {
			if (SystemUtils.IS_OS_WINDOWS) {
				try {
					Runtime.getRuntime().exec("cmd /c start cmd.exe /K \"allure serve allure-results\"");
				} catch (IOException e) {
					throw new CustomExceptions("Failed to generate allure report: " + e.getMessage());
				}
			} else if (SystemUtils.IS_OS_LINUX) {
				try {
					String[] args = new String[] { "/bin/bash", "-c", "allure serve allure-results" };
					new ProcessBuilder(args).start();
				} catch (IOException e) {
					throw new CustomExceptions("Failed to generate allure report: " + e.getMessage());
				}
			} else if (SystemUtils.IS_OS_MAC) {
				try {
					String[] args = new String[] { "/bin/zsh", "-c", "allure serve allure-results" };
					new ProcessBuilder(args).start();
				} catch (IOException e) {
					throw new CustomExceptions("Failed to generate allure report: " + e.getMessage());
				}
			} else {
				log("Allure report needs to be manually generated for now");
			}
		}
	}

	/**
	 * This method will accept the Alert message.
	 * 
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void acceptAlert() {
		waitUntilAlertIsVisible();
		driver.switchTo().alert().accept();
	}

	/**
	 * This method is used to wait until alert is visible.
	 * 
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	private static void waitUntilAlertIsVisible() {
		wait.until(ExpectedConditions.alertIsPresent());
	}

	/**
	 * This method is used to refresh the page.
	 * 
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void refreshPage() {
		driver.navigate().refresh();
	}

	/**
	 * This method is used to upload file.
	 * 
	 * @param enumValue Enum Value
	 * @param filePath  File Path
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void uploadFile(Enum<?> enumValue, String filePath) {
		WebElement element = getElement(enumValue);
		if (remoteWebDriver) {
			((RemoteWebElement) element).setFileDetector(new LocalFileDetector());
		}
		element.sendKeys(filePath);
		log("File has been uploaded. File Path: " + filePath);
	}

	/**
	 * This method is used to create test file at
	 * src\test\resources\TestData\TestDataUpload folder.
	 * 
	 * @param fileName Name of the file
	 * @param fileType File Extension
	 * @return File Path
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static String createTestFile(String fileName, String fileType) {
		String folderPath = System.getProperty(USER_DIR_CONSTANT) + File.separator + "src" + File.separator + "test"
				+ File.separator + "resources" + File.separator + "TestData" + File.separator + "TestDataUpload";
		String filePath = folderPath + File.separator + fileName + "_" + generateRandomNumber() + "." + fileType;
		File file = new File(filePath);
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		log("File has been created successfully. File Path is : " + filePath);
		return filePath;
	}

	/**
	 * This method is used to delete test file at specified location.
	 * 
	 * @param filePath File Path
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void deleteTestFile(String filePath) {
		File file = new File(filePath);
		file.delete();
		log("File has been deleted. File Path: " + filePath);
	}

	/**
	 * This method is used to hit Enter Key.
	 * 
	 * @param enumValue Enum Value
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void hitEnterKey(Enum<?> enumValue) {
		WebElement element = getElement(enumValue);
		element.sendKeys(Keys.ENTER);
	}

	/**
	 * This method is used to hit Tab Key.
	 * 
	 * @param enumValue Enum Value
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void hitTabKey(Enum<?> enumValue) {
		WebElement element = getElement(enumValue);
		element.sendKeys(Keys.TAB);
	}

	/**
	 * This method is used to verify if element List is sorted alphabetically.
	 * 
	 * @param enumValue Enum Value
	 * @return boolean isSorted
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static boolean verifyElementListIsSorted(Enum<?> enumValue) {
		List<WebElement> elements = UtilitiesCommon.getElements(enumValue);
		List<String> elementsText = UtilitiesCommon.getElementsText(elements);
		boolean isSorted = Ordering.allEqual().isOrdered(elementsText);
		return isSorted;
	}

	/**
	 * This method is used to verify if file has been downloaded successfully.
	 * 
	 * @param fileName File Name
	 * @param fileType File Extension
	 * @return boolean fileDownloaded
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static boolean isFileDownloaded(String fileName, String fileType) {
		String folderPath = System.getProperty(USER_DIR_CONSTANT) + File.separator + "src" + File.separator + "test"
				+ File.separator + "resources" + File.separator + "TestData" + File.separator + "TestDataDownload";
		String filePath = folderPath + File.separator + fileName + "." + fileType;
		boolean fileDownloaded = false;
		File directory = new File(folderPath);
		File[] directory_contents = directory.listFiles();
		for (int i = 0; i < directory_contents.length; i++) {
			if (directory_contents[i].getName().contains(fileName)) {
				log("File has been downloaded. File Path: " + filePath);
				fileDownloaded = true;
			}
		}
		return fileDownloaded;
	}

	/**
	 * This method is used to delete file from TestDataDownload folder.
	 * 
	 * @param fileName File Name
	 * @param fileType File Extension
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void deleteDownloadedTestFile(String fileName, String fileType) {
		String folderPath = System.getProperty(USER_DIR_CONSTANT) + File.separator + "src" + File.separator + "test"
				+ File.separator + "resources" + File.separator + "TestData" + File.separator + "TestDataDownload";
		String filePath = folderPath + File.separator + fileName + "." + fileType;
		File file = new File(filePath);
		file.delete();
		log("File has been deleted. File Path: " + filePath);
	}

	/**
	 * This method is used to get test file location.
	 * 
	 * @param fileName Name of the file
	 * @param fileType File Extension
	 * @return File Path
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static String getUploadFileLocation(String fileName, String fileType) {
		String folderPath = System.getProperty(USER_DIR_CONSTANT) + File.separator + "src" + File.separator + "test"
				+ File.separator + "resources" + File.separator + "TestData" + File.separator + "TestDataUpload";
		String filePath = folderPath + File.separator + fileName + "." + fileType;
		return filePath;
	}

	/**
	 * This method is used to drag and drop the upload file.
	 * 
	 * @param uploadFile Target Web Element
	 * @param filePath   File Path
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void dragAndDropUploadFile(WebElement uploadFile, String filePath) {
		WebDriver driver = ((RemoteWebElement) uploadFile).getWrappedDriver();
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		int offsetX = 0;
		int offsetY = 0;
		String JS_DROP_FILE = "var target = arguments[0]," + "    offsetX = offsetX," + "    offsetY = offsetY,"
				+ "    document = target.ownerDocument || document," + "    window = document.defaultView || window;"
				+ "" + "var input = document.createElement('INPUT');" + "input.type = 'file';"
				+ "input.style.display = 'none';" + "input.onchange = function () {"
				+ "  var rect = target.getBoundingClientRect(),"
				+ "      x = rect.left + (offsetX || (rect.width >> 1)),"
				+ "      y = rect.top + (offsetY || (rect.height >> 1)),"
				+ "      dataTransfer = { files: this.files };" + ""
				+ "  ['dragenter', 'dragover', 'drop'].forEach(function (name) {"
				+ "    var evt = document.createEvent('MouseEvent');"
				+ "    evt.initMouseEvent(name, !0, !0, window, 0, 0, 0, x, y, !1, !1, !1, !1, 0, null);"
				+ "    evt.dataTransfer = dataTransfer;" + "    target.dispatchEvent(evt);" + "  });" + ""
				+ "  setTimeout(function () { document.body.removeChild(input); }, 25);" + "};"
				+ "document.body.appendChild(input);" + "return input;";
		WebElement input = (WebElement) jse.executeScript(JS_DROP_FILE, uploadFile, offsetX, offsetY);
		if (remoteWebDriver) {
			((RemoteWebElement) input).setFileDetector(new LocalFileDetector());
		}
		input.sendKeys(filePath);
	}

	/**
	 * This method is used to read csv file.
	 * 
	 * @param filepath File path
	 * @author spandit
	 * @return list
	 * @lastmodifiedby spandit
	 */
	public static List<String[]> readCSVFile(String filepath) {
		CSVReader reader = null;
		List<String[]> list = null;

		try {
			reader = new CSVReader(new FileReader(filepath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		try {
			list = reader.readAll();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * This method is used to verify CSV file data
	 * 
	 * @param list          List
	 * @param index         Index Value
	 * @param expectedValue Expected Value
	 * @return boolean dataVerified
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static boolean verifyCSVFileData(List<String[]> list, int index, String expectedValue) {
		boolean dataVerified = false;
		Iterator<String[]> iterator = list.iterator();
		while (iterator.hasNext()) {
			String[] str = iterator.next();
			for (int i = 0; i < str.length; i++) {
				if ((i == index - 1) && (str[i].equals(expectedValue))) {
					dataVerified = true;
				}
			}
		}
		return dataVerified;
	}

	/**
	 * This method used to enter data into alert popup.
	 * 
	 * @param inputValue Input Value
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void enterDataIntoAlertPopup(String inputValue) {
		driver.switchTo().alert().sendKeys(inputValue);
	}

	/**
	 * This method will clear the specified value in the text field.
	 * 
	 * @param enumValue Enum Value
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void clearValue(Enum<?> enumValue) {
		WebElement element = getElement(enumValue);
		element.clear();
	}

	/**
	 * This method will perform select All operation on the text.
	 * 
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void selectAllText() {
		builder.keyDown(Keys.CONTROL).sendKeys(Keys.chord("A")).keyUp(Keys.CONTROL).build().perform();
	}

	/**
	 * This method will scroll to the top of the page
	 * 
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void scrollToTop() {
		builder.sendKeys(Keys.HOME).build().perform();
	}

	/**
	 * This method will scroll up slightly
	 * 
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void scrollUpSlightly() {
		builder.keyDown(Keys.LEFT_ALT).sendKeys(Keys.ARROW_UP).keyUp(Keys.LEFT_ALT).build().perform();
	}

	/**
	 * This method will scroll down slightly
	 * 
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void scrollDownSlightly() {
		builder.keyDown(Keys.LEFT_ALT).sendKeys(Keys.ARROW_DOWN).keyUp(Keys.LEFT_ALT).build().perform();
	}

	/**
	 * This method will compare and assert the actual text contains expected text
	 * 
	 * @param enumValue    Enum Value
	 * @param expectedText Expected Text
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void verifyContainsText(Enum<?> enumValue, String expectedText) {
		String actualText = getElementAttribute(enumValue, "innerText");
		Assertions.assertThat(actualText.trim()).contains(expectedText);
	}

	/**
	 * This method will wait till specified Milliseconds
	 * 
	 * @param milliseconds Milliseconds
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void waitForMilliseconds(int milliseconds) {
		try {
			int timeInSeconds = milliseconds / 1000;
			WebDriverWait customWait = new WebDriverWait(driver, timeInSeconds);
			customWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//falseosElem/s")));
		} catch (TimeoutException e) {
			log("Waited for: " + milliseconds + " ms");
		} catch (Exception e) {
			log(e.getMessage());
		}
	}

	/**
	 * This method will scroll to the specified web element using javascript
	 * executor.
	 * 
	 * @param enumValue Enum Value
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void javaScriptScrollToElement(Enum<?> enumValue) {
		By locator = getLocator(enumValue);
		WebElement element = driver.findElement(locator);
		executeJS("arguments[0].scrollIntoView(true);", element);
	}

	public static void waitForOverlayToDisappear() {

		WebDriverWait wait = new WebDriverWait(driver, 30);
	    try {  
	        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.loading-mask")));
	    } catch (TimeoutException e) {
	        // Log a message or take appropriate action if the overlay doesn't disappear within the timeout
	        System.out.println("Overlay did not disappear within the timeout.");
	    }
	}

	public static void waitForElementToDisappear(By cssSelector) {
		// TODO Auto-generated method stub
		WebDriverWait wait = new WebDriverWait(driver, 30); // Adjust timeout as needed
        wait.until(ExpectedConditions.invisibilityOfElementLocated(cssSelector));
	}

	public static String gettitle() {
			String Pagetitle = driver.getTitle();
			return Pagetitle;
		}
	
	public static void switchtoTab(int x) {
		 ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
		    driver.switchTo().window(tabs.get(x));
	}

	public static void scrollDownByPixels(int pixels) {
	    JavascriptExecutor js = (JavascriptExecutor) driver;
	    js.executeScript("window.scrollBy(0, arguments[0]);", pixels);
	}

	public static String getCurrentUrl() {
		// TODO Auto-generated method stub
        return driver.getCurrentUrl();
	}

	public static String getTextFromElement(BateelPLPPageEnum bateelPlpPageEnumCss) {
	    WebElement element = UtilitiesCommon.getElement(bateelPlpPageEnumCss);
	    if (element != null) {
	        return element.getAttribute("alt");
	    } else {
	        return null;
	    }
	}

	public static int getCartCount() {
	    WebElement cartCountElement = driver.findElement(By.cssSelector(".counter-number"));
	    String cartCountText = cartCountElement.getText().trim();

	    if (cartCountText.isEmpty()) {
	        System.out.println("Cart count text is empty.");
	        return 0; 
	    }

	    int cartCount;
	    try {
	        cartCount = Integer.parseInt(cartCountText);
	    } catch (NumberFormatException e) {
	        System.out.println("Unable to parse cart count text: " + cartCountText);
	        return 0; 
	    }

	    return cartCount;
	}

	  public static String isElementClickable(Enum<?> enumValue) {
	        
	        return "clickable";
	    }

	
}
