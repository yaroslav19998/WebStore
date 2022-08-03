<template>
    <div class="main-component-container container-fluid">
        <h1>Authorization</h1>
        <div v-for="(error,index) in serverErrors" :key="index">
            <ErrorMsgValidator :error-text="error"/>
        </div>
        <form  class="authForm">
            <div class="form-floating">
                <input type="text" class="form-control" id="Login" placeholder="" v-model="user.username" :class="{'is-invalid':nameErrors!=null}">
                <label for="Login">Login</label>
                <ErrorMsgValidator :error-text="nameErrors" v-if="nameErrors!=null"/>
            </div>
            <div class="form-floating">
                <input type="password" class="form-control" id="Password"  v-model="user.password" placeholder=" "  :class="{'is-invalid':passwordErrors!=null}">
                <label for="Password">Password</label>
                <ErrorMsgValidator :error-text="passwordErrors" v-if="passwordErrors!=null"/>
            </div>
            <input type="button" value="Auth" class="btn btn-primary auth-btn" @click="auth">
        </form>
    </div>
</template>

<script>
    import  {mapActions} from 'vuex'
    import ErrorMsgValidator from "@/components/ForUserComponents/ErrorMsgValidator";
    import {required,minLength,maxLength} from 'vuelidate/lib/validators'
    import {mapGetters} from 'vuex'
    export default {
        name: "loginform",
        components: {ErrorMsgValidator},
        data() {
            return {
                user: {
                    username: '', password: ''
                }

            }
        },
        validations:{
            user: {
                username: {required, minLength: minLength(2)},
                password: {required, minLength: minLength(3),maxLength:maxLength(12)},

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
            passwordErrors(){
                if(!this.$v.user.password.required && this.$v.user.password.$dirty){
                    return "Password must not be empty"
                }
                if(!this.$v.user.password.minLength && this.$v.user.password.$dirty){
                    return "Password must be at least 3 characters";
                }
                if(!this.$v.user.password.maxLength && this.$v.user.password.$dirty){
                    return "Password must be less than 12 characters";
                }
                return null;
            },
        },
        methods: {
             ...mapActions(
                    {
                        authorization: "moduleAuth/login"
                    }),
                auth: function () {
                    if(this.$v.$invalid){
                        this.$v.$touch()
                        return
                    }
                    this.authorization(this.user)
                }
            }
        }
</script>

<style scoped>
    .main-component-container{
        margin-top: 20px;
        text-align: center;
    }
    .form-floating{
        margin-top: 15px;
        margin-bottom: 15px;
    }
    .authForm{
        margin-left: 30%;
        margin-right: 30%;
    }
    .auth-btn{
        width: 100%;
    }
</style>