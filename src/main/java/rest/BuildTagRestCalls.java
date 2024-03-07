package rest;

import common.RestAssuredUtility;
import common.UtilitiesCommon;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class BuildTagRestCalls extends RestAssuredUtility {

	/**
	 * This method will return the Build tag of the application.
	 * @return String buildTag
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static String getBuildTag() {
		Response buildInfoResponse = given().spec(RestAssuredUtility.getRequestSpecBuilder())
				.when()
				.get("/buildInfo.json")
				.then()
				.extract().response();
		JsonPath jsnPath = buildInfoResponse.jsonPath();
		String buildTag = jsnPath.get("buildTag");
		UtilitiesCommon.log("Build Tag: " + buildTag);
		return buildTag;
	}
}
