<template>
    <div class="dialog" v-if="show" @click.stop="hideDialog">
        <div @click.stop class="dialog__content"><div class="cancel"><button  @click.stop="hideDialog" class="btn-close"></button></div> <slot></slot></div>
    </div>
</template>

<script>
    export default {
        name: "CreateUpdateDialog",
        props:
            {
                show:{type:Boolean,default:false}
        },
        methods:{
            hideDialog(){
                this.$emit('update:show',false)
            }
        }
    }
</script>

<style scoped>
    .dialog{
        top:0;bottom: 0;right: 0;left: 0; background: rgba(0,0,0,0.5);
        position: fixed;
        display: flex;
        animation-name: modalaninimation;
        animation-duration: 0.5s;
        overflow-y: auto;
        z-index: 10;

    }
    .cancel{
        text-align: right;
        margin-bottom: 15px;
    }
    .dialog__content{
        margin: auto;
        background: white;
        border-radius: 12px;
        min-height:50px;
        min-width: 500px;
        padding: 20px;
    }
    @keyframes modalaninimation {
        from {
            top:-100%;
        }

        to {
            top:0px
        }
    }
</style>