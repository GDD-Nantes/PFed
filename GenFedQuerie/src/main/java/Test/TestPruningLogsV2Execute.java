/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import PFSQGen.PruningLogs;
import com.fluidops.fedx.Config;
import com.fluidops.fedx.FederationManager;
import java.util.ArrayList;
import PFSQGen.PruningLogsV2;

/**
 *
 * @author khiyari-s
 */
public class TestPruningLogsV2Execute {

    public static void main(String[] args) throws Exception {
        String queriesFilePath1 = "../../../SWDF-CleanQueries.txt";
        String queriesFilePath2 = "../../../DBpedia3.5.1-CleanQueries.txt";
        //String queriesFilePath1 = "/Users/khiyari-s/NetBeansProjects/plausible-fed-queries/CleanQueries/SWDF-CleanQueries.txt";
        //String queriesFilePath2 = "/Users/khiyari-s/NetBeansProjects/plausible-fed-queries/CleanQueries/DBpedia3.5.1-CleanQueries.txt";   

        ArrayList<String> sumFileList = new ArrayList<String>();
        String Sum1 = "../../../summaries/SWDF-Sum.n3";
	//String Sum2 = "../../../summaries/DBPD-Sum.n3";
	String Sum2 = "../../../summaries/DBPD-3.5.n3";
	sumFileList.add(Sum1);
        sumFileList.add(Sum2);
	String sparqlEndpoint1 = "http://sage.univ-nantes.fr/sparql/swdf-2012";
        String sparqlEndpoint2 = "http://sage.univ-nantes.fr/sparql/dbpedia-3-5-1";
//	String sparqlEndpoint1 = "http://172.16.8.50:6010/swdf/query";
//	String sparqlEndpoint2 = "http://172.16.8.50:6011/dbpedia/query";

        
        String genFedMinimalPathFile = "../../../genFedMinimalPathFile.txt";
//	String genFedMinimalStarFile = "../../../genFedMinimalStarFile.txt";
	String genFedMinimalHybridFile = "../../../genFedMinimalHybridFile.txt";
        String genFedLightFile = "../../../genFedLightFile.txt";
        String genFedMaximalFile = "../../../genFedMaximalFile.txt";
	String federatedQueriesFile = "../../../fedMinimalPathQueriesFile.txt";
	String nonFQueriesFile = "../../../nonFedMinimalPathQueriesFile.txt";
	String fedMediumPathQueriesFile = "../../../fedMediumPathQueriesFile.txt";
	String nonFedMediumPathQueriesFile = "../../../nonFedMediumPathQueriesFile.txt";


	String genFedMediumPathFile = "../../../genFedMediumPathFile.txt";

	String log1PredPrunFile = "../../../log1PredPrunFile.txt";
	String log2PredPrunFile = "../../../log2PredPrunFile.txt";
	String log1PrunFile = "../../../log1PrunFile.txt";
	String log2PrunFile = "../../../log2PrunFile.txt";

	String log1ClassSummary = "../../../log1ClassSummary.txt";
	String log2ClassSummary = "../../../log2ClassSummary.txt";

	String genFedMinimalStarFile = "../../../genFedMinimalStarFile.txt";
	String fedMinimalStarQueriesFile = "../../../fedMinimalStarQueriesFile.txt";
	String nonFedMinimalStarQueriesFile = "../../../nonFedMinimalStarQueriesFile.txt";

	String genFedMaximalPathFile = "../../../genFedMaximalPathFileFuseki.txt";
	String fedMaximalPathQueriesFile = "../../../fedMaximalPathQueriesFile.txt";
	String nonFedMaximalPathQueriesFile = "../../../nonFedMaximalPathQueriesFile.txt";


	String genFedMaximalPathFilebasednear = "genFedMaximalPathBasednear.txt";
	String fedMaximalPathQueriesFilebasednear = "fedMaximalPathbasednear.txt";
	String nonFedMaximalPathQueriesFilebasednear = "nonFedMaximalPathbasednear.txt";

	String genFedMaximalStarFilePreflabel = "genFedMaximalStarFilePreflabel.txt";
	String fedMaximalStarQueriesFilePreflabel = "fedMaximalStarQueriesFilePreflabel.txt";
	String nonFedMaximalStarQueriesFilePreflabel = "nonFedMaximalStarQueriesFilePreflabel.txt";

	//        PruningLogs PLpath = new PruningLogs(queriesFilePath1, queriesFilePath2, sumFileList, "nonCrypte");
        PruningLogsV2 PLpath = new PruningLogsV2(queriesFilePath1, queriesFilePath2, sumFileList, log1ClassSummary, log2ClassSummary);

	PLpath.executeQueriesSage("../../../genFedMinimalPathFile10Dec.txt", sparqlEndpoint1, "minimal10Dec.txt", "nonminimal10Dec.txt");

//String genFedMinimalPathFile = "../../../testFedQuery.txt";
//////	PLpath.executeQueriesSage(genFedMinimalPathFile, sparqlEndpoint1, federatedQueriesFile, nonFQueriesFile);
//////  PLpath.executeQueriesSage(genFedMediumPathFile, sparqlEndpoint1, fedMediumPathQueriesFile, nonFedMediumPathQueriesFile);
//////	PLpath.executeQueriesFuseki(genFedMaximalPathFile, sparqlEndpoint1, fedMaximalPathQueriesFile, nonFedMaximalPathQueriesFile);
///////	PLpath.executeQueriesFuseki(genFedMaximalPathFilebasednear, sparqlEndpoint1, fedMaximalPathQueriesFilebasednear, nonFedMaximalPathQueriesFilebasednear);
/////	PLpath.executeQueriesFuseki(genFedMaximalStarFilePreflabel, sparqlEndpoint1, fedMaximalStarQueriesFilePreflabel, nonFedMaximalStarQueriesFilePreflabel);

//////	PLpath.executeQueriesSage(genFedMinimalStarFile, sparqlEndpoint1, fedMinimalStarQueriesFile, nonFedMinimalStarQueriesFile);
	//	String q ="SELECT DISTINCT ?type WHERE { ?s <http://dbpedia.org/ontology/knownFor> ?o . FILTER (isURI(?s) )  ?s <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?type  } ";
	//	PLpath.executeQueryService(q, sparqlEndpoint2);              
          
	//	PLpath.typePredicatesJena(sumFileList, sparqlEndpoint1, sparqlEndpoint2, log1ClassSummary, log2ClassSummary);
	//////       PLpath.typePredicatesSage(sumFileList, sparqlEndpoint1, sparqlEndpoint2, log1ClassSummary, log2ClassSummary);	
	//PLpath.typePredicatesSageAtIndice(sumFileList, sparqlEndpoint1, sparqlEndpoint2, log1ClassSummary, log2ClassSummary, "http://dbpedia.org/property/reference");
	//System.out.println("end typing ...");
//	PLpath.pruningPredicatesLogs(sumFileList, "path", log1PredPrunFile, log2PredPrunFile);
        //PLpath.pruningLog1Predicates(log1PrunFile);
        //PLpath.pruningLog2Predicates(log2PrunFile);
	////// PLpath.pruningLogsPredicatClasses("path");
	////// PLpath.pruningLog1Predicates(log1PrunFile);                                                                                                     
        ////// PLpath.pruningLog2Predicates(log2PrunFile); 
	//	PLpath.pruningLog1();                                                                                                       
	//PLpath.pruningLog2();

	//		PLpath.computeListPredicatesLogs(sumFileList, "path");
	//		PLpath.pruningLog1("path");
		//PLpath.pruningLog2("path");        
//get predicates Pi with object has Authority "http://dbpedia.org"
      //  System.out.println(PLpath.computeListPredicatesLog1(sumFileList)+" *** "+PLpath.computeListPredicatesLog1(sumFileList).size());
	////        System.out.println(PLpath.computeListPredicatesLog1(sumFileList, "path", "http://dbpedia.org")+" *** "+PLpath.computeListPredicatesLog1(sumFileList, "path", "http://dbpedia.org").size());
        //pruning log1(SWDF), keeping only queries that contains a predicate from	/////     System.out.println(PLpath.computeListPredicatesLog2(sumFileList, "http://dbpedia.org")+" *** "+PLpath.computeListPredicatesLog2(sumFileList, "http://dbpedia.org").size());
        //pruning log2(DBPedia), keeping only queries that contains a predicate from Pj
	////   PLpath.pruningLog2();
        //generate minimal fedQueries with result
	System.out.println();//"http://dbpedia.org"
        System.out.println("----------------------------------------------");
        System.out.println();
        System.out.println("Generated federated queries Minimal Path:");
        System.out.println();
        System.out.println("----------------------------------------------");

	//	Config.initialize();        System.out.println();
	//	   PLpath.genFedMinimalPathServiceSage(sparqlEndpoint1, sparqlEndpoint2, genFedMinimalPathFile);
	//	PLpath.genFedMinimalPathService(sparqlEndpoint1, sparqlEndpoint2, genFedMinimalPathFile);        
System.out.println();
        System.out.println("----------------------------------------------");
        System.out.println();
        System.out.println("Generated federated queries Minimal Star:");
        System.out.println();
        System.out.println("----------------------------------------------");
	//        System.out.println(PLpath.computeListPredicatesLog1(sumFileList, "star", "http://dbpedia.org")+" *** "+PLpath.computeListPredicatesLog1(sumFileList, "star", "http://dbpedia.org").size());
	//	PLpath.computeListPredicatesLogs(sumFileList, "star");
	//PLpath.pruningLog1("star");
        //PLpath.pruningLog2("star");
	//	PLpath.pruningPredicatesLogs(sumFileList, "star");
        //PLpath.pruningLog1Predicates();
        //PLpath.pruningLog2Predicates();
        //PLpath.genFedMinimalStarServiceSage(sparqlEndpoint1, sparqlEndpoint2, genFedMinimalStarFile);
        System.out.println();
        System.out.println("----------------------------------------------");
        System.out.println();
        System.out.println("Generated federated queries Minimal Hybrid:");
        System.out.println();
   //     Config.initialize();
        System.out.println("----------------------------------------------");
	//	PLpath.genFedMinimalHybridServiceSage(sparqlEndpoint1, sparqlEndpoint2, genFedMinimalHybridFile);
        System.out.println();
        System.out.println("----------------------------------------------");
        System.out.println();
        System.out.println("Generated federated queries light :");
        System.out.println();
	//        PLpath.genFedLightServiceSage(sparqlEndpoint1, sparqlEndpoint2, genFedLightFile);
        System.out.println("----------------------------------------------");
//	PLpath.genFedLightService(sparqlEndpoint1, sparqlEndpoint2, genFedLightFile);        
/*System.out.println();
        System.out.println("Generated federated queries maximal :");
        System.out.println();
        System.out.println("----------------------------------------------Fin");
        FederationManager.getInstance().shutDown();
        System.exit(0);*/
        
        
       /* PruningLogs PLstar = new PruningLogs(queriesFilePath1, queriesFilePath2, sumFileList);
        //get predicates Pi with subject has Authority "http://dbpedia.org"
        System.out.println(PLstar.computeListPredicatesLog1(sumFileList, "star")+" *** "+PLstar.computeListPredicatesLog1(sumFileList, "star").size());
        //pruning log1(SWDF), keeping only queries that contains a predicate from Pi
        PLstar.pruningLog1();
        //get predicates Pj with subject Authority "http://dbpedia.org"
        System.out.println(PLstar.computeListPredicatesLog2(sumFileList)+" *** "+PLstar.computeListPredicatesLog2(sumFileList).size());
        //pruning log2(DBPedia), keeping only queries that contains a predicate from Pj
        PLstar.pruningLog2();
        //generate minimal fedQueries with result
        System.out.println();
        System.out.println("----------------------------------------------");
        System.out.println();
        System.out.println("Generated federated queries Minimal :");
        System.out.println();
        Config.initialize();
        System.out.println("----------------------------------------------");
        System.out.println();
        //PLstar.genFedMinimalStar(sparqlEndpoint1, sparqlEndpoint2, genFedMinimalFile);
        PLstar.genFedMinimalStarService(sparqlEndpoint1, sparqlEndpoint2, genFedMinimalFile);
        System.out.println();
        System.out.println("----------------------------------------------");*/
    }
}
