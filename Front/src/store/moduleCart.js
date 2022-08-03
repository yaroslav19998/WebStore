import Vue from 'vue'
import Vuex from 'vuex'
import NP from 'number-precision'

Vue.use(Vuex);

const moduleCart={
    namespaced:true,
    state: {
       cart:{
           products:[],
           fullPrice:0
         }
       }
    ,
    mutations: {
            addProduct:(state,{product,quantity})=>
            {
                if(state.cart.products.length){
                    let isProductExist=false;
                    console.log(state.cart.products);
                    state.cart.products.map(function (item){
                        if(item.id===product.id){
                            console.log(2);
                            isProductExist=true;
                            console.log(item);
                                item.quantity=NP.plus(item.quantity,quantity);



                        }
                    });
                    if(!isProductExist){
                        Vue.set(product,'quantity',quantity);

                       state.cart.products.push(product);
                    }
            }
            else {
                {
                    Vue.set(product,'quantity',quantity);
                }


                    state.cart.products.push(product);

            }


            },
             removeProduct:(state,index)=>{
                state.cart.products.splice(index,1);

            },
            countFullPrice:(state)=>
            {
                if(state.cart.products.length)
                {
                    let price=0;
                    state.cart.products.map(function (item){
                         price=NP.plus(price, NP.times(item.quantity, item.price));
                    });
                    state.cart.fullPrice=price
                }
            },
            increment:(state,index)=>{
                console.log( index);
                console.log( state.cart.products[index]);
                state.cart.products[index].quantity++;
            },
            decrement:(state,index)=>{
                if(state.cart.products[index].quantity>1){
                    console.log( index);
                    console.log( state.cart.products[index]);
                    state.cart.products[index].quantity--;
                }
            }
        }
        ,
    actions:
        {
        addToCart({commit},{product,quantity}){
            commit('addProduct',{product,quantity});
            commit('countFullPrice');
        },
        deleteProduct({commit},index){
             commit('removeProduct',index);
             commit('countFullPrice');
        },
        incrementProduct({commit},index){
            commit('increment',index);
            commit('countFullPrice');
        },
            decrementProduct({commit},index){
                commit('decrement',index);
                commit('countFullPrice');
            }
    },
    getters:
        {
        getProducts: state => { return state.cart.products;},
        getFullPrice: state => { return state.cart.fullPrice;},
        isEmpty: state => { return state.cart.products.length===0;}
     }
};
export default moduleCart;