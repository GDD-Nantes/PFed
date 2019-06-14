package PFSQGen;

import java.util.Objects;

public class PredOnEnd{
  private int endpoint;
  private String predicate;
  
  public PredOnEnd(String p, int e){
    predicate = p;
    endpoint = e;
  }
  
  public int getEndpoint(){
    return endpoint;
  }
  
  public String getPredicate(){
    return predicate;
  }
  
  public int hashCode(){
    return Objects.hash(endpoint,predicate);
  }
  
  public boolean equals(Object o){
    if (o == null) return false;
    if (o == this) return true;
    if (!(o instanceof PredOnEnd)) {
      return false;
    }

    PredOnEnd other = (PredOnEnd) o;
    return endpoint == other.getEndpoint() && predicate.equals(other.getPredicate());
  }
}  
