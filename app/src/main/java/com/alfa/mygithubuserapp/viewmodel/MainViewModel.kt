package com.alfa.mygithubuserapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alfa.mygithubuserapp.config.ApiConfig
import com.alfa.mygithubuserapp.config.Event
import com.alfa.mygithubuserapp.response.ItemsSearch
import com.alfa.mygithubuserapp.response.ResponseSearch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val _userList = MutableLiveData<List<ItemsSearch>>()
    val userList: LiveData<List<ItemsSearch>> = _userList

    private val _userCount = MutableLiveData<Int?>()
    val userCount: LiveData<Int?> = _userCount

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _toastText = MutableLiveData<Event<String>>()
    val toastText: LiveData<Event<String>> = _toastText

    fun fetchInitialData() {
        findUser("alfa") // Memuat data pertama kali dengan query kosong
    }

    fun findUser(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUsers(username)
        client.enqueue(object : Callback<ResponseSearch> {
            override fun onResponse(
                call: Call<ResponseSearch>,
                response: Response<ResponseSearch>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _userList.value = response.body()?.items as List<ItemsSearch>
                    _userCount.value = response.body()?.totalCount
                    _toastText.value = Event("Success")
                } else {
                    _toastText.value = Event("Tidak ada data yang ditampilkan!")
                }
            }

            override fun onFailure(call: Call<ResponseSearch>, t: Throwable) {
                _isLoading.value = false
                _toastText.value = Event("onFailure: ${t.message.toString()}")
            }
        })
    }

    companion object{
        private const val TAG = "MainViewModel"
    }
}