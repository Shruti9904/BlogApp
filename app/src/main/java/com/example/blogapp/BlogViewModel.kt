import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blogapp.data.BlogDao
import com.example.blogapp.data.BlogEntity
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.lang.Exception

class BlogViewModel(private val blogDao: BlogDao):ViewModel() {
    private val _blogs = mutableStateOf<List<BlogPost>>(emptyList())
    val blogs : State<List<BlogPost>> = _blogs

    private val _loading = mutableStateOf(false)
    val loading : State<Boolean> =_loading

    private var currentPage=1
    private var isLastPage = false

    init {
        loadBlogPosts()
        loadCachePosts()
    }

    private fun loadCachePosts(){
        viewModelScope.launch {
            _blogs.value = blogDao.getAllBlogs().map {
                it.toBlogPost()
            }
        }
    }

    private fun BlogEntity.toBlogPost():BlogPost{
        return BlogPost(
            id = this.id,
            title = Title(rendered = this.title),
            content = Content(rendered = this.content),
            date =this.date,
            jetpack_featured_media_url = this.imageUrl
        )
    }

    private fun BlogPost.toBlogEntity():BlogEntity{
        return BlogEntity(
            id = this.id,
            title = this.title.rendered,
            content = this.content.rendered,
            date = this.date,
            imageUrl = this.jetpack_featured_media_url
        )
    }

    fun loadBlogPosts() {
        if(isLastPage){
            return
        }
        _loading.value=true
        viewModelScope.launch {
            try {
                val response = apiService.getBlogPosts(perPage = 10,page=currentPage)
                if(response.isNotEmpty()){
                    blogDao.insertBlogs(response.map{
                        it.toBlogEntity()
                    })
                    _blogs.value = blogDao.getAllBlogs().map {
                        it.toBlogPost()
                    }
                    currentPage++
                }else{
                    isLastPage=true
                }
                Log.i("CurrentPage",currentPage.toString())
            }
            catch (e:Exception){
                Log.i("Error","$e occurred while fetching blogs")
            }
            finally {
                _loading.value=false
            }
        }
    }
}
