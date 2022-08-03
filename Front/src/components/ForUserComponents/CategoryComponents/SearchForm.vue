<template>
    <form @submit.prevent>
        <div>
            <span class="characteristic-title">Name</span>
            <div class="input-group">
                <input class="form-control"
                       type="text"
                       placeholder="Name"
                       v-model="productName"/>
            </div>
        </div>
        <div v-if="brands!=null&&brands.length>0">
            <span class="characteristic-title">Brands</span>
            <div  class="form-check" v-for="brand in brands" :key="brand.id">
                <input  class="form-check-input"
                        type="checkbox"
                        :id="'brand'+ brand.id" :value="brand.id" v-model="brandIds">
                <label class="form-check-label" :for="'brand'+ brand.id">{{brand.name}}</label>
            </div>
        </div>
        <div>
            <span class="characteristic-title">Price</span>
            <div class="input-group">
                <input class="form-control"
                       type="number"
                       maxlength="9"
                       placeholder="От"
                       v-model="price.minValue"
                       :class="{'is-invalid':this.checkErrorValue(price.minValue,price.maxValue)}"
                />
                <input class="form-control"
                       type="number"
                       maxlength="9"
                       v-model="price.maxValue"
                       placeholder="До"
                />
                <ErrorMsgValidator :error-text="errorMsg" v-if="this.checkErrorValue(price.minValue,price.maxValue)"/>
            </div>
        </div>
        <div v-if="searchParams!=null">
            <div class="searchParam" v-for="(searchParam,index) in searchParams" :key="index">
                <span class="characteristic-title">{{searchParam.characteristic.name}}</span>
                <div v-if="searchParam.characteristic.valueType=='DATE'" class="input-group">
                    <input type="date"  class="form-control" placeholder=" " v-model="searchParam.dateValue.minValue"
                           :class="{'is-invalid':checkErrorValue(searchParam.dateValue.minValue,searchParam.dateValue.maxValue)}"/>
                    <input type="date"  class="form-control" v-model="searchParam.dateValue.maxValue"/>
                    <ErrorMsgValidator :error-text="errorMsg" v-if="checkErrorValue(searchParam.dateValue.minValue,searchParam.dateValue.maxValue)"/>
                </div>
                <div v-if="searchParam.characteristic.valueType=='DECIMAL'"  class="input-group"  >
                    <input class="form-control"
                           type="number"
                           maxlength="9"
                           placeholder="От"
                           v-model="searchParam.decimalValue.minValue"
                           :class="{'is-invalid':checkErrorValue(searchParam.decimalValue.minValue,searchParam.decimalValue.maxValue)}"
                    />
                    <input class="form-control"
                           type="number"
                           maxlength="9"
                           v-model="searchParam.decimalValue.maxValue"
                           placeholder="До"
                    />
                    <ErrorMsgValidator :error-text="errorMsg" v-if="checkErrorValue(searchParam.decimalValue.minValue,searchParam.decimalValue.maxValue)"/>
                </div>

                <div  class="input-group" v-if="searchParam.characteristic.valueType=='INT'">
                    <input class="form-control"
                           type="text"
                           maxlength="9"
                           onkeyup="this.value = this.value.replace (/\D/, '')"
                           placeholder="От"
                           v-model="searchParam.intValue.minValue"
                           :class="{'is-invalid':checkErrorValue(searchParam.intValue.minValue,searchParam.intValue.maxValue)}"
                    />
                    <input class="form-control" type="text"
                           maxlength="9"
                           onkeyup="this.value = this.value.replace (/\D/, '')"
                           v-model="searchParam.intValue.maxValue"
                           placeholder="До"
                    />
                    <ErrorMsgValidator :error-text="errorMsg" v-if="checkErrorValue(searchParam.intValue.minValue,searchParam.intValue.maxValue)"/>
                </div>
                <div  v-if="searchParam.characteristic.valueType=='SINGLESTRING'">
                    <div class="form-check"  v-for="stringValue in searchParam.characteristic.characteristicSingleStringValues" :key="stringValue.id">
                        <input  class="form-check-input"
                                type="checkbox"
                                :value="stringValue.id"
                                v-model="searchParam.singleStringValues"
                                :id="'singleString'+ stringValue.id">
                        <label class="form-check-label" :for="'singleString'+ stringValue.id">{{stringValue.value}}</label>
                    </div>
                </div>
                <div  v-if="searchParam.characteristic.valueType=='MULTIPLESTRING'">
                    <div  class="form-check" v-for="stringValue in searchParam.characteristic.characteristicMultipleStringValues" :key="stringValue.id">
                        <input  class="form-check-input"
                                type="checkbox"
                                :id="'multipleString'+ stringValue.id" :value="stringValue.id" v-model="searchParam.multipleStringValues">
                        <label class="form-check-label" :for="'multipleString'+ stringValue.id">{{stringValue.value}}</label>
                    </div>
                </div>

            </div>
        </div>

        <div><button class="btn btn-lg btn-primary btn-block" @click="searchProducts">Search</button></div>
    </form>
