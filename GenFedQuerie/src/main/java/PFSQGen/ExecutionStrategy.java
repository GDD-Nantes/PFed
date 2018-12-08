package PFSQGen;

import org.apache.jena.query.QueryException;
import org.apache.jena.graph.Node;

public interface ExecutionStrategy{
    public boolean hasResult(String q, String endPoint) throws QueryException;
    public String createPath(Node n1, Node n2, String servN2);
    public String createStar(Node n1, Node n2, String servN2);
}
