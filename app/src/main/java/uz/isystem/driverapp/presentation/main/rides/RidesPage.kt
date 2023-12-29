package uz.isystem.driverapp.presentation.main.rides


import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import uz.isystem.driverapp.R
import uz.isystem.driverapp.databinding.PageRidesBinding
import uz.isystem.driverapp.presentation.base.BaseFragment

class RidesPage : BaseFragment(R.layout.page_rides){
    private val binding by viewBinding(PageRidesBinding::bind)
    override fun onCreate(view: View, savedInstanceState: Bundle?) {

    }
}
