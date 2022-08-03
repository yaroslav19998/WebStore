import axios from "axios";
import {DEFAURLT_URL} from "../Constants";

const API_URL = DEFAURLT_URL+'categories';

//Добавить глобально
class CategoryService {
    create(category) {
        console.log(category)
        return axios.post(API_URL, category);
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
    getProductsById(page,id,searchParams,order=null,sortBy=null) {
        let obj="";

        if(searchParams!=null)
        {
            obj+=JSON.stringify(searchParams)
        }

        if(page==null || page<=0){
            page=0;
        }
        else {
            page=page-1
        }
        let url=API_URL+'/'+id +'/products?page='+page;
        if(order!=null){
             url=url+"&order="+order
        }
        if(sortBy!=null){
            url=url+"&sort_by="+sortBy
        }
        return axios.get(url,{params:{params:encodeURIComponent(obj)}});
    }


    getById(id,responseFormat=null) {
        return axios.get(API_URL+'/'+id,{headers:{"ResponseResourceFormat":responseFormat}});
    }

    delete(id) {
        return axios.delete(API_URL + '/'+id);
    }
    update(id,category) {
        return axios.put(API_URL + '/'+id,category);
    }

}

export default new CategoryService();