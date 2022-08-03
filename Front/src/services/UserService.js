import axios from "axios";
import {DEFAURLT_URL} from "../Constants";

const API_URL = DEFAURLT_URL+'users';

class UserService {
    create(user) {
        return axios.post(API_URL,user);
    }
    getAll(page,username=null,roleId=null) {
        if(page==null){
            page=0;
        }
        else {
            page=page-1
        }
        let url=API_URL+"?page="+page;
        if(username!=="" && username!=null)
        {
            url=url+"&username="+username;
        }
        if(roleId!=null){
            url=url+"&roleId="+roleId;
        }
        return axios.get(url);
    }
    getById(id,responseFormat=null) {
        return  axios.get(API_URL+'/'+id,{headers:{"ResponseResourceFormat":responseFormat}});
    }
    getUserOrdersById(id,page=null) {
        if(page==null){
            page=0;
        }
        else {
            page=page-1
        }
        let url=API_URL+'/'+id +'/orders'+"?page="+page;
        return  axios.get(url);
    }
    getUserReviewsById(id,page) {
        if(page==null){
            page=0;
        }
        else {
            page=page-1
        }
        let url=API_URL+'/'+id +'/reviews'+"?page="+page;
        return  axios.get(url);
    }
    delete(id) {
        return axios.delete(API_URL + '/'+id);
    }
    update(id,user) {
        return axios.put(API_URL + '/'+id,user);
    }

}

export default new UserService();