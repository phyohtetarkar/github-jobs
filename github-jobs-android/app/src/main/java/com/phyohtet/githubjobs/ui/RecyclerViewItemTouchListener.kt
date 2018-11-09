package com.phyohtet.githubjobs.ui

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View

class RecyclerViewItemTouchListener(
        context: Context?,
        private val listener: OnTouchListener?
) : RecyclerView.OnItemTouchListener {

    private val detector: GestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
        override fun onLongPress(e: MotionEvent) {
        }

        override fun onSingleTapUp(e: MotionEvent): Boolean {
            return true
        }

    })

    interface OnTouchListener {

        fun onTouch(view: View, position: Int)

    }

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {

        val view = rv.findChildViewUnder(e.x, e.y)

        if (view != null && detector.onTouchEvent(e)) {
            listener?.onTouch(view, rv.getChildAdapterPosition(view))
        }

        return false
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}

}
