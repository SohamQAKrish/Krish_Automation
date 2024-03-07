package common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class RestAssuredUtility {

	static XSSFWorkbook workbook;
	static XSSFSheet sheet;
	static FileInputStream file;
	public static final String RESOURCE = "Resource";
	public static final String BODY = "Body";
	public static final String DESCRIPTION = "Description";
	public static final String USERNAME_HEADER = "x-auth-username";
	public static final String PASSWORD_HEADER = "x-auth-password";
	public static Map<String, String> restData;
	public static RequestSpecification request;
	static PrintStream log;

	/**
	 * This method will Read the REST API Data of specified Module from RestAssuredData.xlsx Excel sheet.
	 * @param moduleName Module Name
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static void readExcelData(String moduleName) {
		try {
			String filePath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test"
					+ File.separator + "resources" + File.separator + "TestData" + File.separator
					+ "RestAssuredData.xlsx";
			try {
				file = new FileInputStream(new File(filePath));
			} catch (FileNotFoundException e) {
				UtilitiesCommon.log("RestAssuredData.xlsx is not Present in TestData folder : " + e.getStackTrace());
			}
			try {
				workbook = new XSSFWorkbook(file);
			} catch (IOException e) {
				UtilitiesCommon.log(
						"A Problem encountered while reading the RestAssuredData.xlsx file : " + e.getStackTrace());
			}
			UtilitiesCommon.log("Reading the REST API Calls for Module : " + moduleName);
			sheet = workbook.getSheet(moduleName);
		} finally {
			try {
				workbook.close();
				file.close();
			} catch (IOException e) {
				UtilitiesCommon.log("A Problem encountered while closing the file resources : " + e.getStackTrace());
			}
		}
	}

	/**
	 * This method will Return the REST API Data for Specified REST Call.
	 * @param restCallName Rest Call Name
	 * @return Map restData
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static Map<String, String> getRestData(String restCallName) {
		Iterator<Row> rowIterator = sheet.iterator();
		restData = new HashMap<>();
		UtilitiesCommon.log("Reading the REST API information for : " + restCallName);
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			if (row.getCell(0).getStringCellValue().equalsIgnoreCase(restCallName)) {
				restData.put(RESOURCE, row.getCell(1).getStringCellValue());
				if (row.getCell(3) != null) {
					restData.put(BODY, row.getCell(3).getStringCellValue());
				}
				restData.put(DESCRIPTION, row.getCell(4).getStringCellValue());
				break;
			}
		}
		return restData;
	}

	/**
	 * This method will build Request Specification.
	 * @return RequestSpecification request
	 * @author spandit
	 * @lastmodifiedby spandit
	 */
	public static RequestSpecification getRequestSpecBuilder() {
		if(request == null) {
			try {
				log = new PrintStream(new FileOutputStream(
						new File(System.getProperty("user.dir") + File.separator + "RestCallLogs.txt")));
			} catch (FileNotFoundException e) {
				UtilitiesCommon.log("A Problem encountered while creating RestCallLogs.txt file : " + e.getStackTrace());
			}
			Map<String, String> headers = new HashMap<>();
			headers.put(PASSWORD_HEADER,
					UtilitiesCommon.getDecryptedPassword(UtilitiesCommon.getEnvironmentData("AdminPassword")));
			headers.put(USERNAME_HEADER, UtilitiesCommon.getEnvironmentData("AdminUserName"));
			request = new RequestSpecBuilder().setBaseUri(UtilitiesCommon.getEnvironmentData("ApplicationURL"))
					.addFilter(RequestLoggingFilter.logRequestTo(log))
					.addFilter(ResponseLoggingFilter.logResponseTo(log))
					.setContentType(ContentType.JSON)
					.addHeaders(headers)
					.build();
		}
		return request;
	}
}
