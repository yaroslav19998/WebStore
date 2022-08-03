<template>
    <form @submit.prevent>
        <h3 class="form-header">Characteristic form</h3>
        <div v-for="(error,index) in serverValidErrors" :key="index">
            <ErrorMsgValidator :error-text="error"/>
        </div>
        <div class="form-floating">
            <input type="text" v-model="characteristic.name" placeholder=" " class="form-control" id="Name" :class="{'is-invalid':nameErrors!=null}">
            <label for="Name">Name</label>
            <ErrorMsgValidator :error-text="nameErrors" v-if="nameErrors!=null"/>
        </div>
        <div class="form-floating">

            <select class="form-select" id="characteristicDateType" v-model="characteristic.valueType" :class="{'is-invalid':valueTypeErrors!=null}">
                <option value="DECIMAL">Decimal</option>
                <option value="DATE">Date</option>
                <option value="INT">Integer</option>
                <option value="SINGLESTRING">Single string value</option>
                <option value="MULTIPLESTRING">Multiple string vlaue</option>
            </select>
            <label for="characteristicDateType">Data type</label>
            <ErrorMsgValidator :error-text="valueTypeErrors" v-if="valueTypeErrors!=null"/>
        </div>
        <div v-if="isString">
            <p>
                <button type="button" class="btn btn-secondary btn-add-value" @click="addString">Add string value</button>
            </p>
            <div class="value-container container-fluid">
                <div class="row" v-for="(string, index) in  stringsValues" :key="index">
                    <div class="col-9 form-floating">
                        <input type="text" class="form-control" :id="'characteristicSelect'+index" placeholder=" "
                               v-model="string.value">
                        <label :for="'characteristicSelect'+index">Value</label>
                    </div>
                    <div class="col-3">
                        <button type="button" class="btn btn-danger" @click="removeString(string)">Delete string
                        </button>
                    </div>
                </div>
            </div>
        </div>
        <div class="create-form-btn">
            <input type="button" class="btn btn-success btn-lg" v-if="selectedCharacteristic" value="Change"
                   @click="update()">
            <input type="button" class="btn btn-success btn-lg" v-else value="Create" @click="create">
        </div>

    </form>
</template>

<script>
    import ErrorMsgValidator from "@/components/ForUserComponents/ErrorMsgValidator";
    import {required} from 'vuelidate/lib/validators'
    export default {
        name: "CharacteristicForm",
        props: ["selectedCharacteristic","serverValidErrors"],
        components: {ErrorMsgValidator},
        data() {
            return {
                characteristic: {
                    id: null,
                    name: null,
                    valueType: null
                },
                stringsValues: [],
                deleted:[],
                added:[]
            }
        },
        validations:{
            characteristic:{
                name:{required},
                valueType: {required}
            }


        },
       computed: {
            isString() {
                return this.characteristic.valueType == 'SINGLESTRING' || this.characteristic.valueType == 'MULTIPLESTRING'
            },
            nameErrors(){
                if(!this.$v.characteristic.name.required && this.$v.characteristic.name.$dirty){
                    return "Name must not be empty"
                }


                return null;
            },
           valueTypeErrors(){
               if(!this.$v.characteristic.valueType.required && this.$v.characteristic.valueType.$dirty){
                   return "Select a data type"
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
                var CharacteristicDTO = {characteristic: this.characteristic, stringList: null};
                if (this.isString) {
                    CharacteristicDTO.stringList = this.stringsValues.map(function (item) {
                        return item.value;
                    });
                }
                this.$emit('create', CharacteristicDTO);
            },
            update() {
                if(this.$v.$invalid){
                    this.$v.$touch()
                    return
                }
                var CharacteristicDTO= {characteristic: this.characteristic, stringList: null};
                if (this.isString) {
                    CharacteristicDTO.stringList = this.stringsValues.map(function (item) {
                        return item.value;
                    });
                }
                if(this.deleted.length>0){
                    CharacteristicDTO.deleted=this.deleted
                }
                if(this.selectedCharacteristic != null){
                    this.added=this.stringsValues.filter(string=>!string.id);
                    CharacteristicDTO.added=this.added
                }

                this.$emit('update', CharacteristicDTO);
            },
            addString() {
                this.stringsValues.push({value: ""});
            },
            removeString(string) {
                if(string.id!=null){
                    this.deleted.push(string)
                }
                if (this.stringsValues.length >= 1) {
                    let index = this.stringsValues.indexOf(string);
                    this.stringsValues.splice(index, 1);
                }
                console.log(this.deleted);
            }
        },
        mounted() {
            if (this.selectedCharacteristic != null) {
               this.characteristic={...this.selectedCharacteristic}
               if(this.characteristic.valueType== 'SINGLESTRING'){
                   this.stringsValues=this.characteristic.characteristicSingleStringValues;
               }
                if(this.characteristic.valueType== 'MULTIPLESTRING'){
                    this.stringsValues=this.characteristic.characteristicMultipleStringValues;
                }
            }
            console.log("characteristic")
            console.log(this.characteristic)
            console.log("characteristic")


        }
    }
</script>

<style scoped>
    select {
        margin: 5px 0
    }
    .btn-add-value{margin-bottom: 10px;margin-top:10px;}
    .value-container {
        padding: 0;
    }

    div {
        margin: 10px 0;

    }

    form {
        width: 500px;
    }
</style>