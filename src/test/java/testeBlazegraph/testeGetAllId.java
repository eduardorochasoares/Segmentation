/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testeBlazegraph;

import blazegraph.Blazegraph;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 *
 * @author nicolasferranti
 */
public class testeGetAllId {

    public static void main(String args[]) throws UnsupportedEncodingException {
        blazegraph.Blazegraph bz = new Blazegraph("http://localhost:9999", "RNPvideo");
        /*
        ArrayList<String> cat = new ArrayList<String>();
        cat.add("<http://dbpedia.org/resource/Category:Chemical_compounds>");
        cat.add("<http://dbpedia.org/resource/Category:Chemical_mixtures>");
        cat.add("<http://dbpedia.org/resource/Category:Chemical_processes>");
        bz.getAllIdByCat(cat);
        */        
        //ArrayList<String> cat = bz.getRefences("<http://videoaula.rnp.br/rioflashclient.php?xmlfile=//dados/conversao_html5/instituicao/ufjf/administracao_publica/psicologia/aula1/pscaula1.xml>");
        
        //System.out.println(bz.requisicaoBZ("<http://qodra.ice.ufjf.br/345>", "<http://qodra.ice.ufjf.br/course>"));
    }
}
