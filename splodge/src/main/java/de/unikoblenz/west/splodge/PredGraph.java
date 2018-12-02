package de.unikoblenz.west.splodge;

import java.util.Objects;

public class PredGraph implements Comparable<PredGraph>{
    private long predicate;
    private long graph;
    public PredGraph(long pred, long gr){
        predicate = pred;
        graph = gr;
    }
    
    public long getPredicate(){
        return predicate;
    }
    
    public long getGraph(){
        return graph;
    }
    
    public String toString() {
        return "{ " + predicate + ", " + graph + " }";
    }
    
    @Override
    public int hashCode(){
        return Objects.hash( predicate, graph );
    }
    
    @Override
    public boolean equals(Object o){
        if (o == null) return false;
        if (o == this) return true;
        if (!(o instanceof PredGraph)) {
            return false;
        }

        PredGraph other = (PredGraph) o;
        
        return predicate==other.getPredicate() && graph == other.getGraph();
    }
    
    public int compareTo(PredGraph other){
        long res = graph - other.getGraph();
        if( res == 0){
            res = predicate - other.getPredicate();
        }
        if(res < 0){
            return -1;
        }else if(res > 0){
            return 1;
        }else{
            return 0;
        }
    }
    
}
