function getAllOrders(){
    fetch('http://localhost:8081/order/all', {headers: { 'Content-Type': 'application/json', 'Authorization': "Bearer " + getCookie("sessionKey-client")  }, method: 'post', body:corpo}).then(data => {
        if(data.status==200){
            data=data.json();

            Promise.all([data]).then(data => {
                obj=data[0]
                console.log(obj);
                
                for(i=0; i<obj.length; i++){
                    
                    let tr=document.createElement("tr");

                    let td_id=document.createElement("td");
                    td_id.innerText="1";

                    let td_name=document.createElement("td");
                    td_name.innerText="Split Text";

                    let td_status=document.createElement("td");

                    let div_status=document.createElement("div");
                    div_status.innerHTML="Pending";
                    div_status.className="";
                    td_status.appendChild(div_status);

                    let td_preco=document.createElement("td");
                    
                    tr.appendChild(td_id);
                    tr.appendChild(td_name);
                    tr.appendChild(td_preco);
                    tr.appendChild(td_status);

                    document.getElementById("listOrders").appendChild(tr);
                }
            })
        }else{
            alert("Order not placed");
            $("#passwordTxt").val("");
        }
    });
}

function getCookie(name) {
    var dc = document.cookie;
    var prefix = name + "=";
    var begin = dc.indexOf("; " + prefix);
    if (begin == -1) {
        begin = dc.indexOf(prefix);
        if (begin != 0) return null;
    }
    else
    {
        begin += 2;
        var end = document.cookie.indexOf(";", begin);
        if (end == -1) {
        end = dc.length;
        }
    }
    // because unescape has been deprecated, replaced with decodeURI
    //return unescape(dc.substring(begin + prefix.length, end));
    return decodeURI(dc.substring(begin + prefix.length, end));
}