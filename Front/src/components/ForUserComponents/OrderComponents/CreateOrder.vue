<template>
    <div class="container-fluid main-component-container">
        <div class="row">
            <h1>Create order</h1>
        </div>
        <div v-for="(error,index) in serverValidationErrors" :key="index">
            <ErrorMsgValidator :error-text="error"/>
        </div>
        <div class="row">
            <form @submit.prevent>
                <div class="container-fluid create-order-info-container">
                    <div class="row">
                        <div class="col-6 customer-info-column form-floating">
                            <input type="text" placeholder=" " class="form-control" id="customerEmail"
                                   v-model="orderDTO.orderInfo.customerEmail" :class="{'is-invalid':emailErrors!=null}">
                            <label for="customerEmail">Email</label>
                            <ErrorMsgValidator :error-text="emailErrors" v-if="emailErrors!=null"/>
                        </div>
                        <div class="col-6 customer-info-column form-floating">
                            <input type="text" placeholder=" " class="form-control" id="customerPhone"
                                   v-model="orderDTO.orderInfo.customerPhone " :class="{'is-invalid':phoneErrors!=null}">
                            <label for="customerPhone">Phone</label>
                            <ErrorMsgValidator :error-text="phoneErrors" v-if="phoneErrors!=null"/>
                        </div>
                        <div class="col-6 customer-info-column form-floating">
                            <input type="text" placeholder=" " class="form-control" id="customerAddress"
                                   v-model="orderDTO.orderInfo.customerAddress" :class="{'is-invalid':addressErrors!=null}">
                            <label for="customerAddress">Address</label>
                            <ErrorMsgValidator :error-text="addressErrors" v-if="addressErrors!=null"/>
                        </div>
                        <div class="col-6 customer-info-column form-floating">
                            <input type="text" placeholder=" " class="form-control" id="customerName"
                                   v-model="orderDTO.orderInfo.customerName" :class="{'is-invalid':nameErrors!=null}">
                            <label for="customerName">Name</label>
                            <ErrorMsgValidator :error-text="nameErrors" v-if="nameErrors!=null"/>
                        </div>
                    </div>
                    <div class="row">
                        <div id="products" class="table-container">
                            <table class="table table-bordered">
                                <thead class="table-dark"> <!-- Заголовок таблицы -->
                                <tr>
                                    <th>№</th>
                                    <th>Image</th>
                                    <th>Name</th>
                                    <th>Quantity</th>
                                    <th>Price</th>
                                    <th>Х</th>
                                </tr>
                                </thead>
                                <tbody> <!-- Заполнение таблицы -->
                                <tr v-for=" (product, index) in products" :key="product.id">
                                    <td><b>{{index+1}}</b></td>
                                    <td><img src="" alt="..."/></td>
                                    <td>{{product.name}}</td>

                                    <td>
                                        <div class="quantity">
                                            <span class="btn btn-outline-warning quantity-btn quantity-btn-left"
                                                  @click="decrementProduct(index)">-</span>
                                            {{product.quantity}}
                                            <span class="btn btn-outline-warning quantity-btn quantity-btn-right"
                                                  @click="incrementProduct(index)">+</span></div>
                                    </td>
                                    <td>{{ fullProductPrice(product.price,product.quantity)}}</td>
                                    <td>
                                        <button class="btn btn-danger" @click="deleteProduct(index)">Delete</button>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-10 order-price">Order price: {{fullPrice}}</div>
                        <div class="col-2"><input type="button" value="create" class="btn btn-lg btn-success"
                                                  @click="create"></div>
                    </div>
                </div>
            </form>
        </div>
    </div>
</template>

