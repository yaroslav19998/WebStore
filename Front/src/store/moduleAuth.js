import Vue from 'vue'
import Vuex from 'vuex'
import AuthService from "@/services/AuthService";
import jwtDecode from "jwt-decode";

Vue.use(Vuex);

const moduleAuth = {
    namespaced: true,
    state: {
        user: {
            id: localStorage.getItem('user_id'),
            username: localStorage.getItem('user_name'),
            roles: JSON.parse(localStorage.getItem('roles')) || null
        },
        tokens: {
            accessToken: localStorage.getItem('access_token'),
            refreshToken: localStorage.getItem('refresh_token'),
        },
        errors:null
    }
    ,
    mutations: {
        login: (state, user) => {
            const logPromise=AuthService.login(user)
                logPromise.then(response => {
                if (response.data["access_token"]) {
                    localStorage.setItem('access_token', response.data["access_token"]);
                    localStorage.setItem('refresh_token', response.data["refresh_token"]);
                    localStorage.setItem('user_id', jwtDecode(response.data["access_token"])["sub"]);
                    localStorage.setItem('user_name', jwtDecode(response.data["access_token"])["username"]);
                    localStorage.setItem('roles',JSON.stringify(jwtDecode(response.data["access_token"])["roles"]));
                    state.user.id = localStorage.getItem('user_id');
                    state.user.username = localStorage.getItem('user_name');
                    state.user.roles = JSON.parse(localStorage.getItem('roles'));
                    state.tokens.accessToken = response.data["access_token"]
                    state.tokens.refreshToken =response.data["refresh_token"]
                }
            }).catch(error => {
                    state.errors=[]
                    state.errors.push(error.response.data.message)//(error.response.data.message)
                    console.log("xzcasd")
                }  );
        },
        logout: (state) => {
            AuthService.logout();
            state.user.id = null;
            state.user.username = null;
            state.user.roles = null;
            state.tokens = null;

        },
        registration:  (state, user) =>  {
            AuthService.register(user)
        },
        setErrors:(state,errors)=>{
            if(errors.response.data.message!=null){
                state.errors=[];
                state.errors.push(errors.response.data.message)
            }
            else {
                state.errors=errors.response.data.messages
            }

        },
        refreshToken(state) {
            const logPromise=AuthService.refreshingToken()
            logPromise.then(response => {
                if (response.data["access_token"]) {
                    localStorage.setItem('access_token', response.data["access_token"]);
                    localStorage.setItem('refresh_token', response.data["refresh_token"]);
                    localStorage.setItem('user_id', jwtDecode(response.data["access_token"])["sub"]);
                    localStorage.setItem('user_name', jwtDecode(response.data["access_token"])["username"]);
                    localStorage.setItem('roles',JSON.stringify(jwtDecode(response.data["access_token"])["roles"]));
                    state.user.id = localStorage.getItem('user_id');
                    state.user.username = localStorage.getItem('user_name');
                    state.user.roles = JSON.parse(localStorage.getItem('roles'));
                    state.tokens.accessToken = response.data["access_token"]
                    state.tokens.refreshToken =response.data["refresh_token"]
                }
            }).catch(error => console.log(error));
        }
    }
    ,
    actions:
        {
            login({commit}, user) {
                commit('login', user);
            },
            registration({commit}, user) {
                commit('login', user);
            },
            logout({commit}) {
                commit('logout');
            },
            refreshToken({commit}){
                commit('refreshToken')
            },
            setErrors({commit},errors){
                commit('setErrors',errors)
            }
        },
    getters:
        {
            getUser: state => {
                return state.user;
            },
            isUser: state => {
                if (state.user.roles != null) {
                    return !(state.user.username === null && state.user.id === null);
                } else {
                    return false;
                }
            },
            isAdmin: state => {
                if (state.user.roles != null) {
                    return state.user.roles.find(role => role == "ROLE_ADMIN");
                } else {
                    return false
                }
            },
            getTokens: state => {
                return state.tokens;
            },
            getErrors: state =>{
                return state.errors;
            }
        }
};
export default moduleAuth;