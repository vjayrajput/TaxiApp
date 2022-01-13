package com.taxi.app.ui.register

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.taxi.app.R
import com.taxi.app.data.remote.Status
import com.taxi.app.databinding.FragmentRegisterBinding
import com.taxi.app.utils.autoCleared
import com.taxi.app.utils.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

@AndroidEntryPoint
class RegisterFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    companion object {
        private val TAG: String = RegisterFragment::class.java.simpleName
        private const val REQUEST_LOCATION_PERMISSION = 123

        private val FINE_LOCATION =
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION
            )
    }

    private var binding: FragmentRegisterBinding by autoCleared()

    private val viewModel: RegisterViewModel by viewModels()

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var locationCallback: LocationCallback

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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated")
        setUpLocation()

        observerMessage()
        observerRegisterResponse()
        binding.btnRegister.setSafeOnClickListener {
            activity?.hideKeyboard()
            if (activity!!.isNetworkAvailable()) {
                if (viewModel.isValidData()) {
                    binding.loaderView.progressView.visible()
                    requestLocationPermission()
                }
            } else {
                activity?.toast(getString(R.string.internet_not_available))
            }
        }
        binding.tilName.editText?.doOnTextChanged { _, _, _, _ ->
            viewModel.errorName.value = null
        }
        binding.tilEmail.editText?.doOnTextChanged { _, _, _, _ ->
            viewModel.errorEmail.value = null
        }
        binding.tilPhoneNumber.editText?.doOnTextChanged { _, _, _, _ ->
            viewModel.errorPhoneNumber.value = null
        }
        binding.tilGender.editText?.doOnTextChanged { _, _, _, _ ->
            viewModel.errorGender.value = null
        }
        binding.tilPassword.editText?.doOnTextChanged { _, _, _, _ ->
            viewModel.errorPassword.value = null
        }
        setUpDropDown()
    }

    private fun setUpLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                for (location in locationResult.locations) {
                    Log.d(TAG, "Location : $location")
                    viewModel.latitude.value = location.latitude
                    viewModel.longitude.value = location.longitude
                }
                stopLocationUpdates()
                viewModel.userRegister()
            }
        }
    }

    private fun setUpDropDown() {
        val items = listOf("Male", "Female", "Other")
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
        binding.edtGender.setAdapter(adapter)
    }

    private fun observerMessage() {
        viewModel.message.observeEvent(this, {
            activity?.toast(it)
        })
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {

    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        }
    }

    private fun hasLocationPermissions(): Boolean {
        return EasyPermissions.hasPermissions(activity!!, *FINE_LOCATION)
    }

    @AfterPermissionGranted(REQUEST_LOCATION_PERMISSION)
    private fun requestLocationPermission() {
        if (hasLocationPermissions()) {
            createLocationRequest()
        } else {
            EasyPermissions.requestPermissions(
                this,
                "Our App Requires a permission to access your camera and storage",
                REQUEST_LOCATION_PERMISSION,
                *FINE_LOCATION
            )
        }
    }

    private val locationRequest = LocationRequest.create().apply {
        interval = 30
        fastestInterval = 10
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    private fun createLocationRequest() {
        Log.d(TAG, "createLocationRequest")
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

        val client: SettingsClient = LocationServices.getSettingsClient(activity!!)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            Log.d(TAG, "createLocationRequest addOnSuccessListener")
            startLocationUpdates()
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        Log.d(TAG, "startLocationUpdates")
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

}