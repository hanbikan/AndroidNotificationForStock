package com.example.youshouldcheckthis

 import android.animation.ValueAnimator
 import android.content.Context
 import android.graphics.Color
 import android.text.Layout
 import android.util.Log
 import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
 import android.view.ViewParent
 import android.view.animation.Animation
 import android.view.animation.RotateAnimation
 import android.widget.BaseAdapter
 import android.widget.CheckBox
 import android.widget.LinearLayout
 import android.widget.TextView
 import androidx.core.content.ContextCompat
 import com.google.android.material.floatingactionbutton.FloatingActionButton

class ListViewAdapter : BaseAdapter(){
    private var listViewItemList = ArrayList<ListViewItem>()
    public var isRemoveMode = false
    private lateinit var viewGroupParent:ViewGroup
    public lateinit var rootView:View

    override fun getCount(): Int{
        return listViewItemList.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val context = parent.context
        this.viewGroupParent = parent

        if (view==null){
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.listview_item, parent, false)
        }

        val stockNameTextView = view!!.findViewById<TextView>(R.id.text_stock_name)
        val stockPriceTextView = view.findViewById<TextView>(R.id.text_stock_price)
        val stockRateTextView = view.findViewById<TextView>(R.id.text_stock_rate)

        val listViewItem = listViewItemList[position]

        //각 값들 대입, 형식 변환, 색상 결정
        val stocksetting: StockSetting = StockSetting()
        stockNameTextView.text = listViewItem.stockNameStr
        stockPriceTextView.text = stocksetting.convertStockPrice(listViewItem.stockPriceStr)
        stockRateTextView.text = stocksetting.convertStockRate(listViewItem.stockRateStr)
        if(listViewItem.stockRateStr?.toDouble()!!>=0){
            stockRateTextView.setTextColor(Color.parseColor("#FF0000"))
        }else{
            stockRateTextView.setTextColor(Color.parseColor("#0000FF"))
        }

        //LongClick -> edit mode
        val listviewItem = view.findViewById<LinearLayout>(R.id.listview_item)
        listviewItem.setOnLongClickListener(View.OnLongClickListener{
            this.setCheckBoxVisible()
            this.isRemoveMode = true
            val checkbox = view.findViewById<CheckBox>(R.id.checkbox)
            checkbox.isChecked = true
            true
        })

        //Item click(not only checkbox) -> checked
        listviewItem.setOnClickListener(View.OnClickListener{
            val checkbox = view.findViewById<CheckBox>(R.id.checkbox)
            if(checkbox.visibility==View.VISIBLE){
                checkbox.isChecked = checkbox.isChecked==false
            }
            true
        })
        return view
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItem(position: Int): ListViewItem {
        return listViewItemList[position]
    }

    fun addItem(stockName: String, stockPrice: String, stockRate: String){
        val item = ListViewItem()

        item.stockNameStr = stockName
        item.stockPriceStr = stockPrice
        item.stockRateStr = stockRate

        listViewItemList.add(item)
    }

    fun removeItem(index:Int){
        listViewItemList.removeAt(index)
    }

    fun setCheckBoxVisible(){
        var i:Int= 0
        for(i in 0 until this.count){
            val curView:View = this.viewGroupParent.getChildAt(i)
            val checkboxLayout = curView.findViewById<LinearLayout>(R.id.checkbox_layout)
            checkboxLayout.visibility = View.VISIBLE
        }

        var fabRemove = this.rootView.findViewById<FloatingActionButton>(R.id.fab_remove)
        fabRemove.visibility = View.VISIBLE

        var fabAdd = this.rootView.findViewById<FloatingActionButton>(R.id.fab_add)
        val animationRotate45Degree: RotateAnimation = RotateAnimation(
                0f,
                45f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        )
        animationRotate45Degree.duration = 250
        animationRotate45Degree.fillAfter = true
        fabAdd.startAnimation(animationRotate45Degree)
    }

    fun setCheckBoxInvisible(){
        var i:Int= 0
        for(i in 0 until this.count){
            val curView:View = this.viewGroupParent.getChildAt(i)
            val checkboxLayout = curView.findViewById<LinearLayout>(R.id.checkbox_layout)
            val checkbox = curView.findViewById<CheckBox>(R.id.checkbox)
            checkboxLayout.visibility = View.INVISIBLE
            checkbox.isChecked = false
        }
        var fabRemove = this.rootView.findViewById<FloatingActionButton>(R.id.fab_remove)
        fabRemove.visibility = View.INVISIBLE
        var fabAdd = this.rootView.findViewById<FloatingActionButton>(R.id.fab_add)
        val animationRotate45Degree: RotateAnimation = RotateAnimation(
                45f,
                0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        )
        animationRotate45Degree.duration = 250
        animationRotate45Degree.fillAfter = true
        fabAdd.startAnimation(animationRotate45Degree)
    }
}
