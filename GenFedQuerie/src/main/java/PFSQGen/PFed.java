package PFSQGen;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PFed{
  @RequestMapping("/PFed")
    public String realPFed(@RequestParam(value="endP", defaultValue="http://localhost:3030/dbpedia/sparql")String endP,@RequestParam(value="query", defaultValue="SELECT * { ?s ?p ?o }")String query,@RequestParam(value="feds", defaultValue="http://localhost:3030/swdf/sparql")String feds, @RequestParam(value="type",required=false,defaultValue="true")boolean type){
        ARQ.init();
        ExecutionStrategy executor = new FusekiExecution();
        Query q = QueryFactory.create(query);
        Map<String,Set<String>> varPred;
        this.extractVarPred(q,varPred);
        List<String> otherEndp = new ArrayList<>();
        otherEndp.addAll(Arrays.asList(feds.split(",")));
        //For each var, pick a pred and findingMinStar(Type)
        for(String var : varPred.keySet()){
          String pred = varPred.get(var).Iterator().next();
          ResultSet rst;
          if(type){
            rs = executor.execQuery(endP,GenSparql.findingMinStarType(pred,otherEndp));
          }else{
            rs = executor.execQuery(endP,GenSparql.findingMinStar(pred,otherEndp));          
          }
          Map<Integer,Map<String,Set<String>>> datasWithPredInQuery = new HashMap<>();
          for(int i =0; i<otherEndp.szie();++i){
            datasWithPredInQuery.put(i,new HashMap<>());
          }
        //For each answer, fetch query from pred (if not already saved)
          while(rs.hasNext()){
            QuerySolution soln = rs.next();
            for(String var : soln.varNames()){
              String currPred = soln.get(var).toString();
              try{
                int placeCurEndp = Integer.parseInt(var.substring(1,var.length()));
                if(!datasWithPredInQuery.get(placeCurEndp).containsKey(currPred)){
                  HashSet<String> queriesTmp = new HashSet<>();
                  ResultSet rsPredQuery = executor.execQuery(otherEndp.get(placeCurEndp),GenSparql.queryFromPred(currPred,otherEndp.get(placeCurEndp)));
                  while(rsPredQuery.hasNext()){
                    QuerySolution solnQ = rsPredQuery.next();
                    queriesTmp.add(solnQ.get("query"));
                  }
                  datasWithPredInQuery.get(placeCurEndp).put(currPred,queriesTmp);
                }
                
              }catch(Exception e){
                e.printStackTrace();
              }
            }
          }
        }
        //For each solution, build a federated query (=> for now one big)
        return res;
//         cleanup();
    }
    
    public static void extractVarPred(Query q, Map<String,Set<String>> varPred){
      varPred = new HashMap<>();
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
    }
}
