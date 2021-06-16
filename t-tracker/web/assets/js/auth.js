function signupclient(){
    let username=$("#usernameTxt").val();
    let password=$("#passwordTxt").val();
    let name=$("#nameTxt").val();
    let email=$("#emailTxt").val();

    let client={"name":name, "email":email, "username":username, "password":password};
    console.log(client);
    fetch('http://localhost:8081/client/signup', { headers: { 'Content-Type': 'application/json', 'Access-Control-Allow-Origin':'http://localhost:8081' }, method: 'post', body: JSON.stringify(client)}).then(data => {
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
    fetch('http://localhost:8081/client/login?username='+username+"&password="+password).then(data => {
        if(data.status==200){
            data=data.json();

            Promise.all([data]).then(data => {
                document.cookie = "sessionKey="+data[0].token;
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