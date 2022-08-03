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
                            <label for="SearchNameInput" class="input-group-text">Search by username</label>
                            <input type="text" class="form-control" id="SearchNameInput" v-model="searchName">
                            <input type="button" class="btn btn-outline-primary" value="Search" @click="searchByName">
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="dropdown">
                        <button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenuButton" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            Roles
                        </button>
                        <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
                            <a class="dropdown-item"  @click="searchByRole(null)">No role</a>
                            <a class="dropdown-item" v-for="role in roles" :key="role.id" @click="searchByRole(role.id)">{{role.name}}</a>
                        </div>
                    </div>
                </div>
            </div>
        </form>
        <CreateUpdateDialog :show.sync="dialogVisible">
            <UserForm :serverValidErrors="serverValidationErrors" @update="update" :selectedUser="selectedUser" v-if="selectedUser"/>
            <UserForm :serverValidErrors="serverValidationErrors" @create="create" v-else/>
        </CreateUpdateDialog>

        <div class="table-container">
            <button class="btn btn-outline-success create-btn" @click="showCreateDialog">Create</button>

                <table class="table table-bordered">
                    <thead class="table-dark ">
                    <tr>
                        <th>ID</th>
                        <th>Username</th>
                        <th>Email</th>
                        <th>Roles</th>
                        <th>Created date</th>
                        <th>Updated date</th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr v-for=" (user, index) in users" :key="user.id">
                        <td><b>{{user.id}}</b></td>
                        <td>{{user.username}}</td>
                        <td>{{user.email}}</td>
                        <td><p v-for=" role in user.roles" :key="role.id">
                            {{role.name}}
                        </p></td>
                        <td>{{convertDateTime(user.created)}}</td>
                        <td>{{convertDateTime(user.updated)}}</td>
                        <td>
                            <div class="input-group table-buttons-group">
                                <button @click="showUpdateDialog(user,index)" class="btn btn-outline-warning">Change
                                </button>
                                <button @click="remove(user.id,index)" class="btn btn-outline-danger">Delete</button>
                            </div>
                        </td>
                    </tr>
                    </tbody>
                </table>
        </div>
        <div>
            <PaginationComponent v-if="totalPages" :current-page="currentPage" :total-pages="totalPages" @updatePage="updatePage"/>
        </div>
    </div>
</template>

<script>
    import CreateUpdateDialog from "@/components/DialogWindow";
    import UserForm from "@/components/AdminComponents/UserComponents/UserForm";
    import UserService from "@/services/UserService";
    import updateArray from "@/functions";
    import ConvertDateTime from "@/functions/ConvertDateTime";
    import PaginationComponent from "@/components/PaginationComponent";
    import RoleService from "@/services/RoleService";
    export default {
        name: "UserList",
        components: {UserForm, CreateUpdateDialog,PaginationComponent},
        data() {
            return {
                users: [],
                selectedUser: null,
                selectedIndex: null,
                searchId: null,
                searchName: "",
                search:false,
                dialogVisible: false,
                roles:[],
                selectedRole:null,
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
                this.selectedUser = null;
                this.dialogVisible = true
            },
            showUpdateDialog(user, index) {
                this.selectedIndex = index;
                this.selectedUser = user;
                this.dialogVisible = true
            },
            updatePage(page){
                if(this.search){
                    this.FetchData(page,this.searchName,this.selectedRole)
                }
                else{
                    this.FetchData(page,null,this.selectedRole)
                }
            },
            async FetchData(page,username,roleId) {
                try {
                    await UserService.getAll(page,username,roleId).then(response => {
                        this.users =  response.data["content"]
                        this.currentPage = response.data["currentPage"]+1
                        this.totalPages = response.data["totalPages"]
                    });

                } catch (e) {
                    alert("Ошибка");

                }

            },
            remove(id, index) {
                UserService.delete(id).then(response => {
                        if (response.status === 204) {
                            this.users.splice(index, 1);
                        }
                    }
                ).catch(error => console.log(error));


            },
            create(user) {
                UserService.create(user).then(response => this.users.push(response.data)).catch(error => {
                    if(error.response.status==400){
                        this.serverValidationErrors=error.response.data.messages
                    }
                });
            },
            update(user) {
                UserService.update(user.id, user)
                    .then(response => updateArray(this.selectedIndex, this.users, response.data))
                    .catch(error => {
                        if(error.response.status==400){
                            this.serverValidationErrors=error.response.data.messages
                        }
                    });
            },
            searchByName() {
                this.search=true;
                this.FetchData(null,this.searchName,this.selectedRole);
            },
            searchById() {
                if (this.searchId != null) {
                     UserService.getById(this.searchId,"admin")
                        .then(response => {
                                this.users = [];
                                this.users.push(response.data);
                                this.currentPage = 1
                                this.totalPages = 1
                            }
                        )
                        .catch(error => alert(error.response.data.message));
                }
            },
            searchByRole(id){
                if(id!=null){
                    this.selectedRole=id;
                    this.FetchData(null,this.searchName,this.selectedRole);
                }
                else {
                    this.selectedRole=null;
                    this.FetchData(null,this.searchName);
                }
            },
            async FetchRoles() {
                try {
                    await RoleService.getAll().then(response => {
                        this.roles = response.data
                    });

                } catch (e) {
                    alert("Ошибка");

                }

            }
        },

        mounted() {
            this.FetchData();
            this.FetchRoles();
        }

    }
</script>

<style scoped>
.dropdown{
    margin-top: 10px;
    margin-bottom: 10px;
}
</style>