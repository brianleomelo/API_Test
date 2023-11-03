import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import static io.restassured.RestAssured.when;

public class ServicesTest {

    public Response response;
    public JsonPath jsonpathevaluator;
    public String endpointBase = "https://swapi.dev/api/";
    @BeforeMethod
    public void setRes() {
        setResponse(endpointBase+"people/2/");
    }
    @Test
    public void getC3PO () {
        Assert.assertEquals(response.getStatusCode(),200);
        Assert.assertEquals(jsonpathevaluator.<Short>get("skin_color"),"gold");
        Assert.assertEquals(jsonpathevaluator.getList ("films").size(),6);
    }

    @Test
    public void correctFormatTest() {
        setResponse(jsonpathevaluator.getList("films").get(1).toString());
        String date = jsonpathevaluator.get("release_date").toString();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            dateFormat.parse(date);
            Assert.assertTrue(true,"Válido");
        } catch (ParseException e) {
            Assert.fail("El formato de fecha no es válido.");
        }

    }

    @Test
    public void thereAreElements() {
        setResponse(jsonpathevaluator.getList("films").get(1).toString());
        Assert.assertFalse(jsonpathevaluator.getList("characters").isEmpty());
        Assert.assertFalse(jsonpathevaluator.getList("planets").isEmpty());
        Assert.assertFalse(jsonpathevaluator.getList("starships").isEmpty());
        Assert.assertFalse(jsonpathevaluator.getList("vehicles").isEmpty());
        Assert.assertFalse(jsonpathevaluator.getList("species").isEmpty());
    }

    @Test
    public void checkPlanet() {
        setResponse(this.jsonpathevaluator.getList("films").get(1).toString());
        setResponse(this.jsonpathevaluator.getList("planets").get(0).toString());
        Assert.assertEquals(jsonpathevaluator.get("gravity").toString(),"1.1 standard");
        Assert.assertEquals(jsonpathevaluator.get("terrain").toString(),"tundra, ice caves, mountain ranges");

    }

    @Test
    public void film7Test() {
        setResponse(endpointBase + "films/7/");
        Assert.assertEquals(response.statusCode(), 404);
    }


    public void setResponse(String ep) {
        this.response = when().get(ep);
        this.jsonpathevaluator = getResponse().jsonPath();
    }

    public Response getResponse() {
        return response;
    }
}
