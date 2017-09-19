/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blazegraph;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author nicolasferranti
 */
public class Blazegraph {

    private static String dbName;
    private static String blazeUrl;

    public Blazegraph(String URLdest, String databaseName) {
        this.dbName = databaseName;
        this.blazeUrl = URLdest;
    }

    private String requisicaoBZ(String sujeito, String predicado) {
        String url = this.blazeUrl + "/blazegraph/namespace/" + this.dbName + "/sparql";

        URL obj;
        try {
            obj = new URL(url);

            HttpURLConnection con;
            try {
                con = (HttpURLConnection) obj.openConnection();
                con.setRequestProperty("Accept", "application/json");
                con.setRequestMethod("POST");

                //String message = "INSERT DATA {<http://example/book1> <dc:title> \"A new book loucooohadusdhusioooo\" . }";
                String message = "select ?o {" + sujeito + " " + predicado + " ?o }";
                System.out.println("Mensagem: " + message);
                String urlParameters = "query=" + URLEncoder.encode(message);

                // Send post request
                con.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(urlParameters);
                wr.flush();
                wr.close();
                int responseCode = con.getResponseCode();

                BufferedReader br = new BufferedReader(new InputStreamReader((con.getInputStream())));
                StringBuilder x = new StringBuilder();
                String output;
                while ((output = br.readLine()) != null) {
                    x.append(output);
                }
                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + urlParameters);
                System.out.println("Response Code : " + responseCode);
                System.out.println("Output Message : " + x);
                return x.toString();
            } catch (IOException ex) {
                System.out.println("IO EXCEPTION");
            }
        } catch (MalformedURLException ex) {
            System.out.println("MALFORMED EXCEPTION");
        }
        return null;
    }

    public final ArrayList<String> getRefences(String sujeito) throws UnsupportedEncodingException {
        //final String DCTERMS = "<dcterms:references>";
        final String DCTERMS = "<http://purl.org/dc/terms/references>";

        ArrayList<String> resposta = new ArrayList<>();

        String x = requisicaoBZ(sujeito, DCTERMS);
        System.out.println(x);
        JSONObject jsonObject = new JSONObject(x);
        //jsonObject aux = jsonObject.get("x");
        JSONArray res = (jsonObject.getJSONObject("results")).getJSONArray("bindings");//.get("head").get("vars[0]");
        for (int i = 0; i < res.length() - 1; i++) {
            JSONObject value = res.getJSONObject(i);
            String result = value.getJSONObject("o").getString("value");
            //System.out.println(result);
            resposta.add(result);
        }
        return resposta;

    }

    /**
     * RETORNA TODOS OS IDS DOS VIDEOS QUE POSSUEM UM CONJUNTO DE CATEGORIAS E A
     * QUANTIDADE QUE CADA UM POSSUI, CASO NAO ENCONTRE NENHUM RETORNA NULL
     */
    public Map<String, Integer> getAllIdByCat(ArrayList<String> categorias) {
        String url = this.blazeUrl + "/blazegraph/namespace/" + this.dbName + "/sparql";
        URL obj;
        try {
            obj = new URL(url);

            HttpURLConnection con;
            try {
                con = (HttpURLConnection) obj.openConnection();
                con.setRequestProperty("Accept", "application/json");
                con.setRequestMethod("POST");

                boolean first = true;
                String message = "select ?s (count(?s) as ?total) where {";
                for (String cat : categorias) {
                    if (first) {
                        message += "{ ?s <dcterms:category> <" + cat + "> }"; //<http://qodra.ice.ufjf.br/category>
                        first = false;
                    } else {
                        message += "union { ?s <dcterms:category> <" + cat + ">} ";//<http://qodra.ice.ufjf.br/category>
                    }
                }
                message += "} GROUP BY ?s ORDER BY  DESC(?total) LIMIT 15";

                System.out.println("Mensagem: " + message);
                String urlParameters = "query=" + URLEncoder.encode(message);

                // Send post request
                con.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(urlParameters);
                wr.flush();
                wr.close();
                int responseCode = con.getResponseCode();

                BufferedReader br = new BufferedReader(new InputStreamReader((con.getInputStream())));
                StringBuilder x = new StringBuilder();
                String output;
                while ((output = br.readLine()) != null) {
                    x.append(output);
                }

                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + urlParameters);
                System.out.println("Response Code : " + responseCode);
                System.out.println("Output Message : " + x);
                JSONObject jsonObject = new JSONObject(x.toString());

                ArrayList<String> resposta = new ArrayList<>();
                Map<String, Integer> result = new HashMap<String, Integer>();
                try {
                    JSONArray res = (jsonObject.getJSONObject("results")).getJSONArray("bindings");//.get("head").get("vars[0]");
                    for (int i = 0; i < res.length() - 1; i++) {
                        JSONObject value = res.getJSONObject(i);
                        result.put(value.getJSONObject("s").getString("value"), Integer.parseInt(value.getJSONObject("total").getString("value")));

                    }
                    return result;
                } catch (org.json.JSONException notFound) { // case of first video insertion, no videos in the database
                    return null;
                }
            } catch (IOException ex) {
                System.out.println("IO EXCEPTION");
            }
        } catch (MalformedURLException ex) {
            System.out.println("MALFORMED EXCEPTION");
        }
        return null;
    }

}
