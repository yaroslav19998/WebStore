<template>
    <div class="container-fluid main-component-container">
        <div class="row">
            <h1>{{product.name}}</h1>
        </div>
        <div class="product-container row">
            <div class="col-4">
                <div class="image-container">

                    <div v-for="(image,index) in product.images" :key="image.id" class="mySlides">
                        <div class="numbertext">{{index+1}} / {{product.images.length}}</div>
                        <img :src="'http://localhost:8081/api/products/images/'+image.id" style="width:100%">
                    </div>


                    <a class="prev" @click="plusSlides(-1)">&#10094;</a>
                    <a class="next" @click="plusSlides(1)">&#10095;</a>


                    <div class="image-row">
                        <div class="image-column" v-for="(image,index) in product.images" :key="image.id">
                            <img class="demo cursor" :src="'http://localhost:8081/api/products/images/'+image.id"
                                 style="width:100%" @click="currentSlide(index+1)" alt="product picture">
                        </div>
                    </div>
                </div>
            </div>
            <div class="product-info-container col-6">
                <div v-if="product.brand!=null">
                    Brand: {{product.brand.name}}
                </div>
                <div>
                    Description: {{product.description}}
                </div>
                <div class="rate">
                   <span v-if="product.averageRate!=null"> Rate: {{product.averageRate}}</span>
                    <span>
                        <a href="#" v-if="reviewsCount>0" @click="showReviews()"> {{reviewsCount}} reviews</a>
                        <span @click="showReviews()" v-else>0 reviews</span>
                    </span>
                    <a  href="#"  v-if="isUser" @click="createReview()">(Create review)</a>

                    <DialogWindow :show.sync="dialogVisible">
                        <ReviewForm :productId="this.$route.params.productId"/>
                    </DialogWindow>
                </div>
                <div class="price">
                    Price: {{product.price}}
                </div>
                <div class="col-4">
                    <div class="input-group">
                        <input  class="form-control" type="number" placeholder="quantity" id="quantityValue" v-model="quantity">
                        <button class="btn btn-success"  @click="add">Buy</button>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <h2>Characteristics</h2>

            <table id="characteristic-table" class="table table-bordered">
                <thead class="table-dark ">
                <tr>
                    <th>Characteristic</th>
                    <th>Value</th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="(characteristicValue,index) in characteristics" :key="index">
                    <td>{{characteristicValue.characteristic.name}}</td>
                    <td v-if="characteristicValue.characteristic.valueType=='SINGLESTRING'">{{characteristicValue.value.value}}</td>
                    <td v-else-if="characteristicValue.characteristic.valueType=='MULTIPLESTRING'"><div v-for="(value,index) in characteristicValue.values" :key="index">{{value}}</div> </td>
                    <td v-else>{{characteristicValue.value}} </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</template>

