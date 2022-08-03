import axios from "axios";
import {DEFAURLT_URL} from "../Constants";

const API_URL = DEFAURLT_URL+'products';

class ProductService {
    create(product,images) {
        let fd=new FormData();
        const json = JSON.stringify(product);
        const blob = new Blob([json], {
            type: 'application/json'
        });

        fd.append('productDTO',blob)
        for(let i=0;i<images.length;i++){
        fd.append('images',images[i]);
        }
        return axios.post(API_URL, fd,{
            headers: {
                'Content-Type': 'multipart/form-data'
            }}
        );
    }
    getProductReviewsById(id,page=null){
        if(page==null){
            page=0;
        }
        else {
            page=page-1
        }
        return axios.get(API_URL+"/"+id+"/reviews?page="+page);
    }

    getProductReviewsCount(id){
        return axios.get(API_URL+"/"+id+"/reviews/count");
    }

    createReview(review) {
        return axios.post(API_URL+"/"+review.product.id+"/reviews", review)
    }
    deleteReview(id) {
        return axios.delete(API_URL+"/reviews"+"/"+id)
    }
    updateReview(id,review) {
        return axios.put(API_URL+"/reviews"+"/"+id,review)
    }
    getByNameForNavbar(name=null) {
        let url=API_URL
        if(name!=="" && name!=null)
        {
            url=url+"?name="+name;
        }
        return axios.get(url,{headers:{"ResponseResourceFormat":"usersearch"}});
    }
    getProductsSearch(page,searchParams,order=null,sortBy=null){
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
        let url=API_URL+'?page='+page;
        if(order!=null){
            url=url+"&order="+order
        }
        if(sortBy!=null){
            url=url+"&sort_by="+sortBy
        }
        console.log(url)
        return axios.get(url,{params:{params:encodeURIComponent(obj)}});
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
    getById(id, responseFormat=null) {
        return  axios.get(API_URL+'/'+id,{headers:{"ResponseResourceFormat":responseFormat}});
    }

    delete(id) {
        return axios.delete(API_URL + '/'+id);
    }
    update(id,productDTO,images) {
        let fd=new FormData();

        const json = JSON.stringify(productDTO);
        const blob = new Blob([json], {
            type: 'application/json'
        });
        console.log(productDTO)
        fd.append('productDTO',blob)
        for(let i=0;i<images.length;i++){
            fd.append('images',images[i]);
        }
        return axios.put(API_URL + '/'+id,fd,{
            headers: {
                'Content-Type': 'multipart/form-data'
            }});
    }

}

export default new ProductService();