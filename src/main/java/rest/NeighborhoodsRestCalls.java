package rest;

import static io.restassured.RestAssured.given;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import common.RestAssuredUtility;
import common.UtilitiesCommon;
import io.restassured.response.Response;

/**
 * @author spandit
 * @lastmodifiedby spandit
 * This class will contain all the Neighborhoods related API REST calls
 */
public class NeighborhoodsRestCalls extends RestAssuredUtility {

	static Response createSectionResponse;
	
	/**
	 * This method will return the List of Neighborhoods.
	 * @return Response response
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static Response getNeighborhoodsList() {
		Map<String, String> data = RestAssuredUtility.getRestData("Neighborhoods List");
		return given().spec(RestAssuredUtility.getRequestSpecBuilder())
				.queryParam("limit", 1000)
				.when()
				.get(data.get(RESOURCE))
				.then()
				.extract().response();
	}
	
	/**
	 * This method will return the Id of the Neighborhood.
	 * @param neighborhoodName Neighborhood Name
	 * @return int neighborhoodId
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static int getNeighborhoodId(String neighborhoodName) {
		Response neighborhoodlist = getNeighborhoodsList();
		int id = 0;
		JSONArray arrayObj = new JSONArray(neighborhoodlist.asString());
		for (int i = 0; i < arrayObj.length(); i++) {
			if (arrayObj.getJSONObject(i).getString("name").equalsIgnoreCase(neighborhoodName)) {
				id = arrayObj.getJSONObject(i).getInt("id");
				UtilitiesCommon
						.log("Specified Neighborhood Name is : " + neighborhoodName + " And its respective Id is : " + id);
				break;
			}
		}
		return id;
	}
	
	/**
	 * This method will Create Section in specified Neighborhood.
	 * @param neighborhoodName Neighborhood Name
	 * @return Response
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static Response createSection(String neighborhoodName) {
		Map<String, String> data = RestAssuredUtility.getRestData("Create Section");
		int id = getNeighborhoodId(neighborhoodName);
		createSectionResponse = given().spec(RestAssuredUtility.getRequestSpecBuilder())
				.body(getNeighborhoodSectionBody(data.get(BODY), id))
				.when()
				.post(data.get(RESOURCE))
				.then()
				.extract().response();
		return createSectionResponse;
	}
	
	
	/**
     * ***********************************************************
     * Methods used to generate Payloads are below
     * @author spandit
     *************************************************************
     * /
     
	/**
	 * This method will return the Create Neighborhood Section Payload.
	 * @param body Request Body
	 * @param id Neighborhood ID
	 * @return payloadBody
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	private static String getNeighborhoodSectionBody(String body, int id) {
		JSONObject json = new JSONObject(body);
		json.getJSONObject("neighborhood").put("id", id);
		String sectionName = "Section" + UtilitiesCommon.generateRandomNumber();
		json.put("name", sectionName);
		UtilitiesCommon.log("Created Section Using REST API : " + sectionName);
		return json.toString();
	}
}
