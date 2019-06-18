var root = document.body
var fed = 0
var serverIP= "172.16.9.133"
var endpointAddr="localhost"
function Header(){
  return {
    view: function(vnode) {
      return m("nav",
               {class: "navbar navbar-dark bg-primary"}, [
               m("a", {
                 class: "navbar-brand mb-0 h1",
                 href: "#!/query"
               }, "PFed")
               ])
    }
  }
}

var queryData = function() {
  m.request({
    method: "GET",
    url: "http://"+serverIP+":8080/execQuery?",
    data: {query: editor.getValue(), endP: document.getElementById("endpointEditor").value},
  })
  .then(function(data) {
    //         document.getElementById("feds").value = JSON.stringify(data.Results);
    result.write(JSON.stringify(data));
    //         data.federation.forEach(function(x){ document.getElementById("feds").value +=decodeURIComponent(JSON.stringify(x).replace(/\+/g, '%20'));});
  })
}

function fedIt(){
  m.request({
    method: "GET",
    url: "http://"+serverIP+":8080/PFed?",
    data: {query: document.getElementById("queryToSend").value, endP: document.getElementById("endpoint").value},
  })
  .then(function(data) {
    HFvalues.parseResponse(data);
  })
}

var servDPQ = {
  dataset: "",
  predicate: "",
  arrQueries: [],
  currQueries: 0,
  
  parseResponse: function(jsonResp){
    this.dataset = jsonResp.dataset;
    this.predicate = jsonResp.predicate;
    this.arrQueries = jsonResp.queries;
    this.currQueries = 0;
  },
  nextQuer: function(){
    if(this.currQueries === this.arrQueries.length-1){
      this.currQueries=0;
    }else{
      this.currQueries+=1;
    }
  },
  prevQuer: function(){
    if(this.currQueries === 0){
      this.currQueries=this.arrQueries.length-1;
    }else{
      this.currQueries-=1;
    }
  },
  dispServ: function(){
    return [
    
    m("div",{class:"row"},[
      m("input",{type:"text", readonly:true, class:"form-control-plaintext", id:"CurrDataset", value:this.dataset})
    ]),
    m("div",{class:"row"},[
      m("div",{class:"col"},[
        m("textArea", {id: "queryService", class: "form-control"},decodeURIComponent(this.arrQueries[this.currQueries].replace(/\+/g, '%20'))),
      ]),
      m("div",{class:"col-md-auto"},[
        m("div",{class:"row justify-content-md-center"},[
          m("div",{class:"btn-group",role:"group"},[
            m("button", {
              class: "btn btn-outline-primary",
              onclick: function(){HFvalues.prevServ()}
            }, "<"),
            m("button", {
              class: "btn btn-primary",
              disabled:true,
              onclick: ()=>{}
            }, this.predicate),
            m("button", {
              class: "btn btn-outline-primary",
              onclick: function(){HFvalues.nextServ()}
            }, ">")
          ])
        ]),
        m("div",{class:"row justify-content-md-center"},[
          m("div",{class:"btn-group",role:"group"},[
            m("button", {
              class: "btn btn-outline-primary",
              onclick: function(){HFvalues.prevQuer()}
            }, "<"),
            m("button", {
              class: "btn btn-primary",
              disabled:true,
              onclick: fedIt
            }, (this.currQueries+1) + " / " + this.arrQueries.length),
            m("button", {
              class: "btn btn-outline-primary",
              onclick: function(){HFvalues.nextQuer()}
            }, ">")
          ])
        ])
      ])
    ]),
    
    ]
  }
}

var startForV = {
  vCurr: "",
  start: "",
  arrServ: [],
  currServ: 0,
  
  parseResponse: function(jsonResp){
    this.vCurr = jsonResp.va;
    this.start = jsonResp.start;
    this.currServ= 0;
    this.arrServ = [];
    jsonResp.service.forEach( (x)=>{
      var tmpkwr = Object.create(servDPQ);
      tmpkwr.parseResponse(x);
      this.arrServ.push(tmpkwr);
    })
  },
  nextServ: function(){
    if(this.currServ === this.arrServ.length-1){
      this.currServ=0;
    }else{
      this.currServ+=1;
    }
  },
  prevServ: function(){
    if(this.currServ === 0){
      this.currServ=this.arrServ.length-1;
    }else{
      this.currServ-=1;
    }
  },
  nextQuer: function(){
    if(this.arrServ.length === 0){
      return ;
    }else{
      this.arrServ[this.currServ].nextQuer();
    }
  },
  prevQuer: function(){
    if(this.arrServ.length === 0){
      return ;
    }else{
      this.arrServ[this.currServ].prevQuer();
    }
  },
  getStartQ: function(){
    return this.start;
  },
  getVar: function(){
    return this.vCurr;
  },
  dispServ: function(){
    if(this.arrServ.length >0){
      return this.arrServ[this.currServ].dispServ();
    }
  },
  dispVarSt: function(){
    return [m("div",[
            m("div",{class:"row"},[
              m("div",{class:"col"},[
                m("textArea", {id: "startQuery", class: "form-control"},decodeURIComponent(this.getStartQ().replace(/\+/g, '%20'))),
              ]),
              m("div",{class:"col-md-auto"},[
                m("div",{class:"btn-group",role:"group"},[
                  m("button", {
                    class: "btn btn-outline-primary",
                    onclick: function(){HFvalues.prevVar()}
                  }, "<"),
                  m("button", {
                    class: "btn btn-primary",
                    disabled:true,
                    onclick: ()=>{}
                  }, this.getVar()),
                    m("button", {
                      class: "btn btn-outline-primary",
                      onclick: function(){HFvalues.nextVar()}
                    }, ">")
                  ])
                ])
              ])
            ]),this.dispServ()]
  }
}

