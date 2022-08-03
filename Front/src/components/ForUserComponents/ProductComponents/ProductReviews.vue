<template>
    <div class=" container-fluid" :class="{ 'main-component-container': this.$route.params.productId }" v-if="reviews.length>0">
        <h1>Reviews</h1>
        <ProductReviewsList  @deleteReview="deleteReview" :reviews="reviews"/>
        <div>
            <PaginationComponent :current-page="currentPage" :total-pages="totalPages" @updatePage="updatePage"/>
        </div>
    </div>
</template>

<script>
    import ProductService from "@/services/ProductService";
    import ProductReviewsList from "@/components/ForUserComponents/ProductComponents/ProductReviewsList";
    import PaginationComponent from "@/components/PaginationComponent";
    export default {
        name: "ProductReviews",
        components: {ProductReviewsList,PaginationComponent},
        props:["productId"],
        data(){
            return {
                reviews:[],
                currentPage:1,
                totalPages:1}
        },
        methods:{
          fetchReviews(page) {
              let id=null
              if(this.productId!=null){
                  id=this.productId
              }
              else {
                  id=this.$route.params.productId
              }
              ProductService.getProductReviewsById(id,page).then(
                  response=>{
                      this.reviews=response.data["content"];
                      this.currentPage = response.data["currentPage"]+1;
                      this.totalPages = response.data["totalPages"];
                  }
              ).catch(e=>console.log(e))
          },
            updatePage(page){
                this.fetchReviews(page);
            },
            deleteReview(id, index) {
                ProductService.deleteReview(id).then(response => {
                    if (response.status === 204) {
                        this.reviews.splice(index, 1);
                    }
                }).catch(error => console.log(error));
            }
        },


        mounted(){
            this.fetchReviews()
        }
    }
</script>

<style scoped>
    h1{margin-top: 20px;
        margin-bottom: 20px;
    }
</style>