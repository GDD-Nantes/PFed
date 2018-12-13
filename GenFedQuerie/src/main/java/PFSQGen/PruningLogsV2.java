/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PFSQGen;

/*import org.apache.jena.query.ResultSetFormatter;
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
import java.io.*;
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
import org.gdd.sage.cli.QueryExecutor;
import org.gdd.sage.cli.SelectQueryExecutor;
import org.gdd.sage.engine.SageExecutionContext;
import org.gdd.sage.federated.factory.FederatedQueryFactory;
import org.gdd.sage.federated.factory.ServiceFederatedQueryFactory;
import org.gdd.sage.http.ExecutionStats;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.repository.sail.SailRepository;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.rdf.model.RDFNode;

public class PruningLogsV2 {
    private Queries queries1;
    private Queries queries2;
    Queries queries1SELECT = new Queries();
    Queries queries2SELECT = new Queries();
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
    private ArrayList<Query> genFedMinimalPath = new ArrayList<Query>();
    private ArrayList<Query> genFedMinimalStar = new ArrayList<Query>();
    private ArrayList<Query> genFedMinimalHybrid = new ArrayList<Query>();
    private ArrayList<Query> genFedLightPath = new ArrayList<Query>();
    private ArrayList<Query> genFedLight1 = new ArrayList<Query>();
    private ArrayList<Query> genFedLight2 = new ArrayList<Query>();
    private ArrayList<Query> genFedMaximal = new ArrayList<Query>();

    private List<Node> logsPredicates = new ArrayList<Node>();

    private List<Node> log1Predicates = new ArrayList<Node>();
    private List<Node> log2Predicates = new ArrayList<Node>();
    private Set<Node> log1PredicatesPrun = new HashSet<Node>();
    private Set<Node> log2PredicatesPrun = new HashSet<Node>();
    private BufferedWriter bw = null;
    private FileWriter fw = null;
    private BufferedWriter bw2 = null;
    private FileWriter fw2 = null;
    private TypesSummaries typesSummaries1; 
    private TypesSummaries typesSummaries2;
    private HashMap<String, HashSet<String>> listJoinPredPath = new HashMap<String, HashSet<String>>();*/


import com.fluidops.fedx.Config;
import com.fluidops.fedx.FedXFactory;
import com.fluidops.fedx.exception.FedXException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.query.ARQ;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.sparql.core.TriplePath;
import org.apache.jena.sparql.syntax.ElementPathBlock;
import org.apache.jena.sparql.syntax.ElementVisitorBase;
import org.apache.jena.sparql.syntax.ElementWalker;
import org.gdd.sage.cli.QueryExecutor;
import org.gdd.sage.cli.SelectQueryExecutor;
import org.gdd.sage.engine.SageExecutionContext;
import org.gdd.sage.federated.factory.FederatedQueryFactory;
import org.gdd.sage.federated.factory.ServiceFederatedQueryFactory;
import org.gdd.sage.http.ExecutionStats;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sail.SailRepository;

/**
 *
 * @author khiyari-s
 */
public class PruningLogsV2 {
    private Queries queries1;
    private Queries queries2;
    Queries queries1SELECT = new Queries();
    Queries queries2SELECT = new Queries();
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
    private ArrayList<Query> genFedMinimalPath = new ArrayList<Query>();
    private ArrayList<Query> genFedMinimalStar = new ArrayList<Query>();
    private ArrayList<Query> genFedMinimalHybrid = new ArrayList<Query>();
    private ArrayList<Query> genFedLightPath = new ArrayList<Query>();
    private ArrayList<Query> genFedLight1 = new ArrayList<Query>();
    private ArrayList<Query> genFedLight2 = new ArrayList<Query>();
    private ArrayList<Query> genFedMaximal = new ArrayList<Query>();

    private List<Node> logsPredicates = new ArrayList<Node>();
    private List<Node> log1Predicates = new ArrayList<Node>();
    private List<Node> log2Predicates = new ArrayList<Node>();
    private Set<Node> log1PredicatesPrun = new HashSet<Node>();
    private Set<Node> log2PredicatesPrun = new HashSet<Node>();
    private BufferedWriter bw = null;
    private FileWriter fw = null;
    private BufferedWriter bw2 = null;
    private FileWriter fw2 = null;
    private TypesSummaries typesSummaries1; 
    private TypesSummaries typesSummaries2;
    private HashMap<String, HashSet<String>> listJoinPredPath = new HashMap<String, HashSet<String>>();
    
    public PruningLogsV2(String queriesFilePath1, String queriesFilePath2, ArrayList<String> sumFiles, String log1ClassSummary, String log2ClassSummary) {
        //queries1 = new Queries(queriesFilePath1);
        queries1 = this.getSELECTQueries(queriesFilePath1);
        //queries2 = new Queries(queriesFilePath2);
        queries2 = this.getSELECTQueries(queriesFilePath2);
        matcher = new MatchingPredicates(sumFiles);
	typesSummaries1 = new TypesSummaries(log1ClassSummary);
        typesSummaries2 = new TypesSummaries(log2ClassSummary);
        System.out.println("SWDF size: " + queries1.getQueries().size());
        System.out.println("DBPedia size: " + queries2.getQueries().size());
    }
    
    public PruningLogsV2(String queriesFilePath1, String queriesFilePath2, ArrayList<String> sumFiles, String log1ClassSummary, String log2ClassSummary, String nonCrypte) {
        queries1 = new Queries(queriesFilePath1, "nonCrypte");
        queries2 = new Queries(queriesFilePath2, "nonCrypte");
        matcher = new MatchingPredicates(sumFiles);
	typesSummaries1 = new TypesSummaries(log1ClassSummary);
        typesSummaries2 = new TypesSummaries(log2ClassSummary);
        System.out.println("SWDF size: " + queries1.getQueries().size());
        System.out.println("DBPedia size: " + queries2.getQueries().size());
    }
    
    public Queries getSELECTQueries(String queriesFilePath) {
        Queries queriesSELECT = new Queries();
        Queries queries = new Queries(queriesFilePath);
        int ctre = 0;
        for (int i = 0; i < queries.getSize(); i++) {
            if (queries.getQueries().get(i).toString().contains("SELECT ")) {
                //System.out.println(queries.getQueries().get(i));
                queriesSELECT.getQueries().add(queries.getQueries().get(i));
                ctre++;
            }
        }
        System.out.println("ctre " + ctre);
        return queriesSELECT;
    }
    
