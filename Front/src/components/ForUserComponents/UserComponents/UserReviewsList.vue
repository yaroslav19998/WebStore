<template>
    <div class="container-fluid">
        <h3 v-if="elementsCount>0">The number of reviews:{{elementsCount}}</h3>
        <div v-if="reviews.length>0">
            <UserReviewsListItem v-for="(review,index) in reviews" :review="review" :key="review.id" :serverValidationErrors="serverValidationErrors"
                                                            @deleteReview="deleteReview(review.id,index)" @updateReview="updateReview"/>
        </div>
        <h4 v-else>You don't have any reviews</h4>
        <div>
            <PaginationComponent v-if="totalPages" :current-page="currentPage" :total-pages="totalPages" @updatePage="updatePage"/>
        </div>
    </div>
</template>

<script>
    import UserService from "@/services/UserService";
    import UserReviewsListItem from "@/components/ForUserComponents/UserComponents/UserReviewsListItem";
    import ProductService from "@/services/ProductService";
    import updateArray from "@/functions";
    import PaginationComponent from "@/components/PaginationComponent";

    export default {
        name: "UserReviews",
        props: ["userId"],
        components: {UserReviewsListItem,PaginationComponent},
        data() {
            return {
                reviews: [],
                elementsCount:0,
                currentPage:1,
                totalPages:1,
                serverValidationErrors:null
            }
        }
        ,
        methods: {
            FetchReviews(page) {
                UserService.getUserReviewsById(this.userId,page).then(response => {
                    this.reviews = response.data["content"];
                    this.currentPage = response.data["currentPage"]+1
                    this.totalPages = response.data["totalPages"]
                    this.elementsCount=response.data["elementsCount"]
                }).catch(error => console.log(error));
            },
            deleteReview(id, index) {
                ProductService.deleteReview(id).then(response => {
                    if (response.status === 204) {
                        this.reviews.splice(index, 1);
                    }
                }).catch(error => console.log(error));
            },
            updatePage(page)
                {
                    this.FetchReviews(page)
                },
            updateReview(review) {
                ProductService.updateReview(review.id, review).then(
                    response => {
                        let index=this.reviews.findIndex(el=>el.id===review.id)
                        updateArray(index, this.reviews, response.data)
                    }
                ).catch(error => {
                        if(error.response.status==400){
                            this.serverValidationErrors=error.response.data.messages
                        }

                })
            }
        },
        mounted() {
            this.FetchReviews()
        }
    }
</script>

<style scoped>

</style>