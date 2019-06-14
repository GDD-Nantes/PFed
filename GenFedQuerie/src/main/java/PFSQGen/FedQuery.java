package PFSQGen;

import java.util.Set;
import java.util.HashSet;

public class FedQuery{
  private Set<PredOnEnd> fed;
  
  
  public FedQuery(){
    fed = new HashSet<>();
  }
  public void addPredOnEnd(String pred, int endp){
    fed.add(new PredOnEnd(pred,endp));
  }
  
  public int hashCode(){
    return fed.hashCode();
  }
  
  public Set<PredOnEnd> getFed(){
    return fed;
  }
  public boolean equals(Object o){
    if (o == null) return false;
    if (o == this) return true;
    if (!(o instanceof FedQuery)) {
      return false;
    }

    FedQuery other = (FedQuery) o;
    return fed.equals(other.getFed());
  }
}
