package com.apkdv.mvvmfast.ktx

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope


fun Fragment.launch(block: suspend CoroutineScope.() -> Unit) {
    viewLifecycleOwner.lifecycleScope.launchWhenCreated { block() }
}

fun Fragment.started(block: suspend CoroutineScope.() -> Unit) {
    viewLifecycleOwner.lifecycleScope.launchWhenStarted { block() }
}

fun Fragment.resumed(block: suspend CoroutineScope.() -> Unit) {
    viewLifecycleOwner.lifecycleScope.launchWhenResumed { block() }
}


fun FragmentActivity.launch(block: suspend CoroutineScope.() -> Unit) {
    lifecycleScope.launchWhenCreated { block() }
}

fun FragmentActivity.started(block: suspend CoroutineScope.() -> Unit) {
    lifecycleScope.launchWhenStarted { block() }
}

fun FragmentActivity.resumed(block: suspend CoroutineScope.() -> Unit) {
    lifecycleScope.launchWhenResumed { block() }
}