
package exercise.packagecom.flickrfetcher.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Media {

    @SerializedName("m")
    @Expose
    private String m;

    public String getM() {
        return m;
    }



}
