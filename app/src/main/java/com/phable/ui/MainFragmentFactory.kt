package com.phable.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class MainFragmentFactory
@Inject
constructor(): FragmentFactory(){

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {

            TaskFragment::class.java.name -> {
                val fragment = TaskFragment()
                fragment
            }

            else -> super.instantiate(classLoader, className)
        }
    }
}