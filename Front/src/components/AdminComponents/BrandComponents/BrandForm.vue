<template>
  <form @submit.prevent>
      <h3 class="form-header">Brand form</h3>
      <div v-for="(error,index) in serverValidErrors" :key="index">
          <ErrorMsgValidator :error-text="error"/>
      </div>
     <div class="col form-floating">
                      <input type="text" v-model="brand.name"  placeholder=" " class="form-control" id="Name" :class="{'is-invalid':nameErrors!=null}">
                      <label for="Name">Name</label>
                 <ErrorMsgValidator :error-text="nameErrors" v-if="nameErrors!=null"/>

     </div>
      <div class="create-form-btn">
          <input type="button" class="btn btn-success" v-if="brandId" value="Change" @click="update(brandId)">
          <input type="button"  class="btn btn-success"  v-else value="Create" @click="create">
      </div>
  </form>
</template>

<script>

    import BrandService from "@/services/BrandService";
    import ErrorMsgValidator from "@/components/ForUserComponents/ErrorMsgValidator";
    import {required} from 'vuelidate/lib/validators'
    export default {
        name: "BrandForm",
        components: {ErrorMsgValidator},
        props:["brandId","serverValidErrors"],
        data(){
            return {
                brand:{
                    id:null,
                    name:''
                }
            }
        },
        validations:{
            brand:{
                name:{required}
            }


        },
        computed:{
            nameErrors(){
                if(!this.$v.brand.name.required && this.$v.brand.name.$dirty){
                    return "Name must not be empty"
                }

                return null;
            }
        },
        methods:{
            create(){
                if(this.$v.$invalid){
                    this.$v.$touch()
                    return
                }
                this.$emit('create',this.brand);
            },
            update(id){
                if(this.$v.$invalid){
                    this.$v.$touch()
                    return
                }
                this.brand.id=id;
                this.$emit('update',this.brand);
            }
        },
        mounted() {
            if(this.brandId!=null){
                BrandService.getById(this.brandId) .then(response =>
                {this.brand=response.data}).catch(error=>console.log(error));
            }

        }

    }
</script>

<style scoped>
</style>