    public void genFedMinimalPathService(String sparqlEndpoint1, String sparqlEndpoint2, String genFedFile) throws MalformedQueryException, QueryEvaluationException, Exception {
        dicJoinPath = new HashMap<String, HashSet<String>>();
        int cnt = 0;      
        for (Node nd1 : log1PredicatesPrun) {
            HashSet<String> temp = new HashSet<String>();
            for (Node nd2 : log2PredicatesPrun) {
                String q = "SELECT * WHERE {\n"
                        + "?s <" + nd1 + "> ?x . \n"
                        + "SERVICE <" + sparqlEndpoint2 + "> { \n"
                        + "?x <" + nd2 + "> ?o . } \n"
                        + "} LIMIT 1";
                System.out.println("*q = " + q);
                cnt++;
                QueryExecution queryExecution = QueryExecutionFactory.sparqlService(sparqlEndpoint1, q);
                ResultSet resultSet = queryExecution.execSelect();
                int qResult = 0;
                if (resultSet.hasNext()) {
                    qResult++;
                }
                System.out.println("qResult = "+qResult);
                if (qResult != 0) {
                    temp.add(nd2.toString());
                    System.out.println(q);
                    System.out.println(qResult);
                    Query qGen = QueryFactory.create(q);
                    genFedMinimalPath.add(qGen);
                }
            }
            dicJoinPath.put(nd1.toString(), temp);
        }
        System.out.println("genFedMinimalPath = " + genFedMinimalPath.size());
        System.out.println(genFedMinimalPath);
        try {
            fw = new FileWriter(genFedFile);
            bw = new BufferedWriter(fw);
            bw.write(genFedMinimalPath.size()+"\n");
            bw.write(genFedMinimalPath.toString()+"\n");
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
        for (Node nd1 : listPredicatesLog1Star) {
            HashSet<String> temp = new HashSet<String>();
            for (Node nd2 : listPredicatesLog2) {
                String q = "SELECT * WHERE {\n"
                        + "?s <" + nd1 + "> ?x . \n"
                        + "SERVICE <" + sparqlEndpoint2 + "> { \n"
                        + "?s <" + nd2 + "> ?y . } \n"
                        + "}LIMIT1";
                System.out.println("qStar = " + q);
                QueryExecution queryExecution = QueryExecutionFactory.sparqlService(sparqlEndpoint1, q);
                ResultSet resultSet = queryExecution.execSelect();
                int qResult = 0;
                if (resultSet.hasNext()) {
                    qResult++;
                }
                cnt++;
                if (qResult != 0) {
                    temp.add(nd2.toString());
                    System.out.println(qResult);
                    Query qGen = QueryFactory.create(q);
                    genFedMinimalPath.add(qGen);
                }
            }
            dicJoinStar.put(nd1.toString(), temp);
        }
        System.out.println("genFedMinimalStar = " + genFedMinimalStar.size());
        System.out.println(genFedMinimalStar);
        try {
            fw = new FileWriter(genFedFile);
            bw = new BufferedWriter(fw);
            bw.write(genFedMinimalStar.size()+"\n");
            bw.write(genFedMinimalStar.toString()+"\n");
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
	if ((al.size()>1) && (al.get(0) != null)) {
        while ((bool == false) && (i < al.size())) {
            if (al.get(i).toString().equals(s)) {
                bool = true;
            }
            i++;
        }}
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
                } else if (newtable[i + 2].equals("MINUS")) {
		     newtable[i + 2] = ".";
		     newtable = this.addElementAtIndice(newtable, "MINUS", i + 8);
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
    
    public String concatenateQuery1TripleD2Service(String query, String predicate1Join, String predicate2Join, String sparqlEndpoint) {
        String q = "";
        String[] table = query.split("[\\s\\xA0]+");
        String[] newtable = null;
        for (int i = 0; i < table.length; i++) {
            if ((table[i].contains(predicate1Join)) || (table[i].contains(":" + predicate1Join.split("/")[predicate1Join.split("/").length - 1])) || (table[i].contains(":" + predicate1Join.split("#")[predicate1Join.split("#").length - 1]))) {
                //System.out.println(i+" / "+table[i]);
                newtable = this.addElementAtIndice(table, "SERVICE <" + sparqlEndpoint + "> {", i + 3);
                newtable = this.addElementAtIndice(newtable, "?genFed", i + 4);
                newtable = this.addElementAtIndice(newtable, "<" + predicate2Join + ">", i + 5);
                newtable = this.addElementAtIndice(newtable, table[i - 1], i + 6);
                newtable = this.addElementAtIndice(newtable, " }", i + 7);
                //System.out.println("**"+table[i+1]);
                if (newtable[i + 2].equals("}")) {
                    newtable[i + 2] = ".";
                    newtable = this.addElementAtIndice(newtable, "}", i + 8);
                } else if (newtable[i + 2].equals("FILTER")) {
                    newtable[i + 2] = ".";
                    newtable = this.addElementAtIndice(newtable, "FILTER", i + 8);
                } else if (newtable[i + 2].equals("OPTIONAL")) {
                    newtable[i + 2] = ".";
                    newtable = this.addElementAtIndice(newtable, "OPTIONAL", i + 8);
                } else if (newtable[i + 2].equals("UNION")) {
                    newtable[i + 2] = ".";
                    newtable = this.addElementAtIndice(newtable, "UNION", i + 8);
                } else if (newtable[i + 2].equals("LIMIT")) {
                    newtable[i + 2] = ".";
                    newtable = this.addElementAtIndice(newtable, "LIMIT", i + 8);
                } else if (newtable[i + 2].equals("ORDER BY")) {
                    newtable[i + 2] = ".";
                    newtable = this.addElementAtIndice(newtable, "ORDER BY", i + 8);
                } else if (newtable[i + 2].equals("OFFSET")) {
                    newtable[i + 2] = ".";
                    newtable = this.addElementAtIndice(newtable, "OFFSET", i + 8);
                } else if (newtable[i + 2].equals("MINUS")) {
                     newtable[i + 2] = ".";
                     newtable = this.addElementAtIndice(newtable, "MINUS", i + 8);
                }else {
                    newtable = this.addElementAtIndice(newtable, ".", i + 8);
                }
            }
        }
        for (int j = 0; j < newtable.length; j++) {
            q = q + newtable[j] + " ";
        }
        return q;
    }


    public void genFedLightService(String sparqlEndpoint1, String sparqlEndpoint2, String genFedFile) throws Exception {
        int ctr = 0;
        for (String s : dicJoinPath.keySet()) {
            ArrayList<SparqlQueryParser> queries1prunDicJoin = new ArrayList<SparqlQueryParser>();
            queries1prunDicJoin = pruningLog(queries1prunPath, s);
            for (String st : dicJoinPath.get(s)) {
                ArrayList<SparqlQueryParser> queries2prunDicJoin = new ArrayList<SparqlQueryParser>();
                queries2prunDicJoin = pruningLog(queries2prun, st);
                for (SparqlQueryParser q1ini : queries1prunDicJoin) {
                    System.out.println("q1ini = " + q1ini + "   /   " + s + "   /   " + st);
                    String q = this.concatenateQuery1TripleD1Service(q1ini.toString(), s, st, sparqlEndpoint2);
                    System.out.println("q = " + q);
                    ctr++;
                    int qResult = 0;
                    ExecutionStats spy = new ExecutionStats();
                    Query query = QueryFactory.create(q);
                    FederatedQueryFactory factory = new ServiceFederatedQueryFactory(sparqlEndpoint1, query, spy);
                    factory.buildFederation();
                    query = factory.getLocalizedQuery();
                    Dataset federation = factory.getFederationDataset();
                    // Plug-in the custom ARQ engine for Sage graphs
                    SageExecutionContext.configureDefault(ARQ.getContext());
                    // Evaluate SPARQL query
                    QueryExecutor executor = new SelectQueryExecutor("text");
                    spy.startTimer();
                    executor.execute(federation, query);
                    spy.stopTimer();

                    if (qResult != 0) {
                        System.out.println("                                              Yes ");
                        System.out.println(q);
                        System.out.println(qResult);
                        Query qGen = QueryFactory.create(q);
                        genFedLight1.add(qGen);
                    }
                }
                System.out.println("genFedLight 1 = " + genFedLight1.size());
                    for (SparqlQueryParser q2ini : queries2prunDicJoin) {
                System.out.println("q2ini = " + q2ini + "   /   " + st + "   /   " + s);
                    String q = this.concatenateQuery1TripleD2Service(q2ini.toString(), st, s, sparqlEndpoint2);
                    System.out.println("q = "+q);
                    int qResult = 0;
                        QueryExecution queryExecution = QueryExecutionFactory.sparqlService(sparqlEndpoint2, q);
                        ResultSet resultSet = queryExecution.execSelect();               
                        if (resultSet.hasNext()) {
                            qResult++;
                         }
                    if (qResult != 0) {
                        System.out.println("                                              Yes ");
                        System.out.println(q);
                        System.out.println(qResult);
                        Query qGen = QueryFactory.create(q);
                        genFedLight2.add(qGen);
                    }
                }
                System.out.println("genFedLight 2 = " + genFedLight2.size());
            }
        }
        System.out.println("Done.");
        System.out.println(genFedLight1);
        System.out.println("Done.");
        try {
            fw = new FileWriter(genFedFile);
            bw = new BufferedWriter(fw);
            bw.write(genFedLight1.toString() + "\n");
            bw.write(genFedLight1.size()+ "\n");
            bw.write(genFedLight2.toString()+ "\n");
            bw.write(genFedLight2.size()+ "\n");
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
        
        System.out.println("all constrected queries = " + ctr);
    }

    public void pruningPredicatesLogs(ArrayList<String> sumFiles, String so, String log1PredicatesPrunFile, String log2PredicatesPrunFile) throws IOException {
	        FileWriter fwPre1P = new FileWriter(log1PredicatesPrunFile);
        BufferedWriter bwPre1P = new BufferedWriter(fwPre1P);
        FileWriter fwPre2P = new FileWriter(log2PredicatesPrunFile);
        BufferedWriter bwPre2P = new BufferedWriter(fwPre2P);
        
        log1Predicates = new ArrayList<Node>();
        log2Predicates = new ArrayList<Node>();
        log1PredicatesPrun = new HashSet<Node>();
        log2PredicatesPrun = new HashSet<Node>();
        for (SparqlQueryParser q1 : queries1.getQueries()) {
            Query query1 = QueryFactory.create(q1.getQueryString());
            if (query1 != null) {
            ArrayList<Node> qPredicates = this.getPredicates(query1);
            log1Predicates.addAll(qPredicates);}
        }System.out.println("reading log1 ...");
        for (SparqlQueryParser q2 : queries2.getQueries()) {
            Query query2 = QueryFactory.create(q2.getQueryString());
            if (query2 != null) {
            ArrayList<Node> qPredicates = this.getPredicates(query2);
            log2Predicates.addAll(qPredicates);}
        }System.out.println("reading log2 ...");
	System.out.println("Testing joinable predicates ...");
	System.out.println(log1Predicates.size());
        for (Node nlog1 : log1Predicates) {
	    System.out.println(nlog1);
//System.out.println("Testing joinable predicates ...");
            if ((nlog1 != null) && (!nlog1.toString().substring(0, 1).equals("?"))) {
                List<String> SubjAuthorityP1 = matcher.getSubjAuthority(nlog1.toString(), sumFiles.get(0));
                List<String> ObjAuthorityP1 = matcher.getObjAuthority(nlog1.toString(), sumFiles.get(0));
                if (so.equals("path")) {
		    if ((ObjAuthorityP1 != null)) {//System.out.println(log2Predicates.size());
                    for (Node nlog2 : log2Predicates) {
                        if ((nlog2 != null) && (!nlog2.toString().substring(0, 1).equals("?"))) {
                            List<String> SubjAuthorityP2 = matcher.getSubjAuthority(nlog2.toString(), sumFiles.get(1));
                            if (SubjAuthorityP2 != null) {
                                    Set<String> intersection1 = new HashSet<String>(ObjAuthorityP1); // use the copy constructor
                                    intersection1.retainAll(SubjAuthorityP2);
                                    if (intersection1.size() > 0) {//System.out.println(nlog1+"    .    "+nlog2);
                                        log1PredicatesPrun.add(nlog1);
                                        log2PredicatesPrun.add(nlog2);
					//                                        try{
					// bwPre1P.write(nlog1+"\n");
					// bwPre2P.write(nlog2+"\n");
					/*      }
                                        finally {
                                        try {
                                        if (fwPre1P != null) {
                                            fwPre1P.close();
                                        }
                                        if (fwPre2P != null) {
                                            fwPre2P.close();
                                        }
					if (bwPre1P != null) {
                                            bwPre1P.close();
                                        }
                                        if (bwPre2P != null) {
                                            bwPre2P.close();
                                        }
                                        } catch (IOException ex) {
                                            ex.printStackTrace();
                                        }
                                        }*/
                                    }
                            }
                        }
                    }
		    }    
	 
}
                else if (so.equals("star")) {
                    if ((SubjAuthorityP1 != null)) {
                        for (Node nlog2 : log2Predicates) {
                        if ((nlog2 != null) && (!nlog2.toString().substring(0, 1).equals("?"))) {
                            List<String> SubjAuthorityP2 = matcher.getSubjAuthority(nlog2.toString(), sumFiles.get(1));
                            if (SubjAuthorityP2 != null) {
                                    Set<String> intersection1 = new HashSet<String>(SubjAuthorityP1); // use the copy constructor
                                    intersection1.retainAll(SubjAuthorityP2);
                                    if (intersection1.size() > 0) {
                                        log1PredicatesPrun.add(nlog1);
                                        log2PredicatesPrun.add(nlog2);
					/*                   try{
                                        bwPre1P.write(nlog1+"\n");
                                        bwPre2P.write(nlog2+"\n");
                                        }
                                        finally {
                                        try {
					    if (fwPre1P != null) {
						fwPre1P.close();
					    }
					    if (fwPre2P != null) {
						fwPre2P.close();
					    }
					    if (bwPre1P != null) {
						bwPre1P.close();
					    }
					    if (bwPre2P != null) {
						bwPre2P.close();}                                                                                    
                                        
                                            
                          
                                        } catch (IOException ex) {
                                            ex.printStackTrace();
                                        }
                                        }*/
                                    }
                            }
                        }
                    }
                    }
                    
                }
            }
        }        
	try{
	    bwPre1P.write(log1PredicatesPrun+"\n");
	    bwPre2P.write(log2PredicatesPrun+"\n");
	}
	finally {
	    try {
		if (bwPre1P != null) {
		    bwPre1P.close();
		}
		if (fwPre1P != null) {
		    fwPre1P.close();
		}
		if (bwPre2P != null) {
		    bwPre2P.close();
		}
		if (fwPre2P != null) {
		    fwPre2P.close();
		}
	    } catch (IOException ex) {
		ex.printStackTrace();
	    }
	}
    }
    
    public ArrayList<SparqlQueryParser> pruningLog1Predicates(String log1PrunFile) throws IOException {
        FileWriter fwL1P = new FileWriter(log1PrunFile);
        BufferedWriter bwL1P = new BufferedWriter(fwL1P);
            queries1prunPath = new ArrayList<SparqlQueryParser>();
            for (SparqlQueryParser q1 : queries1.getQueries()) {
                Query query1 = QueryFactory.create(q1.getQueryString());
                if (!Collections.disjoint(getPredicates(query1), log1PredicatesPrun)) {
                    queries1prunPath.add(q1);
		    //                    try {
                        bwL1P.write(q1+"\n");
			bwL1P.flush();		
	/*  } finally {
            try {
                if (bwL1P != null) {
                    bwL1P.close();
                }
                if (fwL1P != null) {
                    fwL1P.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
	    }*/
                }
		else {
			System.out.println("$$$$$$$$$$ log1 = "+q1);
		}
            }
            System.out.println("****** Log1 : " + queries1.getQueries().size() + "   /    " + queries1prunPath.size());
            return queries1prunPath;
        
    }
    
    public ArrayList<SparqlQueryParser> pruningLog2Predicates(String log2PrunFile) throws IOException {
            FileWriter fwL2P = new FileWriter(log2PrunFile);
            BufferedWriter bwL2P = new BufferedWriter(fwL2P);
            queries2prunPath = new ArrayList<SparqlQueryParser>();
            for (SparqlQueryParser q2 : queries2.getQueries()) {
                Query query2 = QueryFactory.create(q2.getQueryString());
                if (!Collections.disjoint(getPredicates(query2), log2PredicatesPrun)) {
                    queries2prunPath.add(q2);
		    //  try {
                        bwL2P.write(q2+"\n");
			bwL2P.flush();
			/*  } finally {
            try {
                if (bwL2P != null) {
                    bwL2P.close();
                }
                if (fwL2P != null) {
                    fwL2P.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
	    }*/
                }
		else {
                        System.out.println("$$$$$$$$$$ log2 = "+q2);
                }
            }
            System.out.println("****** Log2 : " + queries2.getQueries().size() + "   /    " + queries2prunPath.size());
            return queries2prunPath;
         
    }

    public void typePredicates(ArrayList<String> sumFiles, String endpoint1, String endpoint2, String log1ClassSummary, String log2ClassSummary) throws IOException {
        logsPredicates = new ArrayList<Node>();
        for (SparqlQueryParser q1 : queries1.getQueries()) {
            Query query1 = QueryFactory.create(q1.getQueryString());
            if (query1 != null) {
		ArrayList<Node> qPredicates = this.getPredicates(query1);
		log1Predicates.addAll(qPredicates);
            }
        }
        for (SparqlQueryParser q2 : queries2.getQueries()) {
            Query query2 = QueryFactory.create(q2.getQueryString());
            if (query2 != null) {
		ArrayList<Node> qPredicates = this.getPredicates(query2);
		log2Predicates.addAll(qPredicates);}
        }
        System.out.println(log1Predicates.size());
	fw = new FileWriter(log1ClassSummary);
	bw = new BufferedWriter(fw);
	int ctr = 0;
	/*        for (Node plog1 : log1Predicates) {
	    if (plog1 != null) {
            List<String> ObjAuthorityP1 = matcher.getObjAuthority(plog1.toString(), sumFiles.get(0));
            if (ObjAuthorityP1 != null) {
		if (ObjAuthorityP1.contains("http://dbpedia.org")) {System.out.println("***"+plog1);
                String q = "SELECT DISTINCT ?type WHERE { \n"
                        + "?s <"+plog1+"> ?o . \n"
                        + "FILTER (isURI(?o) ) \n"
                        + "SERVICE <"+endpoint2+"> \n"
		    + "{ ?o <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?type } } ";
                System.out.println("q = "+q);
		ctr++;
                QueryExecution queryExecution = QueryExecutionFactory.sparqlService(endpoint1, q);
                ResultSet resultSet = queryExecution.execSelect();
                bw.write("predicate: "+plog1.toString()+"\n");
                bw.write("Object Classes: \n");
                for ( ; resultSet.hasNext() ; )
		    {
			QuerySolution soln = resultSet.nextSolution() ;
			RDFNode x = soln.get("type") ;   
			System.out.println(x);
			bw.write(x+", ");
		    }
                bw.write("---------------");

            
		}}}
		}*/System.out.println("ctre = "+ctr); ctr=0;
	System.out.println(log2Predicates.size());
        fw = new FileWriter(log2ClassSummary);
        bw = new BufferedWriter(fw);
	 for (Node plog2 : log2Predicates) {
            if (plog2 != null) {
	    List<String> SubjAuthorityP2 = matcher.getSubjAuthority(plog2.toString(), sumFiles.get(1));
	    if (SubjAuthorityP2 != null) {
            if (SubjAuthorityP2.contains("http://dbpedia.org")) {
                String q = "SELECT DISTINCT ?type WHERE { "
                        + "?s <"+plog2+"> ?o . "
		    + "FILTER (isURI(?s) ) \n"
		    + " ?s <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?type  } ";
		//		Thread.sleep(6000);
                try{
		    Thread.sleep(10000);
		} 
		catch(InterruptedException e)
		    {
			// this part is executed when an exception (in this example InterruptedException) occurs
		    }
		System.out.println("q = "+q);ctr++;
                QueryExecution queryExecution = QueryExecutionFactory.sparqlService(endpoint2, q);
		//                queryExecution.setTimeout(5000);

                ResultSet resultSet = queryExecution.execSelect();
                bw.write("predicate: "+plog2.toString()+"\n");
                bw.write("Subject Classes: \n");
                for ( ; resultSet.hasNext() ; )
		    {
			QuerySolution soln = resultSet.nextSolution() ;
			RDFNode x = soln.get("type") ;   
						System.out.println(x);
			bw.write(x+", ");
		    }
                bw.write("---------------");
            }}}
        }
	 System.out.println("ctre = "+ctr);
    }
       
    public void executeQueryService(String q, String endpoint) {
	QueryExecution queryExecution = QueryExecutionFactory.sparqlService(endpoint, q);
	ResultSet resultSet = queryExecution.execSelect();
	for ( ; resultSet.hasNext() ; )
	    {
		System.out.println(resultSet.next());
	    }
    }       


    public void typePredicatesFedX(ArrayList<String> sumFiles, String endpoint1, String endpoint2, String log1ClassSummary, String log2ClassSummary) throws IOException, RepositoryException, MalformedQueryException, QueryEvaluationException, FedXException, Exception {
        logsPredicates = new ArrayList<Node>();
        Config.initialize();
        repo = FedXFactory.initializeSparqlFederation(Arrays.asList(endpoint1, endpoint2));
        for (SparqlQueryParser q1 : queries1.getQueries()) {
            Query query1 = QueryFactory.create(q1.getQueryString());
            if (query1 != null) {
		ArrayList<Node> qPredicates = this.getPredicates(query1);
		log1Predicates.addAll(qPredicates);
            }
        }
        for (SparqlQueryParser q2 : queries2.getQueries()) {
            Query query2 = QueryFactory.create(q2.getQueryString());
            if (query2 != null) {
		ArrayList<Node> qPredicates = this.getPredicates(query2);
		log2Predicates.addAll(qPredicates);}
        }
        System.out.println(log1Predicates.size());
	fw = new FileWriter(log1ClassSummary);
	bw = new BufferedWriter(fw);
        for (Node plog1 : log1Predicates) {
            List<String> ObjAuthorityP1 = matcher.getObjAuthority(plog1.toString(), sumFiles.get(0));
            if (ObjAuthorityP1 != null) {
		if (ObjAuthorityP1.contains("http://dbpedia.org")) {
                String q = "SELECT DISTINCT ?type WHERE { \n"
                        + "?s <"+plog1+"> ?o . \n"
                        + "FILTER (isURI(?o) ) \n"
		    + "{ ?o <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?type } } ";
                System.out.println("q = "+q);
                TupleQuery query = null;
                query = repo.getConnection().prepareTupleQuery(QueryLanguage.SPARQL, q);
                TupleQueryResult res = null;
                res = query.evaluate();
                while (res.hasNext()) {
                    System.out.println(res.next());
            
                }
                bw.write("---------------");
		}}
        }
        for (Node plog2 : log2Predicates) {
            List<String> SubjAuthorityP2 = matcher.getSubjAuthority(plog2.toString(), sumFiles.get(2));
            if (SubjAuthorityP2 != null) {
		if (SubjAuthorityP2.contains("http://dbpedia.org")) {
                String q = "SELECT DISTINCT ?type WHERE { "
                        + "?s <"+plog2+"> ?o . "
                        + "FILTER (isURI(?s) ) \n"
		    + " ?s <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?type } ";
                System.out.println("q = "+q);
                TupleQuery query = null;
                query = repo.getConnection().prepareTupleQuery(QueryLanguage.SPARQL, q);
                TupleQueryResult res = null;
                res = query.evaluate();
                while (res.hasNext()) {
                    System.out.println(res.next());
            
                }
                bw.write("---------------");
		}}
        }
        
    }
    public void typePredicatesSageV2(ArrayList<String> sumFiles, String endpoint1, String endpoint2, String log1ClassSummary, String log2ClassSummary) throws IOException {
        logsPredicates = new ArrayList<Node>();
        for (SparqlQueryParser q1 : queries1.getQueries()) {
            Query query1 = QueryFactory.create(q1.getQueryString());
            if (query1 != null) {
		ArrayList<Node> qPredicates = this.getPredicates(query1);
		log1Predicates.addAll(qPredicates);
            }
        }
        for (SparqlQueryParser q2 : queries2.getQueries()) {
            Query query2 = QueryFactory.create(q2.getQueryString());
            if (query2 != null) {
		ArrayList<Node> qPredicates = this.getPredicates(query2);
		log2Predicates.addAll(qPredicates);}
        }
        System.out.println(log1Predicates.size());
	fw = new FileWriter(log1ClassSummary);
	bw = new BufferedWriter(fw);
        for (Node plog1 : log1Predicates) {
	    if (plog1 != null) {
            List<String> ObjAuthorityP1 = matcher.getObjAuthority(plog1.toString(), sumFiles.get(0));
            if (ObjAuthorityP1 != null) {
		if (ObjAuthorityP1.contains("http://dbpedia.org")) {
                String q = "SELECT DISTINCT ?type WHERE { \n"
                        + "?s <"+plog1+"> ?o . \n"
		        + "FILTER (isURI(?o) ) \n"
                        + "{ SERVICE <"+endpoint2+"> \n"
		        + "{ ?o <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?type }} \n"
		        +"Union {?o <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?type} } ";
                System.out.println("q = "+q);
		/*	try
		    {
			Thread.sleep(10000);
		    }
		catch(InterruptedException e)
		    {
			// this part is executed when an exception (in this example InterruptedException) occurs                                          
			}*/
		//	Thread.sleep(10000);
                ExecutionStats spy = new ExecutionStats();
		Query query = QueryFactory.create(q);
		FederatedQueryFactory factory = new ServiceFederatedQueryFactory(endpoint1, query, spy);
		factory.buildFederation();
		query = factory.getLocalizedQuery();
		Dataset federation = factory.getFederationDataset();
		// Plug-in the custom ARQ engine for Sage graphs
		SageExecutionContext.configureDefault(ARQ.getContext());
		// Evaluate SPARQL query
		QueryExecutor executor = new SelectQueryExecutor("csv");
		spy.startTimer();
		QueryExecution qexec = QueryExecutionFactory.create(query, federation);


  ResultSet results = qexec.execSelect();

 spy.stopTimer(); 
int qResult = 0;                                                                        
/*
                    if (results.hasNext()) {

                        qResult++;

                        System.out.println(results.next());


                    }*/
		//executor.executeResultSet(federation, query);   
//		ResultSet results = executor.executeResultSet(federation, query);
                bw.write("capability\n");
		bw.write("[\n");
                bw.write("predicate: "+plog1.toString()+"\n");
                bw.write("Object Classes: ");
//ResultSet results = null;
		//		List<QuerySolution> list = ResultSetFormatter.toList(results);
                //System.out.println(list.size());
                //bw.write(list.toString());
		//		bw.write(ResultSetFormatter.toList(results).toString());
		//		bw.write(executor.execute(federation, query));
		int Compteur = 0;
                for ( ; results.hasNext() ; )
		    {Compteur++;
			QuerySolution soln = results.nextSolution() ;
			RDFNode x = soln.get("type") ;   
					System.out.println(x);
			bw.write(x+", ");
			}
		bw.write("\n] ; \n");
		System.out.println("compteur = "+Compteur);
		//   bw.write("\n---------------\n");
		//List<QuerySolution> list = ResultSetFormatter.toList(results);
                //System.out.println(list.size());
           
		}}/*
	    List<String> SubjAuthorityP1 = matcher.getSubjAuthority(plog1.toString(), sumFiles.get(0));
            if (SubjAuthorityP1 != null) {
		if (SubjAuthorityP1.contains("http://dbpedia.org")) {
                String q = "SELECT DISTINCT ?type WHERE { \n"
                        + "?s <"+plog1+"> ?o . \n"
                        + "FILTER (isURI(?s) ) \n"
                        + "SERVICE <"+endpoint2+"> \n"
		    + "{ ?s <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?type } } ";
                System.out.println("q = "+q);
                ExecutionStats spy = new ExecutionStats();
		Query query = QueryFactory.create(q);
		FederatedQueryFactory factory = new ServiceFederatedQueryFactory(endpoint1, query, spy);
		factory.buildFederation();
		query = factory.getLocalizedQuery();
		Dataset federation = factory.getFederationDataset();
		// Plug-in the custom ARQ engine for Sage graphs
		SageExecutionContext.configureDefault(ARQ.getContext());
		// Evaluate SPARQL query
		QueryExecutor executor = new SelectQueryExecutor("csv");
		spy.startTimer();
		//executor.executeResultSet(federation, query);   
		ResultSet results = executor.executeResultSet(federation, query);
		bw.write("\nSubject Classes: \n");
                for ( ; results.hasNext() ; )
		    {
			QuerySolution soln = results.nextSolution() ;
			RDFNode x = soln.get("type") ;   
			System.out.println(x);
			bw.write(x+", ");
		    }
                bw.write("\n---------------\n");
		}
		}*/

}
        }
	fw.flush();
	bw.flush();
	System.out.println(log2Predicates.size());
	fw2 = new FileWriter(log2ClassSummary);
        bw2 = new BufferedWriter(fw2);
	int compteur = 0;
        for (Node plog2 : log2Predicates) {
	    if (plog2 != null ) {
            List<String> SubjAuthorityP2 = matcher.getSubjAuthority(plog2.toString(), sumFiles.get(1));
            if (SubjAuthorityP2 != null) {
		if (SubjAuthorityP2.contains("http://dbpedia.org")) {
		    if (!plog2.toString().contains("http://www.w3.org/1999/02/22-rdf-syntax-ns#type") && !plog2.toString().contains("comment")&& !plog2.toString().contains("label")&& !plog2.toString().contains("subject")&& !plog2.toString().contains("depiction")&& !plog2.toString().contains("thumbnail") && !plog2.toString().contains("http://xmlns.com/foaf/0.1/name")){
                String q = "SELECT DISTINCT ?type WHERE { "
                        + "?s <"+plog2+"> ?o . "
                        + "FILTER (isURI(?s) ) \n"
		    + " ?s <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?type } ";
		System.out.println("q = "+q);
		/*		try 
		    {
			Thread.sleep(10000);
		    } 
		catch(InterruptedException e)
		    {
			// this part is executed when an exception (in this example InterruptedException) occurs
			}*/
		//		Thread.sleep(10000);
                ExecutionStats spy = new ExecutionStats();
		Query query = QueryFactory.create(q);
		FederatedQueryFactory factory = new ServiceFederatedQueryFactory(endpoint2, query, spy);
		factory.buildFederation();
		query = factory.getLocalizedQuery();
		Dataset federation = factory.getFederationDataset();
		// Plug-in the custom ARQ engine for Sage graphs
		SageExecutionContext.configureDefault(ARQ.getContext());
		// Evaluate SPARQL query
		QueryExecutor executor = new SelectQueryExecutor("csv");
		spy.startTimer();
ResultSet results = null;
		//executor.executeResultSet(federation, query);   
//		ResultSet results = executor.executeResultSet(federation, query);
		//        bw2.write(executor.execute(federation, query));
		bw2.write("capability\n");
                bw2.write("[\n");
                bw2.write("predicate: "+plog2.toString()+"\n");
                bw2.write("Subject Classes: ");
		compteur = 0;
		//List<QuerySolution> list = ResultSetFormatter.toList(results);
		//System.out.println(list.size());
		//	bw2.write(list.toString()); 
		//		bw2.write(ResultSetFormatter.toList(results).toString());
		//		bw2.write(executor.execute(federation, query));
		                for ( ; results.hasNext() ; )
		    {compteur++;
			QuerySolution soln = results.nextSolution() ;
			RDFNode x = soln.get("type") ;   
			//	System.out.println(x);
			bw2.write(x+", ");
			}
		System.out.println("compteur = "+compteur);
		bw2.write("\n] ; \n");
		bw2.flush();
		//		List<QuerySolution> list = ResultSetFormatter.toList(results);
		// System.out.println(list.size());
		//                bw2.write("\n---------------\n");
		    }}}}
        }
        fw2.flush();
        bw2.flush();
    }
       
    public void pruningLogsPredicatClasses(String so) {
        log1Predicates = new ArrayList<Node>();
        log2Predicates = new ArrayList<Node>();
        log1PredicatesPrun = new HashSet<Node>();
        log2PredicatesPrun = new HashSet<Node>();
        
        for (SparqlQueryParser q1 : queries1.getQueries()) {
            Query query1 = QueryFactory.create(q1.getQueryString());
            if (query1 != null) {
		ArrayList<Node> qPredicates = this.getPredicates(query1);
		log1Predicates.addAll(qPredicates);}
        }
        for (SparqlQueryParser q2 : queries2.getQueries()) {
            Query query2 = QueryFactory.create(q2.getQueryString());
            if (query2 != null) {
		ArrayList<Node> qPredicates = this.getPredicates(query2);
		log2Predicates.addAll(qPredicates);}
        }
        
        for (Node nlog1 : log1Predicates) {
            if ((nlog1 != null) && (!nlog1.toString().substring(0, 1).equals("?"))) {
		if (so.equals("path")) {
                    List<String> ObjClasses1 = typesSummaries1.getObjClasses(nlog1.toString());
                    if ((ObjClasses1 != null)) {
			for (Node nlog2 : log2Predicates) {
			    if ((nlog2 != null) && (!nlog2.toString().substring(0, 1).equals("?"))) {
				List<String> SbjClasses2 = typesSummaries2.getSbjClasses(nlog2.toString());
				if ((SbjClasses2 != null)) {
				    System.out.println("ObjClasses1: "+ObjClasses1+"       "+nlog1);
				    System.out.println("SbjClasses2: "+SbjClasses2+"       "+nlog2);
				    Set<String> intersection1 = new HashSet<String>(ObjClasses1); // use the copy constructor                          
                                    intersection1.retainAll(SbjClasses2);
				    System.out.println(nlog1+" / "+nlog2+" / "+intersection1);
                                    if (intersection1.size() > 0) {//System.out.println(nlog1+"    .    "+nlog2);                                         
                                        log1PredicatesPrun.add(nlog1);
                                        log2PredicatesPrun.add(nlog2);
				    }
				}
			    }
			}
		    }
		}
		if (so.equals("star")) {
                      
		}
	    }
	}
    
    }
    
    public void typePredicatesJena(ArrayList<String> sumFiles, String endpoint1, String endpoint2, String log1ClassSummary, String log2ClassSummary) throws IOException {
        logsPredicates = new ArrayList<Node>();
        for (SparqlQueryParser q1 : queries1.getQueries()) {
            Query query1 = QueryFactory.create(q1.getQueryString());
            if (query1 != null) {
                ArrayList<Node> qPredicates = this.getPredicates(query1);
                log1Predicates.addAll(qPredicates);
            }
        }
        for (SparqlQueryParser q2 : queries2.getQueries()) {
            Query query2 = QueryFactory.create(q2.getQueryString());
            if (query2 != null) {
                ArrayList<Node> qPredicates = this.getPredicates(query2);
                log2Predicates.addAll(qPredicates);}
        }
	System.out.println(log1Predicates.size());
        fw = new FileWriter(log1ClassSummary);
        bw = new BufferedWriter(fw);
        for (Node plog1 : log1Predicates) {
            if (plog1 != null) {
		List<String> ObjAuthorityP1 = matcher.getObjAuthority(plog1.toString(), sumFiles.get(0));
		if (ObjAuthorityP1 != null) {
		    if (ObjAuthorityP1.contains("http://dbpedia.org")) {
                String q = "SELECT DISTINCT ?type WHERE { \n"
                        + "?s <"+plog1+"> ?o . \n"
                    //  + "FILTER (isURI(?o) ) \n"                                                                                                        
                        + "SERVICE <"+endpoint2+"> \n"
                    + "{ ?o <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?type } } ";
                System.out.println("q = "+q);
                try
                    {
			Thread.sleep(6000);
                    }
                catch(InterruptedException e)
                    {
                        // this part is executed when an exception (in this example InterruptedException) occurs                                         \
                                                                                                                                                          
                    }
                ExecutionStats spy = new ExecutionStats();
                QueryExecution queryExecution = QueryExecutionFactory.sparqlService(endpoint1, q);
                ResultSet results = queryExecution.execSelect();
               
                bw.write("capability\n");
                bw.write("[\n");
                bw.write("predicate: "+plog1.toString()+"\n");
                bw.write("Object Classes: ");
                                                                                                
                for ( ; results.hasNext() ; )                                                                                                             
                    {                                                                                                                                     
                        QuerySolution soln = results.nextSolution() ;                                                                                     
                        RDFNode x = soln.get("type") ;                                                                                                    
                        System.out.println(x);                                                                                                            
                        bw.write(x+", ");                                                                                                                 
		    }
                bw.write("\n] ; \n");
                //   bw.write("\n---------------\n");                                                                                                     


		    }}
	    }}
	    fw.flush();
	    bw.flush();
	    System.out.println(log2Predicates.size());
	    fw2 = new FileWriter(log2ClassSummary);
	    bw2 = new BufferedWriter(fw2);
	    int compteur = 0;
	    for (Node plog2 : log2Predicates) {
		if (plog2 != null ) {
		    List<String> SubjAuthorityP2 = matcher.getSubjAuthority(plog2.toString(), sumFiles.get(1));
		    if (SubjAuthorityP2 != null) {
			if (SubjAuthorityP2.contains("http://dbpedia.org")) {
                String q = "SELECT DISTINCT ?type WHERE { "
                        + "?s <"+plog2+"> ?o . "
                    //                        + "FILTER (isURI(?s) ) \n"                                                                                  
                    + " ?s <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?type } ";
                System.out.println("q = "+q);
                try
                    {
                        Thread.sleep(6000);
                    }
                catch(InterruptedException e)
                    {
                        // this part is executed when an exception (in this example InterruptedException) occurs                                          
                    }
                //              Thread.sleep(10000);                                                                                                      
                QueryExecution queryExecution = QueryExecutionFactory.sparqlService(endpoint1, q);
                ResultSet results = queryExecution.execSelect();                                                                         
                bw2.write("capability\n");
                bw2.write("[\n");
                bw2.write("predicate: "+plog2.toString()+"\n");
                bw2.write("Subject Classes: ");
                compteur = 0;
                for ( ; results.hasNext() ; )                                                                                                             
                    {                                                                                                                                     
                        QuerySolution soln = results.nextSolution() ;                                                                                     
                        RDFNode x = soln.get("type") ;                                                                                                    
                        System.out.println(x);                                                                                                            
                        bw2.write(x+", ");                                                                                                                 
		    }
                bw2.write("\n] ; \n");
			}}}
	    }
	    fw2.flush();
	    bw2.flush();
	}

    public void pruningLogsPredicatClassesV2(String so) {
        log1Predicates = new ArrayList<Node>();
        log2Predicates = new ArrayList<Node>();
        log1PredicatesPrun = new HashSet<Node>();
        log2PredicatesPrun = new HashSet<Node>();
        listJoinPredPath = new HashMap<String, HashSet<String>>();
        
        for (SparqlQueryParser q1 : queries1.getQueries()) {
            Query query1 = QueryFactory.create(q1.getQueryString());
            if (query1 != null) {
		ArrayList<Node> qPredicates = this.getPredicates(query1);
		log1Predicates.addAll(qPredicates);}
        }
        for (SparqlQueryParser q2 : queries2.getQueries()) {
            Query query2 = QueryFactory.create(q2.getQueryString());
            if (query2 != null) {
		ArrayList<Node> qPredicates = this.getPredicates(query2);
		log2Predicates.addAll(qPredicates);}
        }
        for (Node nlog1 : log1Predicates) {
            if ((nlog1 != null) && (!nlog1.toString().substring(0, 1).equals("?"))) {
		if (so.equals("path")) {
                    List<String> ObjClasses1 = typesSummaries1.getObjClasses(nlog1.toString());
                    if ((ObjClasses1 != null)) {
			HashSet<String> temp = new HashSet<String>();
			for (Node nlog2 : log2Predicates) {
			    if ((nlog2 != null) && (!nlog2.toString().substring(0, 1).equals("?"))) {
				List<String> SbjClasses2 = typesSummaries2.getSbjClasses(nlog2.toString());
				if ((SbjClasses2 != null)) {
				    List<String> intersection1 = new ArrayList<String>(ObjClasses1); // use the copy constructor            
                                    intersection1.retainAll(SbjClasses2);
				    //List<String> intersection1 = this.intersection(ObjClasses1, SbjClasses2);
				    System.out.println("nlog1 = "+nlog1+"  /  nlog2 = "+nlog2);
				    //    System.out.println(ObjClasses1.size()+"*** "+ObjClasses1);
				    //System.out.println(SbjClasses2.size()+"*** "+SbjClasses2);
				    System.out.println(intersection1.size());
				    //				    System.out.println("----"+ObjClasses1.retainAll(SbjClasses2));
				    // System.out.println("----"+SbjClasses2.retainAll(ObjClasses1));
                                    if (intersection1.size() > 0) {//System.out.println(nlog1+"    .    "+nlog2); 
                                        if (!((intersection1.size() == 1) && (intersection1.get(0).contains("http://www.w3.org/2002/07/owl#Thing")))) {
					temp.add(nlog2.toString());
                                        log1PredicatesPrun.add(nlog1);
                                        log2PredicatesPrun.add(nlog2);
					}}
				}
			    }
			}
			listJoinPredPath.put(nlog1.toString(), temp);
		    }
		}
		if (so.equals("star")) {
                      
		}
	    }
	}
    
    }

    public <String> List<String> intersection(List<String> list1, List<String> list2) {
	List<String> list = new ArrayList<String>();

	for (String t: list1) {
	    if(list2.contains(t)) {
		list.add(t);
	    }
	}

	return list;
    }

    HashMap<String, HashSet<String>> dicJoinPathV2 = new HashMap<String, HashSet<String>>();
    public void genFedMinimalPathServiceSageV2(String sparqlEndpoint1, String sparqlEndpoint2, String genFedFile) throws MalformedQueryException, QueryEvaluationException, Exception {
        dicJoinPathV2 = new HashMap<String, HashSet<String>>();
        fw = new FileWriter(genFedFile);
        bw = new BufferedWriter(fw);
        bw.write("#-------------------------------------------------------\n");
        for (String nd1 : listJoinPredPath.keySet()) {
            HashSet<String> temp = new HashSet<String>();
            HashSet<String> values = listJoinPredPath.get(nd1);
            for (String nd2 : values) {
		temp.add(nd2);
                String q = "SELECT * WHERE {\n"
                        + "?s <" + nd1 + "> ?x . \n"
                        + "SERVICE <" + sparqlEndpoint2 + "> { \n"
                        + "?x <" + nd2 + "> ?o . } \n"
		    + "} LIMIT 1";
                bw.write("query: "+q+"\n");
                bw.write("#-------------------------------------------------------\n");
                System.out.println("*q = " + q);
                Query qGen = QueryFactory.create(q);
                genFedMinimalPath.add(qGen);
                bw.flush();
            }
	    dicJoinPathV2.put(nd1, temp);
        }
        HashSet<String> generalPredicate = new HashSet<String>();
        generalPredicate.add("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
        generalPredicate.add("http://www.w3.org/2000/01/rdf-schema#label");
        generalPredicate.add("http://www.w3.org/2000/01/rdf-schema#comment");
        generalPredicate.add("http://www.w3.org/2004/02/skos/core#subject");
        generalPredicate.add("http://xmlns.com/foaf/0.1/depiction");
        generalPredicate.add("http://dbpedia.org/ontology/thumbnail");
        generalPredicate.add("http://xmlns.com/foaf/0.1/name");
        for (Node nd1 : log1PredicatesPrun) {
	    HashSet<String> temp = new HashSet<String>();
            for (String str : generalPredicate) {        
		temp.add(str);   
            String q = "SELECT * WHERE {\n"
                        + "?s <" + nd1 + "> ?x . \n"
                        + "SERVICE <" + sparqlEndpoint2 + "> { \n"
                        + "?x <" + str + "> ?o . } \n"
		+ "} LIMIT 1";
            bw.write("query: "+q+"\n");
            bw.write("#-------------------------------------------------------\n");
	    System.out.println("*q = " + q);
	    Query qGen = QueryFactory.create(q);
	    genFedMinimalPath.add(qGen);
            bw.flush();
            }
	    dicJoinPathV2.put(nd1.toString(), temp);
        }
        System.out.println("genFedMinimalPath = " + genFedMinimalPath.size());
	//        System.out.println(genFedMinimalPath);
	System.out.println("Done.");
        bw.close();
        fw.close();
    }

    public void genFedLightServiceV2(String sparqlEndpoint1, String sparqlEndpoint2, String genFedFile) throws Exception {
        int ctr = 0;
        fw = new FileWriter(genFedFile);
        bw = new BufferedWriter(fw);
        bw.write("#-------------------------------------------------------\n");
        for (String s : dicJoinPathV2.keySet()) {
            ArrayList<SparqlQueryParser> queries1prunDicJoin = new ArrayList<SparqlQueryParser>();
            queries1prunDicJoin = pruningLog(queries1prunPath, s);
            for (String st : dicJoinPathV2.get(s)) {
                ArrayList<SparqlQueryParser> queries2prunDicJoin = new ArrayList<SparqlQueryParser>();
                queries2prunDicJoin = pruningLog(queries2prun, st);
                for (SparqlQueryParser q1ini : queries1prunDicJoin) {
                    System.out.println("q1ini = " + q1ini + "   /   " + s + "   /   " + st);
                    String q = this.concatenateQuery1TripleD1Service(q1ini.toString(), s, st, sparqlEndpoint2);
                    System.out.println("q = " + q);
                    bw.write("query: "+q+"\n");
                    bw.write("#-------------------------------------------------------\n");
                    bw.flush();
                    Query qGen = QueryFactory.create(q);
                    genFedLight1.add(qGen);
		}
                System.out.println("genFedLight 1 = " + genFedLight1.size());
                
                for (SparqlQueryParser q2ini : queries2prunDicJoin) {
		    System.out.println("q2ini = " + q2ini + "   /   " + st + "   /   " + s);
                    String q = this.concatenateQuery1TripleD2Service(q2ini.toString(), st, s, sparqlEndpoint2);
                    System.out.println("q = "+q);
                    bw.write("query: "+q+"\n");
                    bw.write("#-------------------------------------------------------\n");
                    bw.flush();
                    Query qGen = QueryFactory.create(q);
                    genFedLight2.add(qGen);
                }
                System.out.println("genFedLight 2 = " + genFedLight2.size());
            }
        }
        System.out.println("Done.");
        bw.close();
        fw.close();
    }

    public void typePredicatesSageAtIndice(ArrayList<String> sumFiles, String endpoint1, String endpoint2, String log1ClassSummary, String log2ClassSummary, String predicatStart) throws IOException {
        logsPredicates = new ArrayList<Node>();
        for (SparqlQueryParser q2 : queries2.getQueries()) {
            Query query2 = QueryFactory.create(q2.getQueryString());
            if (query2 != null) {
                ArrayList<Node> qPredicates = this.getPredicates(query2);
		for (Node n : qPredicates) {
                    if (!log2Predicates.contains(n)) {log2Predicates.add(n);}
                }
//                log2Predicates.addAll(qPredicates);
}
        }//System.out.println(log2Predicates);
	//	System.out.println(predicatStart);
	int indice = 0;
	for (int i= 0; i<log2Predicates.size(); i++){
	    //	    System.out.println(log2Predicates.get(i));
	    if (((log2Predicates.get(i) != null) && (log2Predicates.get(i).toString().equals(predicatStart)))) 
		{indice = i; System.out.println("indice  = "+i);}
	}
	//        int indice = log2Predicates.indexOf(predicatStart);
        System.out.println(log2Predicates.size()+" indice= "+indice);
        fw2 = new FileWriter(log2ClassSummary, true);
        bw2 = new BufferedWriter(fw2);
        int compteur = 0;
        for (int i= indice; i<log2Predicates.size(); i++) {
            if (log2Predicates.get(i) != null ) {
		List<String> SubjAuthorityP2 = matcher.getSubjAuthority(log2Predicates.get(i).toString(), sumFiles.get(1));
		if (SubjAuthorityP2 != null) {
		    if (SubjAuthorityP2.contains("http://dbpedia.org")) {
			if (!log2Predicates.get(i).toString().contains("http://www.w3.org/1999/02/22-rdf-syntax-ns#type") && !log2Predicates.get(i).toString().contains("comment")&& !log2Predicates.get(i).toString().contains("label")&& !log2Predicates.get(i).toString().contains("subject")&& !log2Predicates.get(i).toString().contains("depiction")&& !log2Predicates.get(i).toString().contains("thumbnail") && !log2Predicates.get(i).toString().contains("http://xmlns.com/foaf/0.1/name")){
                String q = "SELECT DISTINCT ?type WHERE { "
		    + "?s <"+log2Predicates.get(i)+"> ?o . "
                        + "FILTER (isURI(?s) ) \n"
                    + " ?s <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?type } ";
                System.out.println("q = "+q);
		ExecutionStats spy = new ExecutionStats();
                Query query = QueryFactory.create(q);
                FederatedQueryFactory factory = new ServiceFederatedQueryFactory(endpoint2, query, spy);
                factory.buildFederation();
                query = factory.getLocalizedQuery();
                Dataset federation = factory.getFederationDataset();
                // Plug-in the custom ARQ engine for Sage graphs                                                                                          
                SageExecutionContext.configureDefault(ARQ.getContext());
                // Evaluate SPARQL query                                                                                                                  
                QueryExecutor executor = new SelectQueryExecutor("csv");
                spy.startTimer();
ResultSet results = null;
                //executor.executeResultSet(federation, query);                                                                                           
  //              ResultSet results = executor.executeResultSet(federation, query);
                //        bw2.write(executor.execute(federation, query));                                                                                 
                bw2.write("capability\n");
                bw2.write("[\n");
                bw2.write("predicate: "+log2Predicates.get(i).toString()+"\n");
                bw2.write("Subject Classes: ");
		for ( ; results.hasNext() ; )
                    {compteur++;
                        QuerySolution soln = results.nextSolution() ;
                        RDFNode x = soln.get("type") ;
                        //      System.out.println(x);                                                                                                    
                        bw2.write(x+", ");
		    }
                System.out.println("compteur = "+compteur);
                bw2.write("\n] ; \n");
                bw2.flush();
			}}}}
	    }
        
        fw2.flush();
        bw2.flush();
    }

        
    public void executeQueriesSage(String queriesFile, String endpoint, String federatedQueriesFile) throws FileNotFoundException, IOException {
	FileReader fr = null;
	BufferedReader br = null;
	String sCurrentLine;
	fw = new FileWriter(federatedQueriesFile, true);
	bw = new BufferedWriter(fw);
	bw.write("#-------------------------------------------------------\n");
	br = new BufferedReader(new FileReader(queriesFile));
	boolean ok = true;
	StringBuffer temp = new StringBuffer() ;
	while ((sCurrentLine = br.readLine()) != null ) {
	    if (sCurrentLine.startsWith("#-----") ) {
		if (temp.length()!=0) {
		    String q = temp.toString().split(": ")[1];
		    System.out.println("***query: "+q);
		    ExecutionStats spy = new ExecutionStats();
		    Query query = QueryFactory.create(q);
		    FederatedQueryFactory factory = new ServiceFederatedQueryFactory(endpoint, query, spy);
		    factory.buildFederation();
		    query = factory.getLocalizedQuery();
		    Dataset federation = factory.getFederationDataset();
		    // Plug-in the custom ARQ engine for Sage graphs                                                                                          
		    SageExecutionContext.configureDefault(ARQ.getContext());
		    // Evaluate SPARQL query                                                                                                                  
		    QueryExecutor executor = new SelectQueryExecutor("csv");
		    spy.startTimer();
		    //executor.executeResultSet(federation, query);                                                                                           
//		    ResultSet results = executor.executeResultSet(federation, query);
ResultSet results = null;
		    int qResult = 0;
                    if (results.hasNext()) {
                        qResult++;
                    }
                    System.out.println(qResult);
		    if (qResult > 0) {
			bw.write("query: "+q);
			bw.write("#-------------------------------------------------------\n");
		    }
                    temp = new StringBuffer() ;
		}
	    }
	    else 
		{
		    temp.append(" "+sCurrentLine);
		}
	}
        
    }
    
public void executeQueriesSage(String queriesFile, String endpoint, String federatedQueriesFile, String nonPqueries) throws FileNotFoundException, IOException {
            FileReader fr = null;
            BufferedReader br = null;
            FileReader fr2 = null;
            BufferedReader br2 = null;
            String sCurrentLine;
            fw = new FileWriter(federatedQueriesFile);
            bw = new BufferedWriter(fw);
            fw2 = new FileWriter(nonPqueries);
            bw2 = new BufferedWriter(fw2);
            bw.write("\n#-------------------------------------------------------\n");
            bw2.write("\n#-------------------------------------------------------\n");
            br = new BufferedReader(new FileReader(queriesFile));
            boolean ok = true;
            StringBuffer temp = new StringBuffer() ;
            while ((sCurrentLine = br.readLine()) != null ) {
                if (sCurrentLine.startsWith("#-----") ) {
                        if (temp.length()!=0) {
                        String q = temp.toString().substring(7);
//		String q = temp.toString().split(": ")[1];

		System.out.println("query: "+q);
//System.out.println("coucou");
                   ExecutionStats spy = new ExecutionStats();
                Query query = QueryFactory.create(q);
                FederatedQueryFactory factory = new ServiceFederatedQueryFactory(endpoint, query, spy);
                factory.buildFederation();
                query = factory.getLocalizedQuery();
                Dataset federation = factory.getFederationDataset();
                // Plug-in the custom ARQ engine for Sage graphs                                                                                          
                SageExecutionContext.configureDefault(ARQ.getContext());
                // Evaluate SPARQL query                                                                                                                  
//                QueryExecutor executor = new SelectQueryExecutor("csv");
//          SelectQueryExecuto executor = new SelectQueryExecutor("csv");
	        spy.startTimer();
		//ResultSet results = null;
                QueryExecution qexec = QueryExecutionFactory.create(query, federation);
                ResultSet results = qexec.execSelect();
                
                //executor.executeResultSet(federation, query);                                                                                           
  //              ResultSet results = executor.executeResultSet(federation, query);
//if (results != null) {
		//executor.execute(federation, query);                
		spy.stopTimer();
		//System.out.println("* "+results.hasNext()+"        ");		
		int qResult = 0;
		/*for ( ; results.hasNext() ; )
                    {qResult++;
                        QuerySolution soln = results.nextSolution() ;
                        RDFNode x = soln.get("type") ;
                              System.out.println(x);                                                                                                    
                       
                        }*/
                    if (results.hasNext()) {
                        qResult++;
			System.out.println(results.next());
                    }
			System.out.println(qResult);
                        if (qResult > 0) {
                            bw.write("query: "+q);
                            bw.write("\n#-------------------------------------------------------\n");
			    bw.flush();
                        }
                        else {
                            bw2.write("query: "+q);
                            bw2.write("\n#-------------------------------------------------------\n");
			    bw2.flush();
                        }//}
                    temp = new StringBuffer() ;
                    }
                    }
                    else 
                    {
                        temp.append(" "+sCurrentLine);
                    }
            }
        
        }
        

public void joinPredicatesFromMinimalPath(String file) throws FileNotFoundException, IOException {
         dicJoinPath = new HashMap<String, HashSet<String>>();
        BufferedReader br = null;
        HashSet<String> tempbased_near = new HashSet<String>();
        HashSet<String> tempsameAs = new HashSet<String>();
        HashSet<String> temphasLocation = new HashSet<String>();    
        br = new BufferedReader(new FileReader(file));
         String sCurrentLine;
         StringBuffer temp = new StringBuffer() ;
            while ((sCurrentLine = br.readLine()) != null ) {
                if (sCurrentLine.startsWith("query:") ) {
// System.out.println("   "+sCurrentLine);
                        String q = sCurrentLine.toString();
                        String[] T = q.split(" ");
                  /*      for (int i=0; i<T.length; i++) {
                            System.out.println("i = "+i+"    T["+i+"] = "+T[i]);
                        }*/
if (T.length > 16) {
                        if (T[7].contains("based_near")) {
                            tempbased_near.add(T[16].substring(1, T[16].length()-1));
                        }
                        else if (T[7].contains("sameAs")) {
                            tempsameAs.add(T[16].substring(1, T[16].length()-1));
                        }
                        else if (T[7].contains("hasLocation")) {
                            temphasLocation.add(T[16].substring(1, T[16].length()-1));
                        }}
                    }
                    temp = new StringBuffer() ;
                }
            dicJoinPath.put("http://xmlns.com/foaf/0.1/based_near", tempbased_near);
            dicJoinPath.put("http://www.w3.org/2002/07/owl#sameAs", tempsameAs);
            dicJoinPath.put("http://data.semanticweb.org/ns/swc/ontology#hasLocation", temphasLocation);
//            System.out.println("     "+dicJoinPath);
    }


public void genFedLightServiceSage(String genFedFile, String sparqlEndpoint2) throws Exception {
System.out.println("dicJoinPath "+dicJoinPath.size());
        fw = new FileWriter(genFedFile);
        bw = new BufferedWriter(fw);
        bw.write("#-------------------------------------------------------\n");
        int ctr = 0;
        for (String s : dicJoinPath.keySet()) {
            ArrayList<SparqlQueryParser> queries1prunDicJoin = new ArrayList<SparqlQueryParser>();
            queries1prunDicJoin = pruningLog(queries1.getQueries(), s);
            for (String st : dicJoinPath.get(s)) {
                ArrayList<SparqlQueryParser> queries2prunDicJoin = new ArrayList<SparqlQueryParser>();
                queries2prunDicJoin = pruningLog(queries2.getQueries(), st);
                for (SparqlQueryParser q1ini : queries1prunDicJoin) {
                //   System.out.println("q1ini = " + q1ini + "   /   " + s + "   /   " + st);
                    String q = this.concatenateQuery1TripleD1Service(q1ini.toString(), s, st, sparqlEndpoint2);
                    System.out.println("q = " + q);
//                    bw.write(q);
		bw.write("query: "+q);
 		bw.write("\n#-------------------------------------------------------\n");
                    bw.flush();
  //                      Query qGen = QueryFactory.create(q);
    //                    genFedLight1.add(qGen);
                }
//                System.out.println("genFedLight 1 = " + genFedLight1.size());
                    for (SparqlQueryParser q2ini : queries2prunDicJoin) {
                //System.out.println("q2ini = " + q2ini + "   /   " + st + "   /   " + s);
                    String q = this.concatenateQuery1TripleD2Service(q2ini.toString(), st, s, sparqlEndpoint2);
                    System.out.println("q = "+q);
                bw.write("query: "+q);
                bw.write("\n#-------------------------------------------------------\n");//    bw.write(q);
                    bw.flush();
      //                  Query qGen = QueryFactory.create(q);
        //                genFedLight2.add(qGen);
                }
        //        System.out.println("genFedLight 2 = " + genFedLight2.size());
            }
        }
        System.out.println("Done.");
        bw.flush();
            bw.write(genFedLight1.toString() + "\n");
            bw.write(genFedLight1.size()+ "\n");
            bw.write(genFedLight2.toString()+ "\n");
            bw.write(genFedLight2.size()+ "\n");
        System.out.println("all constrected queries = " + ctr);
    }


public void typePredicatesSage(ArrayList<String> sumFiles, String endpoint1, String endpoint2, String log1ClassSummary, String log2ClassSummary) throws IOException
 {
        logsPredicates = new ArrayList<Node>();
        for (SparqlQueryParser q1 : queries1.getQueries()) {
            Query query1 = QueryFactory.create(q1.getQueryString());
            if (query1 != null) {
                ArrayList<Node> qPredicates = this.getPredicates(query1);
                for (Node nd : qPredicates) {
                if (!log1Predicates.contains(nd)) {
                    log1Predicates.add(nd);
                }
                }
            }
        }
        for (SparqlQueryParser q2 : queries2.getQueries()) {
            Query query2 = QueryFactory.create(q2.getQueryString());
            if (query2 != null) {
                ArrayList<Node> qPredicates = this.getPredicates(query2);
                for (Node nd : qPredicates) {
                if (!log2Predicates.contains(nd)) {
                    log2Predicates.add(nd);
                }
                }
                }
        }
        System.out.println(log1Predicates.size());
        System.out.println(log2Predicates.size());
        
        fw = new FileWriter(log1ClassSummary);
        bw = new BufferedWriter(fw);
        for (Node plog1 : log1Predicates) {
            if (plog1 != null) {
            List<String> ObjAuthorityP1 = matcher.getObjAuthority(plog1.toString(), sumFiles.get(0));
            if (ObjAuthorityP1 != null) {
                if (ObjAuthorityP1.contains("http://dbpedia.org")) {
                String q = "SELECT DISTINCT ?type WHERE { \n"
                        + "?s <"+plog1+"> ?o . \n"
                        + "FILTER (isURI(?o) ) \n"
                        + "{ SERVICE <"+endpoint2+"> \n"
                        + "{ ?o <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?type }} \n"
                        +"Union {?o <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?type} } ";
                System.out.println("q = "+q);
                                                                                                                                       
                ExecutionStats spy = new ExecutionStats();
                Query query = QueryFactory.create(q);
                FederatedQueryFactory factory = new ServiceFederatedQueryFactory(endpoint1, query, spy);
                factory.buildFederation();
                query = factory.getLocalizedQuery();
                Dataset federation = factory.getFederationDataset();
                // Plug-in the custom ARQ engine for Sage graphs                                                                                                        
                SageExecutionContext.configureDefault(ARQ.getContext());
                // Evaluate SPARQL query                                                                                                                                
                QueryExecutor executor = new SelectQueryExecutor("csv");
                spy.startTimer();
                QueryExecution qexec = QueryExecutionFactory.create(query, federation);
                ResultSet results = qexec.execSelect();
                spy.stopTimer();                                                                          
                
                bw.write("capability\n");
                bw.write("[\n");
                bw.write("predicate: "+plog1.toString()+"\n");
                bw.write("Object Classes: ");
               
                int Compteur = 0;
                for ( ; results.hasNext() ; )
                 {Compteur++;
                        QuerySolution soln = results.nextSolution() ;
                        RDFNode x = soln.get("type") ;
                        System.out.println(x);
                        bw.write(x+", ");
                        }
                bw.write("\n] ; \n");
                System.out.println("compteur = "+Compteur);
                bw.flush();
                }
        }}}
        fw.flush();
        bw.flush();
        System.out.println(log2Predicates.size());
        fw2 = new FileWriter(log2ClassSummary);
        bw2 = new BufferedWriter(fw2);
        int compteur = 0;
        for (Node plog2 : log2Predicates) {
            if (plog2 != null ) {
            List<String> SubjAuthorityP2 = matcher.getSubjAuthority(plog2.toString(), sumFiles.get(1));
            if (SubjAuthorityP2 != null) {
                if (SubjAuthorityP2.contains("http://dbpedia.org")) {
                    if (!plog2.toString().contains("http://www.w3.org/1999/02/22-rdf-syntax-ns#type") && !plog2.toString().contains("comment")&& !plog2.toString().contains("label")&& !plog2.toString().contains("subject")&& !plog2.toString().contains("depiction")&& !plog2.toString().contains("thumbnail") && !plog2.toString().contains("http://xmlns.com/foaf/0.1/name")){
                String q = "SELECT DISTINCT ?type WHERE { "
                        + "?s <"+plog2+"> ?o . "
                        + "FILTER (isURI(?s) ) \n"
                    + " ?s <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?type } ";
                System.out.println("q = "+q);
                
                 ExecutionStats spy = new ExecutionStats();
                Query query = QueryFactory.create(q);
                FederatedQueryFactory factory = new ServiceFederatedQueryFactory(endpoint2, query, spy);
                factory.buildFederation();
                query = factory.getLocalizedQuery();
                Dataset federation = factory.getFederationDataset();
                // Plug-in the custom ARQ engine for Sage graphs                                                                                                        
                SageExecutionContext.configureDefault(ARQ.getContext());
                // Evaluate SPARQL query                                                                                                                                
                QueryExecution qexec = QueryExecutionFactory.create(query, federation);
                ResultSet results = qexec.execSelect();
                spy.stopTimer();                                                                                                      
                                                                                                          
                bw2.write("capability\n");
                bw2.write("[\n");
                bw2.write("predicate: "+plog2.toString()+"\n");
                bw2.write("Subject Classes: ");
                compteur = 0;
                    for ( ; results.hasNext() ; )
                    {compteur++;
                        QuerySolution soln = results.nextSolution() ;
                        RDFNode x = soln.get("type") ;
                         //     System.out.println(x);
                        bw2.write(x+", ");
                        }
                System.out.println("compteur = "+compteur);
                bw2.write("\n] ; \n");
                bw2.flush();
           }}}}
        }
        fw2.flush();
        bw2.flush();
    
    }

//////////////////////////////

/*public int getIndiceVal(String[] table, String val) {
        for (int j=0; j< table.length; j++) {
            if (table[j].contains(val)) {
                return j;
            }
        }
        return -1;
    }*/
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
            }}
        }
        for (int j = 0; j < table.length; j++) {
            q = q + table[j] + " ";
        }
        return q;
    }
    
  /*  public String concatenate2QueriesService(String query1, String predicate1Join, String query2, String sparqlEndpoint) {
        String q = "";
        String[] table = query1.split("[\\s\\xA0]+");
        String[] table2 = query2.split("[\\s\\xA0]+");
        int indiceWhere = this.getIndiceVal(table2, "WHERE");System.out.println(indiceWhere);
        String[] newtable = null;
        for (int i = 0; i < table.length; i++) {
            if ((table[i].contains(predicate1Join)) || (table[i].contains(":" + predicate1Join.split("/")[predicate1Join.split("/").length - 1])) || (table[i].contains(":" + predicate1Join.split("#")[predicate1Join.split("#").length - 1]))) {
                //System.out.println(i+" / "+table[i]);
                newtable = this.addElementAtIndice(table, "SERVICE <" + sparqlEndpoint + "> {", i + 3);
                int k=i+4;
                for (int j=indiceWhere+2; j<table2.length-1; j++) {
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
        for (int j = 0; j < newtable.length; j++) {
            q = q + newtable[j] + " ";
        }
        return q;
    } */

public String concatenate2QueriesService(String query1, String predicate1Join, String query2, String predicate2Join, String sparqlEndpoint) {
        String q = "";String temp="";
//System.out.println("query1 = "+query1);System.out.println("query2 = "+query2);
        String[] table = query1.split("[\\s\\xA0]+");
        String[] table2 = query2.split("[\\s\\xA0]+");
        int indiceWhere = this.getIndiceVal(table2, "WHERE");//System.out.println(indiceWhere);
        int indicePredicate2Join = this.getIndiceVal(table2, predicate2Join);
 	String valRemplace = null;
//System.out.println("         predicate2Join = "+predicate2Join+"      indicePredicate2Join = "+indicePredicate2Join);
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
                for (int j=indiceWhere+2; j<table2.length-1; j++) {
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
            }
            else { //var&cte
System.out.println("------------1");
                return null;
            }
        }
        else {
            if (temp.substring(0, 1).equals("?")) { //cte&var
System.out.println("------------2");
                return null;
            }
            else { //cte&cte
                if (valRemplace.equals(temp)) {
                    return q;
                }
                else {
System.out.println("------------3");
                    return null;
                }
            }
        }
        }
else {
return null;
}
       // return q;
    }


    public void genFedMaximalPath(String genFedFile, String sparqlEndpoint1, String sparqlEndpoint2) throws IOException {
        System.out.println("dicJoinPath "+dicJoinPath);
        fw = new FileWriter(genFedFile);
        bw = new BufferedWriter(fw);
        ArrayList<Query> genQueries = new ArrayList<Query>();
        bw.write("#-------------------------------------------------------\n");
        for (String s : dicJoinPath.keySet()) {
            ArrayList<SparqlQueryParser> queries1prunDicJoin = new ArrayList<>();
            queries1prunDicJoin = pruningLog(queries1.getQueries(), s);
            System.out.println("s = "+s+" / st = "+dicJoinPath.get(s).size());
            for (SparqlQueryParser q1 : queries1prunDicJoin) {//System.out.println("*"+q1);
                String query1 = this.renameVariables(q1.toString(), "log1");
//                System.out.println("s = "+s+" / st = "+dicJoinPath.get(s).size());
                for (String st : dicJoinPath.get(s)) {
                    ArrayList<SparqlQueryParser> queries2prunDicJoin = new ArrayList<>();
                    queries2prunDicJoin = pruningLog(queries2.getQueries(), st);
                    for (SparqlQueryParser q2 : queries2prunDicJoin) {
                        String query2 = this.renameVariables(q2.toString(), "log2");
	//		Query qquery1 = QueryFactory.create(query1);
	//		Query qquery2 = QueryFactory.create(query2);
	//		String q = this.concatenate2QueriesService(qquery1.toString(), s, qquery2.toString(), st, sparqlEndpoint2);
                        String q = this.concatenate2QueriesService(query1, s, query2, st, sparqlEndpoint2);
			//String q = "";
			if (q != null) {
Query query = QueryFactory.create(q);
                      //  System.out.println("*q = "+query);
			genQueries.add(query);
                      //  if (!genQueries.contains(query)) {
                            bw.write("query: "+q);
                            bw.write("\n#-------------------------------------------------------\n");
                            bw.flush();
                       // }
}
                    }
                }
            }

        }
        bw.flush();
        System.out.println("genQueries = "+genQueries.size());
        System.out.println("done.");
    }  
 

