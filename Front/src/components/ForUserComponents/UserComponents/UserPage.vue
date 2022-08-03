<template>
    <div class="main-component-container container-fluid user-tabs">
        <ul  class="nav nav-tabs" id="myTab" role="tablist">
            <li class="nav-item" role="presentation">
                <button class="nav-link active"
                        id="info-tab"
                        data-bs-toggle="tab"
                        data-bs-target="#info"
                        type="button" role="tab"
                        aria-controls="info"
                        aria-selected="true">Info</button>
            </li>
            <li class="nav-item" role="presentation">
                <button class="nav-link" id="update-tab" data-bs-toggle="tab" data-bs-target="#update" type="button" role="tab" aria-controls="update" aria-selected="false">Update</button>
            </li>
            <li class="nav-item" role="presentation">
                <button class="nav-link" id="orders-tab" data-bs-toggle="tab" data-bs-target="#orders" type="button" role="tab" aria-controls="orders" aria-selected="false">Orders</button>
            </li>
            <li class="nav-item" role="presentation">
                <button class="nav-link" id="reviews-tab" data-bs-toggle="tab" data-bs-target="#reviews" type="button" role="tab" aria-controls="reviews" aria-selected="false">Reviews</button>
            </li>
        </ul>
        <div class="tab-content " id="myTabContent">
            <div class="tab-pane fade show active" id="info" role="tabpanel" aria-labelledby="info-tab"><UserInfo :user="user"/></div>
            <div class="tab-pane fade" id="update" role="tabpanel" aria-labelledby="update-tab"><UpdateUserInfo v-if="user" :userInfo="user"/></div>
            <div class="tab-pane fade" id="orders" role="tabpanel" aria-labelledby="orders-tab"><UserOrders :userId="this.AuthUser.id"/></div>
            <div class="tab-pane fade" id="reviews" role="tabpanel" aria-labelledby="reviews-tab"><UserReviews  :userId="this.AuthUser.id"/></div>
        </div>
    </div>
</template>

<script>
    import UpdateUserInfo from "@/components/ForUserComponents/UserComponents/UpdateUserInfo";
    import UserInfo from "@/components/ForUserComponents/UserComponents/UserInfo";
    import UserOrders from "@/components/ForUserComponents/UserComponents/UserOrders";
    import UserReviews from "@/components/ForUserComponents/UserComponents/UserReviewsList";
    import UserService from "@/services/UserService";
    import {mapGetters} from 'vuex'

    export default {
        name: "UserPage",
        components: {UpdateUserInfo,UserOrders,UserInfo,UserReviews},
        data() {
            return {
                user:null
            }
        },
        computed: {
            ...mapGetters({
                AuthUser: 'moduleAuth/getUser'
            },)
        },
        methods: {getUserInfo(){
            UserService.getById(this.AuthUser.id).then(response => {
                    this.user=response.data;

                }
            )
                .catch(error => console.log(error));
         }},
            beforeMount() {
             this.getUserInfo();
        }
    }
</script>

<style scoped>
    .user-tabs{
        margin-top: 50px;
    }
    .tab-content{
        border-right: 1px gainsboro solid;
        border-bottom: 1px gainsboro solid;
        border-left: 1px gainsboro solid;
        padding-top: 20px;
        padding-bottom: 20px;
    }
    .nav{
        font-size: 1.2em;
    }
</style>