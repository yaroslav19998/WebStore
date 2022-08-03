<template>
    <form @submit.prevent>
        <h3 class="form-header">Order form</h3>
        <div v-for="(error,index) in serverValidErrors" :key="index">
            <ErrorMsgValidator :error-text="error"/>
        </div>
        <div class="col form-floating">
            <input type="text" v-model="order.customerName"  placeholder=" " class="form-control" id="Name" :class="{'is-invalid':nameErrors!=null}">
            <label for="Name">Customer name</label>
            <ErrorMsgValidator :error-text="nameErrors" v-if="nameErrors!=null"/>

        </div>
        <div class="col form-floating">
            <input type="text" v-model="order.customerAddress"  placeholder=" " class="form-control" id="Address" :class="{'is-invalid':addressErrors!=null}">
            <label for="Address">Customer address</label>
            <ErrorMsgValidator :error-text="addressErrors" v-if="addressErrors!=null"/>
        </div>
        <div class="col form-floating">
            <input type="text" v-model="order.customerEmail"  placeholder=" " class="form-control" id="Email" :class="{'is-invalid':emailErrors!=null}">
            <label for="Email">Customer email</label>
            <ErrorMsgValidator :error-text="emailErrors" v-if="emailErrors!=null"/>
        </div>
        <div class="col form-floating">
            <input type="text" v-model="order.customerPhone"  placeholder=" " class="form-control" id="Phone"  :class="{'is-invalid':statusErrors!=null}">
            <label for="Phone">Customer phone</label>
            <ErrorMsgValidator :error-text="statusErrors" v-if="statusErrors!=null"/>
        </div>
        <div class="form-floating">

            <select class="form-select" id="status" v-model="order.status" :class="{'is-invalid':statusErrors!=null}">
                <option value="WAITING">Waiting</option>
                <option value="CONFIRMED">Confirmed</option>
                <option value="SENT">Sent</option>
                <option value="ARRIVED">Arrived</option>
            </select>
            <label for="status">Status</label>
            <ErrorMsgValidator :error-text="statusErrors" v-if="statusErrors!=null"/>
        </div>
        <div class="container-fluid">
            <h3>Products</h3>
            <div class="row">
            <div class="col-3 form-floating"><input type="number" v-model="productId"  placeholder=" " class="form-control" id="ProductId">
                <label for="ProductId">ProductId</label></div>
            <div class="col-3 form-floating"><input type="number" v-model="quantity"  placeholder=" " class="form-control" id="Quantity">
                <label for="Quantity">Quantity</label></div>
            <div class="col-3">
                <input type="button"  class="btn btn-lg btn-success"  value="Add product" @click="addProduct">
            </div>
                <ErrorMsgValidator :error-text="productsErrors" v-if="productsErrors!=null"/>
            </div>
            <table class="table table-bordered">
                <thead class="table-dark">
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Count</th>
                    <th>Price</th>
                    <th></th>
                </tr>
                </thead>
                <tbody v-if="selectedOrder">
                    <tr v-for="(product,index) in  order.products" :key="product.id">
                        <td><b>{{product.product.id}}</b></td>
                        <td>{{product.product.name}}</td>
                        <td>
                            <span class="btn btn-outline-warning quantity-btn quantity-btn-left" @click="decrementProduct(index)">-</span>
                                {{product.quantity}}
                            <span class="btn btn-outline-warning quantity-btn quantity-btn-right" @click="incrementProduct(index)">+</span></td>
                        <td v-if="product.price==null">{{getFullPrice(product.product.price,product.quantity)}}</td>
                        <td v-else>{{product.price}}</td>
                        <td>
                            <button @click="removeProduct(index)" class="btn btn-outline-danger">Delete</button>
                        </td>
                    </tr>
                </tbody>
                <tbody v-else>
                    <tr v-for="(product,index) in  productInOrder" :key="product.id">
                        <td><b>{{product.product.id}}</b></td>
                        <td>{{product.product.name}}</td>
                        <td > <div class="quantity">
                            <span class="btn btn-outline-warning quantity-btn quantity-btn-left" @click="decrementProduct(index)">-</span>
                            {{product.quantity}}
                            <span class="btn btn-outline-warning quantity-btn quantity-btn-right" @click="incrementProduct(index)">+</span>        </div>
                        </td>
                        <td v-if="product.price==null">{{getFullPrice(product.product.price,product.quantity)}}</td>
                        <td v-else>{{product.price}}</td>
                        <td>
                            <button @click="removeProduct(index)" class="btn btn-outline-danger">Delete</button>
                        </td>
                    </tr>
                </tbody>
            </table>

        </div>
        <div class="create-form-btn">
            <input type="button" class="btn btn-success" v-if="selectedOrder" value="Change" @click="update()">
            <input type="button"  class="btn btn-success"  v-else value="Create" @click="create">
        </div>
    </form>
</template>

