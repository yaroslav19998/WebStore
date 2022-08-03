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
                    <div class="col-12 col-md-9 col-lg-5">
                        <div class="input-group ">
                            <label for="SearchNameInput" class="input-group-text">Search by name</label>
                            <input type="text" class="form-control" id="SearchNameInput" v-model="searchName">
                            <input type="button" class="btn btn-outline-primary" value="Search" @click="searchByName">
                        </div>
                    </div>
                </div>
            </div>
        </form>


        <CreateUpdateDialog :show.sync="dialogVisible">

            <ProductForm :serverValidErrors="serverValidationErrors" @update="update" :selectedProduct="selectedProduct" v-if="selectedProduct"/>
            <ProductForm :serverValidErrors="serverValidationErrors" @create="create" v-else/>
        </CreateUpdateDialog>
        <div class="table-container">
            <button class="btn btn-outline-success create-btn" @click="showCreateDialog">Create</button>
            <table class="table table-bordered">
                <thead class="table-dark">
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Description</th>
                    <th>Price</th>
                    <th>Created date</th>
                    <th>Updated date</th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <tr v-for=" (product, index) in products" :key="product.id">
                    <td><b>{{product.id}}</b></td>
                    <td>{{product.name}}</td>
                    <td>{{product.description}}</td>
                    <td>{{product.price}}</td>
                    <td>{{convertDateTime(product.created)}}</td>
                    <td>{{convertDateTime(product.updated)}}</td>
                    <td>
                        <div class="input-group table-buttons-group">
                            <button @click="showUpdateDialog(product,index)" class="btn btn-outline-warning">Change</button>
                            <button @click="remove(product.id,index)" class="btn btn-outline-danger">Delete</button>
                        </div>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <div>
            <PaginationComponent :current-page="currentPage" :total-pages="totalPages" @updatePage="updatePage"/>
        </div>
    </div>
</template>

<script>
    import CreateUpdateDialog from "@/components/DialogWindow";
    import updateArray from "@/functions";
    import ProductService from "@/services/ProductService";
    import ProductForm from "@/components/AdminComponents/ProductComponents/ProductForm";
    import ConvertDateTime from "@/functions/ConvertDateTime";
    import PaginationComponent from "@/components/PaginationComponent";
    export default {
        name: "ProductList",
        components: {ProductForm, CreateUpdateDialog,PaginationComponent},
        data() {
            return {
                products: [],
                selectedProduct: null,
                selectedIndex: null,
                searchId: null,
                searchName: "",
                search:false,
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
                this.selectedProduct = null;
                this.dialogVisible = true
            },
            showUpdateDialog(product, index) {
                this.selectedIndex = index;
                this.selectedProduct = product;
                this.dialogVisible = true
            },

            async FetchData(page,name) {

                await ProductService.getAll(page,"admin",name).then(response => {
                    console.log(response.data)
                    this.products = response.data["content"]
                    this.currentPage = response.data["currentPage"]+1
                    this.totalPages = response.data["totalPages"]
                }).catch(error => console.log(error));


            },
            remove(id, index) {
                ProductService.delete(id).then(response => {
                        if (response.status === 204) {
                            this.products.splice(index, 1);
                        }
                    }
                ).catch(error => console.log(error));


            },
            create(product,images) {
                ProductService.create(product,images).then(response => this.products.push(response.data)).catch(error => {
                    if(error.response.status==400){
                        this.serverValidationErrors=error.response.data.messages
                    }
                });
            },
            update(productDTO,images) {
                ProductService.update(productDTO.product.id, productDTO,images)
                    .then(response => updateArray(this.selectedIndex, this.products, response.data))
                    .catch(error =>{
                        if(error.response.status==400){
                            this.serverValidationErrors=error.response.data.messages
                        }
                    });
            },
            updatePage(page){
                if(this.search){
                    this.FetchData(page,this.searchName)
                }
                else{
                    this.FetchData(page)
                }
            },
            searchByName() {
                this.search=true;
                this.FetchData(null,this.searchName);
            },
            searchById() {
                if (this.searchId != null) {
                    ProductService.getById(this.searchId,"admin")
                        .then(response => {
                                this.products = [];
                                this.products.push(response.data);
                            this.currentPage = 1
                            this.totalPages = 1
                            }
                        )
                        .catch(error => alert(error.response.data.message));
                }
            }
        },
        mounted() {
            this.FetchData();
        }
    }

</script>

<style scoped>
</style>