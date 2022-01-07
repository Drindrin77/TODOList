package com.drindrin.todolist.auth

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.drindrin.todolist.databinding.FragmentLoginBinding
import com.drindrin.todolist.network.Api
import kotlinx.coroutines.launch
import retrofit2.Response
import androidx.navigation.fragment.findNavController
import com.drindrin.todolist.R
import com.drindrin.todolist.models.LoginForm
import com.drindrin.todolist.models.LoginResponse
import com.drindrin.todolist.network.SHARED_PREF_TOKEN_KEY


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val webService = Api.userWebService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        binding.loginBtn.setOnClickListener{
            val email: String = binding.emailTxtview.text.toString()
            val pwd : String = binding.pwdTxtview.text.toString()

            if(email.isEmpty() || pwd.isEmpty()){
                Toast.makeText(context, "Veuillez tout remplir", Toast.LENGTH_LONG).show()
            }else{
                val loginForm = LoginForm(email, pwd);

                lifecycleScope.launch {
                    val res: Response<LoginResponse> = webService.login(loginForm)
                    if(res.isSuccessful){
                        val loginRes: LoginResponse = res.body()!!
                        //add token in shared preference
                        val preferences = activity!!.getSharedPreferences("MY_APP", Context.MODE_PRIVATE)
                        preferences.edit().putString(SHARED_PREF_TOKEN_KEY, loginRes.token).apply()

                        //Open task list
                        findNavController().navigate(R.id.action_loginFragment_to_taskListFragment)

                    }else{
                        Toast.makeText(context, "Ce compte n'existe pas", Toast.LENGTH_LONG).show()
                    }


                }



            }
        }
    }



}