package enums.onitsuka;

/**This enum will store objects available on Onitsuka Home page.
@author spandit
@lastmodifiedby kdave
*/

/* Note: Enum key should be in capital case and should end with either of below locator types:
ID, XPATH, LINKTEXT, PARTIALLINKTEXT, CSS, NAME, TAGNAME, CLASSNAME */

public enum OnitsukaHomePageEnum {
	ONITSUKAPAGE_PAGE_TITLE_CSS(".page-header"),
	ONITSUKAPAGE_MEN_CATEGORY_CSS("ul >.men-cate-top-label"),
	ONITSUKA_MEN_DETAILS_PAGE_CSS("span[class='base'] font font");

	/**
	 * Override the toString() method to return the label instead of the declared name.
	 */
	@Override
	public String toString() {
		return this.label;
	}

	/**
	 * A final variable to store the label, which can't be changed.
	 */
	public final String label;

	/**
	 * A private constructor that sets the label.
	 * @param label
	 */
	OnitsukaHomePageEnum(String label) {
		this.label = label;
	}
}