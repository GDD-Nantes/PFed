/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PFSQGen;

import PFSQGen.Summary;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.query.ARQ;

import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

/**
 *
 * @author khiyari-s
 */
public class TypeSummary extends Summary{
    
    public TypeSummary(String inputFile) {
       
        BufferedReader br = null;
        //int line=0;
        boolean syntaxParser = false;
        int charParserPosition = 0;
        int lastParserPosition = 0;
        int searchPatternPos = 0;
        try {
            String sCurrentLine;

            br = new BufferedReader(new FileReader(inputFile));
            while ((sCurrentLine = br.readLine()) != null) {
                if (sCurrentLine.indexOf("capability") >= 0) {
                    Capability tempCap = new Capability();
                    if ((sCurrentLine = br.readLine()) != null) {
                        if (sCurrentLine.indexOf("[") >= 0) {
                            syntaxParser = true;
                            while (((sCurrentLine = br.readLine()) != null) && (syntaxParser)) {
                                if (sCurrentLine.indexOf("]") >= 0) {
                                    syntaxParser = false;
                                    break;
                                } else {
                                    if (sCurrentLine.startsWith("predicate")) {
                                        String temp = sCurrentLine.split(" ")[1];
                                        if (temp != null) //predicate.add(temp);
                                            tempCap.setPredicate(temp);
                                    }
                                    if (sCurrentLine.startsWith("Subject Classes")) {
                                        String[] tempT = sCurrentLine.split(": ");
                                        if ((tempT.length)>1) {
                                            //tempCap.addSbjAuthority(tempT[1]);
                                            String[] tempTList = tempT[1].split(", ");
                                            for (int i=0; i<tempTList.length; i++) {
                                                tempCap.addSbjAuthority(tempTList[i]);
                                            }
                                        }
                                    }
                                    if(sCurrentLine.startsWith("Object Classes")) {
                                        String[] tempT = sCurrentLine.split(": ");
                                        if ((tempT.length)>1) {
                                            //tempCap.addObjAuthority(tempT[1]);
                                            String[] tempTList = tempT[1].split(", ");
                                            for (int i=0; i<tempTList.length; i++) {
                                                tempCap.addObjAuthority(tempTList[i]);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    this.capabilities.add(tempCap);
                }
            }
        }
        catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {

                if(br != null)
                    br.close();

            } catch (IOException ex) {

                ex.printStackTrace();

            }
        }
    }
    public static void generateSummary(String endpoint, String outputFile, ExecutionStrategy exec) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("/tmp/listpred.txt"));
        System.out.println("Generating predicate list");
        String q = "SELECT DISTINCT ?p WHERE { "
                    + "?s ?p ?o . "
                    + "}";
        List<RDFNode> result = exec.execute1Field(q,endpoint,"p");
        if(result.size()>0){
            for ( RDFNode x : result ){
                if(x != null){
                    bw.write(x.toString());
                    bw.newLine();
                }
            }
        }
//         TypeSummary.generateSummary(endpoint, "/tmp/listpred.txt", outputFile, exec);
    }
    public static void generateSummary(String currAuth, String endpoint, String inputFile, String outputFile, ExecutionStrategy exec, Summary authSum) throws IOException {
        ARQ.init();
        Map<String,String> authToSrc = new HashMap<>();
        authToSrc.put("http://data.semanticweb.org","http://172.16.9.3:3030/swdf/sparql");
        authToSrc.put("http://dbpedia.org","http://172.16.9.3:3030/dbpedia/sparql");
        
        Set<RDFNode> result = new HashSet<>();
        BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));
        String sCurrentLine,q;
        BufferedReader br = new BufferedReader(new FileReader(inputFile));
        while ((sCurrentLine = br.readLine()) != null) {
            String currPred = sCurrentLine.toString();
            System.out.println("Generating Subject types for predicate : "+ currPred);
            result.clear();
            for(String auth : authSum.getSbjSet(currPred) ){
                if(auth.equals(currAuth)){
                    q = "SELECT DISTINCT ?type WHERE { "
                            + "?s <"+currPred+"> ?o . "
                            + "FILTER (isURI(?s) ) \n"
                            + " ?s <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?type  } ";
                    result.addAll(exec.execute1Field(q,endpoint,"type"));                
                }else {
                if(authToSrc.containsKey(auth)){
                System.out.println(auth);
                    q = "SELECT DISTINCT ?type WHERE { "
                            + "?s <"+currPred+"> ?o . "
                            + "FILTER (isURI(?s) ) \n"
                            + "FILTER (regex(STR(?s), '"+auth+"'))"
                            + "SERVICE <"+authToSrc.get(auth)+"> {"
                            + " ?s <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?type  }} ";
                    result.addAll(exec.execute1Field(q,endpoint,"type"));
                    }
                }
            }
            bw.write("capability\n");
            bw.write("[\n");
            bw.write("predicate: "+currPred);
            if(result.size()>0){
                bw.write("\nSubject Classes: ");
                for ( RDFNode x : result ){
                    if(x != null)
                        bw.write(x+", ");
                }
            }
            System.out.println("Generating Object types for predicate : "+ currPred);
            result.clear();
            for(String auth : authSum.getObjSet(currPred) ){
                if(auth.equals(currAuth)){
                    q = "SELECT DISTINCT ?type WHERE { "
                            + "?s <"+currPred+"> ?o . "
                            + "FILTER (isURI(?o) ) \n"
                            + " ?o <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?type  } ";
                    result.addAll(exec.execute1Field(q,endpoint,"type"));                
                }else 
                if(authToSrc.containsKey(auth)){
                    q = "SELECT DISTINCT ?type WHERE { "
                            + "?s <"+currPred+"> ?o . "
                            + "FILTER (isURI(?o) ) \n"
                            + "FILTER (regex(STR(?o), '"+auth+"'))"
                            + "SERVICE <"+authToSrc.get(auth)+"> {"
                            + " ?o <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?type  }} ";
                    result.addAll(exec.execute1Field(q,endpoint,"type"));        
                }
            }
            if(result.size() > 0){
            bw.write("\nObject Classes: ");
                for ( RDFNode x : result ){
                    if(x != null)
                        bw.write(x+", ");
                }
            }
            bw.write("\n] ;\n");
        }
        bw.close();
    }
    public static void main(String[] args) throws Exception{
        if(args.length != 6){
            System.out.println("Need 6 arguments. Usage TypeSummary endpointURL PredicateFile OutputFile EndpointType AuthSummaryFile authorityEndpoint");
            System.out.println("EndpointType can be sage or fuseki");
            return;
        }
        ExecutionStrategy exec;
        /*if(args[3].equals("sage")){
            exec = new SageExecution();
        }else */if(args[3].equals("fuseki")){
            exec = new FusekiExecution();
        }else{
            System.out.println("Unknown endpoint type : " + args[3]);
            System.out.println("Use sage or fuseki");
            return;
        }
        TypeSummary.generateSummary(args[5], args[0], args[1], args[2], exec, new AuthSummary(args[4]));
    }
}

