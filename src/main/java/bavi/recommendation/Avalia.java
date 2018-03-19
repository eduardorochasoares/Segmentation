/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bavi.recommendation;

import java.util.ArrayList;

/**
 *
 * @author Claudio
 */
public class Avalia {
    private TFIDF tf;
    private ArrayList<Integer> solucao;
    
    public Avalia(TFIDF tfidf){
        this.solucao = new ArrayList();
        this.tf = tfidf;
    }
    
    /*public double sumSimilaridadeIntraCenas (ArrayList<Integer> vet){
        double media = 0;
        double mediaC = 0;
        int i = 0;
        int j = 1;
        double similarity[][] = tf.getSimilarityTable();
        while(j < vet.size()-1){
            while(vet.get(j+1) != 1){
                i++;
                if(vet.get(j) == 1){
                    break;
                }else if(vet.get(j) == 0 && vet.get(j+1) == 1){
                    media += similarity[j-1][j];
                }else if(vet.get(j) == 0 && vet.get(j+1) == 0){
                    media += similarity[j-1][j] + similarity[j][j+1];
                }
                media /= i;
                j++;
                if(j >= vet.size() - 1) break;
            }
            i = 0;
            mediaC += media;
            media = 0;
            j++;
        }
        return mediaC;
    }*/

