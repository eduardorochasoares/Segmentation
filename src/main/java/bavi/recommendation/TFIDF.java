//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package bavi.recommendation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class TFIDF {
    private ArrayList<ArrayList<Double>> tfidf = new ArrayList<>();
    private ArrayList<String> terms = new ArrayList<>();
    private ArrayList<Video> videos = new ArrayList<>();
    private ArrayList<Scene> scenes = new ArrayList<>();
    private ArrayList<Double> custo = new ArrayList<>();
    private double[][] sim;

    public TFIDF() {
    }

    public ArrayList<ArrayList<Double>> getTfidf() {
        return this.tfidf;
    }

    public void setTfidf(ArrayList<ArrayList<Double>> tfidf) {
        this.tfidf = tfidf;
    }

    public ArrayList<String> getTerms() {
        return this.terms;
    }

    public void setTerms(ArrayList<String> terms) {
        this.terms = terms;
    }

    public void calculateTFIDF() {
        int i;
        for(i = 0; i < this.terms.size(); ++i) {
            int ni = this.countOccurrences((String)this.terms.get(i));
            ArrayList<Double> auxList = new ArrayList<>();

            for(int j = 0; j < this.videos.size(); ++j) {
                int fij = this.countFrequency((String)this.terms.get(i), (Video)this.videos.get(j));
                if (fij > 0) {
                    auxList.add((1.0D + Math.log10((double)fij) / Math.log10(2.0D)) * (Math.log10((double)(this.videos.size() / ni)) / Math.log10(2.0D)));
                } else {
                    auxList.add(0.0D);
                }
            }

            this.tfidf.add(auxList);
        }
        
        inicializaVet(tfidf.size());
        
        this.sim = new double[this.videos.size()][this.videos.size()];

        int j;
        for(i = 0; i < this.videos.size(); ++i) {
            for(j = 0; j < this.videos.size(); ++j) {
                this.sim[i][j] = this.similarity(i, j);
            }
        }

        this.makeScenes();


        for(i = 0; i < this.scenes.size(); ++i) {
            System.out.print("Cena nº " + i + ": ");

            for(j = 0; j < ((Scene)this.scenes.get(i)).getChunks().size(); ++j) {
                System.out.print(this.videos.get(this.scenes.get(i).getChunks().get(j)).getId() + " ");

            }

            System.out.print(" Categories: ");

            for (String s : ((Scene) this.scenes.get(i)).getCategories()) {
                System.out.print(s + " ");
            }

            System.out.print("\n");
        }

    }

    private int countOccurrences(String term) {
        int num = 0;
        Iterator<Video> var3 = this.videos.iterator();

        while(var3.hasNext()) {
            Video v = var3.next();
            if (v.getCategories() != null && v.getCategories().contains(term)) {
                ++num;
            }

            if (v.getReferences() != null && v.getReferences().contains(term)) {
                ++num;
            }
        }

        return num;
    }

    private int countFrequency(String term, Video v) {
        int num = 0;
        Iterator<String> var4;
        String s;
        if (v.getCategories() != null) {
            var4 = v.getCategories().iterator();

            while(var4.hasNext()) {
                s = var4.next();
                if (s.equals(term)) {
                    ++num;
                }
            }
        }

        var4 = v.getReferences().iterator();

        while(var4.hasNext()) {
            s = var4.next();
            if (s.equals(term)) {
                ++num;
            }
        }

        return num;
    }

    public ArrayList<Video> getVideos() {
        return this.videos;
    }

    public void setVideos(ArrayList<Video> videos) {
        this.videos = videos;
    }

    public double similarityCenas(ArrayList<Double> vectorA, ArrayList<Double> vectorB) {
        
        double sum = 0.0D;
        double normA = 0.0D;
        double normB = 0.0D;

        for(int i = 0; i < vectorA.size(); ++i) {
            sum += ((Double)vectorA.get(i)).doubleValue() * ((Double)vectorB.get(i)).doubleValue();
            normA += ((Double)vectorA.get(i)).doubleValue() * ((Double)vectorA.get(i)).doubleValue();
            normB += ((Double)vectorB.get(i)).doubleValue() * ((Double)vectorB.get(i)).doubleValue();
        }

        normA = Math.sqrt(normA);
        normB = Math.sqrt(normB);
        double s = sum / (normA * normB);
        return s;
    }
     public double similarity(int v1, int v2) {
        ArrayList<Double> vectorA = new ArrayList<>();
        ArrayList<Double> vectorB = new ArrayList<>();

        for(int i = 0; i < this.terms.size(); ++i) {
            vectorA.add((Double) ((ArrayList)this.tfidf.get(i)).get(v1));
            vectorB.add((Double) ((ArrayList)this.tfidf.get(i)).get(v2));
        }

        double sum = 0.0D;
        double normA = 0.0D;
        double normB = 0.0D;

        for(int i = 0; i < vectorA.size(); ++i) {
            sum += ((Double)vectorA.get(i)).doubleValue() * ((Double)vectorB.get(i)).doubleValue();
            normA += ((Double)vectorA.get(i)).doubleValue() * ((Double)vectorA.get(i)).doubleValue();
            normB += ((Double)vectorB.get(i)).doubleValue() * ((Double)vectorB.get(i)).doubleValue();
        }

        normA = Math.sqrt(normA);
        normB = Math.sqrt(normB);
        double s = sum / (normA * normB);
        return s;
    }

    public void makeScenes() {
        /*int current = 0;
        boolean similar = false;
        Scene s = new Scene();
        this.scenes.add(s);
        s.getChunks().add(current);
        s.getCategories().addAll(this.union(((Video)this.videos.get(current)).getCategories(), ((Video)this.videos.get(current)).getReferences()));

        while(true) {
            do {
                if (current + 1 >= this.videos.size()) {
                    return;
                }

                similar = false;
                ++current;
                Iterator<Integer> var5 = s.getChunks().iterator();

                while(var5.hasNext()) {
                    int a = (var5.next()).intValue();
                    if (this.sim[current][a] > 0.8) {

                        s.getChunks().add(current);
                        similar = true;
                        s.getCategories().addAll(union(this.videos.get(current).getCategories(),
                                this.videos.get(current).getReferences()));
                        //ArrayList<String> aux = this.union(((Video)this.videos.get(current)).getCategories(), ((Video)this.videos.get(current)).getReferences());
                        //s.setCategories(aux);
                        break;
                    }
                }
            } while(similar);

            s = new Scene();
            this.scenes.add(s);
            s.getChunks().add(current);

            s.getCategories().addAll(union(this.videos.get(current).getCategories(),
                    this.videos.get(current).getReferences()));
        }*/
        //System.out.println("chama SSD");
        //SSD(0,tfidf.get(0).size()-1,0);
        AG ag = new AG(tfidf, this);
        ag.Main();
    }

    public <T> ArrayList<T> intersection(ArrayList<T> list1, ArrayList<T> list2) {
        ArrayList<T> list = new ArrayList<>();
        Iterator<T> var4 = list1.iterator();

        while(var4.hasNext()) {
            T t = var4.next();
            if (list2.contains(t)) {
                list.add(t);
            }
        }

        return list;
    }

    public <T> ArrayList union(ArrayList<T> list1, ArrayList<T> list2) {
        Set<T> set = new HashSet<>();
        set.addAll(list1);
        set.addAll(list2);
        return new ArrayList(set);
    }
    
    public double desvioQuad(int inicio, int fim){
        //System.out.println("inicio desvioQuad");
        double mediaTermo;
        double soma = 0;
        double desvio = 0;
        for(int i = 0; i < tfidf.size(); i++){
            mediaTermo = MediaTermo(i,inicio,fim);
            for(int j = inicio; j < fim; j++ ){
                desvio += Math.pow(tfidf.get(i).get(j) - mediaTermo,2);
            }
            soma += desvio;
        }
        //System.out.println("Soma " + soma);
        return soma;
    }
    
    public double MediaTermo(int termo, int inicio, int fim){
        //System.out.println("inicio MediaTermo");
        double media = 0;
        for(int i = inicio; i <= fim; i++){ 
            media += tfidf.get(termo).get(i);
        }
        media = (media/((fim - inicio) + 1));
        //System.out.println("media" + media);
        return media;
    }
    
    public void SSD(int inicio, int fim, int cont){
        if((fim - inicio) <= 0 ){
            return;
        }
        //System.out .println("inicio " + inicio + " fim " + fim);
        //System.out.println("cont " + cont);
        if(cont > 3){
            return;
        }
        //System.out.println("inicio SSD");
        double somaT = desvioQuad(inicio,fim);
        //System.out.println("soma total " + somaT);
        double somaLa = 0;
        double somaLb = 0;
        for (int i = inicio; i <= fim; i++){
            somaLa = desvioQuad(inicio,i-1);
            somaLb = desvioQuad(i+1, fim);
            //System.out.println("custo " + (somaT - (somaLa - somaLb)));
            //System.out.println("soma esquerda " + somaLa);
            //System.out.println("soma direita " + somaLb);
            //System.out.println(" ");
            custo.set(i , somaT - (somaLa + somaLb));
        }
        //System.out.println("tamanho do vetor de custo " + custo.size());
        //System.out.println("Calcula aresta de corte");
        double maior = Double.NEGATIVE_INFINITY;
        int indexMaior = -1;
        for(int i = inicio; i < fim; i++){
            if(custo.get(i) > maior){
                maior = custo.get(i);
                indexMaior = i;
                //System.out.println("indice do maior custo " + indexMaior);
            }
        }
        System.out.println("Similaridade: " + similarity(indexMaior, indexMaior+1));
        if(similarity(indexMaior, indexMaior+1) < 0.75){
            System.out.println("(" + indexMaior + ", " + (indexMaior+1) + " )" + "Aresta de corte");
        }
        SSD(inicio, indexMaior , cont+1);
        SSD(indexMaior+1, fim, cont+1);
    }
    
    private void inicializaVet(int tam){
        for(int i = 0; i < tam; i++){
            custo.add(Double.NEGATIVE_INFINITY);
        }
    }
}
