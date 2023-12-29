package uz.isystem.driverapp.presentation.main.home

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import by.kirich1409.viewbindingdelegate.viewBinding
import uz.isystem.driverapp.R
import uz.isystem.driverapp.databinding.PageHomeBinding
import uz.isystem.driverapp.presentation.base.BaseFragment

class HomePage : BaseFragment(R.layout.page_home){
    private val binding by viewBinding(PageHomeBinding::bind)
    override fun onCreate(view: View, savedInstanceState: Bundle?) {
    }
}
