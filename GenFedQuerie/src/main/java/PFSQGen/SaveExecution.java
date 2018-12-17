package PFSQGen;

import PFSQGen.ExecutionStrategy;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QueryException;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.RDFNode;
import java.util.List;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class SaveExecution implements ExecutionStrategy {
    private String filePath;
    
    public SaveExecution(String fp){
        filePath = fp;
    }
    
    public void setFilePath(String fp){
        filePath = fp;
    }
    
    public boolean hasResult(String q, String endPoint) throws QueryException{
        Query qGen = QueryFactory.create(q);
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            fw = new FileWriter(filePath,true);
            bw = new BufferedWriter(fw);
            bw.write("#-------------------------------------------------------\n");
            bw.write(qGen.toString()+"\n");
        } catch (IOException ex) {
            ex.printStackTrace();
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
        return false;        
    }
    
    public List<RDFNode> execute1Field(String q, String endPoint, String field) throws QueryException{
        Query qGen = QueryFactory.create(q);
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            fw = new FileWriter(filePath,true);
            bw = new BufferedWriter(fw);
            bw.write("#-------------------------------------------------------\n");
            bw.write(qGen.toString()+"\n");
        } catch (IOException ex) {
            ex.printStackTrace();
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
        return null;  
    }
    
    public String createPath(String n1, String n2, String servN2){
        return "SELECT * WHERE {\n"
            + "\t?v0 <" + n1 + "> ?v1 . \n"
            + "\tSERVICE <" + servN2 + "> { \n"
            + "\t\t?v1 <" + n2 + "> ?v2 . \n"
            + "\t}\n"
            + "}";
    }
    public String createStar(String n1, String n2, String servN2){
        return "SELECT * WHERE {\n"
            + "\t?v0 <" + n1 + "> ?v1 . \n"
            + "\tSERVICE <" + servN2 + "> { \n"
            + "\t\t?v0 <" + n2 + "> ?v2 . \n"
            + "\t}\n"
            + "}";
    }
}
