package de.unikoblenz.west.splodge.queryGenerator;

import de.unikoblenz.west.splodge.JoinPattern;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;


/**
 * Used to generate the SPARQL query.
 * 
 * @author Daniel Janke &lt;danijankATuni-koblenz.de&gt;
 *
 */
public class QueryGenerator implements Closeable {

  private final File queryDir;

  private final Writer selectivityOutput;

  public QueryGenerator(File outputDir) {
    if (!outputDir.exists()) {
      outputDir.mkdirs();
    }
    queryDir = new File(outputDir.getAbsolutePath() + File.separator + "queries");
    if (!queryDir.exists()) {
      queryDir.mkdirs();
    }
    try {
      selectivityOutput = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
              outputDir.getAbsolutePath() + File.separator + "querySelectivities.txt", true),
              "UTF-8"));
    } catch (UnsupportedEncodingException | FileNotFoundException e) {
      throw new RuntimeException(e);
    }
  }
//Changed ... to [] to allow graphs array
  public void createQuery(String queryFileName, double selectivity, JoinPattern joinPattern,
          int limit, String[] predicates, String[] graphs) {
    //Added true to append the new queries
    try (Writer queryWriter = new BufferedWriter(new OutputStreamWriter(
            new FileOutputStream(queryDir.getAbsolutePath() + File.separator + queryFileName,true),
            "UTF-8"));) {
            //UNCOMMENT NEEDS SELECTIVITY OF EACH QUERY (another array? full object? ...)
//       selectivityOutput.write(queryFileName + "\t" + selectivity + "\n");

      int varId = 0;
      StringBuilder sb = new StringBuilder();
      //Change to for i to associate graphs
//       for (String predicate : predicates) {
     String baseGraph = graphs[0];
     String predicate, currGraph;
     for(int i=0; i<predicates.length; ++i){
        predicate = predicates[i];
        currGraph = graphs[i];
        //Add service
        if(!currGraph.equals(baseGraph)){
            sb.append("    SERVICE <graph::").append(currGraph).append("> {\n").append("    ");
        }
        
        if (joinPattern == JoinPattern.SUBJECT_OBJECT_JOIN) {
          sb.append("    ?v").append(varId).append(" ").append(predicate).append(" ?v")
                  .append(++varId).append(".\n");
        } else if (joinPattern == JoinPattern.SUBJECT_SUBJECT_JOIN) {
          sb.append("    ?v0 ").append(predicate).append(" ?v").append(++varId).append(".\n");
        } else {
          throw new IllegalArgumentException(
                  "The join pattern " + joinPattern + " is currently not supported.");
        }
        
        //close service bracket
        if(!currGraph.equals(baseGraph)){
            sb.append("    }\n");
        }
      }

      queryWriter.write("SELECT ?v0 ?v" + varId + " WHERE {\n");
      queryWriter.write(sb.toString());
      queryWriter.write("}\n");
      if (limit > 0) {
        queryWriter.write(" LIMIT " + limit + "\n");
      }

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void close() {
    try {
      selectivityOutput.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
