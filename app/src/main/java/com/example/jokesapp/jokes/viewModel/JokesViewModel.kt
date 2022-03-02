package com.example.jokesapp.jokes.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jokesapp.jokes.model.JokesResponseModel
import com.example.jokesapp.jokes.services.JokesRepository
import com.example.jokesapp.network.TaskCallback
import com.example.jokesapp.utils.Event
import com.example.jokesapp.utils.SingleLiveEvent
import okhttp3.Response

class JokesViewModel : ViewModel() {
    private val jokesRepository by lazy {
        JokesRepository()
    }

    private val _dataLoading = MutableLiveData<Event<Boolean>>()
    val dataLoading: LiveData<Event<Boolean>>
        get() = _dataLoading

    private val _jokesList = MutableLiveData<ArrayList<JokesResponseModel.Result>>().apply { value = arrayListOf () }
    val jokesList: MutableLiveData<ArrayList<JokesResponseModel.Result>>
        get() = _jokesList

    internal val toastMessage = SingleLiveEvent<String>()

    fun getJokeList(context: Context, searchedJokesName : String) {
        _dataLoading.value = Event(true)
        jokesRepository.getJokesList(context, 1, 30, searchedJokesName, object : TaskCallback<JokesResponseModel> {
            override fun onComplete(result: JokesResponseModel) {
                _dataLoading.value = Event(false)
                if(!result.jokesList.isNullOrEmpty()){
                    _jokesList.value = result.jokesList
                } else {
                    toastMessage.value = "Sorry, No Data Available"
                }
            }

            override fun onError(errorResponse: Response, responseCode: Int) {
                _dataLoading.value = Event(false)
                toastMessage.value = errorResponse.message()
            }

            override fun onException(t: Throwable?) {
                _dataLoading.value = Event(false)
                toastMessage.value = t?.localizedMessage
            }
        })
    }

    override fun onCleared() {
        super.onCleared()
        jokesRepository.completableJob.cancel()
    }
}