package PFSQGen;

import java.util.Set;
import java.util.HashSet;

public abstract class Summary {

    protected Set<Capability> capabilities = new HashSet<Capability>();

    public Set<Capability> getCapabilities() {
        return capabilities;
    }
    
    public Set<String> getSbjSet (String predicate) {
        for (Capability testMatch : this.getCapabilities()) {
            if (testMatch.getPredicate().equals(predicate)) {
                return testMatch.getSbjAuthority();
            }
        }
        return null;
    }
    
    public Set<String> getObjSet (String predicate) {
        for (Capability testMatch : this.getCapabilities()) {
            if (testMatch.getPredicate().equals(predicate)) {
                return testMatch.getObjAuthority();
            }
        }
        return null;
    }
    

    @Override
    public String toString() {
        return "Summaries [capabilities=" + capabilities + "]";
    }

}