<script>
    import {mapGetters} from 'vuex'
    import {mapActions} from 'vuex'
    import OrderService from "@/services/OrderService";
    import NP from 'number-precision'
    import ErrorMsgValidator from "@/components/ForUserComponents/ErrorMsgValidator";
    import {required,email} from 'vuelidate/lib/validators'

    export default {
        name: "CreateOrder",
        components:{ErrorMsgValidator},
        data() {
            return {
                orderDTO: {
                    orderInfo:
                        {
                            customerEmail: null,
                            customerAddress: null,
                            customerPhone: null,
                            customerName: null,
                            status:"WAITING"
                        },
                    productsInOrder: []
                },
                serverValidationErrors:[]
            }
        },
        validations: {
            orderDTO:{
                orderInfo: {
                    customerName: {required},
                    customerAddress: {required},
                    customerEmail: {required, email},
                    customerPhone: {required},
              },
            }


        },
        computed: {

            ...mapGetters({fullPrice: 'moduleCart/getFullPrice'}),
            ...mapGetters({products: 'moduleCart/getProducts'}),
            nameErrors() {
                if (!this.$v.orderDTO.orderInfo.customerName.required && this.$v.orderDTO.orderInfo.customerName.$dirty) {
                    return "Name must not be empty"
                }
                return null;
            },
            addressErrors() {
                if (!this.$v.orderDTO.orderInfo.customerAddress.required && this.$v.orderDTO.orderInfo.customerAddress.$dirty) {
                    return "Address must not be empty"
                }


                return null;
            },
            emailErrors() {
                if (!this.$v.orderDTO.orderInfo.customerEmail.required && this.$v.orderDTO.orderInfo.customerEmail.$dirty) {
                    return "Email must not be empty"
                }
                if (!this.$v.orderDTO.orderInfo.customerEmail.email && this.$v.orderDTO.orderInfo.customerEmail.$dirty) {
                    return "Wrong email";
                }
                return null;
            },
            phoneErrors() {
                if (!this.$v.orderDTO.orderInfo.customerPhone.required && this.$v.orderDTO.orderInfo.customerPhone.$dirty) {
                    return "Phone must not be empty"
                }


                return null;
            },
            productsErrors() {
                if (!this.$v.orderDTO.productInOrder.required && this.$v.orderDTO.productInOrder.$dirty) {
                    return "Add at least 1 product"
                }


                return null;
            }


        },
        methods: {
            ...mapActions(
                {
                    delete: "moduleCart/deleteProduct",
                    increment: "moduleCart/incrementProduct",
                    decrement: "moduleCart/decrementProduct"
                }),
            deleteProduct(index) {
                this.delete(index);
            },
            fullProductPrice(price, quantity) {
                return NP.times(price, quantity);
            }
            ,
            incrementProduct(index) {
                this.increment(index);
            },
            decrementProduct(index) {
                this.decrement(index);
            },

            create() {
                console.log("click")
                if(this.$v.$invalid){
                    this.$v.$touch()
                    return
                }

                this.orderDTO.productsInOrder = [];
                let productsInOrder = [];
                this.products.map(function (item) {
                    var obj = {id: item.id};
                    var obj2 = {product: obj, quantity: item.quantity};
                    productsInOrder.push(obj2)
                });
                this.orderDTO.productsInOrder = productsInOrder;
                console.log(this.orderDTO);
                OrderService.create(this.orderDTO).then(response=>alert("order created"+response)).catch(error =>  {
                    if(error.response.status==400){
                        this.serverValidationErrors=error.response.data.messages
                    }
                }   );
            }
        }
    }
</script>

<style scoped>
    .main-component-container {
        margin-top: 20px;
    }

    .quantity {
        text-align: center
    }

    .quantity-btn-left {
        border-radius: 10px 0 0 10px;
    }

    .quantity-btn-right {
        border-radius: 0 10px 10px 0;
    }

    .table-container {
        padding: 0;
        margin-left: 0;
        margin-right: 0;
        text-align: center;
    }

    h1 {
        margin-left: 100px;
    }

    .quantity-btn {
        padding: 5px;
        margin-right: 3px;
        margin-left: 5px;
        width: 20px;
    }

    .create-order-info-container {
        width: 70%;
    }

    .customer-info-column {
        margin-top: 5px;
        margin-bottom: 5px;
    }

    .order-price {
        font-size: 1.4em;
        font-weight: bold;
    }
</style>