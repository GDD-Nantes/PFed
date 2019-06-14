package PFSQGen;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

import org.apache.jena.query.Query;
import org.apache.jena.query.ResultSet;
import org.apache.jena.graph.Node;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.sparql.syntax.ElementVisitorBase;
import org.apache.jena.sparql.syntax.ElementWalker;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.sparql.core.TriplePath;
import org.apache.jena.sparql.syntax.ElementPathBlock;
import org.apache.jena.sparql.syntax.syntaxtransform.QueryTransformOps;
import org.apache.jena.query.ARQ;
import org.apache.jena.query.QuerySolution;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import java.net.URLEncoder;
import java.net.URLDecoder;

@RestController
public class PFed{
  private EnabledServer servL;
  
    @Autowired
  public void setServL(EnabledServer servL){
    this.servL = servL;
  }
  
  @RequestMapping("/PFed")
    public String realPFed(@RequestParam(value="endP",required=false, defaultValue="http://localhost:3030/dbpedia/sparql")String endP,@RequestParam(value="query", defaultValue="SELECT * { ?s ?p ?o }")String query,@RequestParam(value="feds",required=false, defaultValue="http://localhost:3030/swdf/sparql")String feds, @RequestParam(value="type",required=false,defaultValue="true")boolean type){
//         EnabledServer servL = new EnabledServer();
        ARQ.init();
        ExecutionStrategy executor = new FusekiExecution();
        Query q = QueryFactory.create(query);
        Map<String,Set<String>> varPred = this.extractVarPred(q);
        List<String> otherEndp = new ArrayList<>();
        otherEndp.addAll(Arrays.asList(feds.split(",")));
        Set<String> resFed = new HashSet<>();
//         System.out.println(servL);
        //For each var, pick a pred and findingMinStar(Type)
        for(String var : varPred.keySet()){
          String pred = varPred.get(var).iterator().next();
          ResultSet rs;
          if(type){
            rs = executor.execQuery(servL.getPropByName(endP).getSummary(),QueryFactory.create(GenSparql.findingMinStarType(pred,otherEndp,servL)));
          }else{
            rs = executor.execQuery(servL.getPropByName(endP).getSummary(),QueryFactory.create(GenSparql.findingMinStar(pred,otherEndp,servL)));          
          }
          Map<Integer,Map<String,Set<String>>> datasWithPredInQuery = new HashMap<>();
          for(int i =0; i<otherEndp.size();++i){
            datasWithPredInQuery.put(i,new HashMap<>());
          }
          Set<FedQuery> allFQ = new HashSet<>();
        //For each answer, fetch query from pred (if not already saved)
          while(rs.hasNext()){
            QuerySolution soln = rs.next();
            FedQuery tmpFQ = new FedQuery();
            Iterator<String> solnIte = soln.varNames();
            while(solnIte.hasNext()){
              String varSol = solnIte.next();
              String currPred = soln.get(varSol).toString();
              try{
                int placeCurEndp = Integer.parseInt(varSol.substring(1,varSol.length()));
                if(!datasWithPredInQuery.get(placeCurEndp).containsKey(currPred)){
                  HashSet<String> queriesTmp = new HashSet<>();
                  ResultSet rsPredQuery = executor.execQuery(servL.getPropByName(otherEndp.get(placeCurEndp)).getLogs(),QueryFactory.create(GenSparql.queryFromPred(currPred,otherEndp.get(placeCurEndp))));
                  while(rsPredQuery.hasNext()){
                    QuerySolution solnQ = rsPredQuery.next();
                    queriesTmp.add(solnQ.get("query").toString());
                  }
                  datasWithPredInQuery.get(placeCurEndp).put(currPred,queriesTmp);
                }
                tmpFQ.addPredOnEnd(currPred,placeCurEndp);
              }catch(Exception e){
                e.printStackTrace();
              }
            }
            allFQ.add(tmpFQ);
          }
        //For each solution, build a federated query (=> for now one big)
          String qJoinVar = q.toString().replaceAll(var.substring(1,var.length())+"\\b", "joinVar1");
          qJoinVar = cutQuery(qJoinVar,"0_0");
          for(FedQuery fq : allFQ){
            String federated = "SELECT * {\n";
            federated += qJoinVar+"\n";
            for(PredOnEnd poe : fq.getFed()){
              int cptPred = 0;
              String superQuery = "";
            System.out.println(poe.getPredicate());
              for(String tmpQ: datasWithPredInQuery.get(poe.getEndpoint()).get(poe.getPredicate())){
                try{
                  String decodedQ = URLDecoder.decode(tmpQ,"UTF-8");
                  //Find var name for subject predicate of this query
                  Map<String,Set<String>> tmpVarPred = this.extractVarPred(QueryFactory.create(decodedQ));
                  String tmpVar = "";
                  for(String v : tmpVarPred.keySet()){
                    if(tmpVarPred.get(v).contains(poe.getPredicate())){
                      tmpVar = v;
                      break;
                    }
                  }
                  decodedQ = decodedQ.replaceAll(tmpVar.substring(1,tmpVar.length())+"\\b", "joinVar1");
                  decodedQ = cutQuery(decodedQ,""+poe.getEndpoint()+"_"+ ++cptPred);
                  superQuery += decodedQ;
                }catch(Exception e){
                  e.printStackTrace();
                }
              }
              if(!superQuery.equals("")){
                federated += "SERVICE <"+otherEndp.get(poe.getEndpoint())+"> {\n" + superQuery + "\nFILTER( BOUND( ?joinVar1 ))\n}\n";
              }
            }
            federated +="}";
            try{
              resFed.add("\""+URLEncoder.encode(federated,"UTF-8")+"\"");
            }catch(Exception e){
              e.printStackTrace();
            }
          }
        }
        return resFed.toString();
    }
    
