
package exercise.packagecom.flickrfetcher.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FlickrResponse {


    @SerializedName("items")
    @Expose
    private List<Item> items = null;


    public List<Item> getItems() {
        return items;
    }


}
