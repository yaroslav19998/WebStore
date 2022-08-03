// import { helpers as vuelidateHelpers }  from 'vuelidate/lib/validators'


const passwordValidator = value => {
    if (value==null) {
        return true
    }

    return value.length>=8 && value.length<=12
}
export {passwordValidator}
