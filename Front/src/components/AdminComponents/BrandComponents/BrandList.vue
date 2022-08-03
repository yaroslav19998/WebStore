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
                    <div class="col-12  col-md-9 col-lg-5">
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

            <BrandForm :serverValidErrors="serverValidationErrors" @update="update" :brandId="selectedId" v-if="selectedId"/>
            <BrandForm :serverValidErrors="serverValidationErrors" @create="create" v-else/>
        </CreateUpdateDialog>

        <div class="table-container">
            <button class="btn btn-outline-success create-btn" @click="showCreateDialog">Create</button>
            <table class="table table-bordered">
                <thead class="table-dark ">
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Created date</th>
                    <th>Updated date</th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <tr v-for=" (brand, index) in brands" :key="brand.id">
                    <td><b>{{brand.id}}</b></td>
                    <td>{{brand.name}}</td>
                    <td>{{convertDateTime(brand.created)}}</td>
                    <td>{{convertDateTime(brand.updated)}}</td>
                    <td>
                        <div class="input-group table-buttons-group">
                            <button @click="showUpdateDialog(brand.id,index)" class="btn btn-outline-warning">Change
                            </button>
                            <button @click="remove(brand.id,index)" class="btn btn-outline-danger">Delete</button>
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
    import BrandForm from "@/components/AdminComponents/BrandComponents/BrandForm";
    import BrandService from "@/services/BrandService";
    import updateArray from "@/functions";
    import ConvertDateTime from "@/functions/ConvertDateTime";
    import PaginationComponent from "@/components/PaginationComponent";

    export default {
        name: "BrandList",
        components: {BrandForm, CreateUpdateDialog,PaginationComponent},
        data() {
            return {
                brands: [],
                selectedId: null,
                selectedIndex: null,
                searchId: null,
                searchName: null,
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
                this.selectedId = null;
                this.dialogVisible = true
            },
            showUpdateDialog(id, index) {
                this.selectedIndex = index;
                this.selectedId = id;
                this.dialogVisible = true
            },

            async FetchData(page,name) {
                try {
                    await BrandService.getAll(page,"admin",name).then(response => {
                            this.brands = response.data["content"]
                            this.currentPage = response.data["currentPage"]+1
                            this.totalPages = response.data["totalPages"]
                    });

                } catch (e) {
                    console.log(e)

                }

            },
            updatePage(page){
                if(this.search){
                    this.FetchData(page,this.searchName)
                }
                else{
                    this.FetchData(page)
                }

            },
            remove(id, index) {
                BrandService.delete(id).then(response => {
                        if (response.status === 204) {
                            this.brands.splice(index, 1);
                        }
                    }
                ).catch(error => console.log(error));


            },
            create(brand) {
                BrandService.create(brand)
                    .then(response => this.brands.push(response.data))
                    .catch(error =>{
                        if(error.response.status==400){
                            this.serverValidationErrors=error.response.data.messages
                        }
                      }
                );
            },
            update(brand) {
                BrandService.update(brand.id, brand)
                    .then(response => updateArray(this.selectedIndex, this.brands, response.data))
                    .catch(error => {  if(error.response.status==400){
                                 this.serverValidationErrors=error.response.data.messages
                            }

                    });
            },
            searchByName() {
                this.search=true;
                this.FetchData(null,this.searchName)
            },
            searchById() {
                if (this.searchId != null) {
                    BrandService.getById(this.searchId,"admin")
                        .then(response => {
                                this.brands = [];
                                this.brands.push(response.data);
                                this.currentPage = 1
                                this.totalPages = 1
                            }
                        )
                        .catch(error =>alert(error.response.data.message));
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