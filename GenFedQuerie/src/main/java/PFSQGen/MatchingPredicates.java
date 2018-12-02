//package com.util;
package PFSQGen;

import java.util.ArrayList;
import java.util.List;

import PFed.Capability;
import PFSQGen.MatchingCapabilities;
import PFSQGen.SparqlQueryParser;
import PFed.Summaries;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import org.apache.jena.query.Query;

import java.util.Arrays;

public class MatchingPredicates {

    private Summaries summariesList1;
    private Summaries summariesList2;
    private List<MatchCapabilities> matchingTable;

    
    

//     private ArrayList<String> getJWPredicates(List<Capability> joinableWith) {
//          ArrayList<String> predicates = new ArrayList<>();
//          joinableWith.forEach((cy) -> {
//              predicates.add((String)cy.getPredicate());
//         });
//             return predicates;
//     }

    public class MatchCapabilities {

        private String predicate;
        private List<Capability> joinableWith;

        public MatchCapabilities(String predicate) {
            this.predicate = predicate;
            this.joinableWith = new ArrayList<Capability>();
        }

        public String getPredicate() {
            return predicate;
        }

        public void setPredicate(String predicate) {
            this.predicate = predicate;
        }

        public List<Capability> getMatchingCapabilities() {
            return joinableWith;
        }

        public void setMatchingCapabilities(List<Capability> matchingCapabilities) {
            this.joinableWith = matchingCapabilities;
        }

        public void addMatchingCapability(Capability capability) {
            this.joinableWith.add(capability);
        }
        
         

        @Override
        public String toString() {
            return "\n MatchCapabilities [predicate=" + predicate + ", matchingCapabilities=" + joinableWith + "]";
        }

       
    }
    
    public List<String> getSubjAuthority(String predicate, String sumFile) {
        summariesList1 = new Summaries(sumFile);
        for (Capability testMatch : summariesList1.getCapabilities()) {
            if (testMatch.getPredicate().equals(predicate)) {
                return new ArrayList<String>(testMatch.getSbjAuthority());
            }
        }
        return null;
    }
    
    public List<String> getObjAuthority(String predicate, String sumFile) {
        summariesList1 = new Summaries(sumFile);
        for (Capability testMatch : summariesList1.getCapabilities()) {
            if (testMatch.getPredicate().equals(predicate)) {
                return new ArrayList<String>(testMatch.getObjAuthority());
            }
        }
        return null;
    }

    public MatchingPredicates(ArrayList<String> sumFiles) {
        summariesList1 = new Summaries(sumFiles.get(0));
        summariesList2 = new Summaries(sumFiles.get(1));
        matchingTable = new ArrayList<MatchCapabilities>();

        //testing matching capabilities
//         int i = 0;
        for (Capability testMatch : summariesList1.getCapabilities()) {
            for (Capability currCap : summariesList2.getCapabilities()) {
                Capability resultMatch = testMatchingAuth(testMatch, currCap);
                if ((resultMatch.getSbjAuthority().size() > 0) || (resultMatch.getObjAuthority().size() > 0)) {
                    int matchPosition = testExistingMatch(testMatch.getPredicate());
                    if (matchPosition > -1) {
                        matchingTable.get(matchPosition).addMatchingCapability(resultMatch);
                    } else {
                        //System.out.println(testMatch.getPredicate().get(0));
                        MatchCapabilities match = new MatchCapabilities(testMatch.getPredicate());
                        match.addMatchingCapability(resultMatch);
                        matchingTable.add(match);
                    }
                }
            }

        }

    }

    private Capability testMatchingAuth(Capability testMatch, Capability capability) {
        // TODO Auto-generated method stub		
        Capability resultMatch = new Capability();
        resultMatch.setPredicate(capability.getPredicate());
        //testing subjects compatibilities using loop on SbjAuthority		
        for (String testAuth : testMatch.getSbjAuthority()) {
//             for (String capa : capability.getSbjAuthority()) {
//                 if (testAuth.equals(capa)) {
                if(capability.getSbjAuthority().contains(testAuth)){
                    resultMatch.addSbjAuthority(testAuth);
                }
//             }
        }

        //testing objects compatibilities using loop on ObjAuthority		
        for (String testAuth : testMatch.getObjAuthority()) {
            if(capability.getObjAuthority().contains(testAuth)){
                resultMatch.addObjAuthority(testAuth);
            }
        }

        return resultMatch;

    }

    public int testExistingMatch(String predicate) {
        for (int i = 0; i < matchingTable.size(); i++) {
            if (matchingTable.get(i).getPredicate().equals(predicate)) {
                return i;
            }
        }
        return -1;
    }
    
    

