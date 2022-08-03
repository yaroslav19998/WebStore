<template>
    <form @submit.prevent>
        <h3 class="form-header">Product form</h3>
        <div v-for="(error,index) in serverValidErrors" :key="index">
            <ErrorMsgValidator :error-text="error"/>
        </div>
        <div class="form-floating">
            <input type="text" v-model="product.name" placeholder=" " class="form-control" id="Name"  :class="{'is-invalid':nameErrors!=null}">
            <label for="Name">Name</label>
            <ErrorMsgValidator :error-text="nameErrors" v-if="nameErrors!=null"/>
        </div>
        <div class="form-floating">
            <textarea class="form-control" v-model="product.description" placeholder=" " id="description"></textarea>
            <label for="description">Description</label>
        </div>
        <div class="form-floating">
            <input type="number" class="form-control" placeholder=" " id="price" step="0.01"  :class="{'is-invalid':priceErrors!=null}" min="1" v-model="product.price">
            <label for="price">Price</label>
            <ErrorMsgValidator :error-text="priceErrors" v-if="priceErrors!=null"/>
        </div>
        <div class="form-floating">
            <select class="form-select" id="brand" v-model="product.brand" :class="{'is-invalid':brandErrors!=null}" >
                <option v-for="brand in brands" v-bind:value="brand"
                        :key="brand.id">
                    {{ brand.name }}
                </option>
            </select>
            <label for="brand">Brand</label>
            <ErrorMsgValidator :error-text="brandErrors" v-if="brandErrors!=null"/>
        </div>
        <div class="form-floating">
            <select class="form-select" id="category" v-model="product.category" :class="{'is-invalid':categoryErrors!=null}">
                <option v-for="category in categories" v-bind:value="category"
                        :key="category.id">
                    {{ category.name }}
                </option>
            </select>
            <label for="category">Category</label>
            <ErrorMsgValidator :error-text="categoryErrors" v-if="categoryErrors!=null"/>
        </div>

        <div>
            <h4>Characteristics</h4>
            <p>
                <button type="button" class="btn btn-secondary" @click="addCharacteristic">Add characteristic</button>
            </p>
            <div class="container-fluid characteristic-container">
            <div class="row" v-for="(characteristicInput, index) in  characteristicInputs" :key="index">
                <div class="form-floating col-5">
                <select class="form-select" :id="'characteristicSelect'+index"
                        v-model="characteristicInput.characteristic.id"
                        @change="onChangeCharacteristic(characteristicInput)">
                    <option v-for="characteristic in characteristics" v-bind:value="characteristic.id"
                            :key="characteristic.id">
                        {{ characteristic.name }}
                    </option>
                </select>
                <label :for="'characteristicSelect'+index">Select characteristic</label>
                </div>
                <div  class="col-5" v-if="characteristicInput.characteristic!=null">
                    <input class="form-control characteristic-value"
                           :id="characteristicInput.characteristic.id"
                           type="date" v-model="characteristicInput.value"
                           v-if=" characteristicInput.type=='DATE'"
                           @change="changeCharacteristic(characteristicInput)"/>
                    <input class="form-control  characteristic-value"
                           :id="characteristicInput.characteristic.id"
                           type="number" v-model="characteristicInput.value"
                           v-if="characteristicInput.type=='DECIMAL'"
                           @change="changeCharacteristic(characteristicInput)"/>
                    <input class="form-control  characteristic-value" :id="characteristicInput.characteristic.id"
                           type="text"
                           maxlength="12"
                           onkeyup="this.value = this.value.replace (/\D/, '')"
                           v-model="characteristicInput.value" v-if="characteristicInput.type=='INT'"
                           @change="changeCharacteristic(characteristicInput)"/>
                    <div  v-if=" characteristicInput.type=='SINGLESTRING'">
                        <div class="form-check  characteristic-value"
                             v-for="stringValue in characteristicInput.characteristic.characteristicSingleStringValues"
                             :key="stringValue.id">
                            <input  class="form-check-input"
                                    type="radio"
                                    :value="stringValue.id"
                                    :id="stringValue"
                                    v-model="characteristicInput.value"
                                    @change="changeCharacteristic(characteristicInput)">
                            <label class="form-check-label" :for="stringValue.id">{{stringValue.value}}</label>
                        </div>
                    </div>
                    <div v-if="characteristicInput.type=='MULTIPLESTRING'">
                        <div v-for="stringValue in characteristicInput.characteristic.characteristicMultipleStringValues"
                             :key="stringValue.id">
                            <input class="form-check-input"
                                   type="checkbox"
                                   :id="stringValue.id"
                                   :value="stringValue.id"
                                   v-model="characteristicInput.value"
                            @change="changeCharacteristic(characteristicInput)">
                            <label class="form-check-label" :for="stringValue.id">{{stringValue.value}}</label>
                        </div>
                    </div>


                </div>
                <div class="col-2">
                     <button type="button" class="btn btn-danger" @click="removeCharacteristic(characteristicInput)">Delete</button>
                </div>
            </div>
            </div>
        </div>

        <div>

            <h4>Images</h4>
            <div  class="container">
                <div class="row" v-if="selectedProduct!=null&&selectedProduct.images!=null">
                    <div v-for="(image,index) in product.images" :key="image.id" class="col-3 image-column">
                        <div class="img-btn-container">
                            <label class="btn btn-success img-btn" v-if="!image.is_main" @click="changeMain(index,false)">Make main</label>
                            <label v-else>Main</label>
                            <input type="file" class="input-file-edit"  :id="'newimg_'+index" accept="image/jpeg, image/png, image/jpg" @change="editImage($event,image,index)">

                            <label class="btn btn-warning img-btn" :for="'newimg_'+index">Edit</label>
                            <button class="btn btn-danger img-btn" @click="deleteImage(image,index)">Delete</button>
                        </div>
                        <img :src="'http://localhost:8081/api/products/images/'+image.id"/>

                    </div>
                </div>
                <div class="row">
                    <h5>New or changed images</h5>
                    <div>
                    <input type="file" class="form-control" id="uploadNewImage" accept="image/jpeg, image/png, image/jpg" multiple @change="uploadImage">

                    </div>
                    <div class="col-3 new-image-column" v-for="(image,index) in viewImages" :key="index">
                        <div class="img-btn-container">
                            <label class="btn btn-success img-btn" v-if="!(!checkHaveMain&& index==0)" @click="changeMain(index,true)">Make main</label>
                            <label v-else>Main</label>
                            <input type="file" class="input-file-edit"  :id="'newimg_'+index" accept="image/jpeg, image/png, image/jpg" @change="editImage($event,image,index)">
                            <label class="btn btn-warning img-btn" :for="'newimg_'+index">Edit</label>
                            <button class="btn btn-danger img-btn" @click="deleteImage(image,index)">Delete</button>
                        </div>
                        <img :src="image" alt="new image"  />
                    </div>
                </div>
                <div class="row" v-if="selectedProduct">
                    <ProductReviews :productId="selectedProduct.id"/>
                </div>
            </div>
        </div>
        <div class="create-form-btn">
            <input type="button" class="btn btn-success btn-lg" v-if="selectedProduct" value="Change" @click="update()">
            <input type="button" class="btn btn-success btn-lg" v-else value="Create" @click="create">
        </div>
    </form>

