<template>
    <form @submit.prevent>
        <h3>Role form</h3>
        <div v-for="(error,index) in serverValidErrors" :key="index">
            <ErrorMsgValidator :error-text="error"/>
        </div>
        <div class="col form-floating">
            <input type="text" v-model="role.name"  placeholder=" " class="form-control" id="Name" :class="{'is-invalid':nameErrors!=null}">
            <label for="Name">Name</label>
            <ErrorMsgValidator :error-text="nameErrors" v-if="nameErrors!=null"/>
        </div>
        <div class="create-form-btn">
            <input type="button" class="btn btn-success" v-if="selectedRole" value="Change" @click="update()">
            <input type="button"  class="btn btn-success"  v-else value="Create" @click="create">
        </div>
    </form>
</template>

<script>
    import ErrorMsgValidator from "@/components/ForUserComponents/ErrorMsgValidator";
    import {required} from 'vuelidate/lib/validators'

    export default {
        name: "RoleForm" ,
        props:["selectedRole","serverValidErrors"],
        components: {ErrorMsgValidator},
        data(){
            return {
                role:{
                    id:null,
                    name:''
                }
            }
        },
        validations:{
            role:{
                name:{required}
            }


        },
        computed:{
            nameErrors(){
                if(!this.$v.role.name.required && this.$v.role.name.$dirty){
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
                this.$emit('create',this.role);
            },
            update(){
                if(this.$v.$invalid){
                    this.$v.$touch()
                    return
                }
                this.$emit('update',this.role);
            }
        },
        mounted() {
            if(this.selectedRole!=null){
                this.role=this.selectedRole;
            }

        }
    }
</script>

<style scoped>
h3{
    margin-bottom: 30px;
}
</style>