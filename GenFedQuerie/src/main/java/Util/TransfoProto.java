package Util;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.sparql.core.TriplePath;
import org.apache.jena.sparql.syntax.ElementPathBlock;
import org.apache.jena.sparql.syntax.ElementVisitorBase;
import org.apache.jena.sparql.syntax.ElementWalker;
// import org.apache.jena.sparql.core.Var;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.sparql.syntax.syntaxtransform.QueryTransformOps;
// import org.apache.jena.query.Syntax;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import org.apache.jena.graph.Node;
import java.util.Iterator;

public class TransfoProto{
    public static Query toProto(Query q, ArrayList<String> maps){
//         Map<Var,Node> sbj = new HashMap<Var,Node>();
        Map<String,String> conc = new HashMap<String,String>();      
        ArrayList<String> predNull = new ArrayList<>();
        ElementWalker.walk(q.getQueryPattern(),
                // For each element...                                                                                                                    
            new ElementVisitorBase() {
            // ...when it's a block of triples...                                                                                                         
                public void visit(ElementPathBlock el) {
                    // ...go through all the triples...                                                                                                       
                    Iterator<TriplePath> triples = el.patternElts();
                    while (triples.hasNext()) {
                        // ...and grab the triple               
                        TriplePath curr = triples.next();
                        int nexVal = conc.size()+1;
                        Node curS = curr.getSubject();
                        if(curS.isConcrete()){
                            if(conc.putIfAbsent(curS.toString(),"var"+nexVal)==null)
                                maps.add(curS.toString().replaceAll("(\\\\)*\"","\\\\\"").replaceAll("(\\\\)*\n","\\\\\n"));
                        }else{
                            if(conc.putIfAbsent(curS.toString(),"var"+nexVal)==null)
                                maps.add(curS.toString());
                        }
                        nexVal = conc.size()+1;
                        curS = curr.getObject();
                        if(curS.isConcrete()){
                            if(conc.putIfAbsent(curS.toString(),"var"+nexVal)==null)
                                maps.add(curS.toString().replaceAll("(\\\\)*\"","\\\\\"").replaceAll("(\\\\)*\n","\\\\\n"));
                        }else{
                            if(conc.putIfAbsent(curS.toString(),"var"+nexVal)==null)
                                maps.add(curS.toString());
                        }
                        
//                         nexVal = sbj.size()+conc.size()+1;
                        curS = curr.getPredicate();
                        if(curS!=null){
                            if(curS.isConcrete()){
                                if((curS.toString().equals("http://www.w3.org/1999/02/22-rdf-syntax-ns#type") || curS.toString().equals("a")) && curr.getObject().isConcrete()){
                                    if(conc.remove(curr.getObject().toString())!=null)
                                        conc.put(curr.getObject().toString()+" ","Placeholder");
                                }
                            }else{
                                predNull.add(curS.toString());
                            }
                        }
                    }
                }
            }
        );
        if(predNull.size()> 0)
            return null;
        //DOES NOT WORK WITH NESTED SELECT IF PREFIX!!!!
//         Query newQ = QueryTransformOps.transform(q, sbj);
        q.setQueryResultStar(true);
        q.setDistinct(false);
        Map<String,String> Pref = new HashMap<String,String>();
        String resQ = "";
        boolean litOn = false;
        String curLit = "";
        Boolean fix;
//         System.out.println(conc);
        boolean firstSel = false;
        String minPre="";//The prefix with just :
        // Uses syntaxe from Query, avoiding many potential hiccup with regex
        for(String m : q.toString().split("\n")){
            if(m.startsWith("PREFIX")){
                String[] t = m.split(" ");
                int i = 2;
                fix = false;
                while(i<t.length && !fix){
                    ++i;
                    fix = !t[i].equals("");
                }
                if(fix){
                    if(t[2].equals(":")){
                        minPre = t[i].replaceAll("(<|>)","");
                    }else{
                        Pref.put(t[2],t[i].replaceAll("(<|>)",""));
                    }
                }
            }else if(!m.contains("FILTER") && !m.contains("ORDER") && !m.contains("SELECT") && !m.contains("GROUP")&& !m.contains("OFFSET")&& !m.contains("LIMIT")){
                String newm = "";
//                 for(String t : m.split(" ")){
                for(String t : m.split("\\s")){
                    if(litOn){
                        curLit+=" "+t;
                        if(t.contains("\"")){
                            litOn = false;
                            t = curLit;
                        }
                    }else if(t.startsWith("\"") && !t.matches("(\".*){2}")){
                        litOn = true;
                        curLit = t;
                    }
                    if(!litOn){
//                         for(String pre : Pref.keySet()){
//                             if(t.startsWith(pre)){
//                                 if(conc.containsKey(t.replace(pre,Pref.get(pre)))){
//                                     t = "?" + conc.get(t.replace(pre,Pref.get(pre)));
//                                 }else {
//                                     String[] tm = t.split("\\(");
//                                     t = tm[0].replace(pre,"<" + Pref.get(pre)) + ">";
//                                     for(int i =1; i<tm.length;++i){
//                                         t+="("+tm[i];
//                                     }
//                                 }
//                                 break;
//                             }             
//                         }
//                         if(conc.containsKey(t)){
//                             t = "?" + conc.get(t);
//                         }
                        //If t still starts with :, then no prefix was found
                        String[] tmin = t.split(":");
                        String pre = tmin[0]+":";
                        if(Pref.containsKey(pre)){
                            if(conc.containsKey(t.replace(pre,Pref.get(pre)))){
                                t = "?" + conc.get(t.replace(pre,Pref.get(pre)));
                            }else {
                                String[] tm = t.split("\\(");
                                t = tm[0].replace(pre,"<" + Pref.get(pre)) + ">";
                                for(int i =1; i<tm.length;++i){
                                    t+="("+tm[i];
                                }
                            }
                        }
                        if(!minPre.equals("") && t.startsWith(":")){
                            t = "<" + minPre + t.substring(1) + ">";
                        }

                        if(conc.containsKey(t.replaceAll("(<|>)",""))){
                            t = "?" + conc.get(t.replaceAll("(<|>)",""));
                        }
                        newm += " "+t;
                    }
                }           
                m = newm;
            
            }else if(m.contains("FILTER")){
                String curW = "";
                String newm = "";
                for(String t : m.split("")){
                    if(t.matches("[(), ^]")){
                        if(curW.contains(":")){
                            String[] curMin = curW.split(":");
                            if(!minPre.equals("") && curMin[0].equals("")){
                                curW = "<" + minPre + curW.substring(1) + ">";
                            } else{
                                String pre = curMin[0]+":";
                                if(Pref.containsKey(pre)){
                                    if(conc.containsKey(curW.replace(pre,Pref.get(pre)))){
                                        curW = "?" + conc.get(curW.replace(pre,Pref.get(pre)));
                                    }else {
                                        curW = curW.replace(pre,"<" + Pref.get(pre)) + ">";
                                    }
                                }
                            }
                        }
                        if(conc.containsKey(curW.replaceAll("[<>]",""))){
                            curW = "?" + conc.get(curW.replaceAll("[<>]",""));
                        }
                        newm += curW + t;
                        curW = "";
                    }else{
                        curW += t;
                    }
                }
                m=newm;
            }
//             if(m.startsWith("SELECT") && !firstSel){
//                 m = "";
//                 firstSel = true;
//             }
            if(!m.startsWith("ORDER") && !m.startsWith("GROUP")&& !m.startsWith("OFFSET")&& !m.startsWith("LIMIT") && !m.contains("PREFIX") /*&& !m.contains("FILTER")*/){
                resQ += m +"\n";
            }
            
        }
//         //Write prefix after to remove the one in nested select
//         for(String pre : Pref.keySet()){
//             resQ = "PREFIX "+pre+" <"+Pref.get(pre)+">\n" + resQ;
//         }
//         if(!minPre.equals(""))
//             resQ = "PREFIX : <"+minPre+">\n" + resQ;
        Query res = null;
        try{
            res = QueryFactory.create(resQ);
        }catch(Exception e){
            System.out.println("ERROR QueryFactory Proto: "+e+"\n"+resQ+"#------------------"+q.toString()+"\n+++++++++++++");
            return null;
        }
        return res;
    }
}
