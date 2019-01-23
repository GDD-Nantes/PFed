package PFSQGen;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Map;
import org.apache.jena.graph.Node;
import org.apache.jena.query.ARQ;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.sparql.core.TriplePath;
import org.apache.jena.sparql.syntax.ElementPathBlock;
import org.apache.jena.sparql.syntax.ElementVisitorBase;
import org.apache.jena.sparql.syntax.ElementWalker;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.sparql.syntax.syntaxtransform.QueryTransformOps;

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
    private MatchingPredicates matcher;
    private ArrayList<String> genFedMinimalPath = new ArrayList<String>();
    private ArrayList<String> genFedMinimalStar = new ArrayList<String>();

    private Map<String,Set<Integer>> log1Predicates = new HashMap<String,Set<Integer>>();
    private Map<String,Set<Integer>> log2Predicates = new HashMap<String,Set<Integer>>();
    private Map<String, Set<String>> logPredicatesPrun = new HashMap<String, Set<String>>();
    private BufferedWriter bw = null;
    private FileWriter fw = null;
    
    private ExecutionStrategy executor = new FusekiExecution();
    
    public PruningLogs(String queriesFilePath1, String queriesFilePath2, ArrayList<String> sumFiles) {
        ARQ.init();
        queries1 = this.getSELECTQueries(queriesFilePath1);                                                                                         
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
                for(Node pred : qPredicates){
                    if ((pred != null) && (!pred.toString().substring(0, 1).equals("?"))) {
                        HashSet<Integer> temp = new HashSet<Integer>();
                        temp.add(i);
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
                for(Node pred : qPredicates){
                    if ((pred != null) && (!pred.toString().substring(0, 1).equals("?"))) {
                        HashSet<Integer> temp = new HashSet<Integer>();
                        temp.add(i);
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
//         dicJoinPath = new HashMap<String, HashSet<String>>();
        int cnt = 0;
        int countLarge = 0;
        int countSmall=0;
        int error=0;
        System.out.println(countLarge+" / "+logPredicatesPrun.size());
        for (String nd1 : logPredicatesPrun.keySet()) {
            ++countLarge;
            int totalSmall = logPredicatesPrun.get(nd1).size();
            for (String nd2 : logPredicatesPrun.get(nd1) ) {
                    System.out.println("   "+(countSmall * 100)/totalSmall+"% : "+countSmall+" / "+totalSmall+" , Total saved: "+genFedMinimalPath.size());
                ++countSmall;
                String q = executor.createPath(nd1,nd2,sparqlEndpoint2);
                cnt++;
                try{
                    if (executor.hasResult(q, sparqlEndpoint1)) {
                        genFedMinimalPath.add(q);
                    }
                }catch(QueryException e){
                    ++error;
                }
            }
            
            System.out.println(countLarge+" / "+logPredicatesPrun.size());
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
//         dicJoinStar = new HashMap<String, HashSet<String>>();
        int cnt = 0;
        int countLarge = 0;
        int countSmall=0;
        int error=0;
        System.out.println(countLarge+" / "+logPredicatesPrun.size());
        for (String nd1 : logPredicatesPrun.keySet()) {
            ++countLarge;
            int totalSmall = logPredicatesPrun.get(nd1).size();
            for (String nd2 : logPredicatesPrun.get(nd1)) {
                System.out.println("   "+(countSmall * 100)/totalSmall+"% : "+countSmall+" / "+totalSmall+" , saved: "+genFedMinimalStar.size());
                ++countSmall;
                String q = executor.createStar(nd1,nd2,sparqlEndpoint2);
                cnt++;
                try{
                    if (executor.hasResult(q, sparqlEndpoint1)) {                  
                        genFedMinimalStar.add(q);
                    }
                }catch(QueryException e){
                    ++error;
                }
            }
            System.out.println(countLarge+" / "+logPredicatesPrun.size());
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
    
    public static ArrayList<Node> getPredicates(Query q) {
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
                        // ...and grab the predicate                                                                                                            
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

    public void pruningPredicatesLogs(String so) throws IOException {
        logPredicatesPrun = new HashMap<String, Set<String>>();
        System.out.println("Testing joinable predicates ...");
        int result = 0;
        for (String nlog1 : log1Predicates.keySet()) {  
            if ((nlog1 != null) && (!nlog1.substring(0, 1).equals("?"))) {
                for (String nlog2 : log2Predicates.keySet()) {
                    if ((nlog2 != null) && (!nlog2.substring(0, 1).equals("?"))) {
                        result = matcher.testExistingMatch(nlog1, nlog2);
                        if(result == 1 && so.equals("star")){
                                addPredicatesPrun(nlog1, nlog2);
                        }else if(result == 2 && so.equals("path")){
                                addPredicatesPrun(nlog1, nlog2);
                        }else if(result == 3){
                                addPredicatesPrun(nlog1, nlog2);
                        }
                    }
                }
            }
        }
    }
    
    public void pruningPredicatesLogsType(ArrayList<String> sumFiles, String so) throws IOException {
        MatchingPredicates matcherType = new MatchingPredicates(sumFiles, true);
        logPredicatesPrun = new HashMap<String, Set<String>>();
        System.out.println("Testing joinable predicates ...");
        int result = 0;
        int resultType = 0;
        for (String nlog1 : log1Predicates.keySet()) {  
            if ((nlog1 != null) && (!nlog1.substring(0, 1).equals("?"))) {
                for (String nlog2 : log2Predicates.keySet()) {
                    if ((nlog2 != null) && (!nlog2.substring(0, 1).equals("?"))) {
                        result = matcher.testExistingMatch(nlog1, nlog2);
                        if(result != -1){
                            resultType = matcherType.testExistingMatch(nlog1, nlog2);
                            if(result == 3 && resultType == 3){
                                addPredicatesPrun(nlog1, nlog2);
                            }else if( resultType == result){
                                if(result == 1 && so.equals("star")){
                                        addPredicatesPrun(nlog1, nlog2);
                                }else if(result == 2 && so.equals("path")){
                                        addPredicatesPrun(nlog1, nlog2);
                                }
                            }
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
        Query q = QueryFactory.create(query);
        Map<Var,Node> allVar = new HashMap<Var,Node>();                                                                                                    
        ElementWalker.walk(q.getQueryPattern(),
                // For each element...                                                                                                                    
            new ElementVisitorBase() {
            // ...when it's a block of triples...                                                                                                         
                public void visit(ElementPathBlock el) {
                    // ...go through all the triples...                                                                                                       
                    Iterator<TriplePath> triples = el.patternElts();
                    while (triples.hasNext()) {
                        // ...and grab the triple               
                        TriplePath curr = triples.next();
                        int nextVal = allVar.size()+1;
                        Node curS = curr.getSubject();
                        if(!curS.isConcrete()){
                            allVar.putIfAbsent((Var)curS, NodeFactory.createVariable("val"+nextVal + suffix));
                        }
                        nextVal = allVar.size()+1;
                        curS = curr.getObject();
                        if(!curS.isConcrete()){
                            allVar.putIfAbsent((Var)curS, NodeFactory.createVariable("val"+nextVal + suffix));
                        }
                        nextVal = allVar.size()+1;
                        curS = curr.getPredicate();
                        if(curS!=null && !curS.isConcrete()){
                            allVar.putIfAbsent((Var)curS, NodeFactory.createVariable("val"+nextVal + suffix));
                        }
                    }
                }
            }
        );
        Query newQ = QueryTransformOps.transform(q, allVar);
        newQ.setQueryResultStar(true);
        return newQ.toString();
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
    
    public void genFedMaximal(String sparqlEndpoint1, String sparqlEndpoint2, String genFedFile, String so) throws IOException {
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
                    Set<Integer> queries2prunDicJoin = log2Predicates.get(st);
                    for (Integer idQ2 : queries2prunDicJoin) {
                        ++countPred2;
                        SparqlQueryParser q2 = queries2.getQueryAt(idQ2);
                        String query2 = this.renameVariables(q2.toString(), "log2");
                        String q = null;
                        if( so.equals("path")){
                            q = this.concatenate2QueriesService(query1, s, query2, st, sparqlEndpoint2);
                        }else if(so.equals("star")){
                            q = this.concatenate2QueriesServiceStar(query1, s, query2, st, sparqlEndpoint2);
                        }else{
                            System.out.println("Unknow join type : " + so + " . Use path or star");
                            return;
                        }
                        if (q != null) {
                            try{
                                Query query = QueryFactory.create(q);
                                genQueries.add(query);
                                bw.write("query: "+query.toString());
                                bw.write("\n#-------------------------------------------------------\n");
                                bw.flush();
                            }catch(Exception e){
                                System.out.println("Malformed federated query :" + e+"\n Query:" + q);
                            }
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
}