    public static Map<String,Set<String>> extractVarPred(Query q/*, Map<String,Set<String>> varPred*/){
      Map<String,Set<String>> varPred = new HashMap<String,Set<String>>();
      ElementWalker.walk(q.getQueryPattern(),
        // For each element...                                                                                                                    
        new ElementVisitorBase() {
        // ...when it's a block of triples...                                                                                                         
          public void visit(ElementPathBlock el) {
            // ...go through all the triples...                                                                                                       
            Iterator<TriplePath> triples = el.patternElts();
            while (triples.hasNext()) {
              // ...and grab the predicate                                                                                                            
              TriplePath currTripl = triples.next();
              String subj = currTripl.getSubject().toString();
              if(currTripl.getPredicate() != null){
                if(!varPred.containsKey(subj)){
                  varPred.put(subj, new HashSet<>());
                }
                varPred.get(subj).add(currTripl.getPredicate().toString());
              }
            }
          }
        }
      );
      return varPred;
    }
    
    public static String getPrefix(String q){
      String resQ = "";
      for(String m : QueryFactory.create(q).toString().split("\n")){
        if(m.contains("PREFIX")){
          resQ+=m+"\n";
        }
        if(m.contains("SELECT")){
          return resQ;
        }
      }
      return resQ;
    }
    
    public static String cutQuery(String q, String suff){
      Map<Var, Node> replacer = new HashMap<Var, Node>();
      Query qTmp =QueryFactory.create(q);
//       int inQ = q.hashCode();
//       String preQ = "";
//       if(inQ < 0){
//           preQ = "NEG";
//           inQ *= -1;
//       }
//       String suff = "_" + preQ + inQ;
      ElementWalker.walk(qTmp.getQueryPattern(),
            // For each element...                                                                                                                    
        new ElementVisitorBase() {
        // ...when it's a block of triples...                                                                                                         
          public void visit(ElementPathBlock el) {
            // ...go through all the triples...                                                                                                       
            Iterator<TriplePath> triples = el.patternElts();
            while (triples.hasNext()) {
              // ...and grab the triple               
              TriplePath curr = triples.next();
              Node curS = curr.getSubject();
              if(!curS.isConcrete() && !curS.toString().contains("joinVar")){
                replacer.putIfAbsent((Var)curS, NodeFactory.createVariable(((Var)curS).getVarName()+"_"+suff));
              }
              curS = curr.getObject();
              if(!curS.isConcrete() && !curS.toString().contains("joinVar")){
                replacer.putIfAbsent((Var)curS, NodeFactory.createVariable(((Var)curS).getVarName()+"_"+suff));
              }
            }
          }
        }
      );
      qTmp = QueryTransformOps.transform(qTmp, replacer);
      boolean noSELECT = true;
      boolean isClosed = false;
      String waiter = "";
      String resQ = "";
      for(String m : qTmp.toString().split("\n")){
        if(noSELECT){
          if(m.contains("WHERE")){
            noSELECT = false;
          }
        }else {
          if(!isClosed){
            resQ += m+"\n";
            isClosed = m.contains("}");
          }else{
            waiter += m+"\n";
            if(m.contains("}")){
              resQ += waiter;
              waiter = "";
            }
          }
        }
      }
      return resQ;
    }
}
