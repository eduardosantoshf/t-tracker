function checkToken(){
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
    
    var myCookie = getCookie("sessionKey-rider");

    if (myCookie == null) {
        window.location.href='/login.html';
    }
    else {
        fetch("http://localhost:8080/rider/verify", {headers: { 'Content-Type': 'application/json', 'Authorization': "Bearer " + getCookie("sessionKey-rider") }, method: 'get'}).then(data => data.text()).then(data => {
            if(data!="SUCCESS"){
                document.cookie = "sessionKey-rider= ; expires = Thu, 01 Jan 1970 00:00:00 GMT"
                window.location.href='login.html';
            }
        })
    }
}

function getStats(){

    fetch('http://localhost:8080/admin/stats', { headers: { 'Content-Type': 'application/json', 'Authorization': "Bearer " + getCookie("sessionKey-rider") }, method: 'get'}).then(data => {

        if(data.status==200)
            try{
                dados = data.json();

                Promise.all([dados]).then(dados => {
                    dados[0]["riders"].forEach(element => {
                        $("#table_riders").find('tbody').append("<tr><th scope=\"row\">" + element["id"] + "</th><td>" + element["name"] 
                                                                + "</td><td>" + element["email"] + "</td><td>" + element["latitude"] + "</td><td>" + 
                                                                element["longitude"] + "</td><td>" + element["phoneNumber"] + "</td></tr>");
                    });
                    dados[0]["stores"].forEach(element => {
                        $("#table_stores").find('tbody').append("<tr><th scope=\"row\">" + element["id"] + "</th><td>" + element["name"] 
                                                                + "</td><td>" + element["ownerName"] + "</td><td>" + 
                                                                element["latitude"] + "</td><td>" + element["longitude"] + "</td></tr>");
                    });
                    dados[0]["deliveries"].forEach(element => {
                        $("#table_deliveries").find('tbody').append("<tr><th scope=\"row\">" + element["id"] + "</th><td>" + element["name"] 
                                                                + "</td><td>" + element["deliveryLatitude"] + "</td><td>" + 
                                                                element["deliveryLongitude"] + "</td><td>" + element["commission"] + "</td></tr>");
                    });
                
                })
                
                //data.data.forEach(p=>{
                //    console.log(p);
                //});
                //autoLogin(riderUsername, riderPassword);
            }catch(e){
                //$("#authmessage").show();
            }
            //$("#authmessage").show();
    })
}