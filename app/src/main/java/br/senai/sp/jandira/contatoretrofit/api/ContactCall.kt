package br.senai.sp.jandira.contatoretrofit.api

import br.senai.sp.jandira.contatoretrofit.Constans.Contants
import br.senai.sp.jandira.contatoretrofit.model.Contact
import retrofit2.Call
import retrofit2.http.*

interface ContactCall {

    @GET("contacts")
    fun getAll(): Call<List<Contact>>

    @POST("contacts")
    fun postSave(@Body contact: Contact): Call<Contact>

    @DELETE("Contacts/{id}")
    fun delete(@Path("id") id:Long): Call<String>
}