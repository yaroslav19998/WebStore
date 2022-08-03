<template>
    <form @submit.prevent>
        <h3 class="form-header">Category form</h3>
        <div v-for="(error,index) in serverValidErrors" :key="index">
            <ErrorMsgValidator :error-text="error"/>
        </div>
        <div class="form-floating">
            <input type="text" v-model="category.name" placeholder=" " class="form-control" id="Name" :class="{'is-invalid':nameErrors!=null}">
            <label for="Name">Name</label>
            <ErrorMsgValidator :error-text="nameErrors" v-if="nameErrors!=null"/>

        </div>
        <div class="form-floating">
            <select class="form-select" id="category" v-model="category.parentCategory">
                <option v-for="parentCategory in parentRelateCategories" :value="parentCategory"
                        :key="parentCategory.id">
                    {{ parentCategory.name }}
                </option>
            </select>
            <label for="category">Parent category</label>
        </div>
        <div class="form-floating">
            <select class="form-select" style="height: 150px" id="brands" multiple aria-label="multiple" v-model="category.brands">
                <option  v-for="brand in allBrands" :value="brand"
                         :key="brand.id">{{brand.name}}</option>
            </select>

            <label for="brands">brands</label>
        </div>

        <div>
            <div>
                <div>
                    <h4>Characteristics</h4>
                    <p>
                        <button type="button" class="btn btn-secondary" @click="addCharacteristic">Add characteristic
                        </button>
                    </p>
                </div>
                <div class="container-fluid characteristic-container">
                      <div class="row" v-for="(characteristic,index) in  category.characteristics" :key="index">
                          <div class="col-10 select-characteristic">
                            <select class="form-select " :id="index"
                                    v-model="category.characteristics[index].id">
                                <option v-for="selectCharacteristic in selectCharacteristics"
                                        v-bind:value="selectCharacteristic.id"
                                        :key="selectCharacteristic.id">
                                   Name: {{ selectCharacteristic.name }} type: {{selectCharacteristic.valueType}}
                                </option>
                            </select>
                          </div>
                          <div class="col-2 delete-btn">
                            <button type="button" class="btn btn-danger" @click="removeString(characteristic)">
                                Delete
                            </button>
                          </div>
                    </div>

                </div>
            </div>

            <div class="create-form-btn">
                <input type="button" class="btn btn-success btn-lg" v-if="selectedCategory" value="Change"
                       @click="update()">
                <input type="button" class="btn btn-success btn-lg" v-else value="Create" @click="create">
            </div>
        </div>
    </form>
</template>

<script>
    import CategoryService from "@/services/CategoryService";
    import CharacteristicService from "@/services/СharacteristicService";
    import BrandService from "@/services/BrandService";
    import ErrorMsgValidator from "@/components/ForUserComponents/ErrorMsgValidator";
    import {required} from 'vuelidate/lib/validators'
    export default {
        name: "CategoryForm",
        props: ["selectedCategory","serverValidErrors"],
        components: {ErrorMsgValidator},
        data() {
            return {
                category: {
                    id: null,
                    name: '',
                    characteristics: [],
                    brands:[],
                    parentCategory:null
                },
                parentRelateCategories:[],
                allBrands:[],
                selectCharacteristics: []
            }
        },
        validations:{
            category:{
                name:{required}
            }


        },
        computed:{
            nameErrors(){
                if(!this.$v.category.name.required && this.$v.category.name.$dirty){
                    return "Name must not be empty"
                }

                return null;
            }
        },
        methods: {
            create() {
                if(this.$v.$invalid){
                    this.$v.$touch()
                    return
                }
                this.$emit('create', this.category);
            },
            update() {
                if(this.$v.$invalid){
                    this.$v.$touch()
                    return
                }
                this.$emit('update', this.category);
            },
            addCharacteristic() {
                this.category.characteristics.push({id:null});
            },
            removeString(characteristic) {
                if (this.category.characteristics.length >= 1) {
                    let index = this.category.characteristics.indexOf(characteristic);
                    this.category.characteristics.splice(index, 1);
                }
            }
        },
        mounted() {
            CharacteristicService.getAll(null,"relations").then(
                response => {
                    this.selectCharacteristics = response.data["content"]
                }).catch(error => console.log(error));
            CategoryService.getAll(null,"parent relations").then(
                response => {
                    this.parentRelateCategories = response.data["content"]
                }).catch(error => console.log(error));
            BrandService.getAll(null,"relations").then(
                response => {
                    this.allBrands = response.data["content"]
                }).catch(error => console.log(error));
            if (this.selectedCategory != null) {
                this.category={...this.selectedCategory}

             }



        }
    }
</script>

<style scoped>
    select {
        margin: 5px 0
    }

    div {
        margin: 10px 0;

    }
    .brands-select{
        height: 500px;
    }
    form {
        width: 600px;
    }
    .characteristic-container{
      padding: 0;
    }
.select-characteristic{padding: 0}
    .delete-btn{padding-right:0;padding-top: 5px}
</style>