<script>
    import ProductService from "@/services/ProductService";
    import NP from 'number-precision'
    import ErrorMsgValidator from "@/components/ForUserComponents/ErrorMsgValidator";
    import {required,requiredIf,email} from 'vuelidate/lib/validators'

    export default {
        name: "OrderForm",
        props:["selectedOrder","serverValidErrors"],
        components: {ErrorMsgValidator},
        data(){
            return {
                order:{
                    id:null,
                    customerName:'',
                    customerAddress:'',
                    customerEmail:'',
                    customerPhone:'',
                    status:null,
                    products:[],
                },
                productId:0,
                quantity:0,
                productInOrder:[],
                deletedProducts:[]
            }
        },
        validations:{
            order:{
                customerName:{required},
                customerAddress:{required},
                customerEmail:{required,email},
                customerPhone:{required},
                status:{required},
                products:{required:requiredIf(function () {
                    return this.selectedOrder!=null
                })}
            },
            productInOrder:{required:requiredIf(function () {
                    return this.selectedOrder==null
                })}


        },
        computed: {
            nameErrors(){
                if(!this.$v.order.customerName.required && this.$v.order.customerName.$dirty){
                    return "Name must not be empty"
                }


                return null;
            },
            addressErrors(){
                if(!this.$v.order.customerAddress.required && this.$v.order.customerAddress.$dirty){
                    return "Address must not be empty"
                }


                return null;
            },
            emailErrors(){
                if(!this.$v.order.customerEmail.required && this.$v.order.customerEmail.$dirty){
                    return "Email must not be empty"
                }
                if(!this.$v.order.customerEmail.email && this.$v.order.customerEmail.$dirty){
                    return "Wrong email";
                }
                return null;
            },
            phoneErrors(){
                if(!this.$v.order.customerPhone.required && this.$v.order.customerPhone.$dirty){
                    return "Phone must not be empty"
                }


                return null;
            },
            statusErrors(){
                if(!this.$v.order.status.required && this.$v.order.status.$dirty){
                    return "Select a status"
                }
                return null;
            },
            productsErrors(){
                if((!this.$v.order.products.required && this.$v.order.products.$dirty) ||(!this.$v.productInOrder.required && this.$v.productInOrder.$dirty)){
                    return "Add at least 1 product"
                }


                return null;
            },

        },
        methods:{
            create(){
                if(this.$v.$invalid){
                    this.$v.$touch()
                    return
                }
                let orderDTO={orderInfo:this.order,productsInOrder:this.productInOrder}
                this.$emit('create',orderDTO);
            },
            update(){
                if(this.$v.$invalid){
                    this.$v.$touch()
                    return
                }
                let OrderDTO={order:this.order,deletedProducts: this.deletedProducts};
                this.$emit('update',OrderDTO);
            },
            getFullPrice(price,quantity) {
               return  NP.times(price, quantity)
            },
            decrementProduct(index){
                if(this.selectedOrder)
                {
                    this.order.products[index].quantity--
                }
                else{
                    this.productsInOrder[index].quantity--
                }
            },
            incrementProduct(index){
                if(this.selectedOrder)
                {
                    this.order.products[index].quantity++
                }
                else{
                    this.productsInOrder[index].quantity++
                }
            },
            removeProduct(index) {
                    if(this.selectedOrder)
                    {
                        this.deletedProducts.push(this.order.products[index]);
                        this.order.products.splice(index, 1);
                    }
                    else {
                        this.productsInOrder.splice(index,1)
                    }

            },
            addProductWithCheck(products,product,quantity){
                if(products.length){
                    let isProductExist=false;
                    products.map(function (item){
                        if(item.product.id===product.id){
                            isProductExist=true;
                            item.quantity=NP.plus(item.quantity,quantity);
                        }
                    });
                    if(!isProductExist){
                        products.push({quantity: quantity,product:product});
                        console.log(products)
                    }
                }
                else {
                    products.push({quantity: quantity,product:product});
                }


            },
            addProduct() {

                    ProductService.getById(this.productId,"orderrelation").then(response => {
                        if(this.selectedOrder!=null){
                            console.log("asdas")
                            console.log(response.data)
                            this.addProductWithCheck(this.order.products,response.data,this.quantity)
                        }
                        else {

                           this.addProductWithCheck(this.productInOrder,response.data,this.quantity)
                        }
                    }).catch(e=> {

                    alert(e);

                });

            }
        },
        mounted(){
            if(this.selectedOrder!=null){
                this.order={...this.selectedOrder};
            console.log(this.order)}
        }
    }
</script>

<style scoped>
form{
    width: 800px;
}
.quantity-btn-left{
    border-radius: 10px 0 0 10px;
}
.quantity-btn-right{
    border-radius: 0 10px 10px 0 ;
}
.quantity-btn{
    padding: 5px;
    margin-right: 2px;
    margin-left: 0px;
    width: 20px;
}
div {
    margin: 20px 0;

}
.quantity{text-align: center}
    .container-fluid{padding-right:0px;padding-left: 0}
    table{ margin-left: 0px}
</style>