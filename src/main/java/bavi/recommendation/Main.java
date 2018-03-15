//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package bavi.recommendation;

import dbpedia.DBPedia;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class Main {
    private static ArrayList<Video> videos = new ArrayList();
    private static final String PATH = "/home/eduardo/Documentos/sceneSegmentation/";

    public Main() {
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Scene segmentation");

        String s;
        try {
            for(int i = 0; i < 174; ++i) {
                BufferedReader br = null;
                FileReader fr = null;
                ArrayList msg = new ArrayList();

                try {
                    fr = new FileReader("/home/eduardo/Documentos/sceneSegmentation/VideoInfo Extraction/anotation" + i + ".txt");
                    new BufferedReader(fr);
                br = new BufferedReader(new FileReader("/home/eduardo/Documentos/sceneSegmentation/VideoInfo Extraction/anotation" + i + ".txt"));
                    Video v = new Video(Integer.toString(i));

                    while((s = br.readLine()) != null) {
                        msg.add(s);
                    }

                    v.setReferences(msg);
                    //DBPedia db = new DBPedia();
                    //db.getResourcesRelatedAndRelatedTo(v);
                    videos.add(v);
                } catch (IOException var8) {

                }
            }
        } catch (Exception var9) {
            var9.printStackTrace();
        }

        TFIDF tfid = new TFIDF();
        Iterator var11 = videos.iterator();

        while(var11.hasNext()) {
            Video v = (Video)var11.next();
            Iterator var13;
            if (v.getCategories() != null) {
                var13 = v.getCategories().iterator();

                while(var13.hasNext()) {
                    s = (String)var13.next();
                    if (!tfid.getTerms().contains(s)) {
                        tfid.getTerms().add(s);
                    }
                }
            }

            var13 = v.getReferences().iterator();

            while(var13.hasNext()) {
                s = (String)var13.next();
                if (!tfid.getTerms().contains(s)) {
                    tfid.getTerms().add(s);
                }
            }

            tfid.getVideos().add(v);
        }

        tfid.calculateTFIDF();
    }
}
