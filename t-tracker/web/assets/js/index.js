function getAllProducts() {
    fetch("http://localhost:8081/product/all", { headers: { 'Content-Type': 'application/json'}, method: 'get' }).then(data => data.json()).then(
        data => console.log(data)
    );
}