package br.senai.sp.jandira.contatoretrofit

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.senai.sp.jandira.contatoretrofit.api.ContactCall
import br.senai.sp.jandira.contatoretrofit.api.RetrofitApi
import br.senai.sp.jandira.contatoretrofit.model.Contact
import br.senai.sp.jandira.contatoretrofit.ui.theme.ContatoRetrofitTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ContatoRetrofitTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {

    val context = LocalContext.current

    var nameState by remember{
        mutableStateOf("")
    }
    var emailState by remember{
        mutableStateOf("")
    }
    var phoneState by remember{
        mutableStateOf("")
    }
    var activeState by remember{
        mutableStateOf(false)
    }

    val retrofit = RetrofitApi.getRetrofit()
    val contactsCall = retrofit.create(ContactCall::class.java)
    val call = contactsCall.getAll()

    var contactsAll by remember {
        mutableStateOf(listOf<Contact>())
    }

    call.enqueue(object: Callback<List<Contact>>{
        override fun onResponse(call: Call<List<Contact>>, response: Response<List<Contact>>) {
            contactsAll = response.body()!!
        }

        override fun onFailure(call: Call<List<Contact>>, t: Throwable) {
            Log.i("kkkk", t.message.toString())
        }
    })

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)) {
        Text(text = "Cadastro de contatos")
        OutlinedTextField(
            value = nameState,
            onValueChange = {nameState = it},
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(text = "Contact name")
            })
        OutlinedTextField(
            value = emailState,
            onValueChange = {emailState = it},
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(text = "Contact email")
            })
        OutlinedTextField(
            value = phoneState,
            onValueChange = {phoneState = it},
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(text = "Contact phone")
            })
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = activeState, onCheckedChange = {activeState = it})
            Text(text = "Enable")
        }

        Button(onClick = {
            val newContact = Contact(
                name = nameState,
                email = emailState,
                phone = phoneState,
                active = activeState
            )

            val callContactpost = contactsCall.postSave(newContact)

            callContactpost.enqueue(object: Callback<Contact>{
                override fun onResponse(call: Call<Contact>, response: Response<Contact>) {
                    Log.i("PostMessage", response.body().toString())
                }

                override fun onFailure(call: Call<Contact>, t: Throwable) {

                }

            })


        }) {
            Text(text = "Save new Content")
        }
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(contactsAll){
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 15.dp)
                    .clickable {
                        nameState = it.name
                        emailState = it.email
                        phoneState = it.phone
                        activeState = it.active
                    },
                    backgroundColor = Color.Yellow
                ) {
                    Column() {
                        Text(text = it.name)
                        Text(text = it.email)
                        Text(text = it.phone)
                        Button(onClick = {
                            val callContactDelete = contactsCall.delete(it.id)
                            
                            callContactDelete.enqueue(object : Callback<String>{
                                override fun onResponse( call: Call<String>, response: Response<String>) {
                                    Toast.makeText(context, "Usuário deletado", Toast.LENGTH_LONG).show()
                                }

                                override fun onFailure(call: Call<String>, t: Throwable) {
                                    Log.i("OLA", t.message.toString())
                                }
                            })

                        }) {
                            Text(text = "Delete")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview() {
    ContatoRetrofitTheme {
        Greeting("Android")
    }
}