public void executeQueriesFuseki(String queriesFile, String endpoint, String federatedQueriesFile, String nonPqueries) throws FileNotFoundException, IOException {
            FileReader fr = null;
            BufferedReader br = null;
            FileReader fr2 = null;
            BufferedReader br2 = null;
            String sCurrentLine;
            fw = new FileWriter(federatedQueriesFile, true);
            bw = new BufferedWriter(fw);
            fw2 = new FileWriter(nonPqueries, true);
            bw2 = new BufferedWriter(fw2);
            bw.write("\n#-------------------------------------------------------\n");
            bw2.write("\n#-------------------------------------------------------\n");
            br = new BufferedReader(new FileReader(queriesFile));
            boolean ok = true;
            StringBuffer temp = new StringBuffer() ;
            while ((sCurrentLine = br.readLine()) != null ) {
                if (sCurrentLine.startsWith("#-----") ) {
                        if (temp.length()!=0) {
                        String q = temp.toString().substring(7);
                System.out.println("query: "+q);
                QueryExecution queryExecution = QueryExecutionFactory.sparqlService(endpoint, q);
                ResultSet results = queryExecution.execSelect();
                int qResult = 0;
QuerySolution querysolution = null;
                    if (results.hasNext()) {
                        qResult++;
			querysolution = results.next();
                        System.out.println("solution: "+querysolution);
                    }
                        System.out.println(qResult);
                        if (qResult > 0) {
                            bw.write("query: "+q);
bw.write("\nsolution: "+querysolution.toString());
                            bw.write("\n#-------------------------------------------------------\n");
                            bw.flush();
                        }
                        else {
                            bw2.write("query: "+q);
                            bw2.write("\n#-------------------------------------------------------\n");
                            bw2.flush();
                        }//}
                    temp = new StringBuffer() ;
                    }
                    }
                    else 
                    {
                        temp.append(" "+sCurrentLine);
                    }
            }
        }
    

