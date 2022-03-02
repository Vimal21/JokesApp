package com.example.jokesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.jokesapp.databinding.ActivityMainBinding
import com.example.jokesapp.jokes.JokesFragment
import com.example.jokesapp.utils.setupActionBar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.setupActionBar(R.id.toolbar){
            title = "Jokes"
        }
        replaceFragmentInActivity(JokesFragment.newInstance(), R.id.fragment_container)
    }

    private fun replaceFragmentInActivity(fragment : Fragment, frameId: Int) {
        val tag = fragment.javaClass.name

        val fragmentByTag = supportFragmentManager.findFragmentByTag(tag)
        if(fragmentByTag != null && fragmentByTag.userVisibleHint){
            return
        }
        supportFragmentManager.beginTransaction().apply {
            replace(frameId, fragment)
        }.commit()
    }
}