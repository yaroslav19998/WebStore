import axios from "axios";
import {DEFAURLT_URL} from "../Constants";

const API_URL = DEFAURLT_URL+'orders';

class OrderService {

    create(order) {
        // let productsInOrder=[];
        //
        //
        //
        // order.productsInOrder.map(function (item){
        //     var obj = {id: item.id};
        //     var obj2={product: obj,quantity:item.quantity};
        //             productsInOrder.push(obj2)});
        // order.productsInOrder=productsInOrder;
        console.log("clicked");
        console.log(order);
        return axios.post(API_URL, order);
    }
   getAll(page,status=null) {
       if(page==null){
           page=0;
       }
       else {
           page=page-1
       }
       let url=API_URL+"?page="+page;
       if(status!=null){
           url=url+"&status="+status;
       }
        return axios.get( url);
    }
    getById(id) {
        return  axios.get(API_URL+'/'+id);
    }

    delete(id) {
        return axios.delete(API_URL + '/'+id);
    }
    update(id,order) {
        delete order.created;
        delete order.updated;
        // order.products.forEach(item=> item.order={id:order.id,name:order.name})

        console.log(order);

        return axios.put(API_URL + '/'+id,order);
    }
    getStatuses(){
        return axios.get(API_URL+"/statuses");
    }
}

export default new OrderService();