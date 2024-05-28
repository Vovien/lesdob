package androidx.lifecycle

import android.annotation.SuppressLint
import com.apkdv.mvvmfast.ktx.HttpLifeScope

/**
 * User: ljx
 * Date: 2020-03-14
 * Time: 10:30
 */

private const val JOB_KEY = "androidx.lifecycle.ViewModelRxLifeScope.JOB_KEY"

val ViewModel.lifeScope: HttpLifeScope
    get() {
        val scope: HttpLifeScope? = this.getTag(JOB_KEY)
        if (scope != null) {
            return scope
        }
        return setTagIfAbsent(JOB_KEY, HttpLifeScope())
    }

val LifecycleOwner.lifeScope: HttpLifeScope
    get() = lifecycle.lifeScope

val Lifecycle.lifeScope: HttpLifeScope
    @SuppressLint("RestrictedApi")
    get() {
        while (true) {
            val existing = mInternalScopeRef.get() as HttpLifeScope?
            if (existing != null) {
                return existing
            }
            val newScope = HttpLifeScope(this)
            if (mInternalScopeRef.compareAndSet(null, newScope)) {
                return newScope
            }
        }
    }