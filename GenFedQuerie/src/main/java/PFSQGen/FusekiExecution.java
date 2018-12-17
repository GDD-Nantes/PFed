package PFSQGen;

import PFSQGen.ExecutionStrategy;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.QueryException;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.query.QuerySolution;

import java.util.List;
import java.util.ArrayList;

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
    
    public List<RDFNode> execute1Field(String q, String endPoint, String field) throws QueryException {
        List<RDFNode> resList = new ArrayList<RDFNode>();
        Query qGen = QueryFactory.create(q);
//         ResultSet resultSet = null;
        try( QueryExecution queryExecution = QueryExecutionFactory.sparqlService(endPoint, qGen) ){
//             resultSet = queryExecution.execSelect();
            ResultSet resultSet = queryExecution.execSelect();
            for ( ; resultSet.hasNext() ; ){
                QuerySolution soln = resultSet.nextSolution() ;
                RDFNode x = soln.get("type") ;
                if(x != null)
                    resList.add(x);
            }
        }catch(QueryException e){
            throw e;
        }
        return resList;
    }
    
    public String createPath(String n1, String n2, String servN2){
        return "SELECT * WHERE {\n"
            + "\t?v0 <" + n1 + "> ?v1 . \n"
            + "\tSERVICE <" + servN2 + "> { \n"
            + "\t\t?v1 <" + n2 + "> ?v2 . \n"
            + "\t}\n"
            + "\tFILTER(isURI(?v1))\n"
            + "}";
    }
    public String createStar(String n1, String n2, String servN2){
        return "SELECT * WHERE {\n"
            + "\t?v0 <" + n1 + "> ?v1 . \n"
            + "\tSERVICE <" + servN2 + "> { \n"
            + "\t\t?v0 <" + n2 + "> ?v2 . \n"
            + "\t}\n"
            + "\tFILTER(isURI(?v0))\n"
            + "}";
    }
}
