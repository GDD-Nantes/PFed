package Util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.io.FileReader;
import org.apache.commons.cli.ParseException;
import java.util.Set;

import PFSQGen.*;

public class ConcatSumN3 {
  public static void main(String[] args) throws Exception {
    Properties conf = new Properties();
    conf.load(new FileReader(args[0]));
    String outputFile = args[1];
    Summary authSum = new AuthSummary(checkConfig(conf,"summaryFile"));
    Summary typeSum = new TypeSummary(checkConfig(conf,"summaryTypeFile"));
    String url = checkConfig(conf,"endpointURL");
    Set<Capability> caps = authSum.getCapabilities();
    Capability capaT;
    String tmp = "";
    String last = "";
    int cpt = 0;
    BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));
    bw.write("@prefix ds:<http://aksw.org/fedsum/>.");
    bw.newLine();
    bw.write("@prefix un:<http://univ-nantes.fr/gdd/>.");
    bw.newLine();
    bw.write("[] a ds:Service ;");
    bw.newLine();
    bw.write("\tds:url <"+url+"> ;");
    bw.newLine();
    for(Capability cap : caps){
      bw.write("\tds:capability");
      bw.newLine();
      bw.write("\t\t[");
      bw.newLine();
      bw.write("\t\t\tds:predicate <"+cap.getPredicate()+"> ;");
      bw.newLine();
      //-----------------Authorities-----------
      if(cap.getSbjAuthority().size()> 0){
        last = "";
        bw.write("\t\t\tds:sbjAuthority ");
        for(String sbj : cap.getSbjAuthority()){
          if(!last.equals("")){
            bw.write("<"+last+">, ");
          }
          last = sbj;
        }
        bw.write("<"+last+"> ;");
        bw.newLine();
      }
      
      if(cap.getObjAuthority().size()>0){
        last = "";
        bw.write("\t\t\tds:objAuthority ");
        for(String obj : cap.getObjAuthority()){
          if(!last.equals("")){
            bw.write("<"+last+">, ");
          }
          last = obj;
        }
        bw.write("<"+last+"> ;");
        bw.newLine();
      }
      //----------------Types-------------------
      capaT = typeSum.getCapaFromPred(cap.getPredicate());
      if(capaT != null){
        if(capaT.getSbjAuthority().size()>0){
          last = "";
          bw.write("\t\t\tun:sbjType ");
          for(String sbj : capaT.getSbjAuthority()){
            if(!last.equals("")){
              bw.write("<"+last+">, ");
            }
            last = sbj;
          }
          bw.write("<"+last+"> ;");
          bw.newLine();
        }
        if(capaT.getObjAuthority().size()>0){
          last = "";
          bw.write("\t\t\tun:objType ");
          for(String obj : capaT.getObjAuthority()){
            if(!last.equals("")){
              bw.write("<"+last+">, ");
            }
            last = obj;
          }
          bw.write("<"+last+"> ;");
          bw.newLine();
        }
      }
      bw.write("\t\t] ;");
      bw.newLine();
      ++cpt;
      System.out.println("Wrote "+cpt+"/"+caps.size()+" capabilities"); 
    }
    bw.write("\t.");
    bw.newLine();
    bw.write("#---------End---------");
    bw.close();
  }
  private static String checkConfig(Properties prop,String key) throws ParseException{
      String testConf = prop.getProperty(key);
      if(testConf == null){
          throw new ParseException("Bad configuration for "+prop.getProperty("name")+", "+key+" not found");
      }
      return testConf;
  }
}
