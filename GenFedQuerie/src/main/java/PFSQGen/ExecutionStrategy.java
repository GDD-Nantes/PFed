package PFSQGen;

import org.apache.jena.query.QueryException;

public interface ExecutionStrategy{
    public boolean hasResult(String q, String endPoint) throws QueryException;
    public String createPath(String n1, String n2, String servN2);
    public String createStar(String n1, String n2, String servN2);
}
