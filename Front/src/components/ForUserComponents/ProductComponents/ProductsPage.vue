<template>
    <div class="container-fluid main-component-container">
        <div class="row">
            <h1>Products</h1>
        </div>
        <div class="row">
            <div class="col-12 col-sm-3 col-md-2" >
                <SearchForm @searchProducts="searchProducts"></SearchForm>
            </div>
            <div class="col-12 col-sm-9 col-md-10 ">
                <div class="sort-container container-fluid">
                    <div class="sort-row row">
                        <div class="col-12">
                            <span class="sort-span">Sort:</span>
                            <select class="form-select order-select form-select-sm col-3" @change="changeOrder(order)" aria-label=".form-select-sm" v-model="order">
                                <option value="DESC">Descending</option>
                                <option value="ASC">Ascending</option>
                            </select>

                            <select class="form-select order-select form-select-sm "  @change="changeSortBy(sortField)" aria-label=".form-select-sm" v-model="sortField">
                                <option value="sales">Popularity</option>
                                <option value="name">Name</option>
                                <option value="price">Price</option>
                            </select>

                        </div>
                    </div>
                </div>
                <ProductsList :products="this.products"></ProductsList>
                <div>
                    <PaginationComponent v-if="totalPages" :current-page="currentPage" :total-pages="totalPages" @updatePage="updatePage"/>
                </div>
            </div>

        </div>
    </div>
</template>

<script>
    import ProductsList from "@/components/ForUserComponents/ProductComponents/ProductsList";
    import PaginationComponent from "@/components/PaginationComponent";
    import SearchForm from "@/components/ForUserComponents/CategoryComponents/SearchForm";
    import ProductService from "@/services/ProductService";
    export default {
        name: "ProductsPage",
        components:{ProductsList,PaginationComponent,SearchForm},

        data(){
            return {
                products:[],
                search:false,
                searchParams:null,
                currentPage:1,
                totalPages:1,
                order:"DESC",
                sortField:"sales"
            }
        },
        methods:{
            searchProducts(searchParams){
                this.searchProductsPage(0,searchParams,null,null);
            },
            searchProductsPage(page,searchParams,order,sortBy){
                this.search=true;
                this.searchParams=searchParams;
                ProductService.getProductsSearch(page,searchParams,order,sortBy).then(response => {
                    this.products=response.data["content"];
                    this.currentPage = response.data["currentPage"]+1
                    this.totalPages = response.data["totalPages"]
                }).catch(error=>console.log(error));
            },
            updatePage(page){
                if(this.search){
                    this.searchProductsPage(page,this.searchParams,this.order,this.sortField)
                }
                else{
                    this.searchProductsPage(page,this.searchParams,this.order,this.sortField)
                }
            },
            changeOrder(order){
                this.searchProductsPage(0,0,order,this.sortField)
            },
            changeSortBy(sortField){
                this.searchProductsPage(0,0,this.order,sortField)
            }
        },
            mounted() {
                this.searchProducts(null);
                          }

    }
</script>

<style scoped>
    h1{
        margin-top: 25px;
        margin-bottom: 2515px;
    }
    .sort-span{
        font-size: 1.2em;
        margin-right: 5px;
    }
    .main-component-container{
        width: 90%!important;
    }
    .sort-container{
        text-align: right;
        margin: 20px 10px 15px 10px;
    }
    .sort-row{
        text-align: right;
    }
    .order-select{
        width: 150px;
        display: inline;
    }

</style>