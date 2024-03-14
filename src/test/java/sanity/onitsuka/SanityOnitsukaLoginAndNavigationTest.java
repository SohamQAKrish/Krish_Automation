package sanity.onitsuka;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import common.UtilitiesCommon;
import io.qameta.allure.Description;
import io.qameta.allure.TmsLink;
import page.login.LoginPage;
import page.onitsuka.OnitsukaPage;

/**
 * @author spandit
 * @lastmodifiedby spandit
 * This class will contain all Sanity tests related to Login and Navigations to different modules across the Onitsuka application.
 */
@Listeners({ listeners.ScriptExecutionListener.class })
public class SanityOnitsukaLoginAndNavigationTest {
	/**
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	@Test(testName = "Onitsuka Web Application Navigation")
	@TmsLink("49700")
	@Description("This Test is used to Verify the header title of Onitsuka Web Application Home Page")
	public void testOnitSukaPageHeaderTitle() {
		LoginPage.login("AdminUserName", "AdminPassword");
		OnitsukaPage.verifyOnitSukaPageHeaderTitle();
	}
	
	/**
	 * @author spandit
	 * @lastmodifiedby kdave
	 */
	@Test(testName = "Onitsuka Men Category Navigation")
	@TmsLink("49700")
	@Description("This Test is used to Verify the Navigation to the Men Category")
	public void testOnitSukaMenCategoryNavigation() {
		LoginPage.login("AdminUserName", "AdminPassword");
		UtilitiesCommon.setupWebdriverWait(120); // Increase wait time to 60 seconds

		OnitsukaPage.verifyOnitSukaPageMenCategory();
	}
}