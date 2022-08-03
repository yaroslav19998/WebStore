
import VueRouter from "vue-router";
import LoginForm from "@/components/ForUserComponents/AuthComponents/LoginForm";
import CategoryList from "@/components/ForUserComponents/CategoryComponents/CategoriesPage";
import RegistrationForm from "@/components/ForUserComponents/AuthComponents/RegistrationForm";
import AdminPage from "@/components/AdminComponents/AdminPage";
import ProductList from "@/components/AdminComponents/ProductComponents/ProductsList";
import AdminCategoryList from "@/components/AdminComponents/CategoryComponents/CategoryList";
import BrandList from "@/components/AdminComponents/BrandComponents/BrandList";
import CategoryProducts from "@/components/ForUserComponents/CategoryComponents/CategoryProductsPage";
import CreateOrder from "@/components/ForUserComponents/OrderComponents/CreateOrder";
import CharacteristicList from "@/components/AdminComponents/CharacteristicComponents/CharacteristicList";
import ProductPage from "@/components/ForUserComponents/ProductComponents/ProductPage";
import UserPage from "@/components/ForUserComponents/UserComponents/UserPage";
import UserList from "@/components/AdminComponents/UserComponents/UserList";
import OrderList from "@/components/AdminComponents/OrderComponents/OrderList";
import RoleList from "@/components/AdminComponents/RoleComponents/RoleList";
import Error403Page from "@/components/ForUserComponents/AuthComponents/Error403Page";
import ProductReviews from "@/components/ForUserComponents/ProductComponents/ProductReviews";
import ProductsPage from "@/components/ForUserComponents/ProductComponents/ProductsPage";
import Page404 from "@/components/Page404";
import Home from "../components/Home";

export default new VueRouter({ mode:'history',
    routes:[
        {
        path:'/login', name:'login',component: LoginForm
        },
        {
            path:'/error403', name:'error403',component: Error403Page
        },
        {
            path:'/registration', name:'registration' ,component: RegistrationForm
        },
        {
            path:'/categories', name:'categories' ,component: CategoryList
        },
        {
            path:'/category/:categoryId/products',   name:'categoryProducts',component: CategoryProducts
        },
        {
            path:'/product/:productId',   name:'productPage',component: ProductPage
        },
        {
            path:'/user',   name:'userInfo',component: UserPage
        },
        {
            path:'/product/:productId/reviews',   name:'productReviews',component: ProductReviews
        },
        {
            path:'/products',   name:'products',component: ProductsPage
        },
        {
            path:'/createOrder',  name:'createOrder',component: CreateOrder
        },


        {
            path:'/admin', component: AdminPage,
            children: [
                {

                    path: 'products',
                    name:'productadmin',
                    component: ProductList
                },
                {

                    path: 'categories',
                    name:'categoryadmin',
                    component:  AdminCategoryList
                },
                {
                    path: 'brands',
                    name:'brandadmin',
                    component: BrandList
                },
                {

                    path: 'attributes',
                    name:'characteristicadmin',
                    component: CharacteristicList
                },
                {
                    path: 'users',
                    name:'userAdmin',
                    component: UserList
                },
                {
                    path: 'orders',
                    name:'orderAdmin',
                    component: OrderList
                },
                {
                    path: 'roles',
                    name:'roleAdmin',
                    component: RoleList
                },

            ]
        },
        {
            path:'/', name:'home',component: Home
        },
        { path: '/:pathMatch(.*)*',name:'Page404', component: Page404 }
    ]
})

