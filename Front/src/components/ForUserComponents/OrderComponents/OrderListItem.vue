<template>
    <tr>
        <td>{{order.id}}</td>
        <td>
            <p>Name: {{order.customerName}}</p>
            <p>Address: {{order.customerAddress}}</p>
            <p>Phone: {{order.customerPhone}}</p>
            <p>Email: {{order.customerEmail}}</p>
        </td>
        <td><p v-for="product in order.products" :key="product.id">
            Name: {{product.product.name}}
            Quantity: {{product.count}}
            Price: {{product.price}}
        </p>
        </td>
        <td>
            {{order.fullPrice}}
        </td>
        <td>
            {{order.status}}
        </td>
        <td>
            {{convertDateTime(order.created)}}
        </td>
        <td>
            {{convertDateTime(order.updated)}}
        </td>
    </tr>
</template>

<script>
    import ConvertDateTime from "@/functions/ConvertDateTime";
    export default {
        name: "OrderListItem",
        props: ["order"],
        methods:{ convertDateTime(date) {
            return ConvertDateTime(date);
        },}

    }
</script>

<style scoped>

</style>