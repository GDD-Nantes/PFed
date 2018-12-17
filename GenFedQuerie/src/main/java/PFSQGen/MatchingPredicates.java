//package com.util;
package PFSQGen;

import java.util.ArrayList;
import java.util.List;

import PFSQGen.Capability;
import PFSQGen.SparqlQueryParser;
import PFSQGen.Summary;
import java.util.Map;
import java.util.HashMap;

public class MatchingPredicates {

    private Summary summaryList1;
    private Summary summaryList2;
    private Map<String,List<Capability>> matchingTable;
    
    //---------------Slow and static-------------------
    public List<String> getSubjAuthority(String predicate, String sumFile) {
        summaryList1 = new AuthSummary(sumFile);
        for (Capability testMatch : summaryList1.getCapabilities()) {
            if (testMatch.getPredicate().equals(predicate)) {
                return new ArrayList<String>(testMatch.getSbjAuthority());
            }
        }
        return null;
    }
        
    //---------------Slow and static-------------------
    public List<String> getObjAuthority(String predicate, String sumFile) {
        summaryList1 = new AuthSummary(sumFile);
        for (Capability testMatch : summaryList1.getCapabilities()) {
            if (testMatch.getPredicate().equals(predicate)) {
                return new ArrayList<String>(testMatch.getObjAuthority());
            }
        }
        return null;
    }

    public MatchingPredicates(ArrayList<String> sumFiles) {
        summaryList1 = new AuthSummary(sumFiles.get(0));
        summaryList2 = new AuthSummary(sumFiles.get(1));
        generate();
    }
    public MatchingPredicates(ArrayList<String> sumFiles, boolean isType) {
        if(isType){
            summaryList1 = new TypeSummary(sumFiles.get(0));
            summaryList2 = new TypeSummary(sumFiles.get(1));
        }else{
            summaryList1 = new AuthSummary(sumFiles.get(0));
            summaryList2 = new AuthSummary(sumFiles.get(1));
        }
        generate();
    }
    private void generate(){
        matchingTable = new HashMap<String,List<Capability>>();
        for (Capability testMatch : summaryList1.getCapabilities()) {
            for (Capability currCap : summaryList2.getCapabilities()) {
                Capability resultMatch = testMatchingAuth(testMatch, currCap);
                if ((resultMatch.getSbjAuthority().size() > 0) || (resultMatch.getObjAuthority().size() > 0)) {
                    if(matchingTable.get(testMatch.getPredicate())==null){
                        matchingTable.put(testMatch.getPredicate(), new ArrayList<Capability>() );
                        
                    }
                    matchingTable.get(testMatch.getPredicate()).add(resultMatch);
                }
            }
        }
    }

    private Capability testMatchingAuth(Capability testMatch, Capability capability) {
        Capability resultMatch = new Capability();
        resultMatch.setPredicate(capability.getPredicate());
        //testing subjects compatibilities using loop on SbjAuthority		
        for (String testAuth : testMatch.getSbjAuthority()) {
            if(capability.getSbjAuthority().contains(testAuth)){
                resultMatch.addSbjAuthority(testAuth);
            }
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
    
    

    public int testExistingMatch(String predicate1, String predicate2) {
        List<Capability> joinableWith = matchingTable.get(predicate1);
        if (joinableWith != null) {
            if (predicate2.subSequence(0, 1).equals("<")) {
                predicate2 = predicate2.substring(1, predicate2.length()-1);
            }
            for (Capability testCapa : joinableWith) {
                if(testCapa.getPredicate().equals(predicate2)){
                    if ((testCapa.getSbjAuthority().size() != 0) && (testCapa.getObjAuthority().size() == 0)) {
                        //There is a Star between predicate1 and predicate2
                        return 1;
                    }
                    else if ((testCapa.getSbjAuthority().size() == 0) && (testCapa.getObjAuthority().size() != 0)) {
                        //There is a Path between predicate1 and predicate2
                        return 2;
                    }
                    else if ((testCapa.getSbjAuthority().size() != 0) && (testCapa.getObjAuthority().size() != 0)) {
                            //There is both path and star between predicate1 and predicate2
                        return 3;  
                    }
                }
            }
        }
        return -1;
    }

    public Summary getSummaryList1() {
        return summaryList1;
    }

    public void setSummaryList1(Summary summaryList1) {
        this.summaryList1 = summaryList1;
    }

    public Summary getSummaryList2() {
        return summaryList2;
    }

    public void setSummaryList2(Summary summaryList2) {
        this.summaryList2 = summaryList2;
    }
    
    @Override
    public String toString() {
        return "MatchingPredicates [matchingTable=" + matchingTable + "]";
    }

}
