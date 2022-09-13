package Util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

// Connects to the API and gets the response from the API
public class TDAPI {

    private final String baseURL, APIKEY;
    private HttpClient client;
    private HttpResponse response;
    private HttpRequest request;

    public TDAPI(){
        baseURL = "https://api.tdameritrade.com/v1/marketdata/";
        APIKEY  = "?apikey=UX2GWPMOIXPKSIKW2RWWF7UPCBZVOTIU";
        client = HttpClient.newHttpClient();
    }

    //Build URL for quotes endpoint
    public String buildQuotesURL(String symbol){
        return baseURL + "quotes" + APIKEY + "&symbol=" + symbol.toUpperCase().replaceAll(", ", ",");
    }

    //Build URL for movers endpoint
    public String buildMoversURL(String index){
        return baseURL + index.toUpperCase() +"/movers"+ APIKEY;
    }

    // Building a request and getting the response.
    public HttpResponse<String> getResponse(String URL){
        //Building the request that the client can send
        request = HttpRequest.newBuilder().uri(URI.create(URL)).build();
        //Getting the response from TD API.
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.out.println("Something went wrong while getting a response from the API");
            System.out.println(e);
        }

        return response;
    }
    
    //Geting JSONNode object containing the data form the API after parsing the response body
    public JsonNode parseQuotes(HttpResponse<String> response){
        String value = response.body();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = null;
        try {
            node = mapper.readTree(value);
        } catch (JsonProcessingException e) {
            System.out.println("Something went wrong");
            System.out.println(e);
        }
        return node;
    }
}