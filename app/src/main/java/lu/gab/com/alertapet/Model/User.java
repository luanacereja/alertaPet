package lu.gab.com.alertapet.Model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Luana on 27/06/2017.
 */

public class User implements Serializable {


    private String name;
    private String imageProfile;
    private String thumb_image;


    public User() {
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageProfile() {
        return imageProfile;
    }

    public void setImageProfile(String imageProfile) {
        this.imageProfile = imageProfile;
    }

    public String getThumb_image() {
        return thumb_image;
    }

    public void setThumb_image(String thumb_image) {
        this.thumb_image = thumb_image;
    }



}
