<template>
    <div id="UserReviewListItem">
        <p class="deleteBtn"><span @click="deleteReview(review.id)">X</span> </p>
        <p class="ReviewCreateInfo">Product:{{review.product.name}} Created: {{convertDateTime(review.created)}} <span v-if="review.created!=review.updated">Updated: {{convertDateTime(review.updated)}}</span></p>
        <h4>Rate: {{review.rate}}</h4>
        <p>{{review.review}}</p>
        <p v-if="review.pros!=null"><span class="pros">Pros: </span>{{review.pros}}</p>
        <p v-if="review.cons!=null"><span class="cons">Cons: </span>{{review.cons}}</p>
        <div class="btn-edit-container"><button class="btn btn-warning btn-edit" @click="showCreateDialog">Edit</button></div>
        <DialogWindow :show.sync="dialogVisible">
            <ReviewForm :serverChangeValidationErrors="serverValidationErrors" @updateReview="updateReview" :updatedReview="review"/>
        </DialogWindow>
    </div>
</template>

<script>
    import ConvertDateTime from "@/functions/ConvertDateTime";
    import ReviewForm from "@/components/ForUserComponents/ProductComponents/ReviewForm";
    import DialogWindow from "@/components/DialogWindow";
    export default {
        name: "UserReviewsListItem",
        components: {ReviewForm,DialogWindow},
        props:["review","serverValidationErrors"],
        data(){
            return {dialogVisible: false}
        },
        methods:{
            convertDateTime(date) {
                return ConvertDateTime(date);
            },
            showCreateDialog() {
                this.dialogVisible = true
            },
            deleteReview(id) {
                this.$emit('deleteReview',id);},
            updateReview(review){
                this.$emit('updateReview',review);}
            }
        }

</script>

<style scoped>
    #UserReviewListItem{
        border: 1px solid gainsboro;
        margin: 10px 0 10px 0 ;
        padding:  10px 10px 5px 10px;
    }
.deleteBtn{
text-align: right;
    font-size: 1.4em;
    padding: 10px;
}

.ReviewCreateInfo{
    font-size: 1em;
}
p{
    font-size: 1.2em;


}
.pros{
    color: limegreen;
    padding: 4px;
}
.cons{
    color: #f54842;
    padding: 4px;
}
    .btn-edit-container{
        border: none;
        text-align: right;
        margin: 15px;
    }
    .btn-edit{
        width: 200px;
    }
</style>