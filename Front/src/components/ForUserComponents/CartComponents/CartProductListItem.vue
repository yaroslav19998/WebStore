<template>
    <li class="cart-content-item">
        <div class="container">
            <div class="row align-items-center">
                <div class="col-3 column">
                    <img class="cart-product-img" :src="'http://localhost:8081/api/products/images/'+84" alt="pic-name">
                </div>
                <div class="col-3 column">
                   <h6>{{product.name}}</h6>
                </div>
                <div class="col-3 column">
                    <span class="btn btn-outline-warning quantity-btn quantity-btn-left" @click="decrementProduct">-</span>
                    {{product.quantity}}
                    <span class="btn btn-outline-warning quantity-btn quantity-btn-right" @click="incrementProduct">+</span></div>
                <div class="col-2 column">{{fullProductPrice}}</div>
                <div class="col-1 column">
                    <span class="btn btn-outline-danger delete-cart-btn" @click="deleteProduct">X</span>
                </div>
            </div>
        </div>
    </li>
</template>

<script>
    import NP from 'number-precision'
    export default {
        name: "CartProductListItem",
        props:["product"],
        computed:{
            fullProductPrice:function () {
                return NP.times(this.product.price,this.product.quantity);
            }
        },

        methods:{
            deleteProduct(){
                this.$emit('deleteProduct')
            },
            decrementProduct(){
                this.$emit('decrementProduct')
            },
            incrementProduct(){
                this.$emit('incrementProduct')
            }
        }
    }
</script>

<style scoped>
    .cart-product-img {
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
    li {
        list-style-type: none; /* Убираем маркеры */
    }
    .quantity{text-align: center}
    .quantity-btn-left{
        border-radius: 10px 0 0 10px;
    }
    .quantity-btn-right{
        border-radius: 0 10px 10px 0 ;
    }
    .quantity-btn{
        padding: 2px;
        margin-right: 2px;
        margin-left: 2px;
        width: 20px;
    }
    .delete-cart-btn{
        padding: 3px;
    }
</style>