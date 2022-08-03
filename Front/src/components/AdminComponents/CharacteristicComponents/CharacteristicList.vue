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
            <CharacteristicForm :serverValidErrors="serverValidationErrors" @update="update" :selectedCharacteristic="selectedCharacteristic" v-if="selectedCharacteristic"/>
            <CharacteristicForm :serverValidErrors="serverValidationErrors" @create="create" v-else/>
        </CreateUpdateDialog>
        <div class="table-container">
            <button class="btn btn-outline-success create-btn" @click="showCreateDialog">Create</button>
            <table class="table table-bordered">
                <thead class="table-dark ">
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Data type</th>
                    <th>String values(if string value)</th>
                    <th>Created date</th>
                    <th>Updated date</th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <tr v-for=" (characteristic, index) in characteristics" :key="characteristic.id">
                    <td><b>{{characteristic.id}}</b></td>
                    <td>{{characteristic.name}}</td>
                    <td>{{characteristic.valueType}}</td>
                    <td>
                        <div v-if="characteristic.characteristicMultipleStringValues!=null">
                            <div v-for="value in characteristic.characteristicMultipleStringValues" :key="value.id">
                                {{value.value}}
                            </div>
                        </div>
                        <div v-if="characteristic.characteristicSingleStringValues!=null">
                            <div v-for="value in characteristic.characteristicSingleStringValues" :key="value.id">
                                {{value.value}}
                            </div>
                        </div>
                    </td>
                    <td>{{convertDateTime(characteristic.created)}}</td>
                    <td>{{convertDateTime(characteristic.updated)}}</td>
                <td>
                        <div class="input-group table-buttons-group">
                            <button @click="showUpdateDialog(characteristic,index)" class="btn btn-outline-warning">Изменить</button>
                            <button @click="remove(characteristic.id,index)" class="btn btn-outline-danger">Удалить</button>
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
    import CharacteristicService from "@/services/СharacteristicService";
    import CharacteristicForm from "@/components/AdminComponents/CharacteristicComponents/CharacteristicForm";
    import ConvertDateTime from "@/functions/ConvertDateTime";
    import PaginationComponent from "@/components/PaginationComponent";
    export default {
        name: "CharacteristicList",
        components: {CharacteristicForm, CreateUpdateDialog,PaginationComponent},
        data() {
            return {
                characteristics: [],
                selectedCharacteristic: null,
                selectedIndex: null,
                searchId: null,
                searchName: "",
                dialogVisible: false,
                search:false,
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
                this.selectedCharacteristic = null;
                this.dialogVisible = true
            },
            showUpdateDialog(characteristic, index) {
                this.selectedIndex = index;
                this.selectedCharacteristic = characteristic;
                this.dialogVisible = true
            },

            async FetchData(page,name) {
                await CharacteristicService.getAll(page,"admin",name).then(response => {
                    this.characteristics =  response.data["content"]
                    this.currentPage = response.data["currentPage"]+1
                    this.totalPages = response.data["totalPages"]
                }).catch(error => console.log(error));


            },
            remove(id, index) {
                CharacteristicService.delete(id).then(response => {
                        if (response.status === 204) {
                            this.characteristics.splice(index, 1);
                        }
                    }
                ).catch(error => console.log(error));


            },
            updatePage(page){
                if(this.search){
                    this.FetchData(page,this.searchName)
                }
                else{
                    this.FetchData(page)
                }
            },
            create(CharacteristicDTO) {
                CharacteristicService.create(CharacteristicDTO)
                    .then(response => this.characteristics.push(response.data))
                    .catch(error => {
                        if(error.response.status==400){
                            this.serverValidationErrors=error.response.data.messages
                        }
                    });
            },
            update(CharacteristicDTO) {
                CharacteristicService.update(CharacteristicDTO.characteristic.id, CharacteristicDTO)
                    .then(response => {   console.log(response.data)
                        updateArray(this.selectedIndex, this.characteristics, response.data)})
                    .catch(error => {
                        if(error.response.status==400){
                            this.serverValidationErrors=error.response.data.messages
                        }
                    });
            },
            searchByName() {
                this.search=true;
                this.FetchData(null,this.searchName);
            },
            searchById() {
                if (this.searchId != null) {
                    CharacteristicService.getById(this.searchId)
                        .then(response => {
                                this.characteristics = [];
                                this.characteristics.push(response.data);
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