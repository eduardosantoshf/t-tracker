function getCookie(cname) {
    var name = cname + "=";
    var decodedCookie = decodeURIComponent(document.cookie);
    var ca = decodedCookie.split(';');
    for (var i = 0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) == 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}

function changeState(state){
    fetch('http://localhost:8080/rider/status/' + state, { headers: { 'Content-Type': 'application/json', 'Authorization': "Bearer " + getCookie("sessionKey-rider") }, method: 'post' }).then(data => {
        if (data.status == 200) {
            data = data.json();

            Promise.all([data]).then(data => {
                changeStateStyle(data[0].status);
            });
        } else {
            $("#riderDeliveringBadge").removeClass("bg-success");
            $("#riderDeliveringBadge").addClass("bg-warning");
            $("#riderDeliveringBadge").text('Warning: cannot get your state');
        }
    });
}

function changeStateStyle(state){
    switch (state) {
        case 0:
            $("#riderDeliveringBadge").addClass("bg-danger");
            $("#riderDeliveringBadge").removeClass("bg-success");
            $("#riderDeliveringBadge").removeClass("bg-warning");
            $("#riderDeliveringBadge").text("Offline");
            break;
        case 1:
            $("#riderDeliveringBadge").removeClass("bg-danger");
            $("#riderDeliveringBadge").addClass("bg-success");
            $("#riderDeliveringBadge").removeClass("bg-warning");
            $("#riderDeliveringBadge").text("Idle");
            break;
        case 2:
            $("#riderDeliveringBadge").removeClass("bg-danger");
            $("#riderDeliveringBadge").removeClass("bg-success");
            $("#riderDeliveringBadge").addClass("bg-warning");
            $("#riderDeliveringBadge").text("Delivering");
            break;
        default:
            $("#riderDeliveringBadge").removeClass("bg-danger");
            $("#riderDeliveringBadge").removeClass("bg-success");
            $("#riderDeliveringBadge").addClass("bg-warning");
            $("#riderDeliveringBadge").text("Warning: Cannot get your current state");
    }
}

function getAllDeliveries(){
    let riderID=localStorage.getItem("riderID") //

    fetch('http://localhost:8080/rider/deliveries/' + riderID, { headers: { 'Content-Type': 'application/json', 'Authorization': "Bearer " + getCookie("sessionKey-rider") }, method: 'get' }).then(data => {
        if (data.status == 200) {
            data = data.json();
            
            Promise.all([data]).then(data => {
                let arr=data[0];

                arr.map((obj) => {
                    let tr = document.createElement('tr');

                    let td_id = document.createElement('td');
                    td_id.innerText=obj.id;

                    let td_nome = document.createElement('td');
                    td_nome.innerText=obj.nome;

                    let td_status = document.createElement('td');
                    let div_status = document.createElement('div');
                    div_status.className="statusPending";
                    div_status.innerText=obj.status;
                    // statusDelivering
                    //statusComplete
                    td_status.appendChild(div_status)
                    let td_comissao = document.createElement('td');



                    tr.appendChild(td_id);
                    tr.appendChild(td_nome);
                    tr.appendChild(td_status);
                    tr.appendChild(td_comissao);
                    document.getElementById('deliveriesList').appendChild(tr);
                });
            });
        }
    });
}