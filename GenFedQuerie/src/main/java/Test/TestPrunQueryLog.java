package Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import PFSQGen.SparqlQueryParser;
import PFed.PruningLogsF;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Node;

public class TestPrunQueryLog{
    public static void main(String[] args) throws Exception {
        String queriesFilePath1 = "queries/SWDF-CleanQueriesDecode.txt";
        String queriesFilePath2 = "queries/DBpedia3.5.1-CleanQueriesDecode.txt";
//         String querieSplodge = "predList/StarSplodgeSageLoc.txt";

        ArrayList<String> sumFileList = new ArrayList<String>();
        String Sum1 = "summaries/SWDF-Sum.n3";                                                              
        String Sum2 = "summaries/DBPD-Sum.n3";
        sumFileList.add(Sum1);
        sumFileList.add(Sum2);
        
        String predicate1FilePath = "predSWDF.txt";
        String predicate2FilePath = "predDBPD.txt";
        PruningLogsF PLpath = new PruningLogsF(queriesFilePath1, queriesFilePath2, sumFileList, "nonCypte");
//         PruningLogsF PLpath = new PruningLogsF(querieSplodge, querieSplodge, sumFileList, "nonCypte");
        
        //Load preds
        addAllToLog1(predicate1FilePath, PLpath);
        addAllToLog2(predicate2FilePath, PLpath);
        ArrayList<SparqlQueryParser> resSWDF = PLpath.pruningLog1Predicates("");
        ArrayList<SparqlQueryParser> resDBPD = PLpath.pruningLog2Predicates("");
        
        //write results
        writeAll(resDBPD, "results/QueriesPrunDBPD.txt");
        writeAll(resSWDF, "results/QueriesPrunSWDF.txt");
    }
    
    public static void addAllToLog1(String inputFile, PruningLogsF pl){
        try(BufferedReader br = new BufferedReader(new FileReader(inputFile))){
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                pl.addPredPrunLog1(NodeFactory.createURI(sCurrentLine));
            }
         }catch(IOException e){
            System.out.println(e);
         }
    }
    
    public static void addAllToLog2(String inputFile, PruningLogsF pl){
        try(BufferedReader br = new BufferedReader(new FileReader(inputFile))){
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                Node tmp = NodeFactory.createURI(sCurrentLine);
                pl.addPredPrunLog2(tmp);
            }
         }catch(IOException e){
            System.out.println(e);
         }
    }
    public static void writeAll(ArrayList<SparqlQueryParser> res, String resPath){
        try(BufferedWriter br = new BufferedWriter(new FileWriter(resPath))){
            for(SparqlQueryParser sp : res){
                br.write("#-------------------------------------\n");
                br.write(sp+"\n");
            }
         }catch(IOException e){
            System.out.println(e);
         }
    }
}
