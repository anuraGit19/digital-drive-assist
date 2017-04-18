/**
 * Created by anurag on 4/16/17.
 */
package com.asu.mc.digitalassist.activities.rsclient;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.asu.mc.digitalassist.activities.model.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class GetRestaurantClient {
    /*
	 * Setting OAuth credentials below from the Yelp Developers API site:
	 * http://www.yelp.com/developers/getting_started/api_access
	 */
    private static final String CONSUMER_KEY = "0g_t1_QDCB1I2vm_eNtClg";
    private static final String CONSUMER_SECRET = "JUgGx3X6vNme9jcgxAcSryUdPEQ";
    private static final String TOKEN = "xVgENShX8UlT_Xci_fW8VB_kHchlfV1w";
    private static final String TOKEN_SECRET = "I6z_giUHlzqNqmCEkEVL1QMytXU";

    public RestaurantList getRestaurantList(String zipcodeOrCityName, String storeName) {

        String nearestStoreAddress = "";

        GetRestaurantApiUtil restaurantSearchObject = new GetRestaurantApiUtil(CONSUMER_KEY, CONSUMER_SECRET, TOKEN, TOKEN_SECRET);
        String responseString = restaurantSearchObject.searchForNearbyStoreByLocation(storeName, zipcodeOrCityName);
        RestaurantList restaurantList = new RestaurantList();
        List<Restaurant> resList = new ArrayList<>();

        JSONParser parser = new JSONParser();
        try {

            // parsing response string using simple json parser
            JSONObject jsonObject = (JSONObject) parser.parse(responseString);
            long numOfStoresNearby = (long) jsonObject.get("total");

            // Checking if there is at least one store in the vicinity
            if (numOfStoresNearby > 0) {
                JSONArray arrayObject = (JSONArray) jsonObject.get("businesses");

                for (Object o : arrayObject) {
                    JSONObject innerBusinessObject = (JSONObject) o;
                    boolean isClosed = (boolean) innerBusinessObject.get("is_closed");
                    Restaurant restaurant = new Restaurant();
                    if (!isClosed) {

                        String restName = (String) innerBusinessObject.get("name");
                        String mobileUrl = (String) (innerBusinessObject.get("mobile_url")!=null?innerBusinessObject.get("mobile_url"):innerBusinessObject.get("url"));
                        String ratings = String.valueOf(innerBusinessObject.get("rating"));
                        String contact = String.valueOf(innerBusinessObject.get("phone"));
                        //JSONArray jsa = (JSONArray) innerBusinessObject.get("categories");
                        //String category = (String) jsa.get(0);
                        String category = (String) ((JSONArray) innerBusinessObject.get("categories")).get(0);

                        resList.add(new Restaurant(restName, mobileUrl, ratings, contact, category));
                    }
                }
                restaurantList.setRestaurantList(resList);
            } else {
                nearestStoreAddress = "No nearby restaurants";
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return restaurantList;
    }
}
