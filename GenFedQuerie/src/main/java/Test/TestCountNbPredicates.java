package Test;

import PFSQGen.Queries;
import PFSQGen.SparqlQueryParser;
import org.apache.jena.graph.Node;
import java.util.HashSet;
import java.util.ArrayList;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.sparql.core.TriplePath;
import org.apache.jena.sparql.syntax.ElementPathBlock;
import org.apache.jena.sparql.syntax.ElementVisitorBase;
import org.apache.jena.sparql.syntax.ElementWalker;
import java.util.Iterator;

public class TestCountNbPredicates{
    public static void main(String[] args) throws Exception {
        
        Queries queries1 = new Queries("logs/SWDF-CleanQueriesDecode.txt", "nonCrypte");
        Queries queries2 = new Queries("logs/DBpedia3.5.1-CleanQueriesDecode.txt", "nonCrypte");
        HashSet<String> log1Predicates = new HashSet<String>();
        HashSet<String> log2Predicates = new HashSet<String>();
        System.out.println("reading log1 ...");
        for (SparqlQueryParser q1 : queries1.getQueries()) {
            Query query1 = QueryFactory.create(q1.getQueryString());
            if (query1 != null) {
                ArrayList<Node> qPredicates = getPredicates(query1);
                for (Node nlog1 : qPredicates) {  
                    if ((nlog1 != null) && (!nlog1.toString().substring(0, 1).equals("?"))) {
                        log1Predicates.add(nlog1.toString());
                    }
                }
            }
        }
        System.out.println("reading log2 ...");
        for (SparqlQueryParser q2 : queries2.getQueries()) {
            Query query2 = QueryFactory.create(q2.getQueryString());
            if (query2 != null) {
                ArrayList<Node> qPredicates = getPredicates(query2);
                for (Node nlog1 : qPredicates) {  
                    if ((nlog1 != null) && (!nlog1.toString().substring(0, 1).equals("?"))) {
                        log2Predicates.add(nlog1.toString());
                    }
                }
            }
        }
        System.out.println("File 1 got " + log1Predicates.size() + " predicates");
         System.out.println("File 1 :" + log1Predicates + "\nEnd of File1");
        System.out.println("File 2 got " + log2Predicates.size() + " predicates");
         System.out.println("File 2 :" + log2Predicates + "\nEnd of File2");
    }
    //From PFSQGen.PrunningLogsF
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
                        // ...and grab the subject                                                                                                            
                        predicates.add(triples.next().getPredicate());
                    }
                }
            }
        );

        return predicates;
    }
}
