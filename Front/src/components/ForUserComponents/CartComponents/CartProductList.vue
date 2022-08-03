<template>
    <ul class="cart-content-list">
        <CartProductListItem
                v-for="(product,index) in products"
                :product="product"
                v-bind:key="product.id"
                @deleteProduct="deleteProduct(index)"
                @decrementProduct="decrementProduct(index)"
                @incrementProduct="incrementProduct(index)"


        />
    </ul>
</template>

<script>
    import  {mapGetters} from 'vuex'
    import  {mapActions} from 'vuex'
    import CartProductListItem from "@/components/ForUserComponents/CartComponents/CartProductListItem";
    export default {
        name: "CartProductList",
        components: {CartProductListItem},
        computed:{
            ...mapGetters({products:'moduleCart/getProducts'})
        },
        methods:{
            ...mapActions(
                {
                    delete: "moduleCart/deleteProduct",
                    increment:"moduleCart/incrementProduct",
                    decrement:"moduleCart/decrementProduct"
                }),
            deleteProduct(index) {
                this.delete(index);
            },
            incrementProduct(index) {
                this.increment(index);
            },
            decrementProduct(index) {
                this.decrement(index);
            },

        }
    }
</script>

<style scoped>
    .cart-content-list {
        max-height: 330px;
        overflow-y: auto;

    }
    ul {
        margin-left: 0; /* Отступ слева в браузере IE и Opera */
        padding-left: 0; /* Отступ слева в браузере Firefox, Safari, Chrome */
        margin-bottom: 0px;
    }
</style>