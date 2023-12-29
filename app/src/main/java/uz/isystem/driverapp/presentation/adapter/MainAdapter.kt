package uz.isystem.driverapp.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import uz.isystem.driverapp.presentation.main.home.HomePage
import uz.isystem.driverapp.presentation.main.profile.ProfilePage
import uz.isystem.driverapp.presentation.main.rides.RidesPage

class MainAdapter(fm: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fm, lifecycle) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomePage()
            1-> RidesPage()
            else-> ProfilePage()
        }
    }
}