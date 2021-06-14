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
    switch(productId) {
        case "1":
            tipo="Molecular";
            nome="Split Test";
            preco="$49.00";
            image="assets/images/product/split.png";
            break;
        case "2":
            tipo="Molecular";
            nome="Throat Test";
            preco="$129.00";
            image="assets/images/product/throat.png";
            break;
        case "3":
            tipo="Molecular";
            nome="Nose Test";
            preco="$89.00";
            image="assets/images/product/nose.png";
            break;
        case "4":
            tipo="Antigen";
            nome="Nose Test";
            preco="$59.00";
            image="assets/images/product/nose.png";
            break;
        case "5":
            tipo="Antigen";
            nome="Throat Test";
            preco="$59.00";
            image="assets/images/product/throat.png";
            break;
        case "6":
            tipo="Antibody";
            nome="Take Blood";
            preco="$79.00";
            image="assets/images/product/tirarsange.png";
            break;
        case "7":
            tipo="Antibody";
            nome="Take Blood";
            preco="$89.00";
            image="assets/images/product/fingerblood.png";
            break;
        default:
            window.location.href='index.html';
    }
    //console.log(nome);

    $("#url_mapping_product").text(nome);
    $("#productImg").attr("src",image);
    $("#productName").text(nome);
    $("#productPrice").text(preco);
    $("#productDesc").text(desc);
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
    if(checkToken("sessionKey")){
        // TODO place an order
    }else{
        $("#modalLogin").css({"display":"block"});
    }
}

function closeModal(){
    $("#modalLogin").css({"display":"none"});
}