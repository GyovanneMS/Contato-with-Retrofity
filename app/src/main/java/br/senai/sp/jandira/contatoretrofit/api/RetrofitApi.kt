package br.senai.sp.jandira.contatoretrofit.api

import br.senai.sp.jandira.contatoretrofit.Constans.Contants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitApi {

    companion object {
        private lateinit var instance: Retrofit

        fun getRetrofit(): Retrofit {
            if (!::instance.isInitialized){
              instance = Retrofit.Builder()
                  .baseUrl(Contants.BASE_URL)
                  .addConverterFactory(GsonConverterFactory.create())
                  .build()
            }
            return instance

        }
    }
}