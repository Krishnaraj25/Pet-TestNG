package org.pet;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class PetStore  {
	public static int id;
	@DataProvider(name="status")
	public Object[][] getStatus(){
		return new Object[][] {{"sold"},{"available"},{"pending"}};
		
	}
	
	@BeforeClass
	public void before() {
		System.out.println("Before Class");
	}
	@AfterClass
	public void after() {
		System.out.println("After Class");
	}
	@BeforeMethod
	public void beforeMethod() {
		System.out.println("Before Method");
	}
	@AfterMethod
	public void afterMethod() {
		System.out.println("After Method");
	}
	@Test(priority=1)
	public void post() {
		RestAssured.baseURI="https://petstore.swagger.io/v2";
		String R=given().log().all().header("Content-Type","application/json").body("{\r\n" + 
				"  \"id\": 151,\r\n" + 
				"  \"category\": {\r\n" + 
				"    \"id\": 13,\r\n" + 
				"    \"name\": \"CAT\"\r\n" + 
				"  },\r\n" + 
				"  \"name\": \"Tuna\",\r\n" + 
				"  \"photoUrls\": [\r\n" + 
				"    \"string\"\r\n" + 
				"  ],\r\n" + 
				"  \"tags\": [\r\n" + 
				"    {\r\n" + 
				"      \"id\": 15,\r\n" + 
				"      \"name\": \"Pussy\"\r\n" + 
				"    }\r\n" + 
				"  ],\r\n" + 
				"  \"status\": \"available\"\r\n" + 
				"}").when().post("/pet").then().log().all().assertThat().statusCode(200).body("id", equalTo(151))
		.extract().response().asString();
		System.out.println(R);
		JsonPath js=new JsonPath(R);
		id=js.get("id");
		System.out.println(id);
		int id1=js.get("category.id");
		System.out.println(id1);
		String name=js.get("category.name");
		System.out.println(name);
		int id2=js.get("tags[0].id");
		System.out.println(id2);
		}
	
	@Test(priority=2)
	public void get() {
		String R1=given().log().all().pathParam("id", id).header("Content-Type","application/json")
		.when().get("/pet/{id}").then().log().all().extract().response().asString();
		JsonPath js1=new JsonPath(R1);
		int newid=js1.get("id");
		System.out.println(newid);
		Assert.assertEquals(id,newid);
		}
	@Test(priority=3,dataProvider="status")
	public void findStatus(String status) {
		String R2=given().log().all().queryParam("status",status).header("Content-Type","application/json")
		.when().get("/pet/findByStatus").then().log().all().assertThat().statusCode(200)
		.extract().response().asString();
		JsonPath js2=new JsonPath(R2);
		System.out.println(R2);
		String s=js2.get("status[0]");
		System.out.println(s);
	}
	@Test(priority=4)
	public void put() {
		String R4=given().log().all().header("Content-Type","application/json").body("{\r\n" + 
				"  \"id\": 151,\r\n" + 
				"  \"category\": {\r\n" + 
				"    \"id\": 13,\r\n" + 
				"    \"name\": \"CAT\"\r\n" + 
				"  },\r\n" + 
				"  \"name\": \"Tuna\",\r\n" + 
				"  \"photoUrls\": [\r\n" + 
				"    \"string\"\r\n" + 
				"  ],\r\n" + 
				"  \"tags\": [\r\n" + 
				"    {\r\n" + 
				"      \"id\": 15,\r\n" + 
				"      \"name\": \"Pussy\"\r\n" + 
				"    }\r\n" + 
				"  ],\r\n" + 
				"  \"status\": \"sold\"\r\n" + 
				"}").when().put("/pet").then().log().all().assertThat().statusCode(200)
		.extract().response().asString();
		JsonPath js3=new JsonPath(R4);
		int PutId=js3.get("id");
		System.out.println(PutId);
		String name=js3.get("category.name");
		System.out.println(name);
		String Status=js3.get("status");
		System.out.println(Status);
	}
	
	
	
	
}
