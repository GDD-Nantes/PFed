package PFSQGen;

import PFSQGen.Summary;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class AuthSummary extends Summary {
    public AuthSummary(String inputFile){
        super();
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
            //reading file line by line			
            while ((sCurrentLine = br.readLine()) != null) {
                //System.out.println(sCurrentLine);				
                //extracting capabilities
                if (sCurrentLine.indexOf("capability") >= 0) {
                    Capability tempCap = new Capability();
                    if ((sCurrentLine = br.readLine()) != null) {//Next Line
                        if (sCurrentLine.indexOf("[") >= 0) {
                            syntaxParser = true;
                            while (((sCurrentLine = br.readLine()) != null) && (syntaxParser)) {//Loop on lines
                                if (sCurrentLine.indexOf("]") >= 0) {
                                    syntaxParser = false;
                                    break;//?
                                } else {
                                    //parsing predicate
                                    if (sCurrentLine.indexOf("ds:predicate") >= 0) {
                                        charParserPosition = sCurrentLine.indexOf("<");
                                        lastParserPosition = sCurrentLine.indexOf(">");
                                        tempCap.setPredicate(sCurrentLine.substring(charParserPosition + 1, lastParserPosition));
                                    }
                                    //parsing sbjAuthority
                                    searchPatternPos = sCurrentLine.indexOf("ds:sbjAuthority");
                                    if (searchPatternPos >= 0) {
                                        //number of Subject Authority
                                        for (int i = searchPatternPos; i < sCurrentLine.length(); i++) {
                                            if (sCurrentLine.charAt(i) == '<') {
                                                lastParserPosition = sCurrentLine.indexOf('>', i);
                                                tempCap.addSbjAuthority(sCurrentLine.substring(i + 1, lastParserPosition));
                                                i = lastParserPosition;
                                            }
                                        }
                                    }
                                    //parsing objAuthority																		
                                    searchPatternPos = sCurrentLine.indexOf("ds:objAuthority");
                                    if (searchPatternPos >= 0) {
                                        //number of Subject Authority
                                        for (int i = searchPatternPos; i < sCurrentLine.length(); i++) {
                                            if (sCurrentLine.charAt(i) == '<') {
                                                lastParserPosition = sCurrentLine.indexOf('>', i);
                                                tempCap.addObjAuthority(sCurrentLine.substring(i + 1, lastParserPosition));
                                                i = lastParserPosition;
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

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {

                if (br != null) {
                    br.close();
                }

                if (fr != null) {
                    fr.close();
                }

            } catch (IOException ex) {

                ex.printStackTrace();

            }

        }
    }
}
        
