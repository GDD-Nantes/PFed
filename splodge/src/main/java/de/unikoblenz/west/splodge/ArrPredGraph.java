package de.unikoblenz.west.splodge;

import java.util.Arrays;
import java.util.Objects;
import java.lang.IllegalArgumentException;

public class ArrPredGraph {
    private long[] predicates;
    private long[] graphs;
    public ArrPredGraph(long[] pred, long[] gr){
        if(pred.length != gr.length) {
            throw new IllegalArgumentException("Arrays of predicates and graphs don't match");
        }
        predicates = new long[pred.length];
        graphs = new long[gr.length];
        for(int i=0; i<pred.length; ++i){
            predicates[i] = pred[i];
            graphs[i] = gr[i];
        }
    }
    
    public long[] getPredicates(){
        return predicates;
    }
    
    public long[] getGraphs(){
        return graphs;
    }
    
    public int getLength(){
        return predicates.length;
    }
    //Need checks
    public long getPredId( int i){
        return predicates[i];
    }
    
    public long getGraphId( int i){
        return graphs[i];
    }
    
    @Override
    public int hashCode(){
        return Objects.hash( Arrays.hashCode(predicates), Arrays.hashCode(graphs) );
    }
    
    @Override
    public boolean equals(Object o){
        if (o == null) return false;
        if (o == this) return true;
        if (!(o instanceof ArrPredGraph)) {
            return false;
        }

        ArrPredGraph other = (ArrPredGraph) o;
        
        if(predicates.length != other.getLength()){
            return false;
        }
        boolean res = true;
        int i = 0;
        while(res && i<predicates.length){
            res = predicates[i] == other.getPredId(i);
            res = res && graphs[i] == other.getGraphId(i);
            ++i;
        }
        return res;
    }
    
}
