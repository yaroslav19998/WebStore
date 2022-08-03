import {DEFAURLT_URL} from "../Constants";
import axios from "axios";

const API_URL = DEFAURLT_URL+'roles';

class RoleService {
    create(brand) {

        return axios.post(API_URL, brand);
    }
    getAll() {

        return axios.get(API_URL);
    }
    getById(id) {
        return axios.get(API_URL+'/'+id);
    }

    delete(id) {
        return axios.delete(API_URL + '/'+id);
    }
    update(id,role) {
        return axios.put(API_URL + '/'+id,role);
    }

}

export default new RoleService();