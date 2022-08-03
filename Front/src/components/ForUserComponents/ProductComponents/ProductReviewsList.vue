<template>
    <div>
        <ProductReviewItem @deleteReview="deleteReview(review.id,index)" :review="review" v-for="(review,index) in reviews" :key="review.id"/>
    </div>
</template>

<script>
    import ProductReviewItem from "@/components/ForUserComponents/ProductComponents/ProductReviewItem";
    export default {
        name: "ReviewsList",
        components: {ProductReviewItem},
        props:["reviews"],
        methods:{
            deleteReview(id,index){
                console.log(id)
                this.$emit('deleteReview',id,index);
            }
        }
    }
</script>

<style scoped>

</style>