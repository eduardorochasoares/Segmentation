//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package bavi.recommendation;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class Video {
    private String id;
    private ArrayList<String> references;
    private ArrayList<String> category;

    public Video(String id) throws UnsupportedEncodingException {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getReferences() {
        return this.references;
    }

    public ArrayList<String> getCategories() {
        return this.getCategory();
    }

    public void setReferences(ArrayList<String> ref) {
        this.references = ref;
    }

    public void setCategories(ArrayList<String> cat) {
        this.setCategory(cat);
    }

    public ArrayList<String> getCategory() {
        return this.category;
    }

    public void setCategory(ArrayList<String> category) {
        this.category = category;
    }
}
