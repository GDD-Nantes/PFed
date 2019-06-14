package PFSQGen;


public class ServerProp {

  private String endpoint;
  private String summary;
  private String logs;

  public ServerProp(String e, String s, String l){
    endpoint = e;
    summary = s;
    logs = l;
  }
  
  public ServerProp(){
    endpoint = null;
    summary = null;
    logs = null;
  }
  
  public void setEndpoint(String e){
    endpoint = e;
  }
  
  public void setSummary(String s){
    summary = s;
  }
  
  public void setLogs(String l){
    logs = l;
  }
  
  public String getEndpoint(){
    return endpoint;
  }
  
  public String getSummary(){
    return summary;
  }
  
  public String getLogs(){
    return logs;
  }
  
  public String toString(){
    return "endpoint: "+endpoint+", summary: "+summary+", logs: "+logs;
  }
}
