<template>
    <div class="container-fluid">
        <div class="row">
            <ProductItem v-for="product in products"
                         :product="product"
                         v-bind:key="product.id"/>
        </div>
    </div>

</template>

<script>
    import ProductItem from "@/components/ForUserComponents/ProductComponents/ProductItem";

    export default {
        name: "CategoryProductList",
        components: {ProductItem},
        props: ["products"],

    }
</script>

<style scoped>

</style>