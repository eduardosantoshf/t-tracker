function getAllOrders(){
    fetch('http://192.168.160.222:8081/client/orders', {headers: { 'Content-Type': 'application/json', 'Authorization': "Bearer " + getCookie("sessionKey-client")  }, method: 'get'}).then(data => {
        if(data.status==200){
            data=data.json();

            Promise.all([data]).then(data => {
                obj=data[0]
                console.log(obj);
                
                for(i=0; i<obj.length; i++){
                    const o=obj[i];
                    let orderName=o.products[0].product.name;
                    if(o.products.length>1){
                        orderName+=", and " + (o.products.length-1) + " more";
                    }
                    
                    let tr=document.createElement("tr");

                    let td_id=document.createElement("td");
                    td_id.innerText=o.id;

                    let td_name=document.createElement("td");
                    td_name.innerText=orderName;

                    let td_status=document.createElement("td");

                    let div_status=document.createElement("div");
                    div_status.innerText=o.status;
                    div_status.className="status"+o.status;
                    td_status.appendChild(div_status);

                    let td_preco=document.createElement("td");
                    td_preco.innerText=o.totalPrice+"$";
                    
                    tr.appendChild(td_id);
                    tr.appendChild(td_name);
                    tr.appendChild(td_status);
                    tr.appendChild(td_preco);

                    document.getElementById("listOrders").appendChild(tr);
                }
            })
        }else{
            $("#getOrdersError").css({"display":"block"});
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