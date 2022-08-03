import axios from 'axios';
import {DEFAURLT_URL} from "../Constants";
import store from "@/store/store";

const API_URL = DEFAURLT_URL;

class AuthService {
    login(user) {
        const params = new URLSearchParams();
        params.append('username',user.username);
        params.append('password', user.password);


        const config = {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        };
        return axios.post(API_URL + 'login',
            params,config);
    }

    logout() {
        localStorage.removeItem('access_token');
        localStorage.removeItem('refresh_token');
        localStorage.removeItem('user_id');
        localStorage.removeItem('user_name');
        localStorage.removeItem('roles');
        delete axios.defaults.headers.common['Authorization']
    }

    register(user) {
        return axios.post(API_URL + 'users/registration', user).catch(error=>{
            store.commit('moduleAuth/setErrors',error)
        });
    }
    refreshingToken() {
        console.log("refr")
        console.log(localStorage.getItem('refresh_token'))
        return axios.post(API_URL + 'users/refresh_token',{},{headers:{"AUTHORIZATION":"Bearer "+localStorage.getItem('refresh_token')}});
    }
}

export default new AuthService();