package com.tubewiki.mine.adapter.city

import android.text.TextUtils
import androidx.recyclerview.widget.LinearLayoutManager
import com.apkdv.mvvmfast.ktx.BindingQuickAdapter
import com.jmbon.middleware.bean.CityList
import com.tubewiki.mine.databinding.ItemSelectCityListDefaultLayoutBinding

class SelectCityListAdapter :
    BindingQuickAdapter<CityList.ChinaCity, ItemSelectCityListDefaultLayoutBinding>() {

    private var mLayoutManager: LinearLayoutManager? = null

    fun setLayoutManager(manager: LinearLayoutManager) {
        this.mLayoutManager = manager
    }

    /**
     * 滚动RecyclerView到索引位置
     *
     * @param index
     */
    fun scrollToSection(index: String) {
        if (data.isEmpty()) return
        if (TextUtils.isEmpty(index)) return
        val size: Int = data.size
        for (i in 0 until size) {
            val firstLetter = index[0].uppercase()
            val dataLetter = data[i].getSection()[0].uppercase()
            if (firstLetter == dataLetter) {
                if (dataLetter == "A") {
                    mLayoutManager?.scrollToPosition(0)
                } else {
                    mLayoutManager?.scrollToPosition(i)
                }
                return
            }
        }
    }


    override fun convert(holder: BaseBindingHolder, item: CityList.ChinaCity) {
        holder.getViewBinding<ItemSelectCityListDefaultLayoutBinding>().apply {
            cpListItemName.text = item.title
        }
    }
}