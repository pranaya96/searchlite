package howard.west;

import com.google.gson.Gson;
import howard.west.dto.ResultDTO;
import lombok.extern.slf4j.Slf4j;
import java.util.*;

import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.options;
import static spark.Spark.port;

@Slf4j
public class App {

  //copied from https://sparktutorials.github.io/2016/05/01/cors.html
  // Enables CORS on requests. This method is an initialization method and should be called once.
  private static void enableCORS(final String origin, final String methods, final String headers) {

    options("/*", (request, response) -> {

      String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
      if (accessControlRequestHeaders != null) {
        response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
      }

      String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
      if (accessControlRequestMethod != null) {
        response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
      }

      return "OK";
    });

    before((request, response) -> {
      response.header("Access-Control-Allow-Origin", origin);
      response.header("Access-Control-Request-Method", methods);
      response.header("Access-Control-Allow-Headers", headers);
      // Note: this may or may not be necessary in your particular application
      response.type("application/json");
    });
  }

  public static void main(String[] args) {
    // by default this is 4567 in order to prevent collisions with
    // other things that may be running on the machine.  We are running in a docker container
    // so that is not an issue
    port(8080);

    enableCORS("http://frontend.howard.test:4200", "GET", "");

    //GSON is used to map to json.
    Gson gson = new Gson();

    
    // List<String, String> dummyList1 = new ArrayList<String, String>();
    // List<String, String> dummyList2 = new ArrayList<String, String>();
    // Map<String, List> dummyData = new TreeMap<String, ArrayList>();
    
    // dummyList1.add("https://en.wikipedia.org/wiki/Nepal");
    // dummyList1.add("Nepal is a beautiful country. Everest is in Nepal.");
    // dummyList2.add("https://en.wikipedia.org/wiki/United_States");
    // dummyList2.add("America is the biggest economy of the world.");
    // dummyData.put("Nepal", dummyList1);
    // dummyData.put("America", dummyList2);



    //the route callback is a lambda function
    get("/", (req, res) -> {
      log.info("Loading the index");
      return "Welcome to Howard West!";
    });
    get(
      "/search", //route
      "application/json", //return GET
      (req, res) -> ResultDTO.builder().term(req.queryMap("q").value()),
      gson::toJson); // <- this is called a method reference

   get("/results", (req,res) -> "Hello world");
  
  }
}
