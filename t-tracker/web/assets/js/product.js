function loadProduct(){
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    const productId = urlParams.get('i'); // product id
    
    var nome="";
    var tipo="";
    var preco="";
    var image="";
    var desc="Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus venenatis consectetur nibh et tincidunt. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; In ac turpis neque. Quisque ut laoreet ante. Duis eleifend elementum magna nec suscipit. Duis pretium, tortor id faucibus fermentum, dolor leo ullamcorper massa, non iaculis orci ex sit amet lacus. Fusce non convallis urna. Suspendisse fermentum orci ac maximus congue.";
    
    fetch("http://localhost:8081/product/all", {headers: { 'Content-Type': 'application/json' }, method: 'get'}).then(data => data.json()).then(data => {
        console.log(data);

        for(i=0; i<data.length; i++){
            obj=data[i];
            
            if(obj.id==productId){
                nome=obj.name;
                tipo=obj.type;
                preco=obj.price;
                image=obj.foto; 
                desc=obj.description;
            }
        }
    }).then(() => {
        $("#url_mapping_product").text(nome);
        $("#productImg").attr("src",image);
        $("#productName").text(nome);
        $("#productPrice").text("$"+preco+" ");
        $("#productDesc").text(desc);

        document.getElementById("comprarAgoraBtn").addEventListener("click", () => buy(productId));
        document.getElementById("clientLoginBtn").addEventListener("click", () => loginclient(productId));
    });
}

function getCookie(name) {
    var dc = document.cookie;
    var prefix = name + "=";
    var begin = dc.indexOf("; " + prefix);
    if (begin == -1) {
        begin = dc.indexOf(prefix);
        if (begin != 0) return null;
    }else{
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

function checkToken(name){
    var myCookie = getCookie(name);

    if (myCookie == null) {
        return false;
    }else{
        fetch("http://localhost:8081/client/verify", {headers: { 'Content-Type': 'application/json', 'Authorization': "Bearer " + getCookie("sessionKey-client") }, method: 'get'}).then(data => data.text()).then(data => {
            if(data!="SUCCESS"){
                document.cookie = "sessionKey-client= ; expires = Thu, 01 Jan 1970 00:00:00 GMT"
                return false;
            }
        })
    }
    
    return true;
}

function buy(product_id){
    if(checkToken("sessionKey-client")){
        let quantity = $("#productQuantity").val();
        let corpo=[{"productId":parseInt(product_id), "quantity":parseInt(quantity)}]
        
        fetch('http://localhost:8081/order', {headers: { 'Content-Type': 'application/json', 'Authorization': "Bearer " + getCookie("sessionKey-client")  }, method: 'post', body:JSON.stringify(corpo)}).then(data => {
            if(data.status==200){
                data=data.json();

                Promise.all([data]).then(data => {
                    let resp = data[0];
                    console.log(resp);
                    if(resp.hasOwnProperty("id")){
                        window.location.href='dashboard.html';
                    }
                })
            }else{
                alert("Order not placed");
            }
        });
    }else{
        $("#modalLogin").css({"display":"block"});
    }
}

function closeModal(){
    $("#modalLogin").css({"display":"none"});
}

function loginclient(product_id){
    let username=$("#usernameTxt").val();
    let password=$("#passwordTxt").val();
    
    fetch('http://localhost:8081/client/login?username='+username+"&password="+password).then(data => {
        if(data.status==200){
            data=data.json();

            Promise.all([data]).then(data => {
                document.cookie = "sessionKey-client="+data[0].token;
                
                buy(product_id);
            })
        }else{
            alert("Wrong credentials");
            $("#passwordTxt").val("");
        }
    });
}