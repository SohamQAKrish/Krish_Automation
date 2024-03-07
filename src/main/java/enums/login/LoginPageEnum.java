package enums.login;

/**
 * This enum will store objects available on Login page.
 * @author spandit
 * @lastmodifiedby spandit
 */

/*
 * Note: Enum key should be in capital case and should end with either of below
 * locator types: ID, XPATH, LINKTEXT, PARTIALLINKTEXT, CSS, NAME, TAGNAME, CLASSNAME 
 */
/**
 * @author spandit
 * @lastmodifiedby spandit 
 * This class will contain all the Enums for Login Page
 */
public enum LoginPageEnum {
	LOGINPAGE_USERNAME_TEXT_BOX_CSS("#WebLogin_txtUsername"),
	LOGINPAGE_PASSWORD_TEXT_BOX_CSS("#password"),
	LOGINPAGE_LOGIN_BUTTON_CSS(".btn[type='button']");

	/**
	 * Override the toString() method to return the label instead of the declared name.*/
	@Override
	public String toString() {
		return this.label;
	}

	/**
	 * A final variable to store the label, which can't be changed.*/
	public final String label;

	/**
	 * A constructor that sets the label.
	 * @param label
	 */
	LoginPageEnum(String label) {
		this.label = label;
	}
}