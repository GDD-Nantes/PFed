# PFed Recommending Plausible Federated Queries 

# Source Code:
- GenFedQueries

# Summaries Code
- Hibiscus Code available at: [genSummarieHibi]

# Recommeded Queries
-  PFed/federatedQueries/

# Recommeded queries execution 
- To facilitate the execution of federated queries, we host  SWDF and DBpediain  Sage server 
 http://sage.univ-nantes.fr. 
 
 - Example: Choose "swdf-2012" in available datasets, and execute the following query:
 

 PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
 
 PREFIX foaf: <http://xmlns.com/foaf/0.1/>
 
 PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
 
 PREFIX dbp: <http://dbpedia.org/property/>

PREFIX geo: <http://www.w3.org/2003/01/geo/wgs84_pos#>

 SELECT * WHERE { 
 
     ?inst rdf:type ?dClass .
     
      ?inst foaf:based_near ?place .    
  SERVICE <http://sage.univ-nantes.fr/sparql/dbpedia-3-5-1>
  
    { ?place rdfs:label "United Kingdom"@en . 
      ?place dbp:capital ?capital . 
      
      ?capital geo:lat ?lat .
      
      ?capital geo:long ?long }}
 
 # Prefixes of Queries
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>

PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>

PREFIX foaf: <http://xmlns.com/foaf/0.1/>

PREFIX owl: <http://www.w3.org/2002/07/owl#>

PREFIX swc: <http://data.semanticweb.org/ns/swc/ontology#>

PREFIX swrc: <http://swrc.ontoware.org/ontology#>

PREFIX skos: <http://www.w3.org/2004/02/skos/core#>

PREFIX dbpedia: <http://dbpedia.org/resource/>

PREFIX dbp: <http://dbpedia.org/property/>

PREFIX dbo: <http://dbpedia.org/ontology/>

PREFIX geo: <http://www.w3.org/2003/01/geo/wgs84_pos#>
