//package com.util;
package PFSQGen;

import java.util.ArrayList;
import java.util.List;

import PFSQGen.Capability;
import PFSQGen.MatchingCapabilities;
import PFSQGen.SparqlQueryParser;
import PFSQGen.Summaries;
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

    public class MatchCapabilities {
        //Transform it to Map String -> list<Capa>
        private String predicate;
        //If a capability has a subject in joignableWith, then it exist a Star Sbj->Sbj
        //If a capability has a object in joignableWith, then it exist a Path Obj->Sbj
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
            //For PAth need Obj->Sbj, not Obj->Obj
            if(capability.getSbjAuthority().contains(testAuth)){
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
    
    

    public int testExistingMatch(String predicate1, String predicate2) {
        for (int i = 0; i < matchingTable.size(); i++) {
            if (matchingTable.get(i).getPredicate().equals(predicate1)) {
                if (predicate2.subSequence(0, 1).equals("<")) {
                    predicate2 = predicate2.substring(1, predicate2.length()-1);
                }
                for (int k=0; k < matchingTable.get(i).joinableWith.size(); k++) {
                    if(matchingTable.get(i).joinableWith.get(k).getPredicate().equals(predicate2)){
                     
                        if ((matchingTable.get(i).joinableWith.get(k).getSbjAuthority().size() != 0) && (matchingTable.get(i).joinableWith.get(k).getObjAuthority().size() == 0)) {
                            //There is a Star between predicate1 and predicate2
                            return 1;
                        }
                        else if ((matchingTable.get(i).joinableWith.get(k).getSbjAuthority().size() == 0) && (matchingTable.get(i).joinableWith.get(k).getObjAuthority().size() != 0)) {
                            //There is a Path between predicate1 and predicate2
                            return 2;
                        }
                        else if ((matchingTable.get(i).joinableWith.get(k).getSbjAuthority().size() != 0) && (matchingTable.get(i).joinableWith.get(k).getObjAuthority().size() != 0)) {
                             //There is both path and star between predicate1 and predicate2
                            return 3;  
                        }
                    }
                }
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