public void joinPredicatesFromMinimalStar(String file) throws FileNotFoundException, IOException {
        dicJoinPath = new HashMap<String, HashSet<String>>();
        BufferedReader br = null;
        HashSet<String> temprefLabel = new HashSet<String>();
        br = new BufferedReader(new FileReader(file));
        String sCurrentLine;
        StringBuffer temp = new StringBuffer();
        while ((sCurrentLine = br.readLine()) != null) {
            if (sCurrentLine.startsWith("query:")) {
                String q = sCurrentLine.toString();
                String[] T = q.split(" ");
/*                for (int i = 0; i < T.length; i++) {
                    System.out.println("i = " + i + "    T[" + i + "] = " + T[i]);
                }*/
                if (T.length > 16) {
                    if (T[7].contains("prefLabel")) {
                        temprefLabel.add(T[16].substring(1, T[16].length() - 1));
                    }
                }
            }
            temp = new StringBuffer();
        }
        dicJoinPath.put("http://www.w3.org/2004/02/skos/core#prefLabel", temprefLabel);
System.out.println(dicJoinPath);
    }


public String concatenate2QueriesServiceStar(String query1, String predicate1Join, String query2, String predicate2Join, String sparqlEndpoint) {
        String q = "";String temp="";
System.out.println("query1: "+query1+"      /   "+predicate1Join);
System.out.println("query2: "+query2+"      /   "+predicate2Join);
        String[] table = query1.split("[\\s\\xA0]+");
        String[] table2 = query2.split("[\\s\\xA0]+");
        int indiceWhere = this.getIndiceVal(table2, "WHERE");//System.out.println(indiceWhere);
        int indicePredicate2Join = this.getIndiceVal(table2, predicate2Join);
        System.out.println("indicePredicate2Join: "+indicePredicate2Join);
	String valRemplace = null;
        if (indicePredicate2Join > 0) {
            valRemplace = table2[indicePredicate2Join - 1]; 
		System.out.println("valRemplace: "+valRemplace);
	}
        String[] newtable = null;
        for (int i = 0; i < table.length; i++) {
        if ((table[i].contains(predicate1Join)) || (table[i].contains(":" + predicate1Join.split("/")[predicate1Join.split("/").length - 1])) || (table[i].contains(":" + predicate1Join.split("#")[predicate1Join.split("#").length - 1]))) {                //System.out.println(i+" / "+table[i]);
                temp = table[i - 1]; // get Subject of predicate1Join
//                System.out.println(valRemplace+" / "+temp);
                newtable = this.addElementAtIndice(table, "SERVICE <" + sparqlEndpoint + "> {", i + 3);
                int k=i+4;
                for (int j=indiceWhere+2; j<table2.length-1; j++) {
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
                System.out.println("yes : "+q);
                return q;
            }
            else { //var&cte
	//	System.out.println("1");
                return null;
            }
        }
        else {
            if (temp.substring(0, 1).equals("?")) { //cte&var
          //      System.out.println("2");
		return null;
            }
            else { //cte&cte
                if (valRemplace.equals(temp)) {
                    System.out.println("yes2 : "+q);
        		return q;
                }
                else {
	//	    System.out.println("3");
                    return null;
                }
            }
        }
        }
else {//System.out.println("je ss là");
return null;
}
       // return q;
    }


