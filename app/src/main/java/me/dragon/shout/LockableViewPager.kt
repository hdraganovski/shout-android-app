package me.dragon.shout

import android.content.Context
import android.util.AttributeSet
import androidx.viewpager.widget.ViewPager
import android.view.MotionEvent



class LockableViewPager: ViewPager {
    constructor(context: Context): super(context)
    constructor(context: Context, attr: AttributeSet): super(context, attr)

    var isLocked = true

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        // Allow swiping if isnt locked
        return ! isLocked
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        // Allow swiping if isnt locked
        return ! isLocked
    }
}