package Test;

import PFSQGen.Queries;
import PFSQGen.SparqlQueryParser;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;

import java.util.ArrayList;
import java.lang.NumberFormatException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.TimeUnit;
import org.apache.jena.query.QueryException;
import org.apache.jena.query.QueryParseException;
import PFed.ExecutionStrategy;
import PFed.SageExecution;

public class TestCleanQuerySet {
    public static void main(String[] args) {
        if(args.length != 3){
            System.out.println("TestCleanQuerySet EndpointURL PathToQuery Start");
            return;
        }
        int startFrom = 0;
        try{
            startFrom = Integer.parseInt(args[2]);
        }catch(NumberFormatException e){
            e.printStackTrace();
            System.out.println("The argument has to be an Integer");
        }
        String queriesPath = args[1];
        String endpoint = args[0];
        Path output = Paths.get("results/cleanedQuery.txt");
        Path outputPError = Paths.get("results/ParseErrQuery.txt");
        String startQ = "#-------------------------------------------------------" + System.getProperty("line.separator");
        int total = 0;
        int saved = 0;
        
        System.out.println("Starting queries file");
        Queries queries = new Queries(queriesPath,"nonCrypt");
        System.out.println("Starting queries execution");
        int error = 0;
        ExecutionStrategy executor = new SageExecution();
        for(int i = startFrom; i<queries.getQueries().size(); ++i){
            SparqlQueryParser q = queries.getQueries().get(i);
            try {
                ++total;
                try{
                    if (executor.hasResult(q.toString(), endpoint)) {
                        byte res[] = new String(startQ + q).getBytes();
                        try{
                            Files.write(output, res,StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                        ++saved;
                    }
                }catch(QueryException e){
                    ++error;
                }
            }catch(QueryParseException e){
                byte res[] = new String(startQ + q).getBytes();
                try{
                    Files.write(outputPError, res, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                }catch (IOException ioe){
                    ioe.printStackTrace();
                }
                System.out.println(e);
            }
            if(total % 10 == 0){
                System.out.println("Processed: " + total + ", saved:" + saved);
            }
        }
        System.out.println("Errored query :");
        System.out.println(error);
    }
}
