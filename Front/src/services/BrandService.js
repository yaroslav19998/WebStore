import axios from 'axios';
import {DEFAURLT_URL} from "../Constants";
const API_URL = DEFAURLT_URL+'brands';

class BrandService {
    create(brand) {
        return axios.post(API_URL, brand);
    }
    getAll(page,responseFormat=null,name=null) {
        if(page==null){
            page=0;
        }
        else {
            page=page-1
        }
        let url=API_URL+"?page="+page;
        if(name!=="" && name!=null)
        {
            url=url+"&name="+name;
        }
        return axios.get(url,{headers:{"ResponseResourceFormat":responseFormat}});
    }
    getById(id,responseFormat=null) {
        return axios.get(API_URL+'/'+id,{headers:{"ResponseResourceFormat":responseFormat}});
    }

    delete(id) {
        return axios.delete(API_URL + '/'+id);
    }
    update(id,brand) {
        return axios.put(API_URL + '/'+id,brand);
    }

}

export default new BrandService();