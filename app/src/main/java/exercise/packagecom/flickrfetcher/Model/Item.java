
package exercise.packagecom.flickrfetcher.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item {

    @SerializedName("media")
    @Expose
    private Media media;

    public Media getMedia() {
        return media;
    }


}
