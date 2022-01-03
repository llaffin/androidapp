package finalproject.stn991389277.facebooktwoproject.ui.myPosts
//OWNER: LUC LAFFIN 991389277
//THIS ACTIVITY IS WHERE THE USER CAN ADD POSTS, VIEW THEIR POSTS, AND DELETE THEIR POSTS

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PostsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }
    val text: LiveData<String> = _text
}