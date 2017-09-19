//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package dbpedia;

import bavi.recommendation.Video;
import com.github.kevinsawicki.http.HttpRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;

public class DBPedia {
    private ArrayList<String> categorias = null;

    public DBPedia() throws UnsupportedEncodingException {
    }

    public static final Boolean languageIsPt(String url) {
        return url == null ? false : url.startsWith("http://pt");
    }

    public static final Boolean languageIsEs(String url) {
        return url == null ? false : url.startsWith("http://es");
    }

    public static final Boolean languageIsEn(String url) {
        return url == null ? false : url.startsWith("http://dbpedia");
    }

    public static final ArrayList<String> getResourceSameAs(String resource) {
        return dbpediaGet("distinct ?x where {?x owl:sameAs <" + resource + ">}");
    }

    public static final ArrayList<String> dbpediaGet(String query) {
        StringBuilder textoEncode = new StringBuilder();
        textoEncode.append(query);
        StringBuilder requisicaoDBPedia = new StringBuilder();
        requisicaoDBPedia.append("http://dbpedia.org/sparql?default-graph-uri=http%3A%2F%2Fdbpedia.org&query=select+");

        try {
            requisicaoDBPedia.append(URLEncoder.encode(textoEncode.toString(), "UTF-8"));
        } catch (UnsupportedEncodingException var12) {
            var12.printStackTrace();
        }

        requisicaoDBPedia.append("+LIMIT+100&timeout=30000&debug=on");
        String resultado = HttpRequest.get(requisicaoDBPedia.toString()).accept("text/csv").body();
        resultado = resultado.substring(4).replaceAll("[\"']", "");
        String[] resources = resultado.split("\n");
        ArrayList<String> related = new ArrayList();
        String[] var6 = resources;
        int var7 = resources.length;

        for(int var8 = 0; var8 < var7; ++var8) {
            String s = var6[var8];
            if (!s.trim().isEmpty()) {
                try {
                    s = URLDecoder.decode(s.trim(), "UTF-8");
                } catch (UnsupportedEncodingException var11) {
                    var11.printStackTrace();
                }

                related.add(s);
            }
        }

        return related;
    }

    public static final ArrayList<String> getCategoryByResource(String resource) {
        return dbpediaGet("distinct ?x where {<" + resource + ">  dct:subject ?x}");
    }

    public static final ArrayList<String> getBroaderCategory(String category) {
        return dbpediaGet("distinct ?x where {<" + category + ">  skos:broader ?x}");
    }

    public final ArrayList<String> getResourcesRelatedAndRelatedTo(Video video) throws IOException {
        ArrayList<String> related = new ArrayList();
        this.categorias = new ArrayList();
        StringBuilder buffer = new StringBuilder();
        Iterator var4 = video.getReferences().iterator();

        while(var4.hasNext()) {
            String reference = (String)var4.next();
            ArrayList referencesSameAs;
            if (languageIsPt(reference).booleanValue()) {
                referencesSameAs = getResourceSameAs(reference);
            } else {
                referencesSameAs = new ArrayList();
                referencesSameAs.add(reference);
            }

            Iterator var8 = referencesSameAs.iterator();

            while(var8.hasNext()) {
                String referenceSameAs = (String)var8.next();
                ArrayList<String> categories = getCategoryByResource(referenceSameAs);
                ArrayList<String> superCategorias = new ArrayList();
                Iterator var11 = categories.iterator();

                String category;
                while(var11.hasNext()) {
                    category = (String)var11.next();
                    superCategorias.addAll(getBroaderCategory(category));
                }

                var11 = superCategorias.iterator();

                while(var11.hasNext()) {
                    category = (String)var11.next();
                    related.add(category);
                    buffer.append(video.getId());
                    buffer.append("<http://qodra.ice.ufjf.br/category>");
                    buffer.append("<").append(category).append(">.");
                    this.categorias.add(category);
                }
            }
        }

        video.setCategories(this.categorias);
        return related;
    }
}
