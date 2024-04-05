package stepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;

import static org.testng.Assert.assertEquals;

public class Animals {

    public RequestSpecification httpRequest;
    public Response response;
    public ResponseBody body;
    public String accessToken;

    @Given("I am an authenticated user")
    public void iAmAnAuthenticatedUser() {

        RestAssured.baseURI = "https://api.petfinder.com/v2";

        response = RestAssured.given()
                .contentType("application/x-www-form-urlencoded; charset=utf-8")
                .formParam("grant_type", "client_credentials")
                .formParam("client_id", "client_id")
                .formParam("client_secret", "client_secret")
                .when()
                .post(RestAssured.baseURI + "/oauth2/token");

        body = response.getBody();
        String sResponseBody = body.asString();
        System.out.println(sResponseBody);

        JsonPath jsonPath = response.jsonPath();
        accessToken = jsonPath.getJsonObject("access_token").toString();
    }

    @When("I hit the get animals api url")
    public void iHitTheGetAnimalsApiUrl() {

        response = RestAssured.given()
                .header("Authorization", "Bearer " + accessToken)
                .when()
                .get(RestAssured.baseURI + "/types");

        JsonPath jsonPath2 = response.jsonPath();
        String sTypes = jsonPath2.getJsonObject("types").toString();
        System.out.println(sTypes);
    }

    @Then("I get {int} as the response code")
    public void iGetAsTheResponseCode(int arg0) {
        int expectedResponseCode = response.statusCode();
        assertEquals(expectedResponseCode, arg0);
    }

    @Then("I get animals in the response body of the api")
    public void iGetAnimalsInTheResponseBodyOfTheApi() {
    }

    @Given("I am an unauthenticated user")
    public void iAmAnUnauthenticatedUser() {
    }

    @Then("I do not get animals in the response of the api")
    public void iDoNotGetAnimalsInTheResponseOfTheApi() {
    }

    @Given("I hit the get animals api url without access token")
    public void iHitTheGetAnimalsApiUrlWithoutAccessToken() {
    }
}
