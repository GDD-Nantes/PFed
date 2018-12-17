package PFSQGen;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import java.util.Properties;
import java.io.FileReader;
import java.util.ArrayList;

public class PFed{
    public enum GenerationChoice {
        STAR, PATH_AB, PATH_BA;
    }
    public static void main(String[] args) throws Exception {
        Options options = PFed.createOpt();
        if(args.length == 0){
            PFed.printUsage(options);
            return;
        }
        CommandLine cline = new DefaultParser().parse(options, args);
        Properties confA = new Properties();
        confA.load(new FileReader(cline.getOptionValue("A")));
        
        Properties confB = new Properties();
        confB.load(new FileReader(cline.getOptionValue("B")));
        
        GenerationChoice genChoice = GenerationChoice.valueOf(cline.getOptionValue("g"));
        
        ArrayList<String> sumFileList = new ArrayList<String>();
        PruningLogs PLpath;
        String endpoint1, endpoint2;
        boolean isStar, isRev;
        switch(genChoice){
            case STAR:
                isStar = true;
                isRev = false;
                break;
            case PATH_AB:
                isStar = false;
                isRev = false;
                break;
            case PATH_BA:
                isStar = false;
                isRev = true;
                break;
            default:
                System.out.println("Generation choice " + genChoice + " is not supported.");
                return;
        }
        System.out.println("Reading Query logs and Summaries ...");
        if(isRev){
            sumFileList.add(checkConfig(confB, "summaryFile"));
            sumFileList.add(checkConfig(confA, "summaryFile"));
            endpoint1 = checkConfig(confB, "endpointURL");
            endpoint2 = checkConfig(confA, "endpointURL");
            
            PLpath = new PruningLogs(checkConfig(confB, "logFile"), checkConfig(confA, "logFile"), sumFileList, "nonCypte");
        }else{
            sumFileList.add(checkConfig(confA, "summaryFile"));
            sumFileList.add(checkConfig(confB, "summaryFile"));
            endpoint1 = checkConfig(confA, "endpointURL");
            endpoint2 = checkConfig(confB, "endpointURL");
            
            PLpath = new PruningLogs(checkConfig(confA, "logFile"), checkConfig(confB, "logFile"), sumFileList, "nonCypte");
        }
        System.out.println("Prunning non joinable predicates ...");
        if(!cline.hasOption("t")){
            if(isStar){
                PLpath.pruningPredicatesLogs( "star");
            }else{
                PLpath.pruningPredicatesLogs("path");
            }
        }else{
            ArrayList<String> sumFileListType = new ArrayList<String>();
            sumFileListType.add(checkConfig(confA, "summaryTypeFile"));
            sumFileListType.add(checkConfig(confB, "summaryTypeFile"));
            if(isStar){
                PLpath.pruningPredicatesLogsType(sumFileListType, "star");
            }else{
                PLpath.pruningPredicatesLogsType(sumFileListType, "path");
            }
        }
        
        String outputDir;
        if(cline.hasOption("o")){
            outputDir = cline.getOptionValue("o");
            if(!outputDir.substring(outputDir.length() -1).equals("/")){
                outputDir += "/";
            }
        }else{
            outputDir = "results/";
        }
        
        if(cline.hasOption("sage"))
            PLpath.setExecutor(new SageExecution());
        
        if(cline.hasOption("noExec"))
            PLpath.setExecutor(new SaveExecution(outputDir+"unexecutedQueries.txt"));
        
        if(cline.hasOption("m")){
            if(isStar){
                System.out.println();                                                                                                     
                System.out.println("----------------------------------------------");
                System.out.println();
                System.out.println("Generated federated queries Maximal Star between " + checkConfig(confA, "name") + " and "+checkConfig(confB, "name") +" :");
                System.out.println();
                System.out.println("----------------------------------------------");
                PLpath.genFedMaximal(endpoint1, endpoint2, outputDir+"genFedMaximalStarFile.txt", "star");
            }else if(isRev){
                System.out.println();                                                                                                         
                System.out.println("----------------------------------------------");
                System.out.println();
                System.out.println("Generated federated queries Maximal Path from " + checkConfig(confB, "name") + " to "+checkConfig(confA, "name") +" :");
                System.out.println();
                System.out.println("----------------------------------------------");
                PLpath.genFedMaximal(endpoint2, endpoint1, outputDir+"genFedMaximalPathFile.txt", "path");
            }else{
                System.out.println();                                                                                                     
                System.out.println("----------------------------------------------");
                System.out.println();
                System.out.println("Generated federated queries Maximal Path from " + checkConfig(confA, "name") + " to "+checkConfig(confB, "name") +" :");
                System.out.println();
                System.out.println("----------------------------------------------");
                PLpath.genFedMaximal(endpoint1, endpoint2, outputDir+"genFedMaximalPathFile.txt", "path");
            }
        }else{
            if(isStar){
                System.out.println();                                                                                                     
                System.out.println("----------------------------------------------");
                System.out.println();
                System.out.println("Generated federated queries Minimal Star between " + checkConfig(confA, "name") + " and "+checkConfig(confB, "name") +" :");
                System.out.println();
                System.out.println("----------------------------------------------");
                PLpath.genFedMinimalStarService(endpoint1, endpoint2, outputDir+"genFedMinimalStarFile.txt");
            }else if(isRev){
                System.out.println();                                                                                                         
                System.out.println("----------------------------------------------");
                System.out.println();
                System.out.println("Generated federated queries Minimal Path from " + checkConfig(confB, "name") + " to "+checkConfig(confA, "name") +" :");
                System.out.println();
                System.out.println("----------------------------------------------");
                PLpath.genFedMinimalPathService(endpoint2, endpoint1, outputDir+"genFedMinimalPathFile.txt");
            }else{
                System.out.println();                                                                                                     
                System.out.println("----------------------------------------------");
                System.out.println();
                System.out.println("Generated federated queries Minimal Path from " + checkConfig(confA, "name") + " to "+checkConfig(confB, "name") +" :");
                System.out.println();
                System.out.println("----------------------------------------------");
                PLpath.genFedMinimalPathService(endpoint1, endpoint2, outputDir+"genFedMinimalPathFile.txt");
            }
        }
    }
    
