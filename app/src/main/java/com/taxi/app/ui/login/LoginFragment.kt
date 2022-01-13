package com.taxi.app.ui.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.taxi.app.R
import com.taxi.app.data.local.prefs.UserPreferenceProvider
import com.taxi.app.data.remote.Status
import com.taxi.app.databinding.FragmentLoginBinding
import com.taxi.app.utils.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class LoginFragment : Fragment() {

    companion object {
        private val TAG: String = LoginFragment::class.java.simpleName
    }

    private lateinit var binding: FragmentLoginBinding

    private val viewModel: LoginViewModel by viewModels()

    @Inject
    lateinit var userPref: UserPreferenceProvider

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "onCreateView")
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observerMessage()
        observerLoginResponse()
        binding.btnLogin.setSafeOnClickListener {
            activity?.hideKeyboard()
            viewModel.userLogin()
        }
        binding.tilEmail.editText?.doOnTextChanged { _, _, _, _ ->
            viewModel.errorEmail.value = null
        }
        binding.tilPassword.editText?.doOnTextChanged { _, _, _, _ ->
            viewModel.errorPassword.value = null
        }
        binding.btnRegister.setSafeOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun observerMessage() {
        viewModel.message.observeEvent(this, {
            activity?.toast(it)
        })
    }

    private fun observerLoginResponse() {
        viewModel.loginResponse.observeEvent(this, {
            when (it.status) {
                Status.LOADING -> {
                    Log.d(TAG, "LOADING...")
                    binding.loaderView.progressView.visible()
                }
                Status.SUCCESS -> {
                    Log.d(TAG, "SUCCESS...")
                    binding.loaderView.progressView.gone()
                    if (it.data?.code != 1) {
                        findNavController().navigate(R.id.action_loginFragment_to_dashboardFragment)
                    }
                }
                Status.ERROR -> {
                    Log.d(TAG, "ERROR...")
                    binding.loaderView.progressView.gone()
                    activity?.toast(it.message)
                }
            }
        })
    }

}