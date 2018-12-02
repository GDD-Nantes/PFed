package PFed;

import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

public class Capability {
    //HashMap Stringn Authority?
    private String predicates;
    private Set<String> sbjAuthority;
    private Set<String> objAuthority;

    public Capability(String predicate, Set<String> sbjAuthority, Set<String> objAuthority) {
        this.predicates = predicate;
        this.sbjAuthority = sbjAuthority;
        this.objAuthority = objAuthority;
    }
    
    public Capability(String pred){
        this.predicates = pred;
        this.sbjAuthority = new HashSet<String>();
        this.objAuthority = new HashSet<String>();
    }
    
    public Capability() {
        this.predicates = "";
        this.sbjAuthority = new HashSet<String>();
        this.objAuthority = new HashSet<String>();

    }

    public String getPredicate() {
        return predicates;
    }

    public void setPredicate(String predicate) {
        this.predicates = predicate;
    }
    
    public Set<String> getSbjAuthority() {
        return sbjAuthority;
    }

    public void setSbjAuthority(Set<String> sbjAuthority) {
        this.sbjAuthority = sbjAuthority;
    }

    public Set<String> getObjAuthority() {
        return objAuthority;
    }

    public void setObjAuthority(Set<String> objAuthority) {
        this.objAuthority = objAuthority;
    }

    public void addSbjAuthority(String sbjAuthority) {
        this.sbjAuthority.add(sbjAuthority);
    }

    public void addObjAuthority(String objAuthority) {
        this.objAuthority.add(objAuthority);
    }

    @Override
    public String toString() {
        return "Capability [predicate=" + predicates + ", sbjAuthority=" + sbjAuthority + ", objAuthority="
                + objAuthority + "]";
    }
    @Override
    public boolean equals(Object o){
        if (o == null) return false;
        if (o == this) return true;
        if (!(o instanceof Capability)) {
            return false;
        }

        Capability other = (Capability) o;
        return predicates.equals(other.getPredicate()) && sbjAuthority.containsAll(other.getSbjAuthority()) && objAuthority.containsAll(other.getObjAuthority() );
    }
    @Override
    public int hashCode(){
        return Objects.hash(predicates, sbjAuthority, objAuthority);
    }

}
