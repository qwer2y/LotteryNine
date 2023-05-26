package com.xylitol.lotteryNine

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    companion object {
        var iconArray = arrayOf(
            R.mipmap.ac0,
            R.mipmap.ac1,
            R.mipmap.ac2,
            R.mipmap.ac3,
            R.mipmap.ac4,
            R.mipmap.ac5,
            R.mipmap.ac6,
            R.mipmap.ac7
        )
    }

    private var mLuckyDrawView: LuckyDrawView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mLuckyDrawView = findViewById(R.id.ldv_top)


        //data 传入后台给的抽奖数据
        mLuckyDrawView?.setAdapterAndListener("", object : LuckyDrawView.LotteryListener {
            override fun onClickLottery() {
                //点击开始抽奖调用抽奖接口后开始抽奖动画
                val result = Random.nextInt(0, 7) //模拟抽奖接口
                mLuckyDrawView?.startLottery(result)
            }

            override fun onEnd() {
                //结束后恢复原样
                Handler(Looper.getMainLooper()).postDelayed({
                    mLuckyDrawView?.resetLottery()
                }, 2000L)
            }
        })
    }
}