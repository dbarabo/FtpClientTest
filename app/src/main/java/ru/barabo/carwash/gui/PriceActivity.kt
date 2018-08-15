package ru.barabo.carwash.gui

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import ru.barabo.carwash.R
import ru.barabo.ftpclient.FtpObject

class PriceActivity : AppCompatActivity() {

    private var priceRecyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE)

        setContentView(R.layout.activity_price)

        priceRecyclerView = findViewById(R.id.priceRecyclerView)

        priceRecyclerView?.layoutManager = LinearLayoutManager(this)

        priceRecyclerView?.adapter = FtpAdapter()

        window.setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.main_title)
    }

    private inner class FtpHolder(inflater: LayoutInflater, parent: ViewGroup) :
            RecyclerView.ViewHolder(inflater.inflate(R.layout.ftp_item_list, parent, false)) {

        private var priceName: TextView = itemView.findViewById(R.id.priceName)

        private var priceAmount: TextView = itemView.findViewById(R.id.priceAmount)

        private var spaceAmount = itemView.findViewById<View>(R.id.spaceAmount)

        fun bindData(itemData: PriceItem) {

            priceName.text = itemData.name

            priceAmount.text = itemData.amount

//            if(priceName.lineCount > 1) {
//                priceAmount.text = "${priceAmount.text}\n."
//            }

            val backGroundColorNow = ContextCompat.getColor(this@PriceActivity,
                    if(itemData.isDec) R.color.colorPrice1BackGround
                                            else R.color.colorPrice0BackGround )

            priceAmount.layoutParams.height = priceName.layoutParams.height

            priceName.setBackgroundColor(backGroundColorNow)

            priceAmount.layoutParams.height = priceName.layoutParams.height

            priceAmount.setBackgroundColor(backGroundColorNow)

            spaceAmount.setBackgroundColor(backGroundColorNow)
        }
    }

    private inner class FtpAdapter : RecyclerView.Adapter<FtpHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FtpHolder {

            val inFlater = LayoutInflater.from(parent.context)

            return FtpHolder(inFlater, parent)
        }

        override fun getItemCount(): Int   {
           val count = FtpObject.getPriceCount()

          // Log.d("PriceActivity", "FtpObject.getPriceCount=$count")
           return count
        }

        override fun onBindViewHolder(holder: FtpHolder, position: Int) {

            val itemData = FtpObject.getPriceItem(position)

           // Log.d("PriceActivity", "position=$position itemData=$itemData")

            val priceItem = PriceItem(itemData, position)

            holder.bindData(priceItem)
        }
    }

    data class PriceItem(val name: String="", val amount: String="", val isDec: Boolean = true) {
        constructor(items: List<String>?, position: Int) : this(
                if(items?.size?:0 > 0)items!![0] else "",
                if(items?.size?:0 > 1)items!![1] else "",
                position%2==0
        )
    }

}
