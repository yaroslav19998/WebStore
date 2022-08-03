<template>
    <div class="container-fluid main-component-container">

        <h1>Update info</h1>
        <form @submit.prevent>
            <div class="row">
                <div class="col-6 customer-info-column form-floating">
                    <input type="text" placeholder=" " class="form-control" id="Username"
                           v-model="user.username" :class="{'is-invalid':nameErrors!=null}" >
                    <label for="Username">Username</label>
                    <ErrorMsgValidator :error-text="nameErrors" v-if="nameErrors!=null"/>
                </div>
                <div class="col-6 customer-info-column form-floating">
                    <input type="text" placeholder=" " class="form-control" id="Email"
                           v-model="user.email" :class="{'is-invalid':emailErrors!=null}">
                    <label for="Email">Email</label>
                    <ErrorMsgValidator :error-text="emailErrors" v-if="emailErrors!=null"/>
                </div>
                <div class="col-6 customer-info-column form-floating">
                    <input type="password" placeholder=" " class="form-control" id="Password"
                           v-model="user.password" :class="{'is-invalid':passwordErrors!=null}" >
                    <label for="Password">Password</label>
                    <ErrorMsgValidator :error-text="passwordErrors" v-if="passwordErrors!=null"/>
                </div>
                <div class="col-6 customer-info-column form-floating">
                    <input type="password" placeholder=" " class="form-control" id="confirmPassword"
                           v-model="user.confirmPassword" :class="{'is-invalid':confirmPasswordErrors!=null}">
                    <label for="confirmPassword">Confirm password</label>
                    <ErrorMsgValidator :error-text="confirmPasswordErrors" v-if="confirmPasswordErrors!=null"/>
                </div>
            </div>
            <div class="row btn-row">
                <button class="btn btn-success" @click="update">Update</button>
            </div>
        </form>
    </div>
</template>

<script>
    import ErrorMsgValidator from "@/components/ForUserComponents/ErrorMsgValidator";
    import {required,minLength,sameAs,email,requiredIf} from 'vuelidate/lib/validators'
    import UserService from "@/services/UserService";
    import {passwordValidator} from '@/validatorsJs'

    export default {
        name: "UpdateUserInfo",
        props:["userInfo"],
        components: {ErrorMsgValidator},
        data(){
            return {
                user: {
                    id: null,
                    username: null,
                    password: null,
                    confirmPassword: null,
                    email: null,
                }
            }
        },
        validations: {
            user: {
                username: {required, minLength: minLength(2)},
                password: {passwordValidator, sameAs: sameAs('confirmPassword')},
                confirmPassword: {required:requiredIf(function () {
                        return this.password!=null
                    }),sameAs: sameAs('password')},
                email: {required, email},
            },
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
                if(!this.$v.user.confirmPassword.required && this.$v.user.confirmPassword.$dirty){
                    return "Password must not be empty"
                }
                if(!this.$v.user.confirmPassword.sameAs && this.$v.user.confirmPassword.$dirty){
                    return "Passwords do not match";
                }
                return null;
            },

        },
        methods:{
            update(){
                if(this.$v.$invalid){
                    this.$v.$touch()
                    return
                }
                UserService.update(this.user.id, this.user)
                        .then(alert("user changed"))
                        .catch(error => console.log(error));
            }
        },
        mounted(){
            this.user.id=this.userInfo.id;
            this.user.username=this.userInfo.username;
            this.user.email=this.userInfo.email;
            console.log(this.user)
        }
    }
</script>

<style scoped>
h1{margin-bottom: 20px}
.row div{
    margin-top: 20px;
    margin-bottom: 20px;
}

    .btn-row{
        margin-top: 20px;
        margin-left: 0px;
        margin-right: 0px;
    }
</style>