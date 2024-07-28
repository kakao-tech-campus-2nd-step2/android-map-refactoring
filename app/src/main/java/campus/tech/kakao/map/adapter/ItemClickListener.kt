package campus.tech.kakao.map.adapter

import android.view.View
import campus.tech.kakao.map.Room.MapItem

interface ItemClickListener {
    fun onClick(v: View, selectItem: MapItem)
}