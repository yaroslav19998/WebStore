<template>
    <div class="container-fluid">
        <div class="row">
            <div class="col-4 subcategory-column" @mouseover="openSubCategories(index)" @mouseleave="openIndex=-1" v-for="(category,index) in twoDepthCategories" :key="category.id">
                <router-link :to="{ name: 'categoryProducts', params: { categoryId: category.id }}" class="two-depth-category-link" >
                   {{category.name}}
                </router-link>
                <ul  v-show="isOpen(index)" v-if="isFolder(category)">
                    <div v-for="subcategory in category.subCategories" :key="subcategory.id">
                        <router-link :to="{ name: 'categoryProducts', params: { categoryId: subcategory.id }}" >
                            <span class="two-depth-category-link">{{subcategory.name}}</span></router-link>
                    </div>
                </ul>

            </div>
        </div>


    </div>
</template>

<script>
    export default {
        name: "SubCategoriesList",
        props: ["categories"],
        data: function() {
            return {
                openIndex:-1
            };
        },
        computed: {
            twoDepthCategories: function () {
                return this.categories.filter(c => c.depth === 2)
            }

        },
        methods: {
            toggle: function() {
                if (this.isFolder) {
                    this.isOpen = !this.isOpen;
                }
            },
            isFolder(category){
                return category.subCategories && category.subCategories.length;
            },
            isOpen(index){
                return this.openIndex==index
            },
            openSubCategories(index) {
                if (this.isFolder) {
                    this.openIndex=index
                }
            }
        },
        beforeMount(){
            console.log(this.categories)
        }
    }
</script>

<style scoped>

    .container-fluid{padding-top: 15px}
    .subcategory-column{padding:0}
   .two-depth-category-link{
       padding: 5px 5px 5px 32px;
       text-decoration: none;
       font-size: 1.2em;
       color:black;
       display: block;
       transition: 0.3s;}
   .two-depth-category-link:hover{
       border: gray solid 1px;
       background-color: #ddd;}

   .three-depth-category-link{ padding: 8px 8px 8px 32px;
       text-decoration: none;
       font-size: 1em;
       color:black;
       display: block;

       transition: 0.3s;}
   .three-depth-category-link:hover{
       background-color: #ddd;
   }
</style>