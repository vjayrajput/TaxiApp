package com.taxi.app.ui.register

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
import com.taxi.app.data.remote.Status
import com.taxi.app.databinding.FragmentRegisterBinding
import com.taxi.app.utils.autoCleared
import com.taxi.app.utils.extensions.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    companion object {
        private val TAG: String = RegisterFragment::class.java.simpleName
    }

    private var binding: FragmentRegisterBinding by autoCleared()

    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "onCreateView")
        binding = DataBindingUtil.inflate(
            layoutInflater, R.layout.fragment_register, container, false
        )
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    private fun observerMessage() {
        viewModel.message.observeEvent(this, {
            activity?.toast(it)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated")
        observerMessage()
        observerRegisterResponse()
        binding.btnRegister.setSafeOnClickListener {
            activity?.hideKeyboard()
            viewModel.userRegister()
        }
        binding.tilName.editText?.doOnTextChanged { inputText, _, _, _ ->
            viewModel.errorName.value = null
        }
        binding.tilEmail.editText?.doOnTextChanged { inputText, _, _, _ ->
            viewModel.errorEmail.value = null
        }
        binding.tilPassword.editText?.doOnTextChanged { inputText, _, _, _ ->
            viewModel.errorPassword.value = null
        }
    }

    private fun observerRegisterResponse() {
        viewModel.registerResponse.observeEvent(this, {
            when (it.status) {
                Status.LOADING -> {
                    Log.d(TAG, "LOADING...")
                    binding.loaderView.progressView.visible()
                }
                Status.SUCCESS -> {
                    Log.d(TAG, "SUCCESS...")
                    binding.loaderView.progressView.gone()
                    if (it.data?.code != 1) {
                        findNavController().navigate(R.id.action_registerFragment_to_dashboardFragment)
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