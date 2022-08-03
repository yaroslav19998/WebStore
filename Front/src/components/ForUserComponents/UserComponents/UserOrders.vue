<template>
<div>
    <h3 v-if="elementsCount>0">The number of orders:{{elementsCount}}</h3>
    <OrderList :orders="orders" />
    <div>
        <PaginationComponent v-if="totalPages" :current-page="currentPage" :total-pages="totalPages" @updatePage="updatePage"/>
    </div>
</div>
</template>

<script>
    import UserService from "@/services/UserService";
    import OrderList from "@/components/ForUserComponents/OrderComponents/OrderList";
    import PaginationComponent from "@/components/PaginationComponent";
    export default {
        name: "UserOrders",
        components:{OrderList,PaginationComponent},
        props:["userId"],
        data() {
            return {
                orders: [],
                elementsCount:0,
                currentPage:1,
                totalPages:1
             }
        },
        methods:{
            FetchOrders(page){
                UserService.getUserOrdersById(this.userId,page).then(response => {
                    this.orders = response.data["content"];
                    this.currentPage = response.data["currentPage"]+1
                    this.totalPages = response.data["totalPages"]
                    this.totalPages = response.data["totalPages"]
                    this.elementsCount=response.data["elementsCount"]
                }).catch(error => console.log(error));
            },
            updatePage(page){
                {
                    this.FetchOrders(page)
                }
            }
        },
        mounted(){
           this.FetchOrders();
        }
    }
</script>

<style scoped>
h3{
    margin: 15px;
}
</style>