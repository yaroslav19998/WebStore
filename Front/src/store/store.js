import Vue from 'vue'
import Vuex from 'vuex'
import moduleCart from "@/store/moduleCart";
import moduleAuth from "@/store/moduleAuth";

Vue.use(Vuex);


export default new Vuex.Store({

    modules:
        {moduleCart,moduleAuth}

});
