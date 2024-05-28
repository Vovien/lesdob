package com.jmbon.middleware.utils

import android.content.Context
import android.os.Build
import android.text.InputType
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method





/**
 * 静止输入框弹出系统输入法
 */
fun Window.limitSoftInputMethod(ed: EditText) {
    // 禁止输入法
    this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    val currentVersion = Build.VERSION.SDK_INT
    var methodName: String? = null
    if (currentVersion >= 16) {
        // 4.2
        methodName = "setShowSoftInputOnFocus"
    } else if (currentVersion >= 14) {
        // 4.0
        methodName = "setSoftInputShownOnFocus"
    }
    if (methodName == null) {
        ed.inputType = InputType.TYPE_NULL
    } else {
        val cls = EditText::class.java
        val setShowSoftInputOnFocus: Method
        try {
            setShowSoftInputOnFocus =
                cls.getMethod(methodName, Boolean::class.javaPrimitiveType)
            setShowSoftInputOnFocus.isAccessible = true
            setShowSoftInputOnFocus.invoke(ed, false)
        } catch (e: NoSuchMethodException) {
            ed.inputType = InputType.TYPE_NULL
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }


        // 如果软键盘已经显示，则隐藏
        val imm: InputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(ed.windowToken, 0)
    }
// 如果软键盘已经显示，则隐藏
    // 如果软键盘已经显示，则隐藏
    val imm =
        ed.context.applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(ed.windowToken, 0)
    // 禁止复制粘贴
    ed.customSelectionActionModeCallback = object : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            return false
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            return false
        }

        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
            return false
        }

        override fun onDestroyActionMode(mode: ActionMode?) {
        }

    }
}
