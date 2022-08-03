import Vue from 'vue'
import App from './App.vue'
import VueRouter from "vue-router";
import router from "./router/index"
import store from "@/store/store";
import "bootstrap/dist/css/bootstrap.min.css"
import "bootstrap/dist/js/bootstrap.js"
import "bootstrap/dist/js/bootstrap.bundle"
import "bootstrap"
import axios from "axios";
import Vuelidate from 'vuelidate'

Vue.config.productionTip = false;
Vue.use(Vuelidate)
Vue.use(VueRouter);

router.beforeEach((to, from, next) => {
    // redirect to login page if not logged in and trying to access a restricted page
    // const publicPages = ['/login', '/registration'];
    // const authRequired = !publicPages.includes(to.path);
    // let loggedIn = store.getters['moduleAuth/getTokens']
    //
    //
    // if (authRequired && !loggedIn.refreshToken) {
    //     return next({name: 'login'});
    // }
    next();
});

axios.interceptors.request.use(config => {
    if (localStorage.getItem('access_token')) {
        config.headers.common['AUTHORIZATION'] = "Bearer " + localStorage.getItem('access_token');
    }

    return config;
}, error => {
    return Promise.reject(error);
});
const AuthInterceptor = axios.interceptors.response.use(
    response => {
        return response;
    },
    error => {
        if (error.response) {

            const originalConfig = error.config;
            if (error.response.status === 401) {
                if (originalConfig._retry != true) {
                    originalConfig._retry = true;
                    axios.interceptors.response.eject(AuthInterceptor);
                    store.commit('moduleAuth/refreshToken');
                    return axios(originalConfig);
                } else {
                    store.commit('moduleAuth/logout');
                    axios.interceptors.response.eject(AuthInterceptor);
                    router.replace({
                        name: 'login',
                        query: {redirect: router.currentRoute.fullPath}
                    });
                    return Promise.reject(error);
                }
            }
            if (error.response.status === 403) {
                router.replace({
                    name: 'error403'
                });
                return Promise.reject(error);
            }

            if (error.response.status === 404) {
                const adminPage = '/admin';
                const catchRequired = router.history.current.path.includes(adminPage);
                console.log(catchRequired)
                if (!catchRequired) {
                    router.push({
                        name: 'Page404'
                    })
                }

            }
        }
        return Promise.reject(error);
    }
);


new Vue({
    render: h => h(App), router, store

}).$mount('#app')




