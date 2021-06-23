function getAllProducts() {
    fetch("http://192.168.160.222:8081/product/all", { headers: { 'Content-Type': 'application/json'}, method: 'get' }).then(data => data.json()).then(
        data => console.log(data)
    );
}