<template>
    <div class="main-component-container container-fluid">
        <h1>Registration</h1>
        <div v-for="(error,index) in serverErrors" :key="index">
            <ErrorMsgValidator :error-text="error"/>
        </div>
        <form class="registrationForm">
            <div class="form-floating">
                <input type="text" class="form-control" id="Login" placeholder="" v-model="user.username" :class="{'is-invalid':nameErrors!=null}">
                <label for="Login">Name</label>
                <ErrorMsgValidator :error-text="nameErrors" v-if="nameErrors!=null"/>
            </div>
            <div class="form-floating">
                <input type="email" v-model="user.email" placeholder=" "  class="form-control" :class="{'is-invalid':emailErrors!=null}" id="Email">
                <label for="Email">Email</label>
                <ErrorMsgValidator :error-text="emailErrors" v-if="emailErrors!=null"/>
            </div>
            <div class="form-floating">
                <input type="password" class="form-control" id="Password" v-model="user.password" placeholder=" " :class="{'is-invalid':passwordErrors!=null}">
                <label for="Password">Password</label>
                <ErrorMsgValidator :error-text="passwordErrors" v-if="passwordErrors!=null"/>
            </div>
            <div class="form-floating ">
                <input type="password" class="form-control" id="confirmPassword" v-model="user.confirmPassword"
                       placeholder=" "  :class="{'is-invalid':confirmPasswordErrors!=null}">
                <label for="confirmPassword">Confirm Password</label>
                <ErrorMsgValidator :error-text="confirmPasswordErrors" v-if="confirmPasswordErrors!=null"/>
            </div>
            <input type="button" value="Registration" class="btn btn-primary reg-btn" @click="create">
        </form>
    </div>
</template>

<script>
    import AuthService from "@/services/AuthService";
    import ErrorMsgValidator from "@/components/ForUserComponents/ErrorMsgValidator";
    import {required,minLength,maxLength,sameAs,email} from 'vuelidate/lib/validators'
    import {mapGetters} from 'vuex'

    export default {
        name: "registrationform",
        components: {ErrorMsgValidator},
        data() {
            return {
                user:
                    {
                        username: '', password: null,email:null,  confirmPassword: null
                    },

            }
        },
        validations:{
            user: {
                username: {required, minLength: minLength(2)},
                password: {required, minLength: minLength(8),maxLength:maxLength(12), sameAs: sameAs('confirmPassword')},
                confirmPassword: {sameAs: sameAs('password')},
                email: {required, email},

            },

        },
        computed:{

            ...mapGetters({serverErrors: 'moduleAuth/getErrors'}),
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
                if(!this.$v.user.password.required && this.$v.user.password.$dirty){
                    return "Password must not be empty"
                }
                if(!this.$v.user.password.minLength && this.$v.user.password.$dirty){
                    return "Password must be at least 8 characters";
                }
                if(!this.$v.user.password.maxLength && this.$v.user.password.$dirty){
                    return "Password must be less than 12 characters";
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
        },

        methods: {
            create: function () {
                if (this.$v.$invalid) {
                    this.$v.$touch();
                    return
                }

                AuthService.register(this.user);
            }

        }
    }
</script>

<style scoped>
    .main-component-container {
        margin-top: 20px;
        text-align: center;
    }

    .form-floating {
        margin-top: 15px;
        margin-bottom: 15px;
    }

    .registrationForm {
        margin-left: 30%;
        margin-right: 30%;
    }

    .reg-btn {
        width: 100%;
    }
</style>