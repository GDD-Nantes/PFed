package Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import org.apache.jena.graph.Node;
import org.apache.jena.query.ARQ;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;

import PFSQGen.*;
public class CreateQueryN3 {
  public static void main( String[] args) throws Exception{
    Map<String,Set<Integer>> logPredicates = new HashMap<String,Set<Integer>>();
    ARQ.init();
    //-----------------------Reading----------------
    Queries queries = new Queries(args[0], "nonCrypte");
    String outputFile = args[2];
    String dataset = args[1];
    ArrayList<Query> niceQs = new ArrayList<>();
    int cpt=0;
    for (int i =0; i<queries.getQueries().size();++i) {
      Query query = QueryFactory.create(queries.getQueries().get(i).getQueryString());
      if (query != null) {
        niceQs.add(query);
        ArrayList<Node> qPredicates = PruningLogs.getPredicates(query);
        for(Node pred : qPredicates){
          if ((pred != null) && (!pred.toString().startsWith("?"))) {
            HashSet<Integer> temp = new HashSet<Integer>();
            temp.add(cpt);
            if(logPredicates.putIfAbsent(pred.toString(), temp) != null)
              logPredicates.get(pred.toString()).add(cpt);
          }
        }
        ++cpt;
      }
    }
    //----------------------Writing-----------------
    BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));
    bw.write("@prefix un:<http://univ-nantes.fr/gdd/>.");
    bw.newLine();
    for(int i=0;i<niceQs.size();++i){
      bw.write("_:b"+i+" un:fullQuery \""+niceQs.get(i).toString()+"\" ;");
      bw.newLine();
      bw.write("\t un:hasResultIn <"+dataset+"> .");
      bw.newLine();
    }
    String last;
    for(String p : logPredicates.keySet()){
      bw.write("<"+p+"> un:isIn ");
      last="";
      for(int q : logPredicates.get(p)){
        if(!last.equals("")){
          bw.write("_:b"+last+", ");
        }
        last = ""+q;
      }
      bw.write("_:b"+last+" .");
      bw.newLine();
    }
  }
}
