//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package bavi.recommendation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Scene {
    private int index;
    private ArrayList<Integer> chunks = new ArrayList();
    private Set<String> categories = new HashSet<String>();

    public Scene() {
    }

    public ArrayList<Integer> getChunks() {
        return this.chunks;
    }

    public void setChunks(ArrayList<Integer> chunks) {
        this.chunks = chunks;
    }

    public Set<String> getCategories() {
        return categories;
    }

    public void setCategories(Set<String> categories) {
        this.categories = categories;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
