<template>
    <div class="cart-content">
         <CartProductList/>
        <div class="cart-content-footer container">
            <div class="row align-items-center">
                <div class="col-8 full-price">Full price for all products: {{this.fullprice}}</div>
                <div class="col-4 button-column"><router-link :to="{name:'createOrder'}" class="btn btn-primary">CreateOrder</router-link></div>
            </div>
         </div>

    </div>

</template>

<script>

    import {mapGetters} from 'vuex'
    import CartProductList from "@/components/ForUserComponents/CartComponents/CartProductList";

    export default {
        name: "CartComponent",
        components: {CartProductList},
        computed: {
            ...mapGetters({fullprice: 'moduleCart/getFullPrice'}),

        }
    }
</script>

<style scoped>
    .cart-content {
        cursor: default;
        width: 550px;
        position: absolute;
        top: 120%;
        right: 0;
        border: 1px solid lightgray;
        background-color: rgb(248, 249, 250);
        box-shadow: 0px 4px 4px rgba(135, 135, 135, 0.25);
        opacity: 0;
        visibility: hidden;
        transition: opacity 0.3s ease-in-out, visibility 0.3s ease-in-out;
        z-index: 2;

    }
    .button-column{
       text-align: right;
        padding-right: 10px;
    }
    .cart-content-footer{
        border-top: 1px solid lightgray;
        padding:15px;
    }
    .cart:hover .cart-content {
        opacity: 1;
        visibility: visible;
        transition: opacity 0.3s ease-in-out, visibility 0.3s ease-in-out;
    }
    .full-price{ font-size: 1.2em;
    font-weight: bolder}
</style>