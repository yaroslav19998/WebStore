<template>
    <div class="container-fluid">
        <table class="table table-bordered" v-if="orders.length>0">
            <thead class="table-dark ">
            <tr>
                <th>ID</th>
                <th>Info</th>
                <th>Products</th>
                <th>Price</th>
                <th>Status</th>
                <th>Created date</th>
                <th>Updated date</th>
            </tr>
            </thead>
            <tbody>

            <OrderListItem  :order=order v-for="order in orders" :key="order.id"/>
            </tbody>
        </table>
        <h4 v-else>You have no orders</h4>
    </div>
</template>

<script>
    import OrderListItem from "@/components/ForUserComponents/OrderComponents/OrderListItem";
    export default {
        name: "OrderList",
        components: {OrderListItem},
        props:["orders"]
    }
</script>

<style scoped>

</style>