<script>

    import ProductService from "@/services/ProductService";
    import ReviewForm from "@/components/ForUserComponents/ProductComponents/ReviewForm";
    import DialogWindow from "@/components/DialogWindow";
    import {mapGetters} from 'vuex'
    import {mapActions} from 'vuex'
    export default {
        name: "ProductPage",
        components: { ReviewForm,DialogWindow},
        data() {
            return {
                product: {},
                reviewsCount:null,
                slideIndex: 1,
                quantity:"",
                dialogVisible: false
            }
        },
        computed: {
            mainImage: function () {
                return this.product.images.filter(c => c._main === true)
            },
            checkCharacteristic(){
                return  this.product.intCharacteristics!=null || this.product.dateCharacteristics!=null || this.product.decimalCharacteristics!=null ||
                    this.product.multipleStringCharacteristics!=null || this.product.singleStringCharacteristics!=null
            },
            characteristics: function () {
                if(this.checkCharacteristic){
                    let multipleStringCharacteristic=[]
                    if(this.product.multipleStringCharacteristics!=null){
                        this.product.multipleStringCharacteristics.forEach(characteristicValue=>{
                            let multipleArray=this.product.multipleStringCharacteristics.filter(
                                multipleValue=>multipleValue.characteristic.id===characteristicValue.characteristic.id).reduce((acc, val) => {
                                // Удаляем отфильтрованные элементы из оригинального массива.
                                this.product.multipleStringCharacteristics.splice( this.product.multipleStringCharacteristics.indexOf(val), 1);
                                // Возвращаем новый массив только с отфильтрованными элементами.
                                return acc.concat(val);
                            }, []);
                            let stringValues=[]
                            multipleArray.forEach(val=>stringValues.push(val.value))
                            multipleStringCharacteristic.push({values:stringValues,characteristic:multipleArray[0].characteristic})
                        })
                    }
                    console.log(  multipleStringCharacteristic)
                    return this.product.intCharacteristics.concat(this.product.dateCharacteristics,this.product.decimalCharacteristics,
                        multipleStringCharacteristic,this.product.singleStringCharacteristics)

                }
                else return null

            },
            ...mapGetters({
                isUser: 'moduleAuth/isUser'
            },)
        },
        methods: {
                ...mapActions({addToCart: "moduleCart/addToCart"}),
                add() {
                          this.addToCart({product:this.product,quantity:this.quantity});
                },
            async FetchData() {

                await ProductService.getById(this.$route.params.productId).then(response => {
                    this.product = response.data
                }).catch(error => console.log(error));

            },
            async getReviewCount(){
                await ProductService.getProductReviewsCount(this.$route.params.productId).then(response => {
                    this.reviewsCount = response.data
                }).catch(error => console.log(error));
            },
            showReviews(){
                this.$router.push({name:"productReviews" ,params: { productId:this.$route.params.productId }})
            },
            plusSlides(n) {
                this.showSlides(this.slideIndex += n);
            },
            createReview(){
                this.dialogVisible = true
            },

            currentSlide(n) {
                this.showSlides(this.slideIndex = n);
            },
            showSlides(n) {
                let i;
                let slides = document.getElementsByClassName("mySlides");
                let dots = document.getElementsByClassName("demo");
                if (n > slides.length) {
                    this.slideIndex = 1
                }
                if (n < 1) {
                    this.slideIndex = slides.length
                }
                for (i = 0; i < slides.length; i++) {
                    slides[i].style.display = "none";
                }
                for (i = 0; i < dots.length; i++) {
                    dots[i].className = dots[i].className.replace(" active", "");
                }
                slides[this.slideIndex - 1].style.display = "block";
                dots[this.slideIndex - 1].className += " active";
            }


        },

        mounted() {
            this.FetchData();
            setTimeout(()=>{
                this.showSlides(this.slideIndex);},100)
            this.getReviewCount();
        }
    }
</script>

<style scoped>
    .main-component-container {
        margin-top: 25px;
    }
    #characteristic-table{
        font-size: 1.2em;
    }
    td,th{
        text-align: left;
    }
    .product-info-container{
        margin-left: 70px;
    }
    .product-container{
        margin-left: 50px;
        margin-right: 50px;
        font-size: 1.3em;
    }
    .image-container {
        position: relative;
    }

    /* Hide the images by default */
    .mySlides {
        display: none;
    }

    .cursor {
        cursor: pointer;
    }

    .prev,
    .next {
        cursor: pointer;
        position: absolute;
        top: 40%;
        width: auto;
        padding: 16px;
        margin-top: -50px;
        color: white;
        font-weight: bold;
        font-size: 20px;
        border-radius: 0 3px 3px 0;
        user-select: none;
        -webkit-user-select: none;
    }

    .next {
        right: 0;
        border-radius: 3px 0 0 3px;
    }

    .prev:hover,
    .next:hover {
        background-color: rgba(0, 0, 0, 0.8);
    }

    .numbertext {
        color: #f2f2f2;
        font-size: 12px;
        padding: 8px 12px;
        position: absolute;
        top: 0;
    }

    /* Container for image text */
    .caption-container {
        text-align: center;
        background-color: #222;
        padding: 2px 16px;
        color: white;
    }

    .image-row:after {
        content: "";
        display: table;
        clear: both;
    }

    /* Six columns side by side */
    .image-column {
        float: left;
        width: 25%;
    }

    /* Add a transparency effect for thumnbail images */
    .demo {
        opacity: 0.6;
    }
    .price{
        font-size: 1.2em;
        font-weight: bold;
    }
    .active,
    .demo:hover {
        opacity: 1;
    }
</style>