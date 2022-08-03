<template>
    <div>
        <CreateUpdateDialog :show.sync="dialogVisible">
            <RoleForm :serverValidErrors="serverValidationErrors" @update="update" :selectedRole="selectedRole" v-if="selectedRole"/>
            <RoleForm :serverValidErrors="serverValidationErrors" @create="create" v-else/>
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
                <tr v-for=" (role, index) in roles" :key="role.id">
                    <td><b>{{role.id}}</b></td>
                    <td>{{role.name}}</td>
                    <td>{{convertDateTime(role.created)}}</td>
                    <td>{{convertDateTime(role.updated)}}</td>
                    <td>
                        <div class="input-group table-buttons-group">
                            <button @click="showUpdateDialog(role,index)" class="btn btn-outline-warning">Change
                            </button>
                            <button @click="remove(role.id,index)" class="btn btn-outline-danger">Delete</button>
                        </div>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</template>

<script>
      import CreateUpdateDialog from "@/components/DialogWindow";
    import RoleForm from "@/components/AdminComponents/RoleComponents/RoleForm";
    import RoleService from "@/services/RoleService";
    import updateArray from "@/functions";
    import ConvertDateTime from "@/functions/ConvertDateTime";

    export default {
        name: "RoleList",
        components: {RoleForm, CreateUpdateDialog},
        data() {
            return {
                roles: [],
                selectedRole: null,
                selectedIndex: null,
                dialogVisible: false,
                serverValidationErrors:null
            }
        },
        methods: {
            convertDateTime(date) {
                return ConvertDateTime(date);
            },
            showCreateDialog() {
                this.selectedIndex = null;
                this.selectedRole = null;
                this.dialogVisible = true
            },
            showUpdateDialog(role, index) {
                this.selectedIndex = index;
                this.selectedRole = role;
                this.dialogVisible = true
            },

            async FetchRoles() {
                try {
                    await RoleService.getAll().then(response => {
                        this.roles = response.data
                    });

                } catch (e) {
                    console.log(e);

                }

            },
            remove(id, index) {
                RoleService.delete(id).then(response => {
                        if (response.status === 204) {
                            this.roles.splice(index, 1);
                        }
                    }
                ).catch(error => console.log(error));


            },
            create(role) {
                RoleService.create(role).then(response => this.roles.push(response.data)).catch(error =>  {
                    if(error.response.status==400){
                        this.serverValidationErrors=error.response.data.messages
                    }
                });
            },
            update(role) {
                RoleService.update(role.id, role)
                    .then(response => updateArray(this.selectedIndex, this.roles, response.data))
                    .catch(error =>  {
                        if(error.response.status==400){
                            this.serverValidationErrors=error.response.data.messages
                        }
                    });
            },
        },
        mounted() {
            this.FetchRoles();
        }

    }
</script>

<style scoped>

</style>