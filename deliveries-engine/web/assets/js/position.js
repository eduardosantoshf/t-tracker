function getLocation() {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(showPosition);
    } else {
        x.innerHTML = "Geolocation is not supported by this browser.";
    }
}

function showPosition(position) {
    var lat = position.coords.latitude;
    var long = position.coords.longitude;

    fetch('http://localhost:8080/rider/location/' + lat + "/" + long, { headers: { 'Content-Type': 'application/json', 'Authorization': "Bearer " + getCookie("sessionKey-rider") }, method: 'post' }).then(data => {
        if (data.status == 200) {
            data = data.json();

            Promise.all([data]).then(data => {
                $("#badgePosition").addClass("bg-success");
                $("#badgePosition").removeClass("bg-warning");
                $("#badgePosition").text(data[0].latitude + ", " + data[0].longitude);
                putMarker([data[0].latitude, data[0].longitude]);
            });
        } else {
            $("#badgePosition").removeClass("bg-success");
            $("#badgePosition").addClass("bg-warning");
            $("#badgePosition").text('Warning: cannot get your position');
        }
    });
}

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



var map;

function loadMap(){
    map = L.map('ridermap').setView([40.6443, -8.6455], 13);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
    }).addTo(map);

}

function putMarker(coordinates){
    L.marker(coordinates).addTo(map)
        .bindPopup('I am here')
        .openPopup();
}