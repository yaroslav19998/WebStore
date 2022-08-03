<template>
       <div id="CategoryPage">

           <div class="container-fluid main-component-container">
               <div class="row">
                   <h1>Categories</h1>
               </div>

               <div class="row">
                   <div class="tab col-3">
                       <button class="tablinks"
                               v-for="category in mainCategories"
                               :key="category.id"
                               :id="'button_'+category.id"
                               @click="setActive(category.id)"
                               :class="{ active: isActive }"
                       >{{category.name}}</button>
                   </div>
                   <div class="tabcontent col-9" :id="'content_'+category.id"  v-for="category in mainCategories"
                        :key="category.id">
                    <SubCategoriesList :categories="category.subCategories"/>
               </div>

              </div>

           </div>

       </div>
</template>

<script>
    import SubCategoriesList from "@/components/ForUserComponents/CategoryComponents/SubCategoriesList";
    import CategoryService from "@/services/CategoryService";
    export default {
        name: "CategoryPage",
        components: {SubCategoriesList},
        data(){
            return{
            categories:[],
                counter:0
        }},
        computed: {
            mainCategories: function () {
                return this.categories.filter(c => c.depth === 1)
            },
        },
        methods:{
            async FetchData(){
                await CategoryService.getAll(null).then(response => {

                    this.categories=response.data["content"]
                }).catch(error=>console.log(error));
            },
            setActive(categoryId){
                let tabcontent = document.getElementsByClassName("tabcontent");
                for (let i = 0; i < tabcontent.length; i++) {
                    tabcontent[i].className = tabcontent[i].className.replace(" active", "");
                }
                let tablinks = document.getElementsByClassName("tablinks");
                for (let i = 0; i < tablinks.length; i++) {
                    tablinks[i].className = tablinks[i].className.replace(" active", "");
                }
                document.getElementById('button_'+categoryId).classList.add('active');

                document.getElementById("content_"+categoryId).classList.add('active');
            }

           },

        mounted() {
            if(this.counter===0){
                            this.FetchData();
                            this.counter++;
            }


        }
    }
</script>

<style scoped>
    #CategoryPage{
        margin-top: 30px;
        margin-bottom: 30px;
    }

    * {box-sizing: border-box}

    /* Style the tab */
    .tab {

        border: 1px solid #ccc;
        background-color: #f1f1f1;
        overflow-y: auto;
    }

    /* Style the buttons that are used to open the tab content */
    .tab button {
        display: block;
        background-color: inherit;
        color: black;
        padding: 22px 16px;
        width: 100%;
        border: none;
        outline: none;
        text-align: left;
        cursor: pointer;
        transition: 0.3s;
        font-size: 1.2em;
    }


    /* Change background color of buttons on hover */
    .tab button:hover {
        background-color: #ddd;
    }

    /* Create an active/current "tab button" class */
    .tab button.active {
        background-color: #ccc;
    }

    /* Style the tab content */
    .tabcontent {
        display: none;
        float: left;
        padding: 0px 12px;
        border: 1px solid #ccc;
        width: 70%;
        border-left: none;
    }
    .tabcontent.active{
        display: block!important;
    }

</style>