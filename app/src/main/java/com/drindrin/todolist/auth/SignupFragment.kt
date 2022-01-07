package com.drindrin.todolist.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.drindrin.todolist.R
import com.drindrin.todolist.databinding.FragmentSignupBinding
import com.drindrin.todolist.models.SignupForm
import com.drindrin.todolist.network.Api
import kotlinx.coroutines.launch

class SignupFragment : Fragment() {

    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!
    private val webService = Api.userWebService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.signupBtn.setOnClickListener {
            val email: String = binding.emailSignTxtview.text.toString()
            val lastname: String = binding.lastnameTxtview.text.toString()
            val firstname: String = binding.firstnameTxtview.text.toString()
            val pwdConfirm: String = binding.pwdConfirmSignTxtview.text.toString()
            val pwd: String = binding.pwdSignTxtview.text.toString()

            if(email.isEmpty() || lastname.isEmpty() || firstname.isEmpty() || pwd.isEmpty() || pwdConfirm.isEmpty() ){
                Toast.makeText(context, "Veuillez tout remplir", Toast.LENGTH_LONG).show()
            }else if (pwd != pwdConfirm){
                Toast.makeText(context, "Mot de passe non identiques", Toast.LENGTH_LONG).show()
            }else{
                val signUpForm = SignupForm(firstname,lastname, email, pwd , pwdConfirm )
                lifecycleScope.launch {
                    val signUpRes = webService.signUp(signUpForm)
                    if(signUpRes.isSuccessful){
                        Toast.makeText(context, "Création de compte réussi", Toast.LENGTH_LONG).show()
                        findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
                    }else{
                        Toast.makeText(context, "Vrai email / mdp", Toast.LENGTH_LONG).show()
                    }
                }
            }

        }
    }
}