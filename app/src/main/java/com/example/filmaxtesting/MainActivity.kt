package com.example.filmaxtesting

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.filmaxtesting.databinding.ActivityMainBinding
import com.example.filmaxtesting.fragments.BookMarksFragment
import com.example.filmaxtesting.fragments.movie.MoviesHostFragment
import com.example.filmaxtesting.fragments.show.TvShowsHostFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val moviesFragment= MoviesHostFragment()
        val showsFragment= TvShowsHostFragment()
        val bookMarksFragment= BookMarksFragment()
        val searchFragment=SearchFragment()

        setCurrentFragment(moviesFragment)

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.menuMovies -> setCurrentFragment(moviesFragment)
                R.id.menuShows -> setCurrentFragment(showsFragment)
                R.id.menuBookMarks -> setCurrentFragment(bookMarksFragment)
                R.id.menuSearch -> setCurrentFragment(searchFragment)
            }
            true
        }

    }

    private fun setCurrentFragment(fragment: Fragment) =
        this.supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,fragment)
            commit()
        }
}