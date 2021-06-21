function getAllProducts() {
    fetch("http://localhost:8081/product/all", { headers: { 'Content-Type': 'application/json'}, method: 'get' }).then(data => {
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