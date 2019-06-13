package PFSQGen;


public class GenSparql {
  public static String findingMinStar(String predStart/*, String urlStart*/, Set<String> urlNext){
    int cpt = 0;
    String res="PREFIX ds: <http://aksw.org/fedsum/>\n"+
            "PREFIX un: <http://univ-nantes.fr/gdd/>\n"+
            "SELECT ";
            for(int i =1;i<=urlNext.size();++){
              res+="?p"+i+" ";
            }
      res+= "{\n"+
            " ?x0 ds:predicate <"+predStart+"> ;\n"+
            "   ds:sbjAuthority ?sbjAuth0 .\n";
            for(String url : urlNext){
        res+= " SERVICE <"+urlNext+"> {\n"+
              "   ?x"+ ++cpt +" ds:predicate ?p"+cpt+" ;\n"+
              "     ds:sbjAuthority ?sbjAuth"+cpt+" .\n"+
              " }\n";
            }
      res+="}";
      return res;
  }
  public static String findingMinStarType(String predStart/*, String urlStart*/, List<String> urlNext){
    int cpt = 0;
    String res="PREFIX ds: <http://aksw.org/fedsum/>\n"+
            "PREFIX un: <http://univ-nantes.fr/gdd/>\n"+
            "SELECT ";
            for(int i =0;i<urlNext.size();++i){
              res+="?p"+i+" ";
            }
      res+= " {\n"+
            " ?x0 ds:predicate <"+predStart+"> ;\n"+
            "   ds:sbjAuthority ?sbjAuth ;\n"+
            "   un:sbjType ?sbjType .\n";
            for(int i =0;i<urlNext.size();++i){
        res+= " SERVICE <"+urlNext.get(i)+"> {\n"+
              "   ?x"+ i +" ds:predicate ?p"+i+" ;\n"+
              "     ds:sbjAuthority ?sbjAuth .\n"+
              "     un:sbjType ?sbjType .\n";
              " }\n";
            }
      res+="}";
      return res;
  }
  public static String queryFromPred(String pred, String url){
    return  "PREFIX un: <http://univ-nantes.fr/gdd/>\n"+
            "SELECT ?query {\n"+
            " <"+pred+"> un:isIn ?q .\n"+
            " ?q un:hasResultOn <"+url+"> ;\n"+
            "   un:fullQuery ?query .\n"+
            "}";
  }
}