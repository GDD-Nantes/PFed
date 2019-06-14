package PFSQGen;

import java.util.List;
import java.util.ArrayList;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
// import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application.yml")
@ConfigurationProperties("enabledserver")
public class EnabledServer {

  private List<ServerProp> servlist = new ArrayList<>();

  public List<ServerProp> getServlist() {
    return this.servlist;
  }
  
  public void setServlist(List<ServerProp> servlist){
    this.servlist = servlist;
  }
  
  public ServerProp getPropByName(String n){
    for(ServerProp sp : servlist){
      if(sp.getEndpoint().equals(n)){
        return sp;
      }
    }
    return null;
  }
  public String toString(){
    return servlist.toString();
  }
}
