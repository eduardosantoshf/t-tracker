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
    
    var myCookie = getCookie("sessionKey-client");

    if (myCookie == null) {
        window.location.href='/login.html';
    }
    else {
        fetch("http://192.168.160.222:8081/client/verify", {headers: { 'Content-Type': 'application/json', 'Authorization': "Bearer " + getCookie("sessionKey-client") }, method: 'get'})
        .then(data => {
            if(data.status==200){
                data=data.text();

                Promise.all([data]).then(data => {
                    let resp = data[0];
                    if(resp!="SUCCESS"){
                        document.cookie = "sessionKey-client= ; expires = Thu, 01 Jan 1970 00:00:00 GMT"
                        window.location.href='login.html';
                    }
                })
            }else{
                document.cookie = "sessionKey-client= ; expires = Thu, 01 Jan 1970 00:00:00 GMT"
                window.location.href='login.html';
            }
        });
    }
}

function signupclient(){
    let username=$("#usernameTxt").val();
    let password=$("#passwordTxt").val();
    let name=$("#nameTxt").val();
    let email=$("#emailTxt").val();

    let client={"name":name, "email":email, "username":username, "password":password, "homeLocation": {"latitude": 0.0, "longitude": 0.0}};
    console.log(client);
    fetch('http://192.168.160.222:8081/client/signup', { headers: { 'Content-Type': 'application/json', 'Access-Control-Allow-Origin':'http://192.168.160.222:8081' }, method: 'post', body: JSON.stringify(client)}).then(data => {
        if(data.status==200)
            try{
                data.json();

                autoLogin(username, password);
            }catch(e){
                $("#authmessage").show();
            }
        else
            $("#authmessage").show();
    })
}

function autoLogin(username, password){
    fetch('http://192.168.160.222:8081/client/login?username='+username+"&password="+password).then(data => {
        if(data.status==200){
            data=data.json();

            Promise.all([data]).then(data => {
                document.cookie = "sessionKey-client="+data[0].token;
                window.location.href="/dashboard.html"
            })
        }else{
            alert("Wrong credentials");
            $("#passwordTxt").val("");
        }
    });
}

function loginclient(){
    let username=$("#usernameTxt").val();
    let password=$("#passwordTxt").val();
    autoLogin(username, password);
}