    private static String checkConfig(Properties prop,String key) throws ParseException{
        String testConf = prop.getProperty(key);
        if(testConf == null){
            throw new ParseException("Bad configuration for "+prop.getProperty("name")+", "+key+" not found");
        }
        return testConf;
    }
    private static Options createOpt() {
        Option confA = Option.builder("A")
                        .required(true)
                        .longOpt("configA")
                        .desc("Configuration file for the dataset A.")
                        .hasArg()
                        .build();
        Option confB = Option.builder("B")
                        .required(true)
                        .longOpt("configB")
                        .desc("Configuration file for the dataset B.")
                        .hasArg()
                        .build();
        Option genChoice = Option.builder("g")
                        .required(true)
                        .longOpt("generate")
                        .desc("Type of generation. Can be STAR, PATH_AB or PATH_BA.")
                        .hasArg()
                        .build();
        Option max = Option.builder("m")
                        .required(false)
                        .longOpt("max")
                        .desc("Set the generation for max queries.")
                        .build();
        Option output = Option.builder("o")
                        .required(false)
                        .longOpt("output")
                        .desc("Output folder for queries generated. Defaults to results at the root of the project.")
                        .hasArg()
                        .build();
        Option sage = Option.builder("sage")
                        .required(false)
                        .longOpt("sage")
                        .desc("Uses Sage client instead of Fuseki to execute queries.")
                        .build();
        Option noExec = Option.builder("noExec")
                        .required(false)
                        .longOpt("noExec")
                        .desc("Writes the queries instead of executing them first. Have priorities over execution arguments.")
                        .build();
        Option withType = Option.builder("t")
                        .required(false)
                        .longOpt("withType")
                        .desc("Also check the types of predicates before joining them.")
                        .build();
                        
        Options options = new Options();
        options.addOption(confA);
        options.addOption(confB);
        options.addOption(genChoice);
        options.addOption(max);
        options.addOption(output);
        options.addOption(sage);
        options.addOption(noExec);
        options.addOption(withType);
        return options;
    }
    //From Splodge
    protected static void printUsage(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(
                "java " + PFed.class.getName()
                        + " [-h] -A <firstConfigFile> -B <secondConfigFile> -g <joinType> [-m] [-o <outputDir>] [--sage]",
                options);
    }
}
