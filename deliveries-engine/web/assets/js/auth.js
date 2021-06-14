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
    
    var myCookie = getCookie("sessionKey");

    if (myCookie == null) {
        window.location.href='/login.html';
    }
    else {
        // do cookie-exists stuff
    }
}

function signuprider(){
    var riderName=$("#riderName").val();
    var riderEmail=$("#riderEmail").val();
    var riderUsername=$("#riderUsername").val();
    var riderPassword=$("#riderPassword").val();
    var riderPhone=$("#riderPhone").val();
    var riderAddress=$("#riderAddress").val();
    var riderCEP=$("#riderCEP").val();
    var riderCity=$("#riderCity").val();

    var rider={"name":riderName, "email":riderEmail, "username":riderUsername, "password":riderPassword, "phoneNumber":riderPhone, "address":riderAddress, "zipCode":riderCEP, "city":riderCity};
    //fetch('http://192.168.160.222:8080/rider/signup', { headers: { 'Content-Type': 'application/json' }, method: 'post', body: JSON.stringify(rider)}).then(data => data.json()).then(data => (data.id!=null) ? autoLogin(riderUsername, riderPassword) : $("#authmessage").show());

    fetch('http://192.168.160.222:8080/rider/signup', { headers: { 'Content-Type': 'application/json' }, method: 'post', body: JSON.stringify(rider)}).then(data => {
        if(data.status==200)
            try{
                data.json();
                autoLogin(riderUsername, riderPassword);
            }catch(e){
                $("#authmessage").show();
            }
        else
            $("#authmessage").show();
    })
}

function loginrider(){
    var riderUsername = $("#riderUsername").val();
    var riderPassword = $("#riderPassword").val();
    
    fetch('http://192.168.160.222:8080/login?username='+riderUsername+"&password="+riderPassword).then(data => {
        if(data.status==200){
            data=data.json();
            document.cookie = "sessionKey="+data.token;
            window.location.href="/dashboard.html"
        }else{
            $("#authmessage").show();
            $("#riderPassword").val("");
        }
    });
}

function autoLogin(riderUsername, riderPassword){
    fetch('http://192.168.160.222:8080/login?username='+riderUsername+"&password="+riderPassword).then(data => {
        if(data.status==200){
            data=data.json();
            document.cookie = "sessionKey="+data.token;
            window.location.href="/dashboard.html"
        }else{
            alert("Wrong credentials");
            $("#riderPassword").val("");
        }
    });
}