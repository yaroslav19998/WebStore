<template>
<div class="search-content">
    <div v-for="product in products" :key="product.id">
        <div class="container">
            <div class="row align-items-center">
                <div class="col-5 column">
                    <img class="search-product-img" :src="'http://localhost:8081/api/products/images/'+ mainImage(product)" alt="pic-name">
                </div>
                <div class="col-7 text-column">
                    <h6>{{product.name}}</h6>
                </div>
            </div>
        </div>
    </div>
</div>
</template>

<script>
    export default {
        name: "SearchNavComponent",
        props:["products"],
        methods:{
            mainImage(product){

                    return product.images.filter(image=>image._main==true)[0].id


            }
        }
    }
</script>

<style scoped>
    .search-content {
        cursor: default;
        width: 500px;
        position: absolute;
        top: 120%;
        right: 0;
        border: 1px solid black;
        background-color: rgb(248, 249, 250);
        box-shadow: 0px 4px 4px rgba(135, 135, 135, 0.25);
        opacity: 0;
        visibility: hidden;
        transition: opacity 0.3s ease-in-out, visibility 0.3s ease-in-out;
        z-index: 2;
    }

    .search:hover .search-content {
        opacity: 1;
        visibility: visible;
        transition: opacity 0.3s ease-in-out, visibility 0.3s ease-in-out;
    }
    .search-product-img {
        margin-right: 5px;
        max-width: 100px;
        max-height: 100px;
        object-fit: cover;
    }
    .column{
        padding: 5px;
        text-align: center;
        vertical-align: center;
    }
    .text-column{
        padding: 5px;
        text-align: left;
        vertical-align: center;
    }
</style>