</template>

<script>
    import ErrorMsgValidator from "@/components/ForUserComponents/ErrorMsgValidator";
    export default {
        name: "SearchForm",
        props:{characteristics:{type:Array},brands:{type: Array}},
        components: {ErrorMsgValidator},
        data(){
            return{
                searchParams:[],
                price:{minValue:null,maxValue:null},
                brandIds:[],
                productName:"",
                errorMsg:"Minimum value cannot be greater than the maximum"

            }
        },
        methods: {
            searchProducts() {
                let searchArray = this.searchParams.slice();
                searchArray.push({price: this.price});
                searchArray.push({brandIds: this.brandIds});
                searchArray.push({name: this.productName});
                let errors=0;
                console.log(this.price);
                if(this.checkErrorValue(this.price.minValue,this.price.maxValue)){
                    errors++;
                }
                this.searchParams.forEach(searchParam=>
                {
                    if(searchParam.characteristic.valueType=='INT'){
                        if(this.checkErrorValue(searchParam.intValue.minValue,searchParam.intValue.maxValue)){
                            errors++;
                        }
                    }
                    if(searchParam.characteristic.valueType=='DECIMAL'){
                        if(this.checkErrorValue(searchParam.decimalValue.minValue,searchParam.decimalValue.maxValue)){
                            errors++;
                        }
                    }
                    if(searchParam.characteristic.valueType=='DATE'){
                        if(this.checkErrorValue(searchParam.dateValue.minValue,searchParam.dateValue.maxValue)){
                            errors++;
                        }
                    }
                });
                if(errors==0){
                     this.$emit('searchProducts', searchArray);
                }
                else {alert("incorrectly filled field")}

            },
            checkErrorValue(minValue, maxValue) {
                if(minValue==null || minValue==""){
                    return false
                }
                if(maxValue==null || maxValue==""){
                    return false
                }

                return minValue >= maxValue;

            },
            createParams(){
                if(this.characteristics!=null){
                   this.characteristics.forEach(
                    characteristic=>{
                        let param={characteristic:characteristic};
                        if (characteristic.valueType == 'DATE') {
                            param['dateValue']={minValue:null,maxValue:null};
                        } else if (characteristic.valueType == 'DECIMAL') {
                            param['decimalValue']={minValue:null,maxValue:null};
                        } else if (characteristic.valueType == 'INT') {
                            param['intValue']={minValue:null,maxValue:null};
                        } else if (characteristic.valueType == 'MULTIPLESTRING') {
                            param['multipleStringValues']=[];
                           }
                        else if (characteristic.valueType == 'SINGLESTRING') {
                            param['singleStringValues']=[];
                        }
                        this.searchParams.push(param);

                    });
            }}
        },
        mounted(){
            this.createParams();
        }
    }
</script>

<style scoped>
    .input-group{
        margin-left: 0;
    }
    .searchParam{
        margin-top: 15px;
        margin-bottom: 15px;
    }
    button{
        width: 100%;
    }
    .characteristic-title{
        font-weight: bold;
        font-size: 1.1em;
    }
</style>