</template>

<script>
    import CharacteristicService from "@/services/СharacteristicService";
    import BrandService from "@/services/BrandService";
    import CategoryService from "@/services/CategoryService";
    import ProductReviews from "@/components/ForUserComponents/ProductComponents/ProductReviews";
    import ErrorMsgValidator from "@/components/ForUserComponents/ErrorMsgValidator";
    import {required} from 'vuelidate/lib/validators'

    const greaterThanZero = (value) => value > 0
    export default {
        name: "ProductForm",
        props: ["selectedProduct","serverValidErrors"],
        components:{ProductReviews,ErrorMsgValidator},
        data() {
            return {
                product: {
                    id: null,
                    name: '',
                    description: '',
                    price: null,
                    brand:null,
                    category:null
                },
                categories: [],
                brands:[],
                characteristics: [],
                characteristicInputs: [],
                deletedCharacteristics:[],
                images: [],
                viewImages:[],
                deletedImages:[],
                updatedImageIsMain:false
            }
        },

        validations:{
            product:{
                name:{required},
                price:{required,minValue:greaterThanZero},
                brand:{required},
                category:{required},
            },


        },

        computed:{
            nameErrors(){
                if(!this.$v.product.name.required && this.$v.product.name.$dirty){
                    return "Name must not be empty"
                }


                return null;
            },
            priceErrors(){
                if(!this.$v.product.price.required && this.$v.product.price.$dirty){
                    return "Price must not be empty"
                }
                if(!this.$v.product.price.minValue && this.$v.product.price.$dirty){
                    return "Price must be a positive number";
                }

                return null;
            },
            brandErrors(){
                if(!this.$v.product.brand.required && this.$v.product.brand.$dirty){
                    return "Select brand"
                }

                return null;
            },
            categoryErrors(){
                if(!this.$v.product.category.required && this.$v.product.category.$dirty){
                    return "Select category"
                }


                return null;
            },
            maxImageLength: function () {
                let productsLength=0;
                if(this.product.images!=null){
                    productsLength=this.product.images.length
                }
                return 4 - productsLength - this.images.length;
            },
            checkHaveMain:function () {
                if(this.product.images!=null){
                    return this.product.images.filter(img=>img.is_main==true).length
                }
                else {
                    return false
                }
            }
        },
        methods: {
            uploadImage(e){
                let uploadedFiles=e.target.files;
                let files=uploadedFiles;
                if(files.length<=this.maxImageLength)
                    for( let i = 0; i <files.length; i++ ){
                        this.images.push(uploadedFiles[i]);
                        var reader = new FileReader();
                            reader.onload=(e)=>{
                              this.viewImages.push(e.target.result);
                            };
                            reader.readAsDataURL( files[i] );
                    }
                else {
                    alert("you can upload only 4 images for product")
                  let file = document.getElementById("uploadNewImage");
                    console.log(file.value);
                    file.value=null;
                   // e.target.files=null;
                }
            },
            changeMain(index,updatedImage){
                if(this.selectedProduct == null){
                    this.images.splice(0, 0, this.images.splice(index, 1)[0]);
                    this.viewImages.splice(0, 0, this.viewImages.splice(index, 1)[0]);
                }
                else {
                    this.product.images.forEach(img=>{
                        img.is_main=false;
                    });
                    if(updatedImage){
                        if(index!=0){
                            this.images.splice(0, 0, this.images.splice(index, 1)[0]);
                            this.viewImages.splice(0, 0, this.viewImages.splice(index, 1)[0]);
                        }

                    }
                    else {
                        this.product.images[index].is_main=true;
                    }

                }
            },
            deleteImage (image, index) {
                var r = confirm("remove image");
                if (r == true) {
                    if(image.id!=null ){
                        this.product.images.splice(index,1)
                        this.deletedImages.push(image);
                        console.log(this.deletedImages)
                    }
                    else{
                        this.images.splice(index,1);
                        this.viewImages.splice(index,1);
                    }

                }
            },
            editImage (e,image, index) {
                var reader = new FileReader();
                if(image.id==undefined || image.id==null){
                    this.images.splice(index,1,e.target.files[0]);
                    reader.onload=(e)=>{
                        this.viewImages.splice(index,1,e.target.result);
                    };
                    reader.readAsDataURL(e.target.files[0]);
                }
                else {
                    this.product.images.splice(index,1)
                    this.deletedImages.push(image);
                    this.images.push(e.target.files[0]);

                    reader.onload=(e)=>{
                        this.viewImages.push(e.target.result);
                    };
                    reader.readAsDataURL(e.target.files[0]);

                }

             },
             create() {
                 if(this.$v.$invalid){
                     this.$v.$touch()
                     return
                 }
                let ProductDTO = {
                    product: this.product,
                    intValues: [],
                    decimalValues: [],
                    dateValues: [],
                    multipleStrings: [],
                    singleStrings: [],
                };

                this.characteristicInputs.forEach(characteristicInput => {
                    if(characteristicInput.value!=null&& characteristicInput.value!=''){


                    if (characteristicInput.type == 'DATE') {
                        ProductDTO.dateValues.push(
                            {
                                characteristic: {id: characteristicInput.characteristic.id},
                                value: characteristicInput.value
                            });
                    } else if (characteristicInput.type == 'DECIMAL') {
                        ProductDTO.decimalValues.push(
                            {
                                characteristic: {id: characteristicInput.characteristic.id},
                                value: characteristicInput.value
                            });
                    } else if (characteristicInput.type == 'INT') {
                        ProductDTO.intValues.push(
                            {
                                characteristic: {id: characteristicInput.characteristic.id},
                                value: characteristicInput.value
                            });
                    } else if (characteristicInput.type == 'MULTIPLESTRING') {
                        characteristicInput.value.forEach(id => {
                            ProductDTO.multipleStrings.push(
                                {
                                    characteristic: {id: characteristicInput.characteristic.id},
                                    id: id
                                });
                        })
                    }
                    else if (characteristicInput.type == 'SINGLESTRING') {
                        ProductDTO.singleStrings.push(
                            {
                                characteristic: {id: characteristicInput.characteristic.id},
                                value: {id: characteristicInput.value}
                            });
                    }}
                });
               this.$emit('create', ProductDTO,this.images);
            },
            update() {
                if(this.$v.$invalid){
                    this.$v.$touch()
                    return
                }
                let ProductDTO = {
                    product: this.product,
                    deletedCharacteristics:this.deletedCharacteristics,
                    deletedImages: this.deletedImages
                };
                this.characteristicInputs.forEach(characteristicInput => {
                    if(characteristicInput.value!=null&& characteristicInput.value!='')
                    {
                    if (characteristicInput.type == 'DATE') {
                        let index=this.product.dateCharacteristics.findIndex(x => x.characteristic.id === characteristicInput.characteristic.id);
                        if(index>=0){
                            ProductDTO.product.dateCharacteristics[index].characteristic= {id: characteristicInput.characteristic.id}
                            ProductDTO.product.dateCharacteristics[index].product={id:this.product.id}
                            ProductDTO.product.dateCharacteristics[index].value=characteristicInput.value;
                        }
                        else {
                            ProductDTO.product.dateCharacteristics.push( {
                                product:{id:this.product.id},
                                characteristic: {id: characteristicInput.characteristic.id},
                                value: characteristicInput.value
                            })
                        }
                    } else if (characteristicInput.type == 'DECIMAL') {
                        console.log("decimal")
                        let index=this.product.decimalCharacteristics.findIndex(x => x.characteristic.id === characteristicInput.characteristic.id);
                        if(index>=0){
                            ProductDTO.product.decimalCharacteristics[index].characteristic= {id: characteristicInput.characteristic.id}
                            ProductDTO.product.decimalCharacteristics[index].product={id:this.product.id}
                            ProductDTO.product.decimalCharacteristics[index].value=characteristicInput.value;
                        }
                        else {
                            ProductDTO.product.decimalCharacteristics.push( {
                                product:{id:this.product.id},
                                characteristic: {id: characteristicInput.characteristic.id},
                                value: characteristicInput.value
                            })
                        }
                    } else if (characteristicInput.type == 'INT') {
                        let index=this.product.intCharacteristics.findIndex(x => x.characteristic.id === characteristicInput.characteristic.id);
                        if(index>=0){
                            ProductDTO.product.intCharacteristics[index].characteristic= {id: characteristicInput.characteristic.id}
                            ProductDTO.product.intCharacteristics[index].product={id:this.product.id}
                            ProductDTO.product.intCharacteristics[index].value=characteristicInput.value;
                        }
                        else {
                            ProductDTO.product.intCharacteristics.push( {
                                product:{id:this.product.id},
                                characteristic: {id: characteristicInput.characteristic.id},
                                value: characteristicInput.value
                            })
                        }
                    } else if (characteristicInput.type == 'MULTIPLESTRING') {
                        characteristicInput.value.forEach(id => {ProductDTO.product.multipleStringCharacteristics.push( {
                            characteristic: {id: characteristicInput.characteristic.id},
                            id: id
                        });
                    })}
                    else if (characteristicInput.type == 'SINGLESTRING') {
                        let index=this.product.singleStringCharacteristics.findIndex(x => x.characteristic.id === characteristicInput.characteristic.id);
                        if(index>=0){
                            ProductDTO.product.singleStringCharacteristics[index].characteristic= {id: characteristicInput.characteristic.id}
                            ProductDTO.product.singleStringCharacteristics[index].product={id:this.product.id}
                            ProductDTO.product.singleStringCharacteristics[index].value={id:characteristicInput.value};
                        }
                        else {
                            ProductDTO.product.singleStringCharacteristics.push( {
                                product:{id:this.product.id},
                                characteristic: {id: characteristicInput.characteristic.id},
                                value: {id:characteristicInput.value}
                            })
                        }
                    }}
                });

                this.$emit('update', ProductDTO,this.images);
            },
            addCharacteristic() {
                this.characteristicInputs.push({characteristic:{id:null},value: ""});
            },
            changeCharacteristic(characteristicInput){
                characteristicInput.changed=true;
            },
            removeCharacteristic(characteristicInput) {
                if (this.characteristicInputs.length >= 1) {
                    let index = this.characteristicInputs.indexOf(characteristicInput);
                    this.characteristicInputs.splice(index, 1);
                }
                if(this.selectedProduct != null){
                    this.deletedCharacteristics.push({product:this.product,characteristic:characteristicInput.characteristic})
                }
            },

            onChangeCharacteristic(characteristicInput) {
                characteristicInput.value = null;
                let characteristic=this.characteristics.filter(ch=>ch.id==characteristicInput.characteristic.id)[0]
                characteristicInput.characteristic=characteristic;
                characteristicInput.type = characteristicInput.characteristic.valueType;
                if (characteristicInput.type == "MULTIPLESTRING") {
                    characteristicInput.value = [];
                }

            },
            productCharacteristicsToInput(product){
                if(product.dateCharacteristics!=null && product.dateCharacteristics.length!=0){
                    product.dateCharacteristics.forEach(characteristicValue=>this.characteristicInputs.push({
                        type:characteristicValue.characteristic.valueType,
                        value:characteristicValue.value,
                        characteristic:characteristicValue.characteristic}))
                }
                if(product.decimalCharacteristics!=null && product.decimalCharacteristics.length!=0){
                    product.decimalCharacteristics.forEach(characteristicValue=>this.characteristicInputs.push({
                        type:characteristicValue.characteristic.valueType,
                        value:characteristicValue.value,
                        characteristic:characteristicValue.characteristic}))
                }
                if(product.intCharacteristics!=null && product.intCharacteristics.length!=0){
                    product.intCharacteristics.forEach(characteristicValue=>this.characteristicInputs.push({
                        type:characteristicValue.characteristic.valueType,
                        value:characteristicValue.value,
                        characteristic:characteristicValue.characteristic}))
                }
                if(product.singleStringCharacteristics!=null && product.singleStringCharacteristics.length!=0){

                    product.singleStringCharacteristics.forEach(characteristicValue=>{
                        let ch=this.characteristics.find(ch=>characteristicValue.characteristic.id==ch.id)
                        this.characteristicInputs.push({
                        type:characteristicValue.characteristic.valueType,
                        value:characteristicValue.value.id,
                        characteristic:ch})})
                }
                if(product.multipleStringCharacteristics!=null && product.multipleStringCharacteristics.length!=0){
                    product.multipleStringCharacteristics.forEach(characteristicValue=>{
                        let multipleArray=product.multipleStringCharacteristics.filter(
                            multipleValue=>multipleValue.characteristic.id===characteristicValue.characteristic.id).reduce((acc, val) => {
                            // Удаляем отфильтрованные элементы из оригинального массива.
                            product.multipleStringCharacteristics.splice( product.multipleStringCharacteristics.indexOf(val), 1);
                            // Возвращаем новый массив только с отфильтрованными элементами.
                            return acc.concat(val);
                        }, []);
                        let ids=Array.from(multipleArray,item=>item.id);
                        let ch=this.characteristics.find(ch=>characteristicValue.characteristic.id==ch.id)
                        this.characteristicInputs.push({
                            type:characteristicValue.characteristic.valueType,
                            value:ids,
                            characteristic:ch})})

                }
            },
            fetchData(){
                CharacteristicService.getAll(null,'relations').then(
                    response => {
                        this.characteristics = response.data["content"]
                        console.log("ch")
                    }).catch(error => console.log(error));
                BrandService.getAll(null,"relations").then(
                    response => {
                        this.brands = response.data["content"]
                    }).catch(error => console.log(error));
                CategoryService.getAll(null,"relations").then(
                    response => {
                        this.categories = response.data["content"]
                    }).catch(error => console.log(error));
            }
        },
        mounted() {
            this.fetchData();
            setTimeout(() => {
                if (this.selectedProduct != null) {
                    this.product = {...this.selectedProduct};
                    this.productCharacteristicsToInput(this.product)
                }
            }, 500);

        }

    }
