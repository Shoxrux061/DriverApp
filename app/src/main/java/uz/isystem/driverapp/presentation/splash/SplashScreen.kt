package uz.isystem.driverapp.presentation.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.firebase.auth.FirebaseAuth
import uz.isystem.driverapp.R
import uz.isystem.driverapp.data.cache.AppCache
import uz.isystem.driverapp.databinding.ScreenSplashBinding
import uz.isystem.driverapp.presentation.base.BaseFragment

@SuppressLint("CustomSplashScreen")
class SplashScreen : BaseFragment(R.layout.screen_splash){
    private val binding by viewBinding(ScreenSplashBinding::bind)
    private val cache = AppCache.getObject()
    private val password = cache.getPassword()
    private val email = cache.getEmail()
    private lateinit var auth : FirebaseAuth
    override fun onCreate(view: View, savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()
        if (cache.getIsSigned() && email.isNotEmpty() && email.isNotBlank() && password.isNotBlank() && password.isNotEmpty()){
            auth.signInWithEmailAndPassword(email,password)
            findNavController().navigate(SplashScreenDirections.actionSplashScreenToMainScreen(false))
        }else{
            findNavController().navigate(SplashScreenDirections.actionSplashScreenToRegisterScreen())
        }
    }
}
