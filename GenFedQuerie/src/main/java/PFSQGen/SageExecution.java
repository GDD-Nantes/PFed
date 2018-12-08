package PFSQGen;

import PFSQGen.ExecutionStrategy;

import org.gdd.sage.engine.SageExecutionContext;
import org.gdd.sage.core.factory.ISageQueryFactory;
import org.gdd.sage.core.factory.SageQueryFactory;

import org.apache.jena.graph.Node;
import org.apache.jena.query.ARQ;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryException;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QueryExecution;

public class SageExecution implements ExecutionStrategy {
    @Override
    public boolean hasResult(String q, String endPoint) throws QueryException{
        Query qGen = QueryFactory.create(q);
        qGen.setLimit(1);
        
        ISageQueryFactory factory = new SageQueryFactory(endPoint, qGen);
        factory.buildDataset();
        qGen = factory.getQuery();
        Dataset federation = factory.getDataset();
        SageExecutionContext.configureDefault(ARQ.getContext());
        try(QueryExecution queryExecution = QueryExecutionFactory.create(qGen, federation)){
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
            + "}";
    }
    public String createStar(Node n1, Node n2, String servN2){
        return "SELECT * WHERE {\n"
            + "\t?v0 <" + n1 + "> ?v1 . \n"
            + "\tSERVICE <" + servN2 + "> { \n"
            + "\t\t?v0 <" + n2 + "> ?v2 . \n"
            + "\t}\n"
            + "}";
    }
}
