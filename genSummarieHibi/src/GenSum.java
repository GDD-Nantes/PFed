package genSummarieHibi;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.repository.RepositoryException;

/**
 * Test FedSummaries Generator for a set of SPARQL endpoints
 * @author Saleem
 */
public class GenSum {
	
	public static void main(String[] args) throws IOException, RepositoryException, MalformedQueryException, QueryEvaluationException {
	List<String> endpoints = 	(Arrays.asList(
			 args[0]));
   int start = Integer.parseInt(args[1]);
	String outputFile = "../Summaries/_Summarie.n3";
	FedSumGenerator generator = new FedSumGenerator(outputFile);
	long startTime = System.currentTimeMillis();
	generator.generateSummaries(endpoints,null, start);
	System.out.println("Data Summaries Generation Time (sec): "+ (System.currentTimeMillis()-startTime)/1000);
	System.out.print("Data Summaries are secessfully stored at "+ outputFile);
	}

}
 
