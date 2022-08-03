<template>
    <div>
        <p class="deleteBtn" v-if="(isAdmin)||(user.id==review.user.id)"><span @click="deleteReview(review.id)">X</span> </p>
        <p class="ReviewCreateInfo">{{review.user.username}} Created: {{convertDateTime(review.created)}} <span v-if="review.created!=review.updated">Updated: {{convertDateTime(review.updated)}}</span></p>
        <h4>Rate: {{review.rate}}</h4>
        <p>{{review.review}}</p>
        <p v-if="review.pros!=null"><span class="pros">Pros: </span>{{review.pros}}</p>
        <p v-if="review.cons!=null"><span class="cons">Cons: </span>{{review.cons}}</p>

        <p> </p>
    </div>
</template>

<script>
    import ConvertDateTime from "@/functions/ConvertDateTime";
    import {mapGetters} from 'vuex'

    export default {
        name: "ReviewItem",
        props:["review"],
        computed: {
            ...mapGetters({isAdmin: 'moduleAuth/isAdmin', user: 'moduleAuth/getUser'
            },)
        },

        methods:{
            convertDateTime(date) {
                return ConvertDateTime(date);
            },
            deleteReview(id) {
                console.log(this.review.id)
                this.$emit('deleteReview',id);
            },
        }
    }
</script>

<style scoped>
div{
    border: 1px solid gainsboro;
    margin: 10px 0 10px 0 ;
    padding:  10px 10px 5px 10px;
}
.ReviewCreateInfo{
    font-size: 1em;
}
.deleteBtn{
    text-align: right;
    font-size: 1.4em;
    padding: 10px;
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
</style>