var HolderFull = {
  arrVar: [],
  currVar: 0,
  
  parseResponse: function(jsonResp){
    this.currVar = 0;
    this.arrVar = [];
    jsonResp.forEach( (x)=>{
      var tmpkwr = Object.create(startForV);
      tmpkwr.parseResponse(x);
      this.arrVar.push(tmpkwr);
    })
  },
  nextVar: function(){
    if(this.currVar === this.arrVar.length-1){
      this.currVar=0;
    }else{
      this.currVar+=1;
    }
  },
  prevVar: function(){
    if(this.currVar === 0){
      this.currVar=this.arrVar.length-1;
    }else{
      this.currVar-=1;
    }
  },
  nextServ: function(){
    if(this.arrVar.length === 0){
      return "";
    }else{
      return this.arrVar[this.currVar].nextServ();
    }
  },
  prevServ: function(){
    if(this.arrVar.length === 0){
      return "";
    }else{
      return this.arrVar[this.currVar].prevServ();
    }
  },
  nextQuer: function(){
    if(this.arrVar.length === 0){
      return "";
    }else{
      return this.arrVar[this.currVar].nextQuer();
    }
  },
  prevQuer: function(){
    if(this.arrVar.length === 0){
      return "";
    }else{
      return this.arrVar[this.currVar].prevQuer();
    }
  },
  dispVarSt: function(){
    if(this.arrVar[this.currVar] == undefined){
      return null;
    }else{
      return this.arrVar[this.currVar].dispVarSt();
    }
  },
  getStartQ: function(){
    if(this.arrVar.length === 0){
      return "";
    }else{
      return this.arrVar[this.currVar].getStartQ();
    }
  },
  getVar: function(){
    if(this.arrVar.length === 0){
      return "";
    }else{
      return this.arrVar[this.currVar].getVar();
    }
  },
  view: function(vnode){
    return  m("div",{id:"fedSelector"},[
              m("h3", {
                class: "title"
              }, "Resulting federated queries"),
              this.dispVarSt(),
              m("button", {
                class: "btn btn-primary",
                onclick: function(){
                  editor.setValue("SELECT * {\n"+document.getElementById("startQuery").value + "SERVICE <"+document.getElementById("CurrDataset").value+">\n"+document.getElementById("queryService").value+"\n}"); document.getElementById("endpointEditor").value = document.getElementById("endpoint").value
                }
              }, "Edit federated query"),
            ])
  }
}


var HFvalues = Object.create(HolderFull);
HFvalues.parseResponse([]);

var editor = {
    yasqe:{},
    
    init: function(elt){yasqe=YASQE(document.getElementById(elt));},
    getValue: function(){ return yasqe.getValue(); },
    setValue: function(q){yasqe.setValue(q);}
}

var result = {
    yasr:{},
    place: "",
    
    init: function(elt){place=elt;yasr=YASR(document.getElementById(place));},
    write: function(resp){ 
        document.getElementById(place).class="visible";
        return yasr.setResponse({response:resp, contentType: "application/sparql-results+json"})
    }
}

function QueryEditor(){    
    return {
        oninit: function(vnode){editor.init("showcase"); result.init("Yres")},
        view: function(vnode){return null}
    }
}

var Query = {
    view: function() {
      return m("div",[
          m(Header),
        m("div",{class:"container"},[
          m("div", {class: "col"},[
            m("div",{id:"fedFinder"},[
              m("h3", {
                class: "title"
              }, "Find Stars across datasets"),
              m("div",{class:"form-check form-check-inline"},[
                m("input",{class:"form-check-input",type:"radio",name:"queryChoice", id:"queryChoiceQ",value:"choiceQ", checked:true}),
                m("label",{class:"form-check-label",for:"queryChoiceQ"},"SPARQL query")
              ]),
//               m("div",{class:"form-check form-check-inline"},[
//                 m("input",{class:"form-check-input",type:"radio",name:"queryChoice", id:"queryChoiceP",value:"choiceP"}),
//                 m("label",{class:"form-check-label",for:"queryChoiceP"},"Simple predicate")
//               ]),
              m("select",{id:"endpoint", class: "custom-select", placeholder: "Endpoint URL"},[
                m("option", {value: "http://"+endpointAddr+":3030/dbpedia/sparql", id:"http://"+endpointAddr+":3030/dbpedia/sparql"}, 'DBpedia'),
                m("option", {value: "http://"+endpointAddr+":3030/swdf/sparql", id:"http://"+endpointAddr+":3030/swdf/sparql"}, 'Semantic Web DogFood')
              ]),
              m("textArea", {id: "queryToSend", class: "form-control"},""), 
              m("button", {
                  class: "btn btn-primary",
                  onclick: fedIt
              }, "Federate it")
            ]),
            m(HFvalues),
            m("div",{id:"queryEditor"},[
              m("h3", {
                class: "title"
              }, "Query editor"),
              m("div",{id: "showcase"}),
              m("select",{id:"endpointEditor", class: "custom-select", placeholder: "Endpoint URL"},[
                m("option", {value: "http://"+endpointAddr+":3030/dbpedia/sparql", id:"http://"+endpointAddr+":3030/dbpedia/sparql"}, 'DBpedia'),
                m("option", {value: "http://"+endpointAddr+":3030/swdf/sparql", id:"http://"+endpointAddr+":3030/swdf/sparql"}, 'Semantic Web DogFood')
              
              ]),
              m("div", {class: "btn-group" },[ 
                  m("button", {
                      type: "button",
                  class: "btn btn-primary",
                  onclick: queryData
                  }, "Execute")
              ]),
              m("div", {id: "Yres"})
            ])
          ])
        ]),
        m(QueryEditor)
      ])
    }
}

m.mount(root,Query);

