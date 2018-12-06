package PFed;

import PFed.ExecutionStrategy;

import org.apache.jena.graph.Node;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.QueryException;

public class FusekiExecution implements ExecutionStrategy {
    public boolean hasResult(String q, String endPoint) throws QueryException{
        Query qGen = QueryFactory.create(q);
        qGen.setLimit(1);
        try( QueryExecution queryExecution = QueryExecutionFactory.sparqlService(endPoint, qGen) ){
            ResultSet resultSet = queryExecution.execSelect();
            if (resultSet.hasNext()) {
                return true;
            }
        }catch(QueryException e){
            throw e;
        }
        return false;
    }
    
    public String createPath(Node n1, Node n2, String servN2){
        return "SELECT * WHERE {\n"
            + "\t?v0 <" + n1 + "> ?v1 . \n"
            + "\tSERVICE <" + servN2 + "> { \n"
            + "\t\t?v1 <" + n2 + "> ?v2 . \n"
            + "\t}\n"
            + "\tFILTER(isURI(?v1))\n"
            + "}";
    }
    public String createStar(Node n1, Node n2, String servN2){
        return "SELECT * WHERE {\n"
            + "\t?v0 <" + n1 + "> ?v1 . \n"
            + "\tSERVICE <" + servN2 + "> { \n"
            + "\t\t?v0 <" + n2 + "> ?v2 . \n"
            + "\t}\n"
//             + "\tFILTER(isURI(?v1))\n"
            + "}";
    }
}
