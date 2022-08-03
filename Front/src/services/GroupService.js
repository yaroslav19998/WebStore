import axios from 'axios';

const API_URL = this.$apiurl+'group';

class GroupService {
    create(group) {

        axios.post(API_URL, group).then(response => {if (response.status===201)
        {
           return response.data;
        }
            });
    }

    getAll() {
           axios.get(API_URL).then(response => {return response.data});
    }
    getById(id) {
        axios.get(API_URL+'/'+id).then(response => {return response.data});
    }

    delete(id) {
        return axios.delete(API_URL + '/'+id);
    }
    update(id,group) {
        return axios.put(API_URL + '/'+id,group);
    }

}

export default new GroupService();