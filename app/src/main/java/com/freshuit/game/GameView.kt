package com.freshuit.game


import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Paint
import android.media.MediaPlayer
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import java.lang.Exception
import java.util.*
import kotlin.math.abs
import kotlin.math.max

class GameView(ctx: Context, attributeSet: AttributeSet): SurfaceView(ctx,attributeSet) {


    var bg = BitmapFactory.decodeResource(ctx.resources,R.drawable.bg)

    public var score = 0
    private val random = Random()
    var millis = 0
    private var listener: EndListener? = null
    private var paintB: Paint = Paint(Paint.DITHER_FLAG)

    private var bx = 0f
    private var by = 0f
    var sounds = ctx.getSharedPreferences("prefs",Context.MODE_PRIVATE).getInt("music",0)
    var pl = MediaPlayer.create(ctx,R.raw.slot)
    val bg1 = MediaPlayer.create(ctx,R.raw.bg)
    var basket = BitmapFactory.decodeResource(ctx.resources,R.drawable.basket)

    var avias = mutableListOf<Bitmap>(
        BitmapFactory.decodeResource(ctx.resources,R.drawable.c1),
        BitmapFactory.decodeResource(ctx.resources,R.drawable.c2),
        BitmapFactory.decodeResource(ctx.resources,R.drawable.c3),
        BitmapFactory.decodeResource(ctx.resources,R.drawable.c4),
        BitmapFactory.decodeResource(ctx.resources,R.drawable.c5),
        BitmapFactory.decodeResource(ctx.resources,R.drawable.c6),
    )

    init {
        basket = Bitmap.createScaledBitmap(basket,basket.width/4,basket.height/4,true)
        for(i in avias.indices) avias[i] = Bitmap.createScaledBitmap(avias[i],avias[i].width/4,avias[i].height/4,true)
        holder.addCallback(object : SurfaceHolder.Callback{
            override fun surfaceCreated(holder: SurfaceHolder) {

            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
                val canvas = holder.lockCanvas()
                if(canvas!=null) {
                    bx = canvas.width/2f-avias[0].width/2f
                    by = canvas.height-basket.height-30f
                    bg = Bitmap.createScaledBitmap(bg,canvas.width,canvas.height,true)
                    draw(canvas)

                    holder.unlockCanvasAndPost(canvas)
                }
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                }

        })
        val updateThread = Thread {
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    if (!paused) {
                        update.run()
                        millis ++
                    }
                }
            }, 0, 20)
        }

        updateThread.start()
    }

    var ind = 0
    var paused = true
    val list = mutableListOf<Model>()
    var code = -1f

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event!!.action) {
            MotionEvent.ACTION_DOWN  -> {
                code = event.x
            }
            MotionEvent.ACTION_UP -> {
                code = -1f
            }
        }
        return true
    }

    val update = Runnable{
        var sc = false
        var isEnd = false
        try {
            val canvas = holder.lockCanvas()
            if(code!=-1f) {
                if(bx>=code || bx+avias[ind].width<=code) bx += (if(code>bx) 1 else -1)*15
            }
            while(list.size<5) list.add(Model(random.nextInt(canvas.width).toFloat(),-1*random.nextInt(500).toFloat(),
            random.nextInt(avias.size)))
            canvas.drawBitmap(bg,0f,0f,paintB)
            canvas.drawBitmap(basket,bx,by,paintB)
            var j = 0
            while(j<list.size) {
                val i = list[j]
                i.y+=3
                if(i.y>=canvas.height+avias[ind].height) {
                    list.removeAt(j)
                } else if((bx<=i.x && bx+avias[ind].width>=i.x || i.x<=bx && i.x+avias[i.c].width>=bx)
                            && (i.y<=by && i.y+avias[ind].height>=by || by<=i.y && by+avias[ind].height>=i.y)) {
                    if(sounds>1) {
                        pl.seekTo(0)
                        pl.start()
                    }
                    sc = true
                    score += 20
                    list.removeAt(j)
                } else {
                    canvas.drawBitmap(avias[i.c], i.x, i.y, paintB)
                    j++
                }
            }
            holder.unlockCanvasAndPost(canvas)
            if(millis/50>=30) isEnd = true
            listener!!.score()
           if(isEnd) {
                Log.d("TAG","END")
                togglePause()
                if(listener!=null) listener!!.end()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setEndListener(list: EndListener) {
        this.listener = list
    }
    fun togglePause() {
        paused = !paused
    }
    companion object {
        interface EndListener {
            fun end();
            fun score();
        }
        data class Model(var x:Float, var y:Float,var c: Int)
    }

}