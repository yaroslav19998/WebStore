<template>
    <form @submit.prevent>
        <h2>Review form</h2>
        <div v-for="(error,index) in serverValidationErrors" :key="index">
            <ErrorMsgValidator :error-text="error"/>
        </div>
        <div v-for="(error,index) in serverChangeValidationErrors" :key="index">
            <ErrorMsgValidator :error-text="error"/>
        </div>
        <div>
            <div class="form-check form-check-inline">
                <input class="form-check-input" type="radio" name="RateRadio" id="Rate1" value="1"
                       v-model="review.rate">
                <label class="form-check-label" for="Rate1">1</label>
            </div>
            <div class="form-check form-check-inline">
                <input class="form-check-input" type="radio" name="RateRadio" id="Rate2" value="2"
                       v-model="review.rate">
                <label class="form-check-label" for="Rate2">2</label>
            </div>
            <div class="form-check form-check-inline">
                <input class="form-check-input" type="radio" name="RateRadio" id="Rate3" value="3"
                       v-model="review.rate">
                <label class="form-check-label" for="Rate3">3</label>
            </div>
            <div class="form-check form-check-inline">
                <input class="form-check-input" type="radio" name="RateRadio" id="Rate4" value="4"
                       v-model="review.rate">
                <label class="form-check-label" for="Rate4">4</label>
            </div>
            <div class="form-check form-check-inline">
                <input class="form-check-input" type="radio" name="RateRadio" id="Rate5" value="5"
                       v-model="review.rate">
                <label class="form-check-label" for="Rate5">5</label>
            </div>
            <ErrorMsgValidator :error-text="rateErrors" v-if="rateErrors!=null"/>
            <div class="form-floating">
                <textarea class="form-control" placeholder="Review text" id="ReviewText"
                          v-model="review.review"></textarea>
                <label for="ReviewText">Comments</label>
            </div>
            <div class="form-floating mb-3">
                <input type="text" class="form-control" id="prosInput" placeholder=" " v-model="review.pros">
                <label for="prosInput">Pros</label>
            </div>
            <div class="form-floating mb-3">
                <input type="text" class="form-control" id="consInput" placeholder=" " v-model="review.cons">
                <label for="consInput">Cons</label>
            </div>

        <div class="create-form-btn">
            <input type="button" class="btn btn-success btn-lg" value="Change" v-if="updatedReview" @click="update()">
            <input type="button" class="btn btn-success btn-lg" value="Create" v-else @click="create">
        </div>

    </div>
    </form>
</template>

<script>
    import ProductService from "@/services/ProductService";
    import {mapGetters} from 'vuex'
    import ErrorMsgValidator from "@/components/ForUserComponents/ErrorMsgValidator";
    import {required} from 'vuelidate/lib/validators'

    export default {
        name: "ReviewForm",
        props: ["productId","updatedReview","serverChangeValidationErrors"],
        inject: ['serverChangeValidationErrors'],
        components:{ErrorMsgValidator},
        computed: {
            ...mapGetters({
                AuthUser: 'moduleAuth/getUser'
            },),
            rateErrors(){
                if(!this.$v.review.rate.required && this.$v.review.rate.$dirty){
                    return "Select rate"
                }
                return null;
            }
        },
        data() {
            return {
                review: {
                    review: null,
                    rate:null,
                    product: {id: null},
                    user: null,
                    pros: null,
                    cons: null

                },
                serverValidationErrors:null
            }
        },
        validations:{
            review:{
                rate:{required},
            },
        },
        methods: {
            create() {
                if(this.$v.$invalid){
                    this.$v.$touch()
                    return
                }
                ProductService.createReview(this.review).then(response => console.log(response)).catch(error =>{
                    if(error.response.status==400){
                        this.serverValidationErrors=error.response.data.messages
                    }
                });
            },
            update(){
                if(this.$v.$invalid){
                    this.$v.$touch()
                    return
                }
                this.$emit('updateReview',this.review);
            }
        },
        mounted() {
            if(this.updatedReview!=null){ this.review={...this.updatedReview};}
            else {
                this.review.product.id = this.productId;
                this.review.user = this.AuthUser
            }

        }
    }
</script>

<style scoped>
.form-floating{
    margin-top: 10px;
}
form{
    width: 500px;
}
#ReviewText{
    height: 200px;
}
.create-form-btn{
    text-align: right;
}
.btn{
    width: 200px;
}
    h2{
        margin-top: 20px ;
        margin-bottom: 20px;
    }
</style>