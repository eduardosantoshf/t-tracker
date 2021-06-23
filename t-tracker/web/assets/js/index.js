function getAllProducts() {

    fetch("http://localhost:8081/product/all", {headers: { 'Content-Type': 'application/json' }, method: 'get'}).then(data => data.json()).then(data => {
        console.log(data);

        for(i=0; i<data.length; i++){
            obj=data[i];            
            
            let col = document.createElement('div');
            col.className="col-md-4"

            let items = document.createElement('div');
            items.className="single-product-items"

            let itemImage = document.createElement('div');
            itemImage.className="product-item-image";

            let a = document.createElement('a');
            a.href="product.html?i="+obj.id+"#url_mapping_product";

            let aImg = document.createElement('img');
            aImg.src=obj.foto;
            aImg.alt="Product";

            let itemDiscount = document.createElement('div');
            itemDiscount.className="product-discount-tag";

            let itemDiscountP = document.createElement('p');
            itemDiscountP.style.cssText="font-size: 10pt";
            itemDiscountP.innerText=obj.type;

            let itemContent = document.createElement('div');
            itemContent.className="product-item-content text-center mt-30";

            let itemContentH5 = document.createElement('h5');
            itemContentH5.className="product-title";

            let itemContentH5A = document.createElement('a');
            itemContentH5A.href="#";
            itemContentH5A.innerText=obj.name;

            let itemContentUl = document.createElement('ul');
            itemContentUl.className="rating";
            itemContentUl.innerHTML='<li><i class="lni-star-filled"></i></li> <li><i class="lni-star-filled"></i></li> <li><i class="lni-star-filled"></i></li> <li><i class="lni-star-filled"></i></li>';

            let itemContentRPrice = document.createElement('span');
            itemContentRPrice.className="regular-price";
            itemContentRPrice.innerText="$" + (obj.price+10);

            let itemContentDPrice = document.createElement('span');
            itemContentDPrice.className="discount-price";
            itemContentDPrice.innerText="$" + obj.price;

            itemContent.appendChild(itemContentH5);
            itemContent.appendChild(itemContentH5A);
            itemContent.appendChild(itemContentUl);
            itemContent.appendChild(itemContentRPrice);
            itemContent.appendChild(itemContentDPrice);

            itemDiscount.appendChild(itemDiscountP);
            a.appendChild(aImg);
            itemImage.appendChild(a);
            itemImage.appendChild(itemDiscount);

            items.appendChild(itemImage);
            items.appendChild(itemContent);

            col.appendChild(items);

            switch(obj.type){
                case "Molecular": document.getElementById("molecularList").appendChild(col); break;
                case "Antigen": document.getElementById("antigenList").appendChild(col); break;
                case "Antibody": document.getElementById("antibodyList").appendChild(col); break;
            }
        }
    }).then(() => {
        /* Apply row Corrections */

        let molecular = $("#molecularList .col-md-4");
        let antigen = $("#antigenList .col-md-4");
        let antibody = $("#antibodyList .col-md-4");

        for(i=0; i<3-molecular.length; i++){
            let col = document.createElement('div');
            col.className="col-md-4";
            document.getElementById("molecularList").appendChild(col);
        }

        for(i=0; i<3-antigen.length; i++){
            let col = document.createElement('div');
            col.className="col-md-4";
            document.getElementById("antigenList").appendChild(col);
        }

        for(i=0; i<3-antibody.length; i++){
            let col = document.createElement('div');
            col.className="col-md-4";
            document.getElementById("antibodyList").appendChild(col);
        }
    })
}