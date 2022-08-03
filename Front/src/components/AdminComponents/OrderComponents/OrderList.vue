<template>
    <div>
        <form>
            <div class="container-fluid">
                <div class="row">
                    <div class="col-12 col-md-9 col-lg-5">
                        <div class="input-group">
                            <label for="SearchIdInput" class="input-group-text">Search by id</label>
                            <input type="number" v-model="searchId" class="form-control" id="SearchIdInput">
                            <input type="button" class="btn btn-outline-primary" value="Search" @click="searchById">
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="dropdown">
                        <button class="btn btn-primary dropdown-toggle" type="button" id="dropdownMenuButton" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            Status
                        </button>
                        <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
                            <a class="dropdown-item"  @click="searchByStatus(null)">No status</a>
                            <a class="dropdown-item" v-for="(status,index) in statuses" :key="index" @click="searchByStatus(status)">{{status}}</a>
                        </div>
                    </div>
                </div>
            </div>

        </form>
        <DialogWindow :show.sync="dialogVisible">

            <OrderForm :serverValidErrors="serverValidationErrors" @update="update" :selectedOrder="selectedOrder" v-if="selectedOrder"/>
            <OrderForm :serverValidErrors="serverValidationErrors" @create="create" v-else/>
        </DialogWindow>

        <div class="table-container">
            <button class="btn btn-outline-success create-btn" @click="showCreateDialog">Create</button>

            <div class="table-responsive">
            <table class="table table-bordered">
                <thead class="table-dark ">
                <tr>
                    <th>ID</th>
                    <th>Customer name</th>
                    <th>Customer address</th>
                    <th>Customer email</th>
                    <th>Customer phone</th>
                    <th>Price</th>
                    <th>Status</th>
                    <th>UserID</th>
                    <th>Usermame</th>
                    <th>Created date</th>
                    <th>Updated date</th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <tr v-for=" (order, index) in orders" :key="order.id">
                    <td><b>{{order.id}}</b></td>
                    <td>{{order.customerName}}</td>
                    <td>{{order.customerAddress}}</td>
                    <td>{{order.customerEmail}}</td>
                    <td>{{order.customerPhone}}</td>
                    <td>{{order.fullPrice}}</td>
                    <td>{{order.status}}</td>
                    <td ><span v-if="order.user!=null">{{order.user.id}}</span></td>
                    <td><span v-if="order.user!=null">{{order.user.username}}</span></td>
                    <td>{{convertDateTime(order.created)}}</td>
                    <td>{{convertDateTime(order.updated)}}</td>
                    <td>
                        <div class="input-group table-buttons-group">
                            <button @click="showUpdateDialog(order,index)" class="btn btn-outline-warning">Change
                            </button>
                            <button @click="remove(order.id,index)" class="btn btn-outline-danger">Delete</button>
                        </div>
                    </td>
                </tr>
                </tbody>
            </table>
            </div>
        </div>
        <div>
            <PaginationComponent :current-page="currentPage" :total-pages="totalPages" @updatePage="updatePage"/>
        </div>
    </div>
</template>

<script>
    import DialogWindow from "@/components/DialogWindow";
    import OrderForm from "@/components/AdminComponents/OrderComponents/OrderForm";
    import OrderService from "@/services/OrderService";
    import updateArray from "@/functions";
    import ConvertDateTime from "@/functions/ConvertDateTime";
    import PaginationComponent from "@/components/PaginationComponent";

    export default {
        name: "OrderList" ,components: { OrderForm, DialogWindow,PaginationComponent},
        data() {
            return {
                orders: [],
                statuses:[],
                selectedOrder: null,
                selectedIndex: null,
                searchId: null,
                selectedStatus:null,
                dialogVisible: false,
                currentPage:1,
                totalPages:1,
                serverValidationErrors:null
            }
        },
        methods: {
            convertDateTime(date) {
                return ConvertDateTime(date);
            },
            showCreateDialog() {
                this.selectedIndex = null;
                this.selectedOrder = null;
                this.dialogVisible = true
            },
            showUpdateDialog(order, index) {
                this.selectedIndex = index;
                this.selectedOrder = order;
                this.dialogVisible = true
            },
            searchByStatus(status){
                if(status!=null){
                    this.selectedStatus=status;
                    this.FetchData(null,this.selectedStatus);
                }
                else {
                    this.selectedStatus=null;
                    this.FetchData(null,this.selectedStatus);
                }
            },
            async FetchData(page,status) {
                try {
                    await OrderService.getAll(page,status).then(response => {
                        this.orders =  response.data["content"];
                        this.currentPage = response.data["currentPage"]+1;
                        this.totalPages = response.data["totalPages"];
                    });

                } catch (e) {
                    alert(e);

                }

            },
            async FetchStatuses(){
                try {
                    await OrderService.getStatuses().then(response => {
                        this.statuses=response.data;
                    });

                } catch (e) {

                    alert(e);

                }
            },
            updatePage(page){
                this.FetchData(page,this.selectedStatus)
            },
            remove(id, index) {
                OrderService.delete(id).then(response => {
                        if (response.status === 204) {
                            this.orders.splice(index, 1);
                        }
                    }
                ).catch(error => console.log(error));


            },
            create(order) {
                OrderService.create(order).then(response => this.orders.push(response.data)).catch(error =>  {
                    if(error.response.status==400){
                        this.serverValidationErrors=error.response.data.messages
                    }
                });
            },
            update(order) {
                OrderService.update(order.order.id, order)
                    .then(response => updateArray(this.selectedIndex, this.orders, response.data))
                    .catch(error =>  {
                        if(error.response.status==400){
                            this.serverValidationErrors=error.response.data.messages
                        }
                    });
            },
            searchById() {
                if (this.searchId != null) {
                    OrderService.getById(this.searchId)
                        .then(response => {
                                this.orders = [];
                                this.orders.push(response.data);
                            }
                        )
                        .catch(error => alert(error.response.data.message));
                }
            }
        },
        mounted() {
            this.FetchData(null);
            this.FetchStatuses()
        }
    }
</script>

<style scoped>
table{overflow-x: auto}
.dropdown{
    margin-top: 10px;
    margin-bottom: 10px;
}
</style>