function loadProduct(){
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    const productId = urlParams.get('i'); // product id
    console.log(productId);
    var nome="";
    var tipo="";
    var preco="";
    var image="";
    var desc="Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus venenatis consectetur nibh et tincidunt. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; In ac turpis neque. Quisque ut laoreet ante. Duis eleifend elementum magna nec suscipit. Duis pretium, tortor id faucibus fermentum, dolor leo ullamcorper massa, non iaculis orci ex sit amet lacus. Fusce non convallis urna. Suspendisse fermentum orci ac maximus congue.";
    
    fetch("http://192.168.160.222:8081/product/all", {headers: { 'Content-Type': 'application/json' }, method: 'get'}).then(data => data.json()).then(data => {
        console.log(data);

        for(i=0; i<data.length; i++){
            obj=data[i];
            
            if(obj.id==productId){
                nome=obj.name;
                tipo=obj.type;
                price=obj.price;
                image=obj.foto; 
                desc=obj.description;
            }
        }
    }).then(() => {
        $("#url_mapping_product").text(nome);
        $("#productImg").attr("src",image);
        $("#productName").text(nome);
        $("#productPrice").text(preco);
        $("#productDesc").text(desc);
    });
}

function checkToken(name){
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
    
    var myCookie = getCookie(name);

    if (myCookie == null) {
        return false;
    }
    
    return true;
}

function buy(){
    if(checkToken("sessionKey-client")){
        // TODO place an order
    }else{
        $("#modalLogin").css({"display":"block"});
    }
}

function closeModal(){
    $("#modalLogin").css({"display":"none"});
}