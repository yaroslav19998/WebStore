<template>
    <div class="product-column col-12 col-lg-3">
        <div class="card" style="width: 18rem;">
            <div class="card-image" v-if="mainImage">
                <img :src="'http://localhost:8081/api/products/images/'+mainImage.id" class="card-img-top" alt="...">
            </div>
            <div class="card-body">
                <h4 class="card-title">{{product.id}}</h4>
                <h4 class="card-title">{{product.name}}</h4>
                <h4 class="card-title" v-if="product.brand!=null">Brand: {{product.brand.name}}</h4>
                <h4 class="card-title" v-if="product.category!=null">{{product.category.name}}</h4>
                <h4 class="card-title" v-if="product.averageRate!=null">Rate: {{product.averageRate}}</h4>
                <p class="card-text product-description">{{product.description}}</p>
                <div> <span class="price"> Price: {{product.price}}</span></div>
                <div>Sales:{{product.sales}}</div>
                <div class="buttons">
                    <div class="btn-group">
                        <button class="btn btn-success" @click="add">Buy</button>

                        <button class="btn btn-primary" @click="$router.push({name:'productPage',params: { productId: product.id }})" >Show page</button>

                    </div>
                </div>

            </div>
        </div>
    </div>
</template>

<script>
    import {mapActions} from 'vuex'

    export default {
        name: "ProductItem",
        props: ["product"],
        computed: {
            mainImage: function () {
                console.log(this.product)
                return this.product.images.filter(c => c.is_main === true)[0]
            },
        },
        methods: {
            ...mapActions({addToCart: "moduleCart/addToCart"}),
            add() {
                this.addToCart({product:this.product,quantity:1});
                console.log(this.product)
            }
        }
    }
</script>

<style scoped>
    .col-lg-3 {
        padding: 5px;
    }
    .card-footer{
          text-align: right;
          padding: 5px;
      }
    .price{
        font-weight: bold;
        font-size: 1.2em;
    }
    .buttons{
        margin-top: 15px;
        text-align: right!important;
    }
    .product-description{
        font-size: 1em;
    }
</style>