public void genFedMaximalStar(String genFedFile, String sparqlEndpoint1, String sparqlEndpoint2) throws IOException {
        System.out.println("dicJoinPath "+dicJoinPath);
        fw = new FileWriter(genFedFile);
        bw = new BufferedWriter(fw);
        ArrayList<Query> genQueries = new ArrayList<Query>();
        bw.write("#-------------------------------------------------------\n");
        for (String s : dicJoinPath.keySet()) {
            ArrayList<SparqlQueryParser> queries1prunDicJoin = new ArrayList<>();
            queries1prunDicJoin = pruningLog(queries1.getQueries(), s);System.out.println("queries1: "+queries1prunDicJoin.size());
            System.out.println("s = "+s+" / st = "+dicJoinPath.get(s).size());
            for (SparqlQueryParser q1 : queries1prunDicJoin) {//System.out.println("*"+q1);
                String query1 = this.renameVariables(q1.toString(), "log1");
//                System.out.println("s = "+s+" / st = "+dicJoinPath.get(s).size());
                for (String st : dicJoinPath.get(s)) {
                    ArrayList<SparqlQueryParser> queries2prunDicJoin = new ArrayList<>();
                    queries2prunDicJoin = pruningLog(queries2.getQueries(), st);System.out.println("queries2: "+queries2prunDicJoin.size());
                    for (SparqlQueryParser q2 : queries2prunDicJoin) {
                        String query2 = this.renameVariables(q2.toString(), "log2");
//                        Query qquery1 = QueryFactory.create(query1);
  //                      Query qquery2 = QueryFactory.create(query2);
        //              String q = this.concatenate2QueriesService(qquery1.toString(), s, qquery2.toString(), st, sparqlEndpoint2);
			//QueryFactory.create();
			//QueryFactory.create();    
                       String q = this.concatenate2QueriesServiceStar(query1, s, query2, st, sparqlEndpoint2);
	//		String q = this.concatenate2QueriesServiceStar(qquery1.toString(), s, qquery2.toString(), st, sparqlEndpoint2);
                        //String q = "";
                        if (q != null) {
			Query query = QueryFactory.create(q);
                        System.out.println("*q = "+query);
                      //  if (!genQueries.contains(query)) {
                            bw.write("query: "+q);
                            bw.write("\n#-------------------------------------------------------\n");
                            bw.flush();
                       // }
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
