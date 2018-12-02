# Description

Generates plausible queries from query logs

# Requirements

Require endpoints hosting the datas and matching query logs.

# Compile

Build using [Maven](http://maven.apache.org/)
`mvn build package`

# Execute

*Needs an interface to execute with arguments*
With Maven: `mvn exec:java -Dexec.mainClass="Test.TestPrunningF" -Dexec.args=""`
Pure Java: `java -cp target/PFed-1.0-SNAPSHOT.jar Test.TestPrunnigF`