    //public int testExistingMatch(String predicate1, String predicate2) {
    public int testExistingMatch2Predicates(String predicate1, String predicate2, Query q1, Query q2) {
        // System.out.println("****** "+matchingTable.get(2).getPredicate());//+" *sara* "+matchingTable.get(0).joinableWith.contains(predicate2));
        for (int i = 0; i < matchingTable.size(); i++) {
           // predicate2 = "http://www.w3.org/2004/02/skos/core#subject";
           //System.out.println(getJWPredicates(matchingTable.get(12).joinableWith));
           // System.out.println("****** "+matchingTable.get(i).getPredicate()+"         "+matchingTable.get(12).joinableWith.toString());
          //  System.out.println("****** "+matchingTable.get(12).joinableWith.get(0).getPredicate()contains(predicate2));
            //System.out.println("matchingTable.get(i).getPredicate() = "+matchingTable.get(i).getPredicate());
         //   System.out.println(matchingTable.get(i)+" *** "+matchingTable.get(i).joinableWith);
         //System.out.println("predicate1 = "+predicate1+" / getPredicate() = "+matchingTable.get(i).getPredicate());
         //System.out.println("predicate1 = "+q1.addPrefixeString(predicate1));
         //System.out.println("predicate2 = "+predicate2+"     "+q2.addPrefixeString(predicate2));
         
         
        /*  if (matchingTable.get(i).getPredicate().equals(q1.addPrefixeString("rdf:type"))) {
         for (int k=0; k < matchingTable.get(i).joinableWith.size(); k++) {
             System.out.println("*"+matchingTable.get(i).joinableWith.get(k).getPredicate());
             System.out.println(q2.addPrefixeString("rdfs:comment"));
         if(matchingTable.get(i).joinableWith.get(k).getPredicate().get(0).equals(q2.addPrefixeString("rdfs:comment"))){
                        System.out.println("********************************************************");
                    }
         }
          }*/
         
          
          
            //if (matchingTable.get(i).getPredicate().equals(q1.addPrefixString(predicate1))) {
            if (matchingTable.get(i).getPredicate().equals(predicate1)) {
                //System.out.println(" found "+i);
                //System.out.println("* "+getJWPredicates(matchingTable.get(i).joinableWith));
                //if (getJWPredicates(matchingTable.get(i).joinableWith).toString().contains(predicate2.split(":")[1].toString())) {
                if (predicate2.subSequence(0, 1).equals("<")) {
                    predicate2 = predicate2.substring(1, predicate2.length()-1);
                }
                for (int k=0; k < matchingTable.get(i).joinableWith.size(); k++) {
                    //System.out.println("predicate1 = "+predicate1);
                    //System.out.println("getPredicate = "+matchingTable.get(i).joinableWith.get(k).getPredicate().get(0)+" *  predicate2 = "+q2.addPrefixeString(predicate2));
                    //System.out.println(predicate2+"    "+q2.addPrefixeString(predicate2));
                    
                   // if(matchingTable.get(i).joinableWith.get(k).getPredicate().get(0).equals(q2.addPrefixString(predicate2))){
                    if(matchingTable.get(i).joinableWith.get(k).getPredicate().equals(predicate2)){
                     
                        if ((matchingTable.get(i).joinableWith.get(k).getSbjAuthority().size() != 0) && (matchingTable.get(i).joinableWith.get(k).getObjAuthority().size() == 0)) {
                            return 1;
                        }
                        else if ((matchingTable.get(i).joinableWith.get(k).getSbjAuthority().size() == 0) && (matchingTable.get(i).joinableWith.get(k).getObjAuthority().size() != 0)) {
                            return 2;
                        }
                        else if ((matchingTable.get(i).joinableWith.get(k).getSbjAuthority().size() != 0) && (matchingTable.get(i).joinableWith.get(k).getObjAuthority().size() != 0)) {
                            return 3;  
                        }
                    }
                }
   
                
                
                
                //if (getJWPredicates(matchingTable.get(i).joinableWith).toString().contains(q2.addPrefixeString(predicate2))) {
                  //  return i;
                //}
                //return getJWPredicates(matchingTable.get(i).joinableWith).toString().contains(predicate2);
                // return i;
            }
        }
        return -1;
    }

    public Summaries getSummariesList1() {
        return summariesList1;
    }

    public void setSummariesList1(Summaries summariesList1) {
        this.summariesList1 = summariesList1;
    }

    public Summaries getSummariesList2() {
        return summariesList2;
    }

    public void setSummariesList2(Summaries summariesList2) {
        this.summariesList2 = summariesList2;
    }

    public List<MatchCapabilities> getMatchingTable() {
        return matchingTable;
    }

    public void setMatchingTable(List<MatchCapabilities> matchingTable) {
        this.matchingTable = matchingTable;
    }

    @Override
    public String toString() {
        return "MatchingPredicates [matchingTable=" + matchingTable + "]";
    }

}
