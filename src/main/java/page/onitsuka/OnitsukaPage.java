package page.onitsuka;

import common.UtilitiesCommon;
import enums.onitsuka.OnitsukaHomePageEnum;

/**
 * @author spandit
 * @lastmodifiedby spandit 
 * This class will contain all the Home Page methods
 */
public class OnitsukaPage {
	
	/**
	 * This method is used to verify Onitsuka Tiger's page Title.
	 * @author spandit
	 * @lastmodifiedby kdave
	 */
	public static void verifyOnitSukaPageHeaderTitle() {
		UtilitiesCommon.waitForElementIsPresent(OnitsukaHomePageEnum.ONITSUKAPAGE_PAGE_TITLE_CSS);
		UtilitiesCommon.verifyText(OnitsukaHomePageEnum.ONITSUKAPAGE_PAGE_TITLE_CSS, UtilitiesCommon.getTestData("OnitSukaHeader"));
	}
	
	/**
	 * This method is used to verify Onitsuka Tiger's Men Category.
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void verifyOnitSukaPageMenCategory() {
		UtilitiesCommon.waitForElementIsPresent(OnitsukaHomePageEnum.ONITSUKAPAGE_MEN_CATEGORY_CSS);
		UtilitiesCommon.click(OnitsukaHomePageEnum.ONITSUKAPAGE_MEN_CATEGORY_CSS);
		//UtilitiesCommon.verifyText(OnitsukaHomePageEnum.ONITSUKAPAGE_PAGE_TITLE_CSS, UtilitiesCommon.getTestData("OnitSukaMenPageTitle"));
	}
}