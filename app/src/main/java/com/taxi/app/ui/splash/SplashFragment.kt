package com.taxi.app.ui.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.taxi.app.R
import com.taxi.app.data.local.prefs.UserPreferenceProvider
import com.taxi.app.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : Fragment() {

    companion object {
        private val TAG: String = SplashFragment::class.java.simpleName
        private const val SPLASH_TIME_OUT = 3000L
    }

    private var _binding: FragmentSplashBinding? = null

    private val binding get() = _binding!!

    @Inject
    lateinit var userPref: UserPreferenceProvider

    private var isFinished = false

    private lateinit var splashHandler: Handler

    private var splashRunnable: Runnable = Runnable {
        isFinished = true
        if (userPref.isLoggedIn) {
            findNavController().navigate(R.id.action_fragmentSplash_to_dashboardFragment)
        } else {
            findNavController().navigate(R.id.action_fragmentSplash_to_loginFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        Log.d(TAG, "onCreateView")
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
        startRunner()
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
        try {
            if (!isFinished) {
                splashHandler.removeCallbacks(splashRunnable)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun startRunner() {
        Log.d(TAG, "startRunner")
        splashHandler = Handler(Looper.getMainLooper())
        splashHandler.postDelayed(splashRunnable, SPLASH_TIME_OUT)
    }


}