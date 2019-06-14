package PFSQGen;

import org.apache.jena.query.QueryException;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.RDFNode;
import java.util.List;
import org.apache.jena.query.Query;

public interface ExecutionStrategy{
    public ResultSet execQuery(String endpointURL,Query q);
    public boolean hasResult(String q, String endPoint) throws QueryException;
    public List<RDFNode> execute1Field(String q, String endPoint, String field) throws QueryException;
    public String createPath(String n1, String n2, String servN2);
    public String createStar(String n1, String n2, String servN2);
}
