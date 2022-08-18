package org.jira;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

import java.io.File;

import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class JiraIssue {
	SessionFilter Session =new SessionFilter();
	public static String id;
	//@Parameters("URL")
	@Test(priority=1)
	public void Session() {
		RestAssured.baseURI="http://localhost:8080/";
		given().log().all().header("Content-Type","application/json")
		.body("{ \"username\": \"Krishnaraj\", \"password\": \"Krishna@2598\" }").filter(Session)
		.when().post("rest/auth/1/session").then().assertThat().statusCode(200);
	}
	@Test(priority=2)
	public void Issue() {
		String R=given().log().all().header("Content-Type","application/json")
				.body("{\r\n" + 
				"    \"fields\": {\r\n" + 
				"       \"project\":\r\n" + 
				"       {\r\n" + 
				"          \"key\": \"RES\"\r\n" + 
				"       },\r\n" + 
				"       \"summary\": \"Search Button is not Visible\",\r\n" + 
				"       \"description\": \"Creating of an issue using ids for projects and issue types using the REST API\",\r\n" + 
				"       \"issuetype\": {\r\n" + 
				"          \"name\": \"Bug\"\r\n" + 
				"       }\r\n" + 
				"   }\r\n" + 
				"}").filter(Session)
				.when().post("rest/api/2/issue").then().assertThat().statusCode(201).extract().response().asString();
			System.out.println(R);
			JsonPath js=new JsonPath(R);
			id=js.get("id");
	}
	@Test(priority=3)
	public void Comment() {
		String R1=given().log().all().pathParam("id", id).header("Content-Type","application/json").body("{\r\n" + 
				"    \"body\": \"This is First Comment.\",\r\n" + 
				"    \"visibility\": {\r\n" + 
				"        \"type\": \"role\",\r\n" + 
				"        \"value\": \"Administrators\"\r\n" + 
				"    }\r\n" + 
				"}").filter(Session).when().post("rest/api/2/issue/{id}/comment")
		.then().assertThat().statusCode(201).extract().response().asString();
	}
	@Test(priority=4)
	public void Attachment() {
		given().log().all().pathParam("id", id).header("X-Atlassian-Token","no-check").header("Content-Type","multipart/form-data")
		.multiPart("file",new File("./jira.txt")).filter(Session).when().post("rest/api/2/issue/{id}/attachments")
		.then().assertThat().statusCode(200);
	}
}
