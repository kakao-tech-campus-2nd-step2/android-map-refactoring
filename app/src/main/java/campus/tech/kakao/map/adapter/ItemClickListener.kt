package campus.tech.kakao.map.adapter

import android.view.View
import campus.tech.kakao.map.Room.KakaoMapItem
import campus.tech.kakao.map.Room.MapItem
import campus.tech.kakao.map.SelectMapItem

interface ItemClickListener {
    fun onClick(v: View, selectItem: KakaoMapItem)
}

interface SelectItemClickListener {
    fun onClick(v: View, selectItem: MapItem)
}