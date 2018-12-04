package Test;

import PFed.PruningLogsF;
import java.util.Set;
import java.util.ArrayList;
import org.apache.jena.graph.Node;

public class TestJoinablePred {

    public static void main(String[] args) throws Exception {
    
        String queriesFilePath1 = "queries/SWDF-CleanQueriesDecode.txt";
        String queriesFilePath2 = "queries/DBpedia3.5.1-CleanQueriesDecode.txt";
        String Sum1 = "summaries/SWDF-Sum.n3";                                                              
        String Sum2 = "summaries/DBPD-Sum.n3";
        
        String log1PrunFile = "log1PrunFile.txt";
        String log2PrunFile = "log2PrunFile.txt";
        
        Set<Node> res1;
        Set<Node> res2;
        
        if(args.length > 0 && args[0].equals("pathRev")){
                ArrayList<String> sumFileListRev = new ArrayList<String>();
                sumFileListRev.add(Sum2);
                sumFileListRev.add(Sum1);
                PruningLogsF PLpathRev = new PruningLogsF(queriesFilePath2, queriesFilePath1, sumFileListRev, "nonCypte");
                PLpathRev.pruningPredicatesLogs(sumFileListRev, "path", log1PrunFile, log2PrunFile);
                res1 = PLpathRev.getLog1PredicatesPrun();
                res2 = PLpathRev.getLog2PredicatesPrun();
        }else if(args.length>0 && args[0].equals("star")){
            ArrayList<String> sumFileList = new ArrayList<String>();
            sumFileList.add(Sum1);
            sumFileList.add(Sum2);
            PruningLogsF PLpath = new PruningLogsF(queriesFilePath1, queriesFilePath2, sumFileList, "nonCypte");
            PLpath.pruningPredicatesLogs(sumFileList, "star", log1PrunFile, log2PrunFile);
            res1 = PLpath.getLog1PredicatesPrun();
            res2 = PLpath.getLog2PredicatesPrun();
        }else{
            ArrayList<String> sumFileList = new ArrayList<String>();
            sumFileList.add(Sum1);
            sumFileList.add(Sum2);
            PruningLogsF PLpath = new PruningLogsF(queriesFilePath1, queriesFilePath2, sumFileList, "nonCypte");
            PLpath.pruningPredicatesLogs(sumFileList, "path", log1PrunFile, log2PrunFile);
            res1 = PLpath.getLog1PredicatesPrun();
            res2 = PLpath.getLog2PredicatesPrun();
        }
        
        System.out.println("File 1 got " + res1.size() + " predicates");
        System.out.println("File 1 :" + res1 + "\nEnd of File1");
        System.out.println("File 2 got " + res2.size() + " predicates");
        System.out.println("File 2 :" + res2 + "\nEnd of File2");
    }
}
