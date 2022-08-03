<template>
    <div class="container-fluid main-component-container">
        <div class="row">
        <h1>{{user.username}} info</h1>
        </div>
        <div class="row">
            <div class="col-6">
                UserName
            </div>
            <div class="col-6">
                {{user.username}}
            </div>
        </div>
        <div class="row">
            <div class="col-6">
                Email
            </div>
            <div class="col-6">
                {{user.email}}
            </div>
        </div>
        <div class="row">
            <div class="col-6">
                Last update
            </div>
            <div class="col-6">
                {{convertDateTime(user.updated)}}
            </div>
        </div>
        <div class="row">
            <div class="col-6">
                Created
            </div>
            <div class="col-6">
                {{convertDateTime(user.created)}}
            </div>
        </div>
    </div>
</template>

<script>
    import ConvertDateTime from "@/functions/ConvertDateTime";
    export default {
        name: "UserInfo",
        props:["user"],
        methods: {
            convertDateTime(date) {
                return ConvertDateTime(date);
            }}
    }
</script>

<style scoped>

</style>