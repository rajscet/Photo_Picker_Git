package ffmpeg.raj.com.gallery.model;

/**
 * Created by root on 3/6/16.
 */
public class Album {


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Album(String title) {
        this.title = title;
    }

    public Album() {

    }
    String title;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    int count;

    public Album(String title, int count, String imagePath) {
        this.title = title;
        this.count = count;
        this.imagePath = imagePath;
    }

    String imagePath;


}
