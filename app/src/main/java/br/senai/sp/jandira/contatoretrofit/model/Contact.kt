package br.senai.sp.jandira.contatoretrofit.model

data class Contact(

    var id: Long = 0,
    var name: String = "",
    var phone: String = "",
    var email: String = "",
    var active: Boolean = true
){
    override fun toString(): String {
        return "Contact(id=$id, name='$name', phone='$phone', email='$email', active=$active)"
    }
}
