/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PFSQGen;

import PFSQGen.Summary;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author khiyari-s
 */
public class TypeSummary extends Summary{
    
    public TypeSummary(String inputFile) {
       
        BufferedReader br = null;
        FileReader fr = null;
        //int line=0;
        boolean syntaxParser = false;
        int charParserPosition = 0;
        int lastParserPosition = 0;
        int searchPatternPos = 0;
        try {

            fr = new FileReader(inputFile);
            br = new BufferedReader(fr);
            String sCurrentLine;

            br = new BufferedReader(new FileReader(inputFile));
            while ((sCurrentLine = br.readLine()) != null) {
                if (sCurrentLine.indexOf("capability") >= 0) {
                    Capability tempCap = new Capability();
                    if ((sCurrentLine = br.readLine()) != null) {
                        if (sCurrentLine.indexOf("[") >= 0) {
                            syntaxParser = true;
                            while (((sCurrentLine = br.readLine()) != null) && (syntaxParser)) {
                                if (sCurrentLine.indexOf("]") >= 0) {
                                    syntaxParser = false;
                                    break;
                                } else {
                                    if (sCurrentLine.startsWith("predicate")) {
                                        String temp = sCurrentLine.split(" ")[1];
                                        if (temp != null) //predicate.add(temp);
                                            tempCap.setPredicate(temp);
                                    }
                                    if (sCurrentLine.startsWith("Subject Classes")) {
                                        String[] tempT = sCurrentLine.split(": ");
                                        if ((tempT.length)>1) {
                                            //tempCap.addSbjAuthority(tempT[1]);
                                            String[] tempTList = tempT[1].split(", ");
                                            for (int i=0; i<tempTList.length; i++) {
                                                tempCap.addSbjAuthority(tempTList[i]);
                                            }
                                        }
                                    }
                                    if(sCurrentLine.startsWith("Object Classes")) {
                                        String[] tempT = sCurrentLine.split(": ");
                                        if ((tempT.length)>1) {
                                            //tempCap.addObjAuthority(tempT[1]);
                                            String[] tempTList = tempT[1].split(", ");
                                            for (int i=0; i<tempTList.length; i++) {
                                                tempCap.addObjAuthority(tempTList[i]);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    this.capabilities.add(tempCap);
                }
            }
        }
        catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {

                if(br != null)
                    br.close();

                if(fr != null)
                    fr.close();

            } catch (IOException ex) {

                ex.printStackTrace();

            }
        }
    }
}