</script>

<style scoped>
    select {
        margin: 5px 0
    }
    .characteristic-value{
        margin:5px 0
    }
    div {
        margin: 10px 0;

    }
    .input-file-edit{
        width: 0.01px;
        height: 0.01px;
        overflow: hidden;
        z-index: -1;
    }
.image-column img{
     max-height: 100%;
     max-width: 100%;
 }
.img-btn-container{
    margin-bottom: 0px;
    text-align: right;
}
    .img-btn{
        border-radius: 5px 5px 0 0;
        padding: 5px;
    }
    .new-image-column img{
        max-height: 100%;
        max-width: 100%;
    }
    .new-image-column .delete-image{
        position: relative; left: 100%; top:11.5%;
        font-size: 1.2em;
        background-color:lightcoral;
        border: black 1px solid;
        border-radius: 0 5px 5px 0 ;
    }
    .image-column .delete-image:hover{background-color: red;}
    form {
        width: 1000px;
    }
    .image-uploader>>>.image-container{height: 300px!important;width: 300px!important}
    .image-uploader>>>.image-icon-drag{height: 70px!important;width: 70px!important;}
    .image-uploader>>>.image-center .text-center{font-size: 1.3em!important;}
    .image-uploader>>>.image-container{height: 300px!important;width: 300px!important}
    .image-uploader>>>.image-container .image-bottom{height: 20%!important;}

    .image-uploader>>>.preview-image .show-img{max-height: 220px!important;max-width: 230px!important; }

    .image-uploader>>>.image-container .image-bottom .image-primary{font-size: 1.5em!important;}
    .image-uploader>>>.image-container .image-bottom .image-icon-edit, .image-uploader>>>.image-container .image-bottom .image-icon-delete{
        height: 20px!important;width: 20px!important;
    }
    .image-uploader>>>.image-container .image-bottom .image-icon-info{
        width: 20px!important;
        height: 20px!important;
    }
    .image-uploader>>>.image-container .preview-image{
        height: 240px!important;
    }
    .image-uploader>>>.image-list-container{
        max-width:300px!important;
    }

        /*.image-bottom{font-size}*/
    /*.image-container>*{)}*/
    .image-uploader>>>.image-container .image-bottom .image-icon-primary{
        width: 15px!important;
        height: 15px!important;
        margin-right: 5px;
    }
    .image-uploader>>>.image-container .image-bottom .popper-custom{
        font-size: 0.8em!important;
    }
    .image-uploader>>>.image-container .image-bottom .text-small{
       font-size: 15px;
    }
    .image-uploader>>>.image-list-container .image-list-item{
        height:45px!important;width: 45px!important;
    }
    .image-uploader>>>.image-list-container .image-list-item .show-img {
        max-height:35px!important;max-width: 45px!important;
    }
    .characteristic-container {
        padding-top: 0;
    }



</style>