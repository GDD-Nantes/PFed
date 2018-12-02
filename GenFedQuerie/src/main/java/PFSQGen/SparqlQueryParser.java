package PFSQGen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import java.util.ArrayList;
public class SparqlQueryParser {

    /**
     * @param args
     */
    private String QueryString;
    ArrayList<String> prefixes = new ArrayList<String>();
    ArrayList<String> subj = new ArrayList<String>();
    ArrayList<String> obj = new ArrayList<String>();
    ArrayList<String> pred = new ArrayList<String>();
    ArrayList<String> prefixedPred = new ArrayList<String>();
    ArrayList<String> logicConnector = new ArrayList<String>();
    
  
    public SparqlQueryParser(String s) {
        subj = new ArrayList<String>();
        obj = new ArrayList<String>();
        pred = new ArrayList<String>();      
        prefixes = new ArrayList<String>();
       
        QueryString = s;
        //prefixes parsing
        Pattern prefixPattern = Pattern.compile("PREFIX(.*?)>");
        Matcher prefixMatcher = prefixPattern.matcher(s);
        while (prefixMatcher.find()) {
            prefixes.add(prefixMatcher.group());
        }

        //System.out.println(prefixes.toString());
        //Pattern pattern = 
        //Pattern pattern1= Pattern.compile("(\\s(.*?)\\s)");
        Matcher matcher = Pattern.compile("\\{.*\\}", Pattern.DOTALL).matcher(s);
        //Matcher matcher1;
        while (matcher.find()) {
            String str = matcher.group().replaceAll("[{}]", "");
            String[] fullresult = str.split("\n");
            for (int j = 0; j < fullresult.length; j++) {
                String[] result = (fullresult[j].trim()).split("\\s");
                //System.out.println(fullresult[j].trim());
                for (int i = 0; i < result.length; i++) {
                    //prefix replacement				
                    int temppos = 0;
                    if (((temppos = result[i].indexOf(':')) > -1) && (result[i].indexOf('<') == -1) && (i == 1)) {
                        for (String prefix : this.prefixes) {
                            if (prefix.contains(result[i].substring(0, temppos))) {
                                String fullAdress = prefix.substring(prefix.indexOf('<'), prefix.lastIndexOf('>'));
                                fullAdress = fullAdress + result[i].substring(temppos + 1, result[i].length()) + '>';
                                //System.out.println("fullAdress="+fullAdress);
                                //result[i]=fullAdress;
                                prefixedPred.add(fullAdress);
                                break;
                            }
                        }
                    } else {
                        if ((i == 1) && (result[i] != ".") && (result[i].indexOf("(") == -1)) {
                            prefixedPred.add(result[i]);
                        }
                    }
                    result[i] = result[i].trim();
                    if ((result[i] != ".")) {
                        if ((result[i].indexOf("?") == -1) && (result[i].indexOf(":") == -1)) {
                            logicConnector.add(result[i]);
                        } else {
                            if ((result[i].indexOf("(") == -1) || (result[i].indexOf("<") > -1)) {
                                switch (i) {
                                    case 0:
                                        subj.add(result[i]);
                                        break;
                                    case 1:
                                        pred.add(result[i]);
                                        break;
                                    case 2:
                                        obj.add(result[i]);
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                    }
                }
            }
        } 
    }

    public SparqlQueryParser() {
        this.QueryString = null;
    }

    public String getQueryString() {
        return QueryString;
    }

    public ArrayList<String> getPred() {
        return pred;
    }

    public ArrayList<String> getSubj() {
        return subj;
    }

    public ArrayList<String> getObj() {
        return obj;
    }
    
    public void remplaceUnifRes(String toRemplace, String unificationRes) {
        for (int i=0; i<subj.size(); i++) {
            if (subj.get(i).equals(toRemplace)) {
               subj.set(i, unificationRes);
            }
        } 
        for (int i=0; i<obj.size(); i++) {
            if (obj.get(i).equals(toRemplace)) {
                obj.set(i, unificationRes);
            }
        } 
    }
    
    public void remplaceUnifResString(String toRemplace, String unificationRes) {
        //System.out.println("je suis là pour "+toRemplace+" et "+unificationRes);
        //toRemplace = toRemplace.replaceAll("\\?", "\\\\\\\\?");
        //System.out.println("toRemplace : "+toRemplace);
        if (toRemplace.subSequence(0, 1).equals("?")) {
            QueryString = QueryString.replaceAll("\\"+toRemplace, unificationRes);
        }
        else 
        QueryString = QueryString.replaceAll(toRemplace, unificationRes);
        //System.out.println("------ "+QueryString);
    }
   
    
    
    

    @Override
    public String toString() {
        //return "Query [QueryString=" + QueryString + ", subj=" + subj + ", obj=" + obj + ", pred=" + pred + "]";
        return QueryString;
    }
    
    //public String toString() {
    //    return "Query [QueryString=" + addPrefix(QueryString) + ", subj=" + subj + ", obj=" + obj + ", pred=" + pred + "]";
    //}
    
    //public String toString2() {
      //  return "Query [QueryString=" + QueryString + ", subj=" + this.addPrefixe(subj) + ", obj=" + this.addPrefixe(obj) + ", pred=" + this.addPrefixe(pred) + "]";
    //}

    public ArrayList<String> getPrefixedPred() {
        return prefixedPred;
    }
    
     public String addPrefix(String s) {
        String res = s;
        Map<String, String> temp = new LinkedHashMap<>();
        for (String st : this.prefixes) {
            String[] stSplt = st.split("  ");
           // System.out.println("---"+stSplt.length+" "+st);
            //if ((!stSplt[1].equals("")) && (!stSplt[2].equals(""))) {
            if (stSplt.length>3) {
                temp.put(stSplt[1], stSplt[2]);
            }
            
          //  System.out.println(temp);
        }
        
        Set<Entry<String, String>> settemp = temp.entrySet();
        Iterator<Entry<String, String>> it = settemp.iterator();
         while(it.hasNext()){
        Entry<String, String> e = it.next();
        
        //System.out.println("e.getKey() = "+e.getKey()+" e.getValue() = "+e.getValue());
        res = res.replaceAll(e.getKey(), e.getValue());
        
         //System.out.println(e.getKey() + " : " + e.getValue());
      }
        return res;
    }
     
     public String addPrefixString(String s) {
        String res = s;
        String[] ssplit = s.toString().split(":");
        for (String st : this.prefixes) {
            String[] stspl = st.split(": ");
            if ((stspl[0].equals("PREFIX  "+ssplit[0])) || (stspl[0].equals("PREFIX "+ssplit[0])) ) {
                //res = stspl[1].substring(0, stspl[1].length()-1)+""+ssplit[1]+">";
                if ((stspl[1].substring(0, 4).equals("    ")) && (ssplit.length > 1)) {
                    res = stspl[1].substring(5, stspl[1].length()-1)+""+ssplit[1];
                }
                else if ((stspl[1].substring(0, 3).equals("   ")) && (ssplit.length > 1)) {
                    res = stspl[1].substring(4, stspl[1].length()-1)+""+ssplit[1];
                }
                else if ((stspl[1].substring(0, 2).equals("  ")) && (ssplit.length > 1)) {
                    res = stspl[1].substring(3, stspl[1].length()-1)+""+ssplit[1];
                }
                else if ((stspl[1].substring(0, 1).equals(" ")) && (ssplit.length > 1))  {
                    res = stspl[1].substring(2, stspl[1].length()-1)+""+ssplit[1];
                }
                else if ((ssplit.length > 1)) {
                    res = stspl[1].substring(1, stspl[1].length()-1)+""+ssplit[1];
                }
                
                //System.out.println(s+"          *res = "+res);
            }
        }//System.out.println("res="+res);
        return res;
    }
            
    public void setPrefixedPred(ArrayList<String> prefixedPred) {
        this.prefixedPred = prefixedPred;
    }

}
