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
                        <div class="input-group">
                            <label for="SearchNameInput" class="input-group-text">Search by name</label>
                            <input type="text" class="form-control" id="SearchNameInput" v-model="searchName">
                            <input type="button" class="btn btn-outline-primary" value="Search" @click="searchByName">
                        </div>
                    </div>
                </div>
            </div>

        </form>

        <CreateUpdateDialog :show.sync="dialogVisible">

            <CategoryForm :serverValidErrors="serverValidationErrors" @update="update" :selectedCategory="selectedCategory" v-if="selectedCategory"/>
            <CategoryForm :serverValidErrors="serverValidationErrors" @create="create" v-else/>
        </CreateUpdateDialog>
        <div class="table-container">
            <button @click="showCreateDialog" class="btn btn-outline-success create-btn">Create</button>
            <table class="table table-bordered">
                <thead  class="table-dark ">
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Depth</th>
                    <th>Parent category</th>
                    <th>Created date</th>
                    <th>Updated date</th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <tr v-for=" (category, index) in categories" :key="category.id">
                    <td><b>{{category.id}}</b></td>
                    <td>{{category.name}}</td>
                    <td>{{category.depth}}</td>
                    <td><span v-if="category.parentCategory!=null">{{category.parentCategory.name}}</span></td>
                    <td>{{convertDateTime(category.created)}}</td>
                    <td>{{convertDateTime(category.updated)}}</td>
                    <td>
                        <div class="input-group table-buttons-group">
                            <button @click="showUpdateDialog(category,index)" class="btn btn-outline-warning">Change</button>
                            <button @click="remove(category.id,index)" class="btn btn-outline-danger">Delete</button>
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
    import CategoryForm from "@/components/AdminComponents/CategoryComponents/CategoryForm";
    import CategoryService from "@/services/CategoryService";
    import ConvertDateTime from "@/functions/ConvertDateTime";
    import PaginationComponent from "@/components/PaginationComponent";
    export default {
        name: "CategoryList",
        components: {CategoryForm, CreateUpdateDialog,PaginationComponent},
        data() {
            return {
                categories: [],
                selectedCategory: null,
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
                this.selectedCategory = null;
                this.dialogVisible = true
            },
            showUpdateDialog(category, index) {
                this.selectedIndex = index;
                this.selectedCategory = category;
                this.dialogVisible = true
            },

            async FetchData(page,name) {
                await CategoryService.getAll(page,"admin",name).then(response => {
                    this.categories = response.data["content"]
                    this.currentPage = response.data["currentPage"]+1
                    this.totalPages = response.data["totalPages"]
                }).catch(error => console.log(error));
            },
            remove(id, index) {
                CategoryService.delete(id).then(response => {
                        if (response.status === 204) {
                            this.categories.splice(index, 1);
                        }
                    }
                ).catch(error => console.log(error));

            },
            create(category) {
                CategoryService.create(category).then(response => this.categories.push(response.data)).catch(error => {
                    if(error.response.status==400){
                        this.serverValidationErrors=error.response.data.messages
                    }
                });
            },
            update(category) {
                CategoryService.update(category.id, category)
                    .then(response =>{


                            updateArray(this.selectedIndex, this.categories, response.data)
                    }

                    )
                    .catch(error => {
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
                    CategoryService.getById(this.searchId,"admin")
                        .then(response => {
                                this.categories = [];
                                this.categories.push(response.data);
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