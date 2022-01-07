package com.drindrin.todolist.auth

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import com.drindrin.todolist.R
import com.drindrin.todolist.databinding.FragmentAuthenticationBinding
import com.drindrin.todolist.databinding.FragmentTaskListBinding
import com.drindrin.todolist.network.Api

class AuthenticationFragment : Fragment() {

    private var _binding: FragmentAuthenticationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAuthenticationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val token = Api.getToken()
        if(token != null){
            findNavController().navigate(R.id.action_authenticationFragment_to_taskListFragment)
        }else {
            binding.signupButton.setOnClickListener {
                findNavController().navigate(R.id.action_authenticationFragment_to_signupFragment)
            }

            binding.loginButton.setOnClickListener {
                findNavController().navigate(R.id.action_authenticationFragment_to_loginFragment)
            }
        }
    }
}