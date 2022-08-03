<template>
    <div v-if="totalPages>0">
        <nav aria-label="...">
            <ul class="pagination  justify-content-center">
                <li class="page-item" :class="{disabled:isFirst}">
                    <span class="page-link"  @click="updatePage(currentPage-1)">Previous</span>
                </li>
                <li class="page-item" v-if="currentPage>2" @click="updatePage(1)"><a class="page-link" href="#">1</a></li>
                <li class="page-item" v-if="currentPage>3"><a class="page-link" href="#">...</a></li>
                <li class="page-item"><a class="page-link" v-if="!isFirst" @click="updatePage(currentPage-1)">{{currentPage-1}}</a></li>
                <li class="page-item active">
                  <span class="page-link">
                    {{currentPage}}
                   </span>
                </li>
                <li class="page-item"><a class="page-link" href="#" v-if="currentPage<totalPages" @click="updatePage(currentPage+1)">{{currentPage+1}}</a></li>
                <li class="page-item" v-if="currentPage<totalPages-2"><a class="page-link">...</a></li>
                <li class="page-item" v-if="currentPage<totalPages-1" @click="updatePage(totalPages)"><a class="page-link" href="#">{{totalPages}}</a></li>
                <li class="page-item" :class="{disabled:isLast}">
                    <a class="page-link" @click="updatePage(currentPage+1)"  >Next</a>
                </li>
            </ul>
        </nav>
    </div>
</template>

<script>
    export default {
        name: "PaginationComponent",
        props: ["currentPage", "totalPages"],
        computed: {
            isFirst() {
                return this.currentPage == 1;
            },
            isLast(){
                return this.currentPage == this.totalPages || this.totalPages==0;
            },

        },
        methods:{
            updatePage(pageNumber){
                this.$emit("updatePage",pageNumber)
            }
        }
    }
</script>

<style scoped>

</style>