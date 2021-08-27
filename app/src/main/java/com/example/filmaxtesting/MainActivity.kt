package com.example.filmaxtesting

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.filmaxtesting.databinding.ActivityMainBinding
import com.example.filmaxtesting.fragments.BookMarksFragment
import com.example.filmaxtesting.fragments.movie.MoviesHostFragment
import com.example.filmaxtesting.fragments.people.PopularPeopleFragment
import com.example.filmaxtesting.fragments.show.TvShowsHostFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}