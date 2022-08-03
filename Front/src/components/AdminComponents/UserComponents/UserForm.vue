<template>
    <form @submit.prevent>
        <h3 class="form-header">User form</h3>
        <div v-for="(error,index) in serverValidErrors" :key="index">
            <ErrorMsgValidator :error-text="error"/>
        </div>
        <div class="form-floating">
            <input type="text" v-model="user.username" placeholder=" " class="form-control"  :class="{'is-invalid':nameErrors!=null}" id="Name">
            <label for="Name">Name</label>
            <ErrorMsgValidator :error-text="nameErrors" v-if="nameErrors!=null"/>
        </div>
        <div class="form-floating">
            <input type="password" v-model="user.password" placeholder=" " class="form-control" :class="{'is-invalid':passwordErrors!=null}" id="Password">
            <label for="Password">Password</label>
            <ErrorMsgValidator :error-text="passwordErrors" v-if="passwordErrors!=null"/>
        </div>
        <div class="form-floating">
            <input type="password" v-model="user.confirmPassword" placeholder=" " class="form-control" id="confirmPassword" :class="{'is-invalid':confirmPasswordErrors!=null}">
            <label for="confirmPassword">Confirm password</label>
            <ErrorMsgValidator :error-text="confirmPasswordErrors" v-if="confirmPasswordErrors!=null"/>
        </div>
        <div class="form-floating">
            <input type="email" v-model="user.email" placeholder=" "  class="form-control" :class="{'is-invalid':emailErrors!=null}" id="Email">
            <label for="Email">Email</label>
            <ErrorMsgValidator :error-text="emailErrors" v-if="emailErrors!=null"/>
        </div>
        <div>
            <h4>Roles</h4>
            <div v-for="role in roles"
                 :key="role.id">
                <input class="form-check-input"
                       type="checkbox"
                       :id="'role_'+role.id"
                       :value="role"
                       v-model="user.roles" :class="{'is-invalid':rolesErrors!=null}">
                <label class="form-check-label" :for="'role_'+role.id">{{role.name}}</label>
                <ErrorMsgValidator :error-text="rolesErrors" v-if="rolesErrors!=null"/>

            </div>
        </div>
        <div>


            <div class="create-form-btn">
                <input type="button" class="btn btn-success btn-lg" v-if="selectedUser" value="Change"
                       @click="update()">
                <input type="button" class="btn btn-success btn-lg" v-else value="Create" @click="create">
            </div>
        </div>
    </form>
</template>

<script>
    import RoleService from "@/services/RoleService";
    import ErrorMsgValidator from "@/components/ForUserComponents/ErrorMsgValidator";
    import {required,minLength,sameAs,email,maxLength} from 'vuelidate/lib/validators'
    import {passwordValidator} from '@/validatorsJs'
    export default {
        name: "UserForm",
        components: {ErrorMsgValidator},
        props:["selectedUser","serverValidErrors"],
        data(){
            return {
                user:{
                    id:null,
                    username:'',
                    password:'',
                    confirmPassword:'',
                    email:'',
                    roles:[]
                },
                roles:[]
            }
        },
        validations:{
            user:{
                username:{required,minLength:minLength(2),maxLength:maxLength(15)},
                password: {passwordValidator,sameAs:sameAs('confirmPassword')},
                confirmPassword:{sameAs:sameAs('password')},
                email:{required,email},
                roles:{required}}

        },
        computed:{
            nameErrors(){
                if(!this.$v.user.username.required && this.$v.user.username.$dirty){
                    return "Username must not be empty"
                }
                if(!this.$v.user.username.minLength && this.$v.user.username.$dirty){
                    return "Username must be at least 2 characters";
                }
                return null;
            },
            emailErrors(){
                if(!this.$v.user.email.required && this.$v.user.email.$dirty){
                    return "Email must not be empty"
                }
                if(!this.$v.user.email.email && this.$v.user.email.$dirty){
                    return "Wrong email";
                }
                return null;
            },
            passwordErrors(){
                if(!this.$v.user.password.passwordValidator && this.$v.user.password.$dirty){
                    return "Password must be between 8 and 12 characters";
                }
                if(!this.$v.user.password.sameAs && this.$v.user.password.$dirty){
                    return "Passwords do not match";
                }
                return null;
            },
            confirmPasswordErrors(){
                if(!this.$v.user.confirmPassword.sameAs && this.$v.user.confirmPassword.$dirty){
                    return "Passwords do not match";
                }
                return null;
            },
            rolesErrors(){
                if(!this.$v.user.roles.required && this.$v.user.roles.$dirty){
                    return "Select at least one role"
                }
                return null
            }

        },

        methods:{
            create(){
                if(this.$v.$invalid){
                    this.$v.$touch()
                    return
                }
                this.$emit('create',this.user);
            },
            update(){
                if(this.$v.$invalid){
                    this.$v.$touch()
                    return
                }
                this.$emit('update',this.user);
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
        mounted(){
            if(this.selectedUser!=null){ this.user={...this.selectedUser};}
            this.FetchRoles();
        }
    }
</script>

<style scoped>
form>div{
    margin-bottom: 20px;
    margin-top:20px;
}
</style>