package ffmpeg.raj.com.gallery.model;

/**
 * Created by root on 3/6/16.
 */
public class Photo {


    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }



    public Photo(String imagePath) {

        this.imagePath = imagePath;
    }

    String imagePath;


}