    public double sumSimilaridadeIntraCenas (ArrayList<Integer> vet){
        double media = 0;
        double mediaC = 0;
        int i = 0;
        int j = 0;
        double similarity[][] = tf.getSimilarityTable();
        while(j < vet.size()){
            while(vet.get(j) != 1){
                i++;
                if(vet.get(j) == 1){
                    break;
                    //esta dentro do range do vetor e estao todos na mesma cena
                }else if(j-1 >= 0 && j -2 >= 0 && j+1 <= vet.size() - 1 && j + 2 <= vet.size() -1 &&
                        vet.get(j) == 0 && vet.get(j-1) == 0 && vet.get(j + 1) == 0 && vet.get(j + 2) == 0){
                    media+=similarity[j-2][j] + similarity[j-1][j] + similarity[j+1][j] + similarity[j+2][j];
                    //estao dentro do range do vetor, mas o vizinho da frente nao esta na mesma cena,
                    // apenas os dois anteriores
                }else if(j-1 >= 0 && j -2 >= 0 && j+1 <= vet.size() - 1 && j + 2 <= vet.size() -1 &&
                        vet.get(j) == 0 && vet.get(j-1) == 0 && vet.get(j + 1) == 1){
                    media+=similarity[j-2][j] + similarity[j-1][j];
                    //apenas o ultimo vizinho de tras nao esta no range do vetor e todos os outros estao na cena
                }else if(j-1 >= 0 && j - 2 < 0 && j +1 <= vet.size() - 1 && j + 2 <= vet.size() -1 &&
                        vet.get(j) == 0 && vet.get(j+1) == 0 && vet.get(j + 2) == 0) {
                    // apenas o ultimo vizinho de tras nao esta no range do vetor mas o vizinho da frente nao esta
                    //na mesma cena
                }else if(j-1 >= 0 && j - 2 < 0 && j +1 <= vet.size() - 1 && j + 2 <= vet.size() -1 &&
                        vet.get(j) == 0 && vet.get(j+1) == 1){
                    media += similarity[j - 1][j];
                    // todos vizinhos estao no range, e o ultimo vizinho nao faz parte da mesma cena
                }else if(j-1 >= 0 && j -2 >= 0 && j+1 <= vet.size() - 1 && j + 2 <= vet.size() -1 &&
                        vet.get(j) == 0 && vet.get(j-1) == 1 && vet.get(j + 1) == 0 && vet.get(j + 2) == 0){
                    media+=  similarity[j-1][j] + similarity[j+1][j] + similarity[j+2][j];

                //os vizinhos de trás nao estao no range, os da frente estao e pertencem a cena
                }else if (j -1 < 0 && j + 1 <= vet.size() - 1 && j +2 <= vet.size() - 1 &&
                        vet.get(j) == 0 && vet.get(j + 1) == 0 && vet.get(j + 2) == 0){
                    media += similarity[j+1][j] + similarity[j +2][j];
                    //apenas o primeiro vizinho da frente esta no range do vetor e na cena
                }else if( j- 1  < 0 && j + 1 <= vet.size() - 1 && j + 2 > vet.size() - 1 && vet.get(j +1) == 0 ){
                    media+= similarity[j+1][j];
                    //apenas os vizinhos de tras estao no range e na cena
                }else if (j -1 >= 0 && j - 2 >= 0 && j + 1 > vet.size() - 1 &&
                        vet.get(j-1) == 0){
                    media += similarity[j-2][j] + similarity[j-1][j];
                    //apenas os vizinhos de tras estao no range, porem apenas o primeiro vizinho de tras esta
                    //na cena
                }else if (j -1 >= 0 && j - 2 >= 0 && j + 1 > vet.size() - 1 &&
                        vet.get(j-1) == 1){
                    media +=  similarity[j-1][j];
                    //todos os vizinhos no range e na cena, menos o ultimo vizinho da frente
                }else if(j -1 >= 0 && j -2 >= 0 && j + 1 <= vet.size() -1 && j + 2 > vet.size() - 1
                        && vet.get(j-1) == 0 && vet.get(j+1) == 0){
                    media+= similarity[j-2][j] + similarity[j-1][j] + similarity[j+1][j];
                    //vizinhos de tras no range, porem o ultimo de tras nao esta na cena
                    //ultimo vizinho da frente nao esta no range
                }else if(j -1 >= 0 && j -2 >= 0 && j + 1 <= vet.size() -1 && j + 2 > vet.size() - 1
                    && vet.get(j-1) == 1 && vet.get(j+1) == 0) {
                    media += similarity[j - 1][j] + similarity[j + 1][j];
                    //apenas o primeiro vizinho da frente e o primeiro de tras estão na cena
                }else if( j -1 >= 0 && j -2 < 0 && j + 1 <= vet.size() - 1 && j + 2 > vet.size() - 1 &&
                        vet.get(j + 1) == 0){
                        media += similarity[j-1][j] + similarity[j+1][j];

                }else{
                    media+= 0;
                }
                media /= i;
                j++;
                if(j >= vet.size() - 1) break;
            }
            i = 0;
            mediaC += media;
            media = 0;
            j++;
        }
        return mediaC;
    }
    public double sumSimilaridadeInterCenas(ArrayList<Integer> vet){
        ArrayList<ArrayList<Double>> centroidCena  = CalculaCentroid(vet);
        double media = 0;
        double mediaC = 0;
        int i = 0;
        for(int j = 1; j < centroidCena.size()-1; j++){
            media += tf.similarityCenas(centroidCena.get(j-1), centroidCena.get(j)) + 
                    tf.similarityCenas(centroidCena.get(j), centroidCena.get(j+1));
                    //j = j  + 1;
            i = 0;
            mediaC += media;
            media = 0;
        }
        return mediaC;
    }
            
    public ArrayList<ArrayList<Double>> CalculaCentroid(ArrayList<Integer> vet){
        ArrayList<ArrayList<Double>> centroidCena = new ArrayList();
        CriaVetPos(vet);
        double acumula = 0;
        int cont;
        int inicio = 0;
        int i = 0;
        i = solucao.get(0);
        for(int k = 1; k < solucao.size();k++ ){
            ArrayList<Double> s = new ArrayList<>();
            for(int j = 0; j < tf.getTfidf().size(); j++){
               while(i < solucao.get(k)){
                   acumula+=tf.getTfidf().get(j).get(i);
                   i++;
               }
               s.add(acumula/i);
              
            }
            centroidCena.add(s);
            i = solucao.get(k);
        }
       
        return centroidCena;
    }
    
    public void CriaVetPos(ArrayList<Integer> vet){
        for(int i = 0; i < vet.size(); i++){
            if(vet.get(i) == 1){
                solucao.add(i);
            }
        }
    }
}
