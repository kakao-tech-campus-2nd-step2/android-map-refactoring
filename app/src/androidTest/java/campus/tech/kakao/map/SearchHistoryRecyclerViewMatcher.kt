package campus.tech.kakao.map

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher

class SearchHistoryRecyclerViewMatcher {
    // 특정 위치의 리사이클러뷰 아이템의 매처를 정의합니다.
    fun atPosition(position: Int, itemMatcher: Matcher<View>): Matcher<View> {
        return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
            override fun matchesSafely(recyclerView: RecyclerView): Boolean {
                val viewHolder = recyclerView.findViewHolderForAdapterPosition(position)
                return itemMatcher.matches(viewHolder?.itemView)
            }

            override fun describeTo(description: Description) {
                description.appendText("has item at position $position: ")
                itemMatcher.describeTo(description)
            }
        }
    }

    // 특정 TextView의 텍스트를 확인합니다.
    fun hasTextInViewWithId(viewId: Int, text: String): Matcher<View> {
        return object : BoundedMatcher<View, View>(View::class.java) {
            override fun matchesSafely(view: View): Boolean {
                val textView = view.findViewById<TextView>(viewId)
                return textView?.text.toString() == text
            }

            override fun describeTo(description: Description) {
                description.appendText("has text '$text' in view with id $viewId")
            }
        }
    }
}
