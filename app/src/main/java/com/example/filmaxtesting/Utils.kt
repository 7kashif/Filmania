package com.example.filmaxtesting

import android.app.Activity
import android.app.Dialog
import android.view.WindowManager
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.filmaxtesting.dataClasses.detail.ItemDetails
import com.example.filmaxtesting.roomDatabase.BookMark
import com.example.filmaxtesting.viewModel.sharedViewModel.SharedViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object Constants {

    const val BASE_URL="https://api.themoviedb.org"
    const val END_POPULAR_MOVIES_URL="/3/movie/popular"
    const val END_TOP_RATED_MOVIES_URL="/3/movie/top_rated"
    const val END_POPULAR_SHOWS_URL="/3/tv/popular"
    const val END_TOP_RATED_SHOWS_URL="/3/tv/top_rated"
    const val END_SEARCH_MOVIES_URL="/3/search/movie"
    const val API_KEY="f5202ed4328c007861005ae7fbc2ad86"
    const val BASE_IMAGE_PATH="https://image.tmdb.org/t/p/w500/"

}
//
//fun RecyclerView.getCurrentPosition(): Int {
//    return (this.layoutManager as GridLayoutManager).findFirstVisibleItemPosition()
//}

object ViewDialog {
    fun showDetailDialog(activity: Activity?, item: ItemDetails, sharedViewModel: SharedViewModel) {

        val dialog= Dialog(activity!!,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen)
        dialog.setContentView(R.layout.detail_view)
        dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT)

        val dialogRemoveButton = dialog.findViewById<ImageButton>(R.id.closeButton)
        val dialogPoster=dialog.findViewById<ImageView>(R.id.dialogPoster)
        val dialogTitle=dialog.findViewById<TextView>(R.id.dialogTitle)
        val dialogLanguage=dialog.findViewById<TextView>(R.id.dialogLanguage)
        val dialogReleaseDate=dialog.findViewById<TextView>(R.id.dialogReleaseDate)
        val dialogOverView=dialog.findViewById<TextView>(R.id.dialogOverView)
        val bookMarkCb=dialog.findViewById<CheckBox>(R.id.bookMarkCB)
        val dialogRating=dialog.findViewById<TextView>(R.id.dialogRating)

        dialogRemoveButton.setOnClickListener {
            dialog.dismiss()
        }

        var itemFound=false
        val list=sharedViewModel.allBookMarks
        if(list.value!=null) {
           for(i in list.value!!) {
               if(i.title==item.title) {
                   itemFound = true
                   break
               }
           }
        }

        bookMarkCb.isChecked=itemFound

        loadImage(activity,item.posterPath,dialogPoster)

        dialogTitle.text= item.title
        dialogLanguage.text= item.language
        dialogReleaseDate.text= item.releaseDate
        dialogOverView.text= item.overView
        dialogRating.text="${item.voteAverage}/10"


        bookMarkCb.setOnCheckedChangeListener{view,bool->
            if(view.isPressed) {
                if(bool){
                    val bookMark=BookMark(
                        title=item.title,
                        voteAverage=item.voteAverage,
                        language=item.language,
                        releaseDate=item.releaseDate,
                        overView=item.overView,
                        posterPath=item.posterPath,
                        backDropPath = item.backDropPath
                    )
                    sharedViewModel.createBookMark(bookMark)
                }
                else
                    sharedViewModel.getBookMarkWithTitle(item.title)
            }
        }


        dialog.show()
    }
}

fun loadImage(activity: Activity,posterPath:String,imageView:ImageView ) {
    Glide.with(activity).load(Constants.BASE_IMAGE_PATH + posterPath).into(imageView)
}

fun androidx.appcompat.widget.SearchView.getQueryTextChangeStateFlow() : StateFlow<String> {
    val query= MutableStateFlow("")
    setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
        override fun onQueryTextSubmit(query: String?): Boolean =true

        override fun onQueryTextChange(newText: String?): Boolean {
            if (newText != null) {
                query.value=newText
            }
            return true
        }
    })
    return query
}
