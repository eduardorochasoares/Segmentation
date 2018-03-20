/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bavi.recommendation;

import jdk.nashorn.internal.runtime.arrays.ArrayLikeIterator;
import org.omg.CORBA.INTERNAL;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import sun.security.x509.AVA;

import java.util.*;
import java.util.concurrent.Executor;
import java.util.function.ToDoubleFunction;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
/**
 *
 * @author Claudio
 */


public class AG {

    private static final int NUM_THREADS = Runtime.getRuntime().availableProcessors();
    private static final int FALLBACK = 2;
    private static Executor pool = Executors.newFixedThreadPool(NUM_THREADS);
    private static int population = 200;
    private ArrayList<ArrayList<Double>> tf;
    private ArrayList<ArrayList<Integer>> ag = new ArrayList();
    private TFIDF tfidf;
    private static double alfa = 0.6;

    public AG(ArrayList<ArrayList<Double>> tf, TFIDF tfidf) {
        this.tf = tf;
        this.tfidf = tfidf;
    }

    public double MediaTermo(int termo, int inicio, int fim) {
        //System.out.println("inicio MediaTermo");
        double media = 0;
        for (int i = inicio; i <= fim; i++) {
            media += tf.get(termo).get(i);
        }
        media = (media / ((fim - inicio) + 1));
        //System.out.println("media" + media);
        return media;
    }

    public double Commonality(int inicio, int meio, int fim) {
        double soma = 0;
        double normaA = 0;
        double normaB = 0;
        double Sp;
        for (int i = 0; i < tf.size(); i++) {
            Sp = MediaTermo(i, inicio, fim);
            soma += Sp * IndicatorC(i, inicio, meio, fim);
        }
        for (int j = 0; j < tf.size(); j++) {
            normaA += MediaTermo(j, inicio, meio) * IndicatorNorma(j, inicio, meio);
            normaB += MediaTermo(j, meio, fim) * IndicatorNorma(j, meio, fim);
        }
        //System.out.println("soma" + soma);
        //System.out.println("normaA da soma" + normaA);
        //System.out.println("normaB da soma" + normaB);
        if (normaA == 0 || normaB == 0) {
            return 0;
        } else {
            return ((soma / normaA) + (soma / normaB));
        }
    }

    public int IndicatorC(int termo, int inicio, int meio, int fim) {
        if (tf.get(termo).get(inicio) != 0 && tf.get(termo).get(meio) != 0 && tf.get(termo).get(fim) != 0) {
            return 1;
        } else
            return 0;
    }

    public int IndicatorNorma(int termo, int inicio, int fim) {
        if (tf.get(termo).get(inicio) != 0 && tf.get(termo).get(fim) != 0) {
            return 1;
        } else
            return 0;
    }

    public double Difference(int inicio, int meio, int fim) {
        double diferenca = 0;
        double normaA = 0;
        double normaB = 0;
        double Sp;
        for (int i = 0; i < tf.size(); i++) {
            Sp = MediaTermo(i, inicio, fim);
            diferenca += Sp * IndicatorD(i, inicio, meio, fim);
        }
        for (int j = 0; j < tf.size(); j++) {
            normaA += MediaTermo(j, inicio, meio) * IndicatorNorma(j, inicio, meio);
            normaB += MediaTermo(j, meio, fim) * IndicatorNorma(j, meio, fim);
        }
        //System.out.println("diferenca" + diferenca);
        //System.out.println("normaA da diferenca" + normaA);
        //System.out.println("normaB de diferenca" + normaB);
        if (normaA == 0 || normaB == 0) {
            return 0;
        } else {
            return ((diferenca / normaA) + (diferenca / normaB));
        }
    }

    public int IndicatorD(int termo, int inicio, int meio, int fim) {
        if (tf.get(termo).get(inicio) == 0 && tf.get(termo).get(meio) != 0 && tf.get(termo).get(fim) == 0) {
            return 1;
        }
        return 0;
    }

    public void InicalizaVetor() {
        for (int i = 0; i < population; i++) {
            ArrayList<Integer> solucoes = new ArrayList();
            Random rand = new Random();
            for (int j = 0; j < tf.get(0).size(); j++) {
                solucoes.add(rand.nextInt(2));
            }
            ag.add(solucoes);
        }
    }

    //modificar futuramente pra algoritmo mais eficiente
    public ArrayList<ArrayList<Integer>> ordena() {
        /**QuickSort(ag, 0, ag.size() - 1);
         return ag;**/
        // Para ordenar por numeros
        ordenaPorNumero(ag);
        return ag;
    }
    public void ordenaPorNumero(ArrayList<ArrayList<Integer>> lista) {
        CompareFitness cf = new CompareFitness();
        Collections.sort(lista, cf);
    }
    class CompareFitness implements Comparator<ArrayList<Integer>>
    {
        @Override
        public int compare(ArrayList<Integer> o1, ArrayList<Integer> o2) {
            return Double.compare(AvaliaS(o1), AvaliaS(o2));
        }
    }


    public double AvaliaS(ArrayList<Integer> vet) {
        Avalia av = new Avalia(tfidf);

           return (alfa* av.sumSimilaridadeInterCenas(vet)) + (1-alfa)*(1/av.sumSimilaridadeIntraCenas(vet));

    }

    public void Mutacao(int i, double j) {
        Random rand = new Random();
        ArrayList<Integer> aux;
        for (int k = 0; k < j * population; k++) {
            aux = ag.get(i + rand.nextInt(population - i));
            //for (int x = 0; x < tf.get(0).size(); x++) {
            int x = rand.nextInt(tf.get(0).size());
            if(aux.get(x) == 0){
                aux.set(x, 1);

            }else{
                aux.set(x, 0);

            }


            //}

        }
    }

    public void CrossOver(int i, int x) {
        Random rand = new Random();
        ArrayList<Integer> aux;
        for (int j = x; j < population; j++) {
            int ag1 = rand.nextInt(x + 1);
            int ag2 = rand.nextInt(x + 1);
            aux = ag.get(j);
            /*for(int c : aux){
                System.out.print(c);
            }*/
            int k1 = rand.nextInt(tf.get(0).size() + 1);
            int k2 = rand.nextInt(tf.get(0).size() + 1);


            for (int k = 0; k < k1; k++) {
                aux.set(k, ag.get(ag1).get(k));
            }
            for (int k = k1; k < k2; k++) {
                aux.set(k, ag.get(ag2).get(k));
            }
            for (int k = k2; k < tf.get(0).size(); k++) {
                aux.set(k, ag.get(ag1).get(k));
            }
            //System.out.print(" ");

            /*for(int c : aux){
                System.out.print(c);
            }*/
            //System.out.println();
        }
        if (ag.get(0).get(0) == 0) {
            ag.get(0).set(0, 1);

        } else {
            ag.get(0).set(0, 0);
        }
    }

    public void Main() {
        double bestSolution = Double.MAX_VALUE;
        int iterations_without_improviment = 0;
        System.out.println("0");
        InicalizaVetor();
        int k = 0;
        while (k < 500 && iterations_without_improviment < 100) {
            System.out.println("1");
            int x = (int) (population * 0.3) - 1;
            ag = ordena();
            System.out.println("2");
            double valorSolucao = AvaliaS(ag.get(0));
            System.out.println(valorSolucao);
            if(valorSolucao < bestSolution){
                bestSolution = valorSolucao;
                iterations_without_improviment = 0;
            }else{
                iterations_without_improviment++;
            }
            Mutacao(x, 0.1);
            System.out.println("iteração: " + k);
            CrossOver(x, ((int) ((population - x) * 0.1) + x));
            System.out.println("3");

            k++;
        }
        for (int j = 0; j < tf.get(0).size(); j++) {
            System.out.print(j + ":" + ag.get(0).get(j) + "  ");
        }
    }

    void swap(ArrayList<ArrayList<Integer>> arr, int i, int j)
    {
        ArrayList<Integer> t = arr.get(i);
        arr.set(i, arr.get(j));
        arr.set(j, t);
    }

    int partition (ArrayList<ArrayList<Integer>> arr, int l, int h)
    {
        double x = AvaliaS(arr.get(h));
        int i = (l - 1);

        for (int j = l; j <= h- 1; j++)
        {
            if (AvaliaS(arr.get(j)) >= x)
            {
                i++;
                // swap arr[i] and arr[j]
                swap(arr,i,j);
            }
        }
        // swap arr[i+1] and arr[h]
        swap(arr,i+1,h);
        return (i + 1);
    }

    // Sorts arr[l..h] using iterative QuickSort
    void QuickSort(ArrayList<ArrayList<Integer>> arr, int l, int h)
    {
        // create auxiliary stack
        int stack[] = new int[h-l+1];

        // initialize top of stack
        int top = -1;

        // push initial values in the stack
        stack[++top] = l;
        stack[++top] = h;

        // keep popping elements until stack is not empty
        while (top >= 0)
        {
            // pop h and l
            h = stack[top--];
            l = stack[top--];

            // set pivot element at it's proper position
            int p = partition(arr, l, h);

            // If there are elements on left side of pivot,
            // then push left side to stack
            if ( p-1 > l )
            {
                stack[ ++top ] = l;
                stack[ ++top ] = p - 1;
            }

            // If there are elements on right side of pivot,
            // then push right side to stack
            if ( p+1 < h )
            {
                stack[ ++top ] = p + 1;
                stack[ ++top ] = h;
            }
        }
    }

    // A utility function to print contents of arr
    void printArr( int arr[], int n )
    {
        int i;
        for ( i = 0; i < n; ++i )
            System.out.print(arr[i]+" ");
    }



}




    




