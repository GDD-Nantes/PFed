# Description

Generates plausible queries from query logs using summaries.

PFed uses one configuration per datasets detailing its log, summary and an endpoint. The project uses files used for PAPER as examples.

Configurations are detailed in the configs file.

To generate summaries, a tool was extracted from [HiBISCuS](https://code.google.com/archive/p/hibiscusfederation/). It is available [here](https://github.com/GDD-Nantes/PFed/tree/master/genSummarieHibi)

PFed currently supports execution on Fuseki and Sage endpoints. If your endpoint require another clients, you can create a new class implementing [ExecutionStrategy](https://github.com/GDD-Nantes/PFed/blob/master/GenFedQuerie/src/main/java/PFSQGen/ExecutionStrategy.java) and set it in the main.

# Compile

Build using [Maven](http://maven.apache.org/)
`mvn clean package`

# Execute

`java -jar target/PFed.jar [-h] -A <firstConfigFile> -B <secondConfigFile> -g <joinType> [-m] [-o <outputDir>] [--sage] [--noExec] [--withType]`


 `-A,--configA <arg>`    Configuration file for the dataset A.
 
 `-B,--configB <arg>`    Configuration file for the dataset B.
 
 `-g,--generate <arg>`   Type of generation. Can be STAR, PATH_AB or PATH_BA.
 
 `-m,--max`              Set the generation for max queries.
 
 `-o,--output <arg>`     Output folder for queries generated. Defaults to results at the root of the project.
 
 `-sage,--sage`          Uses Sage client instead of Fuseki to execute queries.

 `-noExec,--noExec`      Writes the queries instead of executing them first. Have priorities over execution arguments.

 `-t,--withType`         Also check the types of predicates before joining them.
 
# Examples
 
Generate all min path between SWDF 2012 and DBpedia 3.5.1, with results on a Fuseki endpoint:

`java -jar target/PFed.jar -A configs/configSWDF.conf -B configs/configDBpedia.conf -g PATH_AB`

Generate all max star between SWDF 2012 and DBpedia 3.5.1, without execution and using Type summaries:

`java -jar target/PFed.jar -A configs/configSWDF.conf -B configs/configDBpedia.conf -g STAR -t -m --noExec`
