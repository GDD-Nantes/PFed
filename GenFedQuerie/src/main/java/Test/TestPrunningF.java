package Test;

import PFSQGen.PruningLogsF;
import PFSQGen.SageExecution;
import com.fluidops.fedx.Config;
import com.fluidops.fedx.FederationManager;
import java.util.ArrayList;

/**                                                                                                                                                       
 *                                                                                                                                                        
 * @author khiyari-s                                                                                                                                      
 */
public class TestPrunningF {

    public static void main(String[] args) throws Exception {
        String queriesFilePath1 = "logs/SWDF-CleanQueriesDecode.txt";
        String queriesFilePath2 = "logs/DBpedia3.5.1-CleanQueriesDecode.txt";

        ArrayList<String> sumFileList = new ArrayList<String>();
        String Sum1 = "summaries/SWDF-Sum.n3";                                                              
        String Sum2 = "summaries/DBPD-Sum.n3";
        sumFileList.add(Sum1);
        sumFileList.add(Sum2);
        
//         String sparqlEndpoint1 = "http://192.168.1.100:3030/swdf/sparql";
//         String sparqlEndpoint2 = "http://192.168.1.100:3030/dbpedia/sparql";
        String sparqlEndpoint1 = "http://192.168.1.100:8000/sparql/swdf";
        String sparqlEndpoint2 = "http://192.168.1.100:8000/sparql/dbpedia";
        
        String genFedMinimalPathFileDB = "results/genFedMinimalPathFileDB.txt";
        String genFedMinimalPathFileSW = "results/genFedMinimalPathFileSW.txt";
        String genFedMinimalStarFile = "results/genFedMinimalStarFile.txt";
//         String genFedMinimalHybridFile = "genFedMinimalHybridFile.txt";
//         String genFedLightFile = "genFedLightFile.txt";

//         String genFedMaximalFile = "genFedMaximalFile.txt";

//         String log1PredPrunFile = "log1PredPrunFile.txt";
//         String log2PredPrunFile = "log2PredPrunFile.txt";
        String log1PrunFile = "log1PrunFile.txt";
        String log2PrunFile = "log2PrunFile.txt";
        String log1PrunFileRev = "log1PrunFileRev.txt";
        String log2PrunFileRev = "log2PrunFileRev.txt";

//         String log1ClassSummary = "log1ClassSummary.txt";
//         String log2ClassSummary = "log2ClassSummary.txt";
                                 
        PruningLogsF PLpath = new PruningLogsF(queriesFilePath1, queriesFilePath2, sumFileList, "nonCypte");
        PLpath.setExecutor(new SageExecution());
        
        //generate minimal fedQueries with result                                                                                                         
        System.out.println();                                                                                                     
        System.out.println("----------------------------------------------");
        System.out.println();
        System.out.println("Generated federated queries Minimal Path DBPedia:");
        System.out.println();
        System.out.println("----------------------------------------------");
        
        PLpath.pruningPredicatesLogs(sumFileList, "path", log1PrunFile, log2PrunFile);                                                            
//         PLpath.pruningLog1Predicates(log1PrunFile);                                                                                                     
//         PLpath.pruningLog2Predicates(log2PrunFile);
        
        PLpath.genFedMinimalPathService(sparqlEndpoint1, sparqlEndpoint2, genFedMinimalPathFileDB);
        System.out.println();
        System.out.println("----------------------------------------------");
        System.out.println();
        System.out.println("Generated federated queries Minimal Star:");
        System.out.println();
        System.out.println("----------------------------------------------");
        
//         PLpath.pruningPredicatesLogs(sumFileList, "star", log1PrunFile, log2PrunFile);                                                            
/*        PLpath.pruningLog1Predicates(log1PrunFile);                                                                                                     
        PLpath.pruningLog2Predicates(log2PrunFile);  */  
        
//         PLpath.genFedMinimalStarService(sparqlEndpoint1, sparqlEndpoint2, genFedMinimalStarFile);
        
        System.out.println();                                                                                                     
        System.out.println("----------------------------------------------");
        System.out.println();
        System.out.println("Generated federated queries Minimal Path SWDF:");
        System.out.println();
        System.out.println("----------------------------------------------");
//         ArrayList<String> sumFileListRev = new ArrayList<String>();
//         sumFileListRev.add(Sum2);
//         sumFileListRev.add(Sum1);
//         PruningLogsF PLpathRev = new PruningLogsF(queriesFilePath2, queriesFilePath1, sumFileListRev, "nonCypte");
//         PLpathRev.setExecutor(new SageExecution());
// //         
//         PLpathRev.pruningPredicatesLogs(sumFileListRev, "path", log1PrunFileRev, log2PrunFileRev);
// //         PLpath.pruningLog1Predicates(log1PrunFileRev);                                                                                                     
// //         PLpath.pruningLog2Predicates(log2PrunFileRev);
// //         
//         PLpathRev.genFedMinimalPathService(sparqlEndpoint2, sparqlEndpoint1, genFedMinimalPathFileSW);
    }
}
