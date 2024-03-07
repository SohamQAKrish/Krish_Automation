package listeners;

import org.testng.IClassListener;
import org.testng.ITestClass;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import common.UtilitiesCommon;

public class ScriptExecutionListener implements ITestListener, IClassListener {

	@Override
	public void onStart(ITestContext context) {
		UtilitiesCommon.init(context);
		UtilitiesCommon.readEnvironmentData(context);
		UtilitiesCommon.readTestrailData();
		UtilitiesCommon.createTestrailRun(context);
		UtilitiesCommon.setAllureEnvironment(context);
		UtilitiesCommon.deleteAllureReportsAndAttachments();
	}

	@Override
	public void onBeforeClass(ITestClass testClass) {
		UtilitiesCommon.loadTestClassData(testClass);
	}

	@Override
	public void onTestStart(ITestResult result) {
		UtilitiesCommon.loadTestCaseData(result);
		UtilitiesCommon.getTestrailCaseID(result);
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		//UtilitiesCommon.setTestrailSuccessResults(result);
		UtilitiesCommon.setTestCaseNameInAllure(result);
		UtilitiesCommon.applicationLogout();
		UtilitiesCommon.stopScreenRecording();
		UtilitiesCommon.deletePassedTestCaseRecording(result);
		UtilitiesCommon.closeDriver();
	}

	@Override
	public void onTestFailure(ITestResult result) {
		//UtilitiesCommon.setTestrailFailureResults(result);
		UtilitiesCommon.captureAllureScreenshot(result.getName());
		UtilitiesCommon.applicationLogout();
		UtilitiesCommon.stopScreenRecording();
		UtilitiesCommon.attachScreenRecording(result);
		UtilitiesCommon.setTestCaseNameInAllure(result);
		UtilitiesCommon.closeDriver();
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		UtilitiesCommon.closeDriver();
	}

	@Override
	public void onFinish(ITestContext context) {
		UtilitiesCommon.generateAllureReport(context);
	}
}