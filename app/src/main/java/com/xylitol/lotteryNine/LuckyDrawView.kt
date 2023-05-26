package com.xylitol.lotteryNine

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * @Author xylitol
 * @Date 2020-11-26 15:47
 * @Desc 继承RecyclerView完成一个3X3的抽奖转盘
 */
class LuckyDrawView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    private val animator = ValueAnimator()
    private var mLuckyIndex = 4//最终抽中的位置
    private var lotteryStatus = 0//当前可以抽奖状态
    private var oldIndex = 0
    private var mLotteryListener: LotteryListener? = null

    private var mAdapter: LuckyDrawAdapter? = null

    init {
        animator.duration = 4000
        animator.setIntValues(0, 4 * 8 + mLuckyIndex)
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.addUpdateListener {
            val position = it.animatedValue as Int
            setCurrentPosition(position % 8)
        }
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                setCurrentPosition(mLuckyIndex)
                lotteryStatus = 0
                mLotteryListener?.onEnd()
            }
        })

        layoutManager = object : GridLayoutManager(context, 3) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
    }

    /**
     * 传入后台数据
     * @param data 后台数据、传入到adapter中去
     */
    fun setAdapterAndListener(data: Any,lotteryListener: LotteryListener) {
        mLotteryListener = lotteryListener
        mAdapter = LuckyDrawAdapter(mLotteryListener!!/*,data*/)
        this.adapter = mAdapter
    }

    /**
     * 开始抽奖
     * @param luckyIndex 最后中奖位置
     */
    fun startLottery(luckyIndex: Int){
        if (lotteryStatus == 0) {
            if (luckyIndex in 0..7) {
                mLuckyIndex = luckyIndex
                animator.setIntValues(0, 4 * 8 + mLuckyIndex)
            }
            animator.start()
            lotteryStatus = 1
        }
    }


    /**
     * 恢复到未抽奖状态
     */
    fun resetLottery() {
        mAdapter?.resetAll()
    }

    private fun setCurrentPosition(position: Int) {
        //刷新当前所在位置
        mAdapter?.setSelectionPosition(position)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animator.cancel()
    }

    interface LotteryListener {
        fun onClickLottery()
        fun onEnd()
    }

}