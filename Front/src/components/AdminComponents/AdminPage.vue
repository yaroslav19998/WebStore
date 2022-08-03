<template>
    <div id="admin-page" class="container-fluid">
        <div class="row">
            <div class="col-12 col-lg-2">
                <div id="sidebar">
                    <router-link
                            id="productsSidebarLink"
                            tag="a"
                            :to="{name:'productadmin'}">
                            <span class="admin-sidebar-link" @click="setActive">Products</span>
                    </router-link>
                    <router-link
                            id="categoriesSidebarLink"
                            tag="a"
                            :to="{name:'categoryadmin'}">
                            <span class="admin-sidebar-link" @click="setActive">Categories</span>
                    </router-link>
                    <router-link
                            id="brandsSidebarLink"
                            tag="a" @click="setActive" :to="{name:'brandadmin'}">
                        <span class="admin-sidebar-link" @click="setActive">Brands</span>
                    </router-link>
                    <router-link id="productCharacteristicsSidebarLink"
                                 @click="setActive"
                                 tag="a"
                                 :to="{name:'characteristicadmin'}">
                        <span class="admin-sidebar-link" @click="setActive">Product characteristics</span>
                    </router-link>
                    <router-link
                            id="usersSidebarLink"
                            tag="a"
                            @click="setActive"
                            :to="{name:'userAdmin'}">
                        <span class="admin-sidebar-link" @click="setActive">Users</span>
                    </router-link>
                    <router-link
                            id="rolesSidebarLink"
                            tag="a"
                            @click="setActive"
                            :to="{name:'roleAdmin'}">
                        <span class="admin-sidebar-link" @click="setActive">Roles</span>
                    </router-link>
                    <router-link
                            id="ordersSidebarLink"
                            tag="a"
                            @click="setActive"
                            :to="{name:'orderAdmin'}">
                        <span class="admin-sidebar-link" @click="setActive">Orders</span>
                    </router-link>
                </div>
            </div>
            <div class="col-12 col-lg-10" id="content">
                <router-view></router-view>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: "AdminPage",
        methods: {
            setActive(event) {
                let tabcontent = document.getElementsByClassName("admin-sidebar-link");
                for (let i = 0; i < tabcontent.length; i++) {
                    tabcontent[i].className = tabcontent[i].className.replace(" active", "");
                }
                event.target.classList.add('active');
            }

        }
    }
</script>

<style>
    @import "../../CSS/AdminCSS.css";

    #admin-page {
        width: 95%;
    }

    #sidebar {
        margin-top: 40px;
        padding: 0;
        background-color: #f1f1f1;
        font-size: 1.05em;
    }

    /* Sidebar links */
    #sidebar a {

        text-decoration: none;
    }

    #sidebar a span {
        display: block;
        color: black;
        padding: 16px;
        text-decoration: none;
    }

    /* Active/current link */
    #sidebar a span.active {
        display: block;
        background-color: #04AA6D;
        color: white;
    }

    /* Links on mouse-over */
    #sidebar a span:hover:not(.active) {
        background-color: #555;
        color: white;
    }

    /* Page content. The value of the margin-left property should match the value of the sidebar's width property */
    div#content {
        margin-top: 30px;
        padding: 1px 1px;
    }

</style>