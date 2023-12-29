package uz.isystem.driverapp.data.cache

import android.content.Context
import android.content.SharedPreferences

class AppCache private constructor(context : Context){
    private val keyIsSigned = "KEY_IS_SIGNED"
    private val keyEmail = "KEY_EMAIL"
    private val keyPassword = "KEY_PASSWORD"
    init {
        sharedPreferences = context.getSharedPreferences("cache",Context.MODE_PRIVATE)
    }
    companion object {
        private var appCache: AppCache? = null
        private var sharedPreferences : SharedPreferences? = null

        fun  init(context: Context){
            if (appCache == null){
                appCache= AppCache(context)
            }
        }

        fun getObject():AppCache{
            return appCache!!
        }
    }

    fun saveEmail(email:String){
        sharedPreferences!!.edit().putString(keyEmail,email).apply()
    }
    fun savePassword(password:String){
        sharedPreferences!!.edit().putString(keyPassword,password).apply()
    }
    fun isSigned(){
        sharedPreferences!!.edit().putBoolean(keyIsSigned,true).apply()
    }
    fun isNotSigned(){
        sharedPreferences!!.edit().putBoolean(keyIsSigned,false).apply()
    }
    fun getIsSigned():Boolean{
        return sharedPreferences!!.getBoolean(keyIsSigned,false)
    }
    fun getEmail():String{
        return sharedPreferences!!.getString(keyEmail,"no")!!
    }
    fun getPassword():String{
        return sharedPreferences!!.getString(keyPassword,"no")!!
    }
}