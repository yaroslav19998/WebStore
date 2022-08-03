import axios from "axios";
import {DEFAURLT_URL} from "../Constants";

const API_URL = DEFAURLT_URL+'characteristics';

class CharacteristicService {
    create(CharacteristicDTO) {
        return axios.post(API_URL,CharacteristicDTO);
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
    getById(id) {
        return  axios.get(API_URL+'/'+id);
    }

    delete(id) {
        return axios.delete(API_URL + '/'+id);
    }
    update(id,CharacteristicDTO) {
        return axios.put(API_URL + '/'+id,CharacteristicDTO);
    }

}

export default new CharacteristicService();