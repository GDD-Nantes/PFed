package PFSQGen;

import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sail.SailRepository;
import com.fluidops.fedx.Config;
import com.fluidops.fedx.FedXFactory;
import com.fluidops.fedx.exception.FedXException;
import com.fluidops.fedx.FedXFactory;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Map;
import org.apache.jena.graph.Node;
import org.apache.jena.query.ARQ;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.sparql.core.TriplePath;
import org.apache.jena.sparql.syntax.ElementPathBlock;
import org.apache.jena.sparql.syntax.ElementVisitorBase;
import org.apache.jena.sparql.syntax.ElementWalker;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.repository.sail.SailRepository;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.rdf.model.RDFNode;

import org.apache.jena.query.QueryException;
import PFSQGen.MatchingPredicates;
import PFSQGen.Queries;
import PFSQGen.SparqlQueryParser;
/**                                                                                                                                                       
 *                                                                                                                                                        
 * @author khiyari-s                                                                                                                                      
 */
public class PruningLogs {
    private Queries queries1;
    private Queries queries2;
//     Queries queries1SELECT = new Queries();
//     Queries queries2SELECT = new Queries();
    ArrayList<SparqlQueryParser> queries1prunPath = new ArrayList<SparqlQueryParser>();
    ArrayList<SparqlQueryParser> queries1prunStar = new ArrayList<SparqlQueryParser>();
    ArrayList<SparqlQueryParser> queries2prunPath = new ArrayList<SparqlQueryParser>();
    ArrayList<SparqlQueryParser> queries2prunStar = new ArrayList<SparqlQueryParser>();
    ArrayList<SparqlQueryParser> queries2prun = new ArrayList<SparqlQueryParser>();
    private MatchingPredicates matcher;
    private ArrayList<Node> listPredicatesLog1Path = new ArrayList<Node>();
    private ArrayList<Node> listPredicatesLog1Star = new ArrayList<Node>();
    private ArrayList<Node> listPredicatesLog2 = new ArrayList<Node>();
    private ArrayList<Node> listPredicatesLog2Path = new ArrayList<Node>();
    private ArrayList<Node> listPredicatesLog2Star = new ArrayList<Node>();
    HashMap<String, HashSet<String>> dicJoinPath;
    HashMap<String, HashSet<String>> dicJoinStar;
    private SailRepository repo;
    private ArrayList<String> genFedMinimalPath = new ArrayList<String>();
    private ArrayList<String> genFedMinimalStar = new ArrayList<String>();
    private ArrayList<Query> genFedMinimalHybrid = new ArrayList<Query>();
    private ArrayList<Query> genFedLightPath = new ArrayList<Query>();
    private ArrayList<Query> genFedLight1 = new ArrayList<Query>();
    private ArrayList<Query> genFedLight2 = new ArrayList<Query>();
    private ArrayList<Query> genFedMaximal = new ArrayList<Query>();
private Set<Node> logsPredicates = new HashSet<Node>();

    private Map<String,Set<Integer>> log1Predicates = new HashMap<String,Set<Integer>>();
    private Map<String,Set<Integer>> log2Predicates = new HashMap<String,Set<Integer>>();
    private Map<String, Set<String>> logPredicatesPrun = new HashMap<String, Set<String>>();
    private BufferedWriter bw = null;
    private FileWriter fw = null;
    
    private ExecutionStrategy executor = new FusekiExecution();
    
    public PruningLogs(String queriesFilePath1, String queriesFilePath2, ArrayList<String> sumFiles) {
        //queries1 = new Queries(queriesFilePath1);
        ARQ.init();
        queries1 = this.getSELECTQueries(queriesFilePath1);
        //queries2 = new Queries(queriesFilePath2);                                                                                                       
        queries2 = this.getSELECTQueries(queriesFilePath2);
        matcher = new MatchingPredicates(sumFiles);
        prunPredicates();
        System.out.println("First querie file size: " + queries1.getQueries().size());
        System.out.println("Second querie file size: " + queries2.getQueries().size());
    }

    public PruningLogs(String queriesFilePath1, String queriesFilePath2, ArrayList<String> sumFiles, String nonCrypte) {
        ARQ.init();
        queries1 = new Queries(queriesFilePath1, "nonCrypte");
        queries2 = new Queries(queriesFilePath2, "nonCrypte");
        matcher = new MatchingPredicates(sumFiles);
        prunPredicates();
        System.out.println("First querie file size: " + queries1.getQueries().size());
        System.out.println("Second querie file size: " + queries2.getQueries().size());
    }

    private void prunPredicates(){
        System.out.println("reading log1 ...");
        for (int i =0; i<queries1.getQueries().size();++i) {
            Query query1 = QueryFactory.create(queries1.getQueries().get(i).getQueryString());
            if (query1 != null) {
                ArrayList<Node> qPredicates = this.getPredicates(query1);
                HashSet<Integer> temp = new HashSet<Integer>();
                temp.add(i);
                for(Node pred : qPredicates){
                    if ((pred != null) && (!pred.toString().substring(0, 1).equals("?"))) {
                        if(log1Predicates.putIfAbsent(pred.toString(), temp) != null)
                            log1Predicates.get(pred.toString()).add(i);
                    }
                }
            }
        }
        System.out.println("reading log2 ...");
        for (int i =0; i<queries2.getQueries().size();++i) {
            Query query1 = QueryFactory.create(queries2.getQueries().get(i).getQueryString());
            if (query1 != null) {
                ArrayList<Node> qPredicates = this.getPredicates(query1);
                HashSet<Integer> temp = new HashSet<Integer>();
                temp.add(i);
                for(Node pred : qPredicates){
                    if ((pred != null) && (!pred.toString().substring(0, 1).equals("?"))) {
                        if(pred != null && log2Predicates.putIfAbsent(pred.toString(), temp) != null)
                            log2Predicates.get(pred.toString()).add(i);
                    }
                }
            }
        }
    }
    
    public Queries getSELECTQueries(String queriesFilePath) {
        Queries queriesSELECT = new Queries();
        Queries queries = new Queries(queriesFilePath);
        int ctre = 0;
        for (int i = 0; i < queries.getSize(); i++) {
            if (queries.getQueries().get(i).toString().contains("SELECT ")) {                                                                               
                queriesSELECT.getQueries().add(queries.getQueries().get(i));
                ctre++;
            }
        }
        System.out.println("ctre " + ctre);
        return queriesSELECT;
    }
    
    public void setExecutor(ExecutionStrategy exec){
        executor = exec;
    }  
    
    public void genFedMinimalPathService(String sparqlEndpoint1, String sparqlEndpoint2, String genFedFile) throws MalformedQueryException, QueryEvaluationException, Exception {
        dicJoinPath = new HashMap<String, HashSet<String>>();
        int cnt = 0;
        int countLarge = 0;
        int countSmall=0;
        int error=0;
        System.out.println(countLarge+" / "+logPredicatesPrun.size());
        for (String nd1 : logPredicatesPrun.keySet()) {
            ++countLarge;
            int totalSmall = logPredicatesPrun.get(nd1).size();
//             HashSet<String> temp = new HashSet<String>();
            for (String nd2 : logPredicatesPrun.get(nd1) ) {
                    System.out.println("   "+(countSmall * 100)/totalSmall+"% : "+countSmall+" / "+totalSmall+" , Total saved: "+genFedMinimalPath.size());
                ++countSmall;
                String q = executor.createPath(nd1,nd2,sparqlEndpoint2);
                cnt++;
                try{
                    if (executor.hasResult(q, sparqlEndpoint1)) {
//                         temp.add(nd2.toString());
                        genFedMinimalPath.add(q);
                    }
                }catch(QueryException e){
                    ++error;
                }
            }
            
            System.out.println(countLarge+" / "+logPredicatesPrun.size());
//             dicJoinPath.put(nd1.toString(), temp);
            countSmall = 0;
        }
        System.out.println("genFedMinimalPath = " + genFedMinimalPath.size());
        System.out.println("Error = " + error);
        try {
            fw = new FileWriter(genFedFile);
            bw = new BufferedWriter(fw);
            for(String s : genFedMinimalPath){
                bw.write("#-------------------------------------------------------\n");
                bw.write(s+"\n");
            }
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
                if (fw != null) {
                    fw.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        System.out.println("all constrected queries = " + cnt);
        System.out.println("write done ...");
    }
 
    public void genFedMinimalStarService(String sparqlEndpoint1, String sparqlEndpoint2, String genFedFile) throws MalformedQueryException, QueryEvaluationException, Exception {
        dicJoinStar = new HashMap<String, HashSet<String>>();
        int cnt = 0;
        int countLarge = 0;
        int countSmall=0;
        int error=0;
        System.out.println(countLarge+" / "+logPredicatesPrun.size());
        for (String nd1 : logPredicatesPrun.keySet()) {
            ++countLarge;
            int totalSmall = logPredicatesPrun.get(nd1).size();
//             HashSet<String> temp = new HashSet<String>();
            for (String nd2 : logPredicatesPrun.get(nd1)) {
                System.out.println("   "+(countSmall * 100)/totalSmall+"% : "+countSmall+" / "+totalSmall+" , saved: "+genFedMinimalStar.size());
                ++countSmall;
                String q = executor.createStar(nd1,nd2,sparqlEndpoint2);
                cnt++;
                try{
                    if (executor.hasResult(q, sparqlEndpoint1)) {
//                         temp.add(nd2.toString());                        
                        genFedMinimalStar.add(q);
                    }
                }catch(QueryException e){
                    ++error;
                }
            }
            System.out.println(countLarge+" / "+logPredicatesPrun.size());
//             dicJoinStar.put(nd1.toString(), temp);
            countSmall=0;
        }
        System.out.println("genFedMinimalStar = " + genFedMinimalStar.size());
        System.out.println("Error = " + error);
        try {
            fw = new FileWriter(genFedFile);
            bw = new BufferedWriter(fw);
            for(String s : genFedMinimalStar){
                bw.write("#-------------------------------------------------------\n");
                bw.write(s+"\n");
            }
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
                if (fw != null) {
                    fw.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
        System.out.println("all constrected queries = " + cnt);
        System.out.println("write done ...");
    }

//     public void genFedMinimalHybridService(String sparqlEndpoint1, String sparqlEndpoint2, String genFedFile) throws FileNotFoundException, MalformedQueryException, QueryEvaluationException, Exception {
//         int cnt = 0;
//         for (String spath : dicJoinPath.keySet()) {
//             for (String sstar : dicJoinStar.keySet()) {
//                 Set<String> intersection = new HashSet<String>(dicJoinPath.get(spath)); // use the copy constructor                                       
//                 intersection.retainAll(dicJoinStar.get(sstar));
//                 for (String s : intersection) {
//                     String q = "SELECT * WHERE {\n"
//                             + "?s " + spath + " ?x .\n"
//                             + "?x " + sstar + " ?o .\n"
//                             + "SERVICE <" + sparqlEndpoint2 + "> { \n"
//                             + "?x " + s + " . }\n"
//                             + "} LIMIT 1";
//                     cnt++;
//                     QueryExecution queryExecution = QueryExecutionFactory.sparqlService(sparqlEndpoint1, q);
//                     ResultSet resultSet = queryExecution.execSelect();
//                     int qResult = 0;
//                     if (resultSet.hasNext()) {
//                         qResult++;
//                     }
//                     if (qResult != 0) {
//                         System.out.println(q);
//                         System.out.println(qResult);
//                         Query qGen = QueryFactory.create(q);
//                         genFedMinimalHybrid.add(qGen);
//                     }
//                 }
//             }
//         }
//         System.out.println("genFedMinimalHybrid = " + genFedMinimalHybrid.size());
//         try {
//             fw = new FileWriter(genFedFile);
//             bw = new BufferedWriter(fw);
//             bw.write(genFedMinimalHybrid.size()+"\n");
//             bw.write(genFedMinimalHybrid.toString()+"\n");
//              } finally {
//             try {
//                 if (bw != null) {
//                     bw.close();
//                 }
//                 if (fw != null) {
//                     fw.close();
//                 }
//             } catch (IOException ex) {
//                 ex.printStackTrace();
//             }
//         }
//         System.out.println("all constrected queries = " + cnt);
//         System.out.println("write done ...");
//     }

    public boolean containsNode(ArrayList<Node> al, String s) {
        boolean bool = false;
        int i = 0;
        while ((bool == false) && (i < al.size())) {
            if (al.get(i).toString().equals(s)) {
                bool = true;
            }
            i++;
        }
        return bool;
    }

    public ArrayList<SparqlQueryParser> pruningLog(ArrayList<SparqlQueryParser> queries, String predicate) {
        ArrayList<SparqlQueryParser> queriesprun = new ArrayList<SparqlQueryParser>();
        for (SparqlQueryParser q : queries) {
            Query query = QueryFactory.create(q.getQueryString());
            try{
            if (this.containsNode(getPredicates(query), predicate)) {
                queriesprun.add(q);
            }
            }catch (Exception e){
                System.out.println("null");
            }
        }
        return queriesprun;
    }
    
    public ArrayList<Node> getPredicates(Query q) {
        ArrayList<Node> predicates = new ArrayList<Node>();
        //System.out.println("q = "+q);                                                                                                                   
        ElementWalker.walk(q.getQueryPattern(),
                // For each element...                                                                                                                    
            new ElementVisitorBase() {
            // ...when it's a block of triples...                                                                                                         
                public void visit(ElementPathBlock el) {
                    // ...go through all the triples...                                                                                                       
                    Iterator<TriplePath> triples = el.patternElts();
                    while (triples.hasNext()) {
                        // ...and grab the subject                                                                                                            
                        predicates.add(triples.next().getPredicate());
                    }
                }
            }
        );

        return predicates;
    }

    public String[] addElementAtIndice(String[] tab, String element, int indice) {
        String[] tabTemp = new String[tab.length + 1];
        for (int i = 0; i < indice; i++) {
            tabTemp[i] = tab[i];
        }
        for (int i = tab.length - 1; i >= indice; i--) {
            tabTemp[i + 1] = tab[i];
        }
        tabTemp[indice] = element;
        return tabTemp;
    }
        
//     public String concatenateQuery1TripleD1Service(String query, String predicate1Join, String predicate2Join, String sparqlEndpoint) {
//         String q = "";
//         String[] table = query.split("[\\s\\xA0]+");
//         String[] newtable = null;
//         for (int i = 0; i < table.length; i++) {
//             if ((table[i].contains(predicate1Join)) || (table[i].contains(":" + predicate1Join.split("/")[predicate1Join.split("/").length - 1])) || (table[i].contains(":" + predicate1Join.split("#")[predicate1Join.split("#").length - 1]))) {
//                 //System.out.println(i+" / "+table[i]);                                                                                                   
//                 newtable = this.addElementAtIndice(table, "SERVICE <" + sparqlEndpoint + "> {", i + 3);
//                 newtable = this.addElementAtIndice(newtable, table[i + 1], i + 4);
//                 newtable = this.addElementAtIndice(newtable, "<" + predicate2Join + ">", i + 5);
//                 newtable = this.addElementAtIndice(newtable, "?genFed", i + 6);
//                 newtable = this.addElementAtIndice(newtable, " }", i + 7);
//                 //System.out.println("**"+table[i+1]);                                                                                                    
//                 if (newtable[i + 2].equals("}")) {
//                     newtable[i + 2] = ".";
//                     newtable = this.addElementAtIndice(newtable, "}", i + 8);
//                 } else if (newtable[i + 2].equals("FILTER")) {
//                     newtable[i + 2] = ".";
//                     newtable = this.addElementAtIndice(newtable, "FILTER", i + 8);
//                 } else if (newtable[i + 2].equals("LIMIT")) {
//                     newtable[i + 2] = ".";
//                     newtable = this.addElementAtIndice(newtable, "LIMIT", i + 8);
//                 } else if (newtable[i + 2].equals("ORDER BY")) {
//                     newtable[i + 2] = ".";
//                     newtable = this.addElementAtIndice(newtable, "ORDER BY", i + 8);
//                 } else if (newtable[i + 2].equals("OFFSET")) {
//                     newtable[i + 2] = ".";
//                     newtable = this.addElementAtIndice(newtable, "OFFSET", i + 8);
//                 } else {
//                     newtable = this.addElementAtIndice(newtable, ".", i + 8);
//                 }
//             }
//         }
//         for (int j = 0; j < newtable.length; j++) {
//             q = q + newtable[j] + " ";
//         }
//         return q;
//     }

    public void pruningPredicatesLogs(ArrayList<String> sumFiles, String so, String log1PredicatesPrunFile, String log2PredicatesPrunFile) throws IOException {
//         FileWriter fwPre1P = new FileWriter(log1PredicatesPrunFile);
//         BufferedWriter bwPre1P = new BufferedWriter(fwPre1P);
//         FileWriter fwPre2P = new FileWriter(log2PredicatesPrunFile);
//         BufferedWriter bwPre2P = new BufferedWriter(fwPre2P);
        
        //Change them to Set of couplePred so we don't excute predicate with mismatch auth
        logPredicatesPrun = new HashMap<String, Set<String>>();
//         log2PredicatesPrun = new HashSet<Node>();
//         System.out.println("reading log1 ...");
//         for (SparqlQueryParser q1 : queries1.getQueries()) {
//             Query query1 = QueryFactory.create(q1.getQueryString());
//             if (query1 != null) {
//                 ArrayList<Node> qPredicates = this.getPredicates(query1);
//                 log1Predicates.addAll(qPredicates);
//             }
//         }
//         System.out.println("reading log2 ...");
//         for (SparqlQueryParser q2 : queries2.getQueries()) {
//             Query query2 = QueryFactory.create(q2.getQueryString());
//             if (query2 != null) {
//                 ArrayList<Node> qPredicates = this.getPredicates(query2);
//                 log2Predicates.addAll(qPredicates);
//             }
//         }
        System.out.println("Testing joinable predicates ...");
        int result = 0;
        for (String nlog1 : log1Predicates.keySet()) {  
            if ((nlog1 != null) && (!nlog1.substring(0, 1).equals("?"))) {
                for (String nlog2 : log2Predicates.keySet()) {
                    if ((nlog2 != null) && (!nlog2.substring(0, 1).equals("?"))) {
                        result = matcher.testExistingMatch(nlog1, nlog2);
                        if(result == 1 && so.equals("star")){
//                             log1PredicatesPrun.add(nlog1);
//                             log2PredicatesPrun.add(nlog2);
                                addPredicatesPrun(nlog1, nlog2);
                        }else if(result == 2 && so.equals("path")){
//                             log1PredicatesPrun.add(nlog1);
//                             log2PredicatesPrun.add(nlog2);
                                addPredicatesPrun(nlog1, nlog2);
                        }else if(result == 3){
//                             log1PredicatesPrun.add(nlog1);
//                             log2PredicatesPrun.add(nlog2);
                                addPredicatesPrun(nlog1, nlog2);
                        }
                    }
                }
            }
        }
    }
    private void addPredicatesPrun(String l1, String l2){
        HashSet<String> temp = new HashSet<String>();
        temp.add(l2);
        if(logPredicatesPrun.putIfAbsent(l1, temp) != null)
            logPredicatesPrun.get(l1).add(l2);
    }
    //-----------MAX------------
    public int getIndiceVal(String[] table, String val) {
        for (int j = 0; j < table.length; j++) {
            if (table[j].contains(val) || table[j].contains(val.split("/")[val.split("/").length - 1])) {
                return j;
            }
        }
        return 0;
    }
    
    public String renameVariables(String query, String suffix) {
        String q = "";
        String[] table = query.split("[\\s\\xA0]+");
        for (int i = 0; i < table.length; i++) {//System.out.println("--"+i+"  "+table.toString());
            if (table[i].length() > 1) {
                if (table[i].substring(0,1).equals("?")) {
                    table[i] = table[i]+"_"+suffix;
                }
            }
        }
        for (int j = 0; j < table.length; j++) {
            q = q + table[j] + " ";
        }
        return q;
    }
    //-----------MAX PATH-------
    public String concatenate2QueriesService(String query1, String predicate1Join, String query2, String predicate2Join, String sparqlEndpoint) {
        String q = "";String temp="";
        String[] table = query1.split("[\\s\\xA0]+");
        String[] table2 = query2.split("[\\s\\xA0]+");
//         System.out.println("-------------------");
//         for(String s: table2)
//             System.out.println(s);
//         System.out.println("--------------------");
        int indiceWhere = this.getIndiceVal(table2, "WHERE");//System.out.println(indiceWhere);
        int indicePredicate2Join = this.getIndiceVal(table2, predicate2Join);
        String valRemplace = null;
        if (indicePredicate2Join > 0) {
        	valRemplace = table2[indicePredicate2Join - 1];
        }
        String[] newtable = null;
        for (int i = 0; i < table.length; i++) {
            if ((table[i].contains(predicate1Join)) || (table[i].contains(":" + predicate1Join.split("/")[predicate1Join.split("/").length - 1])) || (table[i].contains(":" + predicate1Join.split("#")[predicate1Join.split("#").length - 1]))) {
                //System.out.println(i+" / "+table[i]);
                temp = table[i + 1];
//                System.out.println(valRemplace+" / "+temp);
                newtable = this.addElementAtIndice(table, "SERVICE <" + sparqlEndpoint + "> {", i + 3);
                int k=i+4;
                int posEnd2 = 0;
                for(int j=table2.length-1;j>indiceWhere+2;--j){
                    if(table2[j].contains("}")){
                        posEnd2 = j;
                        break;
                    }
                }
//                 for (int j=indiceWhere+2; j<table2.length-1; j++) {
                for (int j=indiceWhere+2; j<posEnd2; j++) {
                    newtable = this.addElementAtIndice(newtable, table2[j], k);
                    k++;
                }
                newtable = this.addElementAtIndice(newtable, " }", k);
                //System.out.println("**"+table[i+1]);
                if (newtable[i + 2].equals("}")) {
                    newtable[i + 2] = ".";
                    newtable = this.addElementAtIndice(newtable, "}", k+1);
                } else if (newtable[i + 2].equals("FILTER")) {
                    newtable[i + 2] = ".";
                    newtable = this.addElementAtIndice(newtable, "FILTER", k+1);
                } else if (newtable[i + 2].equals("OPTIONAL")) {
                    newtable[i + 2] = ".";
                    newtable = this.addElementAtIndice(newtable, "OPTIONAL", k+1);
                } else if (newtable[i + 2].equals("UNION")) {
                    newtable[i + 2] = ".";
                    newtable = this.addElementAtIndice(newtable, "UNION", k+1);
                } else if (newtable[i + 2].equals("LIMIT")) {
                    newtable[i + 2] = ".";
                    newtable = this.addElementAtIndice(newtable, "LIMIT", k+1);
                } else if (newtable[i + 2].equals("ORDER BY")) {
                    newtable[i + 2] = ".";
                    newtable = this.addElementAtIndice(newtable, "ORDER BY", k+1);
                } else if (newtable[i + 2].equals("OFFSET")) {
                    newtable[i + 2] = ".";
                    newtable = this.addElementAtIndice(newtable, "OFFSET", k+1);
                } else if (newtable[i + 2].equals("MINUS")) {
                    newtable[i + 2] = ".";
                    newtable = this.addElementAtIndice(newtable, "MINUS", k+1);
                }else {
                    newtable = this.addElementAtIndice(newtable, ".", k+1);
                }
            }
        }
	// add query2 prefixes
        int indiceSelect = this.getIndiceVal(table2, "SELECT");
        String prefixes = "";
        for (int l = 0; l<indiceSelect; l++) {
            prefixes = prefixes + table2[l] + " ";
        }
        q = q + prefixes;
        for (int j = 0; j < newtable.length; j++) {
            q = q + newtable[j] + " ";
        }
        
        // unification join point (var&var -> var / var&cte -> cte / cte&cte -> null)
        // we assume that a federated query Q is composed on two simple queries
        if (valRemplace != null) {
            if (valRemplace.substring(0, 1).equals("?")) {
                if (temp.substring(0, 1).equals("?")) { //var&var 
                    q = q.replaceAll("\\"+valRemplace, "\\"+temp);
                    return q;
                } else { //var&cte
//                     System.out.println("------------1");
                    return null;
                }
            } else {
                if (temp.substring(0, 1).equals("?")) { //cte&var
//                     System.out.println("------------2");
                    return null;
                } else { //cte&cte
                    if (valRemplace.equals(temp)) {
                        return q;
                    } else {
//                         System.out.println("------------3");
                        return null;
                    }
                }
            }
        }else {
            return null;
        }
       // return q;
    }
    
    public void genFedMaximalPath(String sparqlEndpoint1, String sparqlEndpoint2, String genFedFile) throws IOException {
//         System.out.println("dicJoinPath "+logPredicatesPrun);
        fw = new FileWriter(genFedFile);
        bw = new BufferedWriter(fw);
        ArrayList<Query> genQueries = new ArrayList<Query>();
        bw.write("#-------------------------------------------------------\n");
        int countQ1 = 0;
        int countPred1 = 0;
        int countQ2 = 0;
        int countPred2 = 0;
        for (String s : logPredicatesPrun.keySet()) {
            System.out.println(""+countQ1 + " / " + logPredicatesPrun.keySet().size());
            ++countQ1;
            Set<Integer> queries1prunDicJoin = log1Predicates.get(s);
            System.out.println("s = "+s+" / st = "+logPredicatesPrun.get(s).size());
            for (Integer idQ1 : queries1prunDicJoin) {
                System.out.println("   "+countPred1 + " / " + queries1prunDicJoin.size());
                ++countPred1;
                SparqlQueryParser q1 = queries1.getQueryAt(idQ1);
                String query1 = this.renameVariables(q1.toString(), "log1");
                for (String st : logPredicatesPrun.get(s)) {
                    System.out.println("      "+countQ2 + " / " + logPredicatesPrun.get(s).size());
                    ++countQ2;
                    Set<Integer> queries2prunDicJoin = log2Predicates.get(s);
                    for (Integer idQ2 : queries2prunDicJoin) {
//                         System.out.println("         "+countPred2 + " / " + queries2prunDicJoin.size());
                        ++countPred2;
                        SparqlQueryParser q2 = queries2.getQueryAt(idQ2);
                        String query2 = this.renameVariables(q2.toString(), "log2");
                        String q = this.concatenate2QueriesService(query1, s, query2, st, sparqlEndpoint2);
                        if (q != null) {
                            Query query = QueryFactory.create(q);
                            genQueries.add(query);
                            bw.write("query: "+q);
                            bw.write("\n#-------------------------------------------------------\n");
                            bw.flush();
                        }
                    }
                    countPred2 = 0;
                }
                countQ2 = 0;
            }
            countPred1 = 0;
        }
        bw.flush();
        System.out.println("genQueries = "+genQueries.size());
        System.out.println("done.");
    }  
    
    //----------------------------MAX STAR-----------
    public String concatenate2QueriesServiceStar(String query1, String predicate1Join, String query2, String predicate2Join, String sparqlEndpoint) {
        String q = "";String temp="";
//         System.out.println("query1: "+query1+"      /   "+predicate1Join);
//         System.out.println("query2: "+query2+"      /   "+predicate2Join);
        String[] table = query1.split("[\\s\\xA0]+");
        String[] table2 = query2.split("[\\s\\xA0]+");
        int indiceWhere = this.getIndiceVal(table2, "WHERE");//System.out.println(indiceWhere);
        int indicePredicate2Join = this.getIndiceVal(table2, predicate2Join);
//         System.out.println("indicePredicate2Join: "+indicePredicate2Join);
        String valRemplace = null;
        if (indicePredicate2Join > 0) {
            valRemplace = table2[indicePredicate2Join - 1]; 
//             System.out.println("valRemplace: "+valRemplace);
        }
        String[] newtable = null;
        for (int i = 0; i < table.length; i++) {
        if ((table[i].contains(predicate1Join)) || (table[i].contains(":" + predicate1Join.split("/")[predicate1Join.split("/").length - 1])) || (table[i].contains(":" + predicate1Join.split("#")[predicate1Join.split("#").length - 1]))) {                //System.out.println(i+" / "+table[i]);
                temp = table[i - 1]; // get Subject of predicate1Join
//                System.out.println(valRemplace+" / "+temp);
                newtable = this.addElementAtIndice(table, "SERVICE <" + sparqlEndpoint + "> {", i + 3);
                int k=i+4;
                int posEnd2 = 0;
                for(int j=table2.length-1;j>indiceWhere+2;--j){
                    if(table2[j].contains("}")){
                        posEnd2 = j;
                        break;
                    }
                }
//                 for (int j=indiceWhere+2; j<table2.length-1; j++) {
                for (int j=indiceWhere+2; j<posEnd2; j++) {
                    newtable = this.addElementAtIndice(newtable, table2[j], k);
                    k++;
                }
                newtable = this.addElementAtIndice(newtable, " }", k);
                //System.out.println("**"+table[i+1]);
                if (newtable[i + 2].equals("}")) {
                    newtable[i + 2] = ".";
                    newtable = this.addElementAtIndice(newtable, "}", k+1);
                } else if (newtable[i + 2].equals("FILTER")) {
                    newtable[i + 2] = ".";
                    newtable = this.addElementAtIndice(newtable, "FILTER", k+1);
                } else if (newtable[i + 2].equals("OPTIONAL")) {
                    newtable[i + 2] = ".";
                    newtable = this.addElementAtIndice(newtable, "OPTIONAL", k+1);
                } else if (newtable[i + 2].equals("UNION")) {
                    newtable[i + 2] = ".";
                    newtable = this.addElementAtIndice(newtable, "UNION", k+1);
                } else if (newtable[i + 2].equals("LIMIT")) {
                    newtable[i + 2] = ".";
                    newtable = this.addElementAtIndice(newtable, "LIMIT", k+1);
                } else if (newtable[i + 2].equals("ORDER BY")) {
                newtable[i + 2] = ".";
                    newtable = this.addElementAtIndice(newtable, "ORDER BY", k+1);
                } else if (newtable[i + 2].equals("OFFSET")) {
                    newtable[i + 2] = ".";
                    newtable = this.addElementAtIndice(newtable, "OFFSET", k+1);
                } else if (newtable[i + 2].equals("MINUS")) {
                    newtable[i + 2] = ".";
                    newtable = this.addElementAtIndice(newtable, "MINUS", k+1);
                } else if (newtable[i + 2].equals("ORDER")) {
                    newtable[i + 2] = ".";
                    newtable = this.addElementAtIndice(newtable, "ORDER", k+1);
                } else {
                    newtable = this.addElementAtIndice(newtable, ".", k+1);
                }
            }
        }
        // add query2 prefixes
        int indiceSelect = this.getIndiceVal(table2, "SELECT");
        String prefixes = "";
        for (int l = 0; l<indiceSelect; l++) {
            prefixes = prefixes + table2[l] + " ";
        }
        q = q + prefixes;
        for (int j = 0; j < newtable.length; j++) {
            q = q + newtable[j] + " ";
        }
        
        // unification join point (var&var -> var / var&cte -> cte / cte&cte -> null)
        // we assume that a federated query Q is composed on two simple queries
        if (valRemplace != null) {
            if (valRemplace.substring(0, 1).equals("?")) {
                if (temp.substring(0, 1).equals("?")) { //var&var 
                    q = q.replaceAll("\\"+valRemplace, "\\"+temp);
//                     System.out.println("yes : "+q);
                    return q;
                } else { //var&cte
                    return null;
                }
            } else {
                if (temp.substring(0, 1).equals("?")) { //cte&var
                    return null;
                } else { //cte&cte
                    if (valRemplace.equals(temp)) {
//                         System.out.println("yes2 : "+q);
                        return q;
                    } else {
                        return null;
                    }
                }
            }
        } else {//System.out.println("je ss là");
            return null;
        }
    }


    public void genFedMaximalStar(String sparqlEndpoint1, String sparqlEndpoint2, String genFedFile) throws IOException {
        fw = new FileWriter(genFedFile);
        bw = new BufferedWriter(fw);
        ArrayList<Query> genQueries = new ArrayList<Query>();
        bw.write("#-------------------------------------------------------\n");
        for (String s : logPredicatesPrun.keySet()) {
            Set<Integer> queries1prunDicJoin = log1Predicates.get(s);
            System.out.println("s = "+s+" / st = "+logPredicatesPrun.get(s).size());
            for (Integer idQ1 : queries1prunDicJoin) {
                SparqlQueryParser q1 = queries1.getQueryAt(idQ1);
                String query1 = this.renameVariables(q1.toString(), "log1");
                for (String st : logPredicatesPrun.get(s)) {
                    Set<Integer> queries2prunDicJoin = log2Predicates.get(s);
                    for (Integer idQ2 : queries2prunDicJoin) {
                        SparqlQueryParser q2 = queries2.getQueryAt(idQ2);
                        String query2 = this.renameVariables(q2.toString(), "log2");
                        String q = this.concatenate2QueriesServiceStar(query1, s, query2, st, sparqlEndpoint2);
                        if (q != null) {
                            Query query = QueryFactory.create(q);
                            genQueries.add(query);
//                             bw.write("query: "+q);
                            bw.write(q);
                            bw.write("\n#-------------------------------------------------------\n");
                            bw.flush();
                        }
                    }
                }
            }
        }
        bw.flush();
        System.out.println("genQueries = "+genQueries.size());
        System.out.println("done.");
    }
}

