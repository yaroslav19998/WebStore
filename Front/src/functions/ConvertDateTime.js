export default function convertDateTime(stringDate) {
        const date=new Date(stringDate)
        return  date.toLocaleDateString() +" "+ date.toLocaleTimeString()
}