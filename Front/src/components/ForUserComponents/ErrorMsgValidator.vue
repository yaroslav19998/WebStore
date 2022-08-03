<template>
    <small class="invalid">
      {{errorText}}
    </small>
</template>

<script>
    export default {
        name: "ErrorMsgValidator",
        props:["errorText"]

    }
</script>

<style scoped>
small{
    font-size: 1em;
    color: red;
}
</style>