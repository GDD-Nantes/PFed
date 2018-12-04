package PFed;

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
// import org.gdd.sage.cli.QueryExecutor;
// import org.gdd.sage.cli.SelectQueryExecutor;
// import org.gdd.sage.engine.SageExecutionContext;
// import org.gdd.sage.federated.factory.FederatedQueryFactory;
// import org.gdd.sage.federated.factory.ServiceFederatedQueryFactory;
// import org.gdd.sage.http.ExecutionStats;
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
public class PruningLogsF {
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

    private Set<Node> log1Predicates = new HashSet<Node>();
    private Set<Node> log2Predicates = new HashSet<Node>();
    private Set<Node> log1PredicatesPrun = new HashSet<Node>();
    private Set<Node> log2PredicatesPrun = new HashSet<Node>();
    private BufferedWriter bw = null;
    private FileWriter fw = null;
    
    private ExecutionStrategy executor = new FusekiExecution();
    
    public PruningLogsF(String queriesFilePath1, String queriesFilePath2, ArrayList<String> sumFiles) {
        //queries1 = new Queries(queriesFilePath1);                                                                                                       
        queries1 = this.getSELECTQueries(queriesFilePath1);
        //queries2 = new Queries(queriesFilePath2);                                                                                                       
        queries2 = this.getSELECTQueries(queriesFilePath2);
        matcher = new MatchingPredicates(sumFiles);
        System.out.println("First querie file size: " + queries1.getQueries().size());
        System.out.println("Second querie file size: " + queries2.getQueries().size());
    }

    public PruningLogsF(String queriesFilePath1, String queriesFilePath2, ArrayList<String> sumFiles, String nonCrypte) {
        queries1 = new Queries(queriesFilePath1, "nonCrypte");
        queries2 = new Queries(queriesFilePath2, "nonCrypte");
        matcher = new MatchingPredicates(sumFiles);
        System.out.println("First querie file size: " + queries1.getQueries().size());
        System.out.println("Second querie file size: " + queries2.getQueries().size());
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
        System.out.println(countLarge+" / "+log1PredicatesPrun.size());
        for (Node nd1 : log1PredicatesPrun) {
            ++countLarge;
            HashSet<String> temp = new HashSet<String>();
            for (Node nd2 : log2PredicatesPrun) {
//                 if( countSmall != 1 && (countSmall * 100)/log2PredicatesPrun.size() % 25 <= ((countSmall-1) * 100)/log2PredicatesPrun.size() % 25){
                    System.out.println("   "+(countSmall * 100)/log2PredicatesPrun.size()+"% : "+countSmall+" / "+log2PredicatesPrun.size()+" , saved: "+temp.size());
//                 }
                ++countSmall;
                String q = executor.createPath(nd1,nd2,sparqlEndpoint2);
                cnt++;
                try{
                    if (executor.hasResult(q, sparqlEndpoint1)) {
                        temp.add(nd2.toString());
                        genFedMinimalPath.add(q);
                    }
                }catch(QueryException e){
                    ++error;
                }
            }
            
            System.out.println(countLarge+" / "+log1PredicatesPrun.size());
            dicJoinPath.put(nd1.toString(), temp);
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
        System.out.println(countLarge+" / "+log1PredicatesPrun.size());
        for (Node nd1 : log1PredicatesPrun) {
            ++countLarge;
            HashSet<String> temp = new HashSet<String>();
            for (Node nd2 : log2PredicatesPrun) {
                if( countSmall != 1 && (countSmall * 100)/log2PredicatesPrun.size() % 25 <= ((countSmall-1) * 100)/log2PredicatesPrun.size() % 25){
                    System.out.println("   "+(countSmall * 100)/log2PredicatesPrun.size()+"% : "+countSmall+" / "+log2PredicatesPrun.size()+" , saved: "+temp.size());
                }
                ++countSmall;
                String q = executor.createStar(nd1,nd2,sparqlEndpoint2);
                cnt++;
                try{
                    if (executor.hasResult(q, sparqlEndpoint1)) {
                        temp.add(nd2.toString());                        
                        genFedMinimalStar.add(q);
                    }
                }catch(QueryException e){
                    ++error;
                }
            }
            System.out.println(countLarge+" / "+log1PredicatesPrun.size());
            dicJoinStar.put(nd1.toString(), temp);
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

    public void genFedMinimalHybridService(String sparqlEndpoint1, String sparqlEndpoint2, String genFedFile) throws FileNotFoundException, MalformedQueryException, QueryEvaluationException, Exception {
        int cnt = 0;
        for (String spath : dicJoinPath.keySet()) {
            for (String sstar : dicJoinStar.keySet()) {
                Set<String> intersection = new HashSet<String>(dicJoinPath.get(spath)); // use the copy constructor                                       
                intersection.retainAll(dicJoinStar.get(sstar));
                for (String s : intersection) {
                    String q = "SELECT * WHERE {\n"
                            + "?s " + spath + " ?x .\n"
                            + "?x " + sstar + " ?o .\n"
                            + "SERVICE <" + sparqlEndpoint2 + "> { \n"
                            + "?x " + s + " . }\n"
                            + "} LIMIT 1";
                    cnt++;
                    QueryExecution queryExecution = QueryExecutionFactory.sparqlService(sparqlEndpoint1, q);
                    ResultSet resultSet = queryExecution.execSelect();
                    int qResult = 0;
                    if (resultSet.hasNext()) {
                        qResult++;
                    }
                    if (qResult != 0) {
                        System.out.println(q);
                        System.out.println(qResult);
                        Query qGen = QueryFactory.create(q);
                        genFedMinimalHybrid.add(qGen);
                    }
                }
            }
        }
        System.out.println("genFedMinimalHybrid = " + genFedMinimalHybrid.size());
        try {
            fw = new FileWriter(genFedFile);
            bw = new BufferedWriter(fw);
            bw.write(genFedMinimalHybrid.size()+"\n");
            bw.write(genFedMinimalHybrid.toString()+"\n");
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
            if (this.containsNode(getPredicates(query), predicate)) {
                queriesprun.add(q);
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
        
    public String concatenateQuery1TripleD1Service(String query, String predicate1Join, String predicate2Join, String sparqlEndpoint) {
        String q = "";
        String[] table = query.split("[\\s\\xA0]+");
        String[] newtable = null;
        for (int i = 0; i < table.length; i++) {
            if ((table[i].contains(predicate1Join)) || (table[i].contains(":" + predicate1Join.split("/")[predicate1Join.split("/").length - 1])) || (table[i].contains(":" + predicate1Join.split("#")[predicate1Join.split("#").length - 1]))) {
                //System.out.println(i+" / "+table[i]);                                                                                                   
                newtable = this.addElementAtIndice(table, "SERVICE <" + sparqlEndpoint + "> {", i + 3);
                newtable = this.addElementAtIndice(newtable, table[i + 1], i + 4);
                newtable = this.addElementAtIndice(newtable, "<" + predicate2Join + ">", i + 5);
                newtable = this.addElementAtIndice(newtable, "?genFed", i + 6);
                newtable = this.addElementAtIndice(newtable, " }", i + 7);
                //System.out.println("**"+table[i+1]);                                                                                                    
                if (newtable[i + 2].equals("}")) {
                    newtable[i + 2] = ".";
                    newtable = this.addElementAtIndice(newtable, "}", i + 8);
                } else if (newtable[i + 2].equals("FILTER")) {
                    newtable[i + 2] = ".";
                    newtable = this.addElementAtIndice(newtable, "FILTER", i + 8);
                } else if (newtable[i + 2].equals("LIMIT")) {
                    newtable[i + 2] = ".";
                    newtable = this.addElementAtIndice(newtable, "LIMIT", i + 8);
                } else if (newtable[i + 2].equals("ORDER BY")) {
                    newtable[i + 2] = ".";
                    newtable = this.addElementAtIndice(newtable, "ORDER BY", i + 8);
                } else if (newtable[i + 2].equals("OFFSET")) {
                    newtable[i + 2] = ".";
                    newtable = this.addElementAtIndice(newtable, "OFFSET", i + 8);
                } else {
                    newtable = this.addElementAtIndice(newtable, ".", i + 8);
                }
            }
        }
        for (int j = 0; j < newtable.length; j++) {
            q = q + newtable[j] + " ";
        }
        return q;
    }

    public void pruningPredicatesLogs(ArrayList<String> sumFiles, String so, String log1PredicatesPrunFile, String log2PredicatesPrunFile) throws IOException {
        FileWriter fwPre1P = new FileWriter(log1PredicatesPrunFile);
        BufferedWriter bwPre1P = new BufferedWriter(fwPre1P);
        FileWriter fwPre2P = new FileWriter(log2PredicatesPrunFile);
        BufferedWriter bwPre2P = new BufferedWriter(fwPre2P);

        log1Predicates = new HashSet<Node>();
        log2Predicates = new HashSet<Node>();
        log1PredicatesPrun = new HashSet<Node>();
        log2PredicatesPrun = new HashSet<Node>();
        System.out.println("reading log1 ...");
        for (SparqlQueryParser q1 : queries1.getQueries()) {
            Query query1 = QueryFactory.create(q1.getQueryString());
            if (query1 != null) {
                ArrayList<Node> qPredicates = this.getPredicates(query1);
                log1Predicates.addAll(qPredicates);
            }
        }
        System.out.println("reading log2 ...");
        for (SparqlQueryParser q2 : queries2.getQueries()) {
            Query query2 = QueryFactory.create(q2.getQueryString());
            if (query2 != null) {
                ArrayList<Node> qPredicates = this.getPredicates(query2);
                log2Predicates.addAll(qPredicates);
            }
        }
        System.out.println("Testing joinable predicates ...");
        System.out.println(log1Predicates.size());
        System.out.println(log2Predicates.size());
        int result = 0;
        for (Node nlog1 : log1Predicates) {  
            if ((nlog1 != null) && (!nlog1.toString().substring(0, 1).equals("?"))) {
                for (Node nlog2 : log2Predicates) {
                    if ((nlog2 != null) && (!nlog2.toString().substring(0, 1).equals("?"))) {
                        result = matcher.testExistingMatch(nlog1.toString(), nlog2.toString());
                        if(result == 1 && so.equals("star")){
                            log1PredicatesPrun.add(nlog1);
                            log2PredicatesPrun.add(nlog2);
                        }else if(result == 2 && so.equals("path")){
                            log1PredicatesPrun.add(nlog1);
                            log2PredicatesPrun.add(nlog2);
                        }else if(result == 3){
                            log1PredicatesPrun.add(nlog1);
                            log2PredicatesPrun.add(nlog2);
                        }
                    }
                }
            }
        }
    }

    public ArrayList<SparqlQueryParser> pruningLog1Predicates(String so) {
    
        System.out.println("****** Preds File 1 : " + log1PredicatesPrun.size());
        queries1prunPath = new ArrayList<SparqlQueryParser>();
        for (SparqlQueryParser q1 : queries1.getQueries()) {
            Query query1 = QueryFactory.create(q1.getQueryString());
            if (!Collections.disjoint(getPredicates(query1), log1PredicatesPrun)) {
                //System.out.println("       dij = "+getPredicates(query1)+"\n    ---    ");
                queries1prunPath.add(q1);//System.out.println(q1);
            }
        }
        System.out.println("****** Querie File 1 : " + queries1.getQueries().size() + "   /    " + queries1prunPath.size());
        return queries1prunPath;
    }

    public ArrayList<SparqlQueryParser> pruningLog2Predicates(String so) {
            System.out.println("****** Preds File 2 : " + log2PredicatesPrun.size());
            queries2prunPath = new ArrayList<SparqlQueryParser>();
            for (SparqlQueryParser q2 : queries2.getQueries()) {
                Query query2 = QueryFactory.create(q2.getQueryString());
                if (!Collections.disjoint(getPredicates(query2), log2PredicatesPrun)) {
                    //System.out.println("       dij = "+getPredicates(query1)+"\n    ---    ");
                    queries2prunPath.add(q2);//System.out.println(q1);
                }
            }
            System.out.println("****** Querie File 2 : " + queries2.getQueries().size() + "   /    " + queries2prunPath.size());
            return queries2prunPath;
    }
    public Set<Node> getLog1PredicatesPrun(){
        return log1PredicatesPrun;
    }
    public Set<Node> getLog2PredicatesPrun(){
        return log2PredicatesPrun;
    }
    public void addPredPrunLog1(Node nlog1){
        log1PredicatesPrun.add(nlog1);
    }
    public void addPredPrunLog2(Node nlog2){
        log2PredicatesPrun.add(nlog2);
    }
}

