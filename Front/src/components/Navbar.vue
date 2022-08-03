<template>
    <div>
        <nav class="navbar navbar-expand-lg navbar-light bg-light">
            <div class="container-fluid main-component-container">
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                        data-bs-target="#navbarTogglerDemo03" aria-controls="navbarTogglerDemo03" aria-expanded="false"
                        aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <a class="navbar-brand" href="#">Navbar</a>
                <div class="collapse navbar-collapse" id="navbarTogglerDemo03">
                    <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                        <li class="nav-item">
                            <router-link class="nav-link" aria-current="page" :to="{name:'home'}">Home
                            </router-link>
                        </li>
                        <li class="nav-item">
                            <router-link class="nav-link" aria-current="page" :to="{name:'registration'}">Registration
                            </router-link>
                        </li>
                        <li class="nav-item dropdown">
                            <router-link class="nav-link" aria-current="page" :to="{name:'categories'}">Categories</router-link>
                        </li>
                        <li class="nav-item dropdown">
                            <router-link class="nav-link" aria-current="page" :to="{name:'products'}">Products</router-link>
                        </li>
                    </ul>
                    <div class="d-flex">

                        <div class="nav-item cart" tabindex="1"  v-if="!cartIsEmpty">

                            <span class="nav-link">Cart   <span class="cart-quantity">12</span></span>
                            <CartComponent
                                    v-if="!cartIsEmpty"
                            />
                        </div>
                        <div class="nav-item search input-group" >
                            <input class="form-control" type="search" placeholder="Search" aria-label="Search" v-model="productName">
                            <button class="btn btn-outline-success" type="submit" @click="searchProducts">Search</button>
                            <SearchNavComponent :products="products" v-if="products.length>0"/>
                        </div>


                        <div  class="nav-item dropdown" v-if="isUser">
                            <a class="nav-link dropdown-toggle" href="#" id="userDropdown" role="button"
                               data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                                                {{this.user.username}}
                            </a>
                            <ul class="dropdown-menu" aria-labelledby="userDropdown">
                                <li><router-link class="dropdown-item" :to="{name:'userInfo'}">Info</router-link></li>
                                <li v-if="isAdmin"> <router-link class="dropdown-item" :to="{path:'/admin'}"> Admin</router-link></li>
                                <div class="dropdown-divider"></div>
                                <li><a @click="logout" class="dropdown-item" href="#">Logout</a></li>
                            </ul>
                        </div>
                        <div class="nav-item usermenu" v-else>
                            <router-link class="nav-link" aria-current="page" :to="{name:'login'}">Log in</router-link>
                        </div>
                    </div>

                </div>
            </div>
        </nav>


     </div>
</template>

<script>
    import CartComponent from "@/components/ForUserComponents/CartComponents/CartComponent";
    import SearchNavComponent from "@/components/SearchNavComponent";
    import {mapGetters} from 'vuex'
    import {mapActions} from 'vuex'
    import ProductService from "@/services/ProductService";

    export default {
        name: "Navbar",
        components: {CartComponent,SearchNavComponent},
        data(){return {
            products:[],
            productName:""
        }},
        computed: {
            ...mapGetters({
                tokens: 'moduleAuth/getTokens', cartIsEmpty: 'moduleCart/isEmpty',
                isUser: 'moduleAuth/isUser', isAdmin: 'moduleAuth/isAdmin', user: 'moduleAuth/getUser'
            },)
        },
        methods: {
            ...mapActions(
                {
                    logout: "moduleAuth/logout"
                }),
            searchProducts(){
                    ProductService.getByNameForNavbar(this.productName).then(response=> {this.products=response.data["content"];}).catch(e=>console.log(e))
            }
        },
        mounted() {
            console.log("navbar")
            console.log(this.isUser)
            console.log(this.tokens)
            console.log(this.user)
            console.log(this.isAdmin)
            console.log("navbar")
        }
    }
</script>

<style scoped>
    .usermenu{
        min-width: 80px;
    }
    .cart {
        position: relative;
    }
    .cart-quantity {
        position: absolute;
        left: 42px;
        top: 50%;
        transform: translateY(-50%);
        margin-top: -15px;
        background-color: #de707b;
        border-radius: 100%;
        padding: 2px;
        color: #fff;
        font-size: 12px;
        min-height: 16px;
        min-width: 16px;
        display: inline-flex;
        align-items: center;
        justify-content: center;
    }
    .search{position: relative;margin: 0 10px 0 10px}
</style>