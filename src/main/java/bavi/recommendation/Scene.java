//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package bavi.recommendation;

import java.util.ArrayList;

public class Scene {
    private int index;
    private ArrayList<Integer> chunks = new ArrayList();
    private ArrayList<String> categories = new ArrayList();

    public Scene() {
    }

    public ArrayList<Integer> getChunks() {
        return this.chunks;
    }

    public void setChunks(ArrayList<Integer> chunks) {
        this.chunks = chunks;
    }

    public ArrayList<String> getCategories() {
        return this.categories;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
