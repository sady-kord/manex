package com.baman.manex.controls

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.viewpager.widget.ViewPager
import com.baman.manex.R
import kotlinx.android.synthetic.main.control_choicechipgroup.view.*
import kotlin.math.min


class ChoiceChipGroup : LinearLayoutCompat {

    companion object {
        private const val itemCountPerPage = 6
    }

    private lateinit var tags: List<String>
    private lateinit var listener: (String) -> Unit
    private var selectedItemPosition = -1
    private var itemIndexSelected = -1
    private val tagItems = mutableListOf<TextView>()
    private var firstItemSelected = false
    private lateinit var pager: WrapContentAbleViewPager
    private lateinit var adapter: FragmentStatePagerAdapter

    constructor(context: Context) : super(context) {
        initialize(context, null, 0)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initialize(context, attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initialize(context, attrs, defStyleAttr)
    }

    private fun initialize(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) {
        val inflater = LayoutInflater.from(context)
        var foo = inflater.inflate(R.layout.control_choicechipgroup, this, true)
        pager = foo.findViewById(R.id.pager_chips)
        orientation = VERTICAL
    }

    fun setup(
        fragmentManager: FragmentManager,
        tags: Set<String>,
        indexSelected: Int
    ) {
        this.itemIndexSelected = indexSelected
        this.tags = tags.toList()

        adapter = Adapter(fragmentManager)

        pager.adapter = adapter
        pager.currentItem = adapter.count - 1

        if (itemCountPerPage < tags.size) {
            indicator.visibility = View.VISIBLE
            indicator.count = adapter.count
            pager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
                override fun onPageSelected(position: Int) {
                    indicator.selection = position
                }
            })
        } else {
            indicator.visibility = GONE
        }

    }

    fun setOnTagSelectedListener(listener: (String) -> Unit) {
        this.listener = listener
    }

    fun clearSelectedItems() {
        tagItems.forEach { textView: TextView ->
            textView.isSelected = false
        }
    }

    fun setSelectedSecondTime(item: String) {
        tagItems.forEach { textView: TextView ->
            if (item == textView.text)
                textView.isSelected = true
        }
    }

    private fun getTagsForPosition(position: Int): List<String> {
        val fromIndex = itemCountPerPage * position
        return tags.subList(fromIndex, min(fromIndex + itemCountPerPage, tags.size))
    }

    private fun findPosition(tag: String): Int = tags.indexOf(tag)

    fun setSelectedItem(tag: String) {
        selectedItemPosition = findPosition(tag)
        tagItems.forEach { textView: TextView ->
            textView.isSelected = textView.text == tag
        }
        if (::listener.isInitialized) {
            listener.invoke(tag)
        }
    }

    @SuppressLint("WrongConstant")
    inner class Adapter(
        fm: FragmentManager
    ) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getItem(position: Int): ChoiceChipPageFragment {
            val fistItemPosition = count - position - 1
            val instance =
                ChoiceChipPageFragment.getInstance(getTagsForPosition(fistItemPosition))
            if (position == count - 1) {
                instance.lifecycle.addObserver(object : LifecycleObserver {
                    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
                    fun onResume() {
                        if (itemIndexSelected != -1 && !firstItemSelected) {
                            instance.selectFirst(itemIndexSelected)
                            firstItemSelected = true
                        }
                    }
                })
            }
            instance.setListener {
                tagItems.add(it)
                setSelectedItem(it.text.toString())
            }
            return instance
        }

        override fun getCount(): Int = if (tags.isEmpty()) 0 else {
            (tags.size / itemCountPerPage) + 1
        }

    }


    class ChoiceChipPageFragment : Fragment() {
        private lateinit var listener: (TextView) -> Unit

        companion object {
            private const val KEY_TAGS = "position"

            fun getInstance(tags: List<String>): ChoiceChipPageFragment {
                val instance = ChoiceChipPageFragment()
                val args = Bundle()
                args.putStringArrayList(KEY_TAGS, ArrayList(tags))
                instance.arguments = args
                return instance
            }
        }

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            return inflater.inflate(R.layout.fragment_choicechippage, container, false)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            val tags = arguments?.getStringArrayList(KEY_TAGS) ?: return

            var row: ViewGroup? = null
            tags.forEachIndexed { index, s ->
                if (index % 2 == 0) {
                    row = TableRow(context)
                    (view as ViewGroup).addView(row)
                }
                addTagView(row!!, s)
            }
        }

        private fun addTagView(parent: ViewGroup, tag: String) {
            val tagItem = layoutInflater.inflate(
                R.layout.choicechipgroup_tagitem,
                parent,
                false
            ) as TextView
            tagItem.text = tag
            tagItem.setOnClickListener {
                if (::listener.isInitialized) {
                    listener.invoke(tagItem)
                }
            }
            tagList.add(tagItem)
            parent.addView(tagItem)
        }

        fun setListener(listener: (TextView) -> Unit) {
            this.listener = listener
        }

        private val tagList = mutableListOf<View>()

        fun selectFirst(index : Int) {
            if (tagList.isNotEmpty()) {
                tagList[index].performClick()
            }
        }

    }
}


