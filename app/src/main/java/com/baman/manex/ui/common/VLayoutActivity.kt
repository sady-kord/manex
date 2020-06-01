/*
 * MIT License
 *
 * Copyright (c) 2016 Alibaba Group
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.baman.manex.ui.common

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.RecyclablePagerAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.alibaba.android.vlayout.VirtualLayoutManager.LayoutParams
import com.alibaba.android.vlayout.extend.LayoutManagerCanScrollListener
import com.alibaba.android.vlayout.extend.PerformanceMonitor
import com.alibaba.android.vlayout.extend.ViewLifeCycleListener
import com.alibaba.android.vlayout.layout.*
import com.alibaba.android.vlayout.layout.RangeGridLayoutHelper.GridRangeStyle
import com.baman.manex.R
import java.util.*

/**
 * @author villadora
 */
class VLayoutActivity : Activity() {

    private var mSwipeRefreshLayout: SwipeRefreshLayout? = null

    private var mFirstText: TextView? = null
    private var mLastText: TextView? = null

    private var mCountText: TextView? = null

    private var mTotalOffsetText: TextView? = null

    private var trigger: Runnable? = null

    internal var isOpened = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        mSwipeRefreshLayout = findViewById<View>(R.id.swipe_container) as SwipeRefreshLayout
        mFirstText = findViewById<View>(R.id.first) as TextView
        mLastText = findViewById<View>(R.id.last) as TextView
        mCountText = findViewById<View>(R.id.count) as TextView
        mTotalOffsetText = findViewById<View>(R.id.total_offset) as TextView

        val recyclerView = findViewById<View>(R.id.main_view) as RecyclerView

        val layoutManager = VirtualLayoutManager(this)
        layoutManager.setPerformanceMonitor(object : PerformanceMonitor() {

            internal var start: Long = 0
            internal var end: Long = 0

            override fun recordStart(phase: String, view: View) {
                start = System.currentTimeMillis()
            }

            override fun recordEnd(phase: String, view: View) {
                end = System.currentTimeMillis()
                Log.d("VLayoutActivity", view.javaClass.name + " " + (end - start))
            }
        })
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, scrollState: Int) {

            }

            override fun onScrolled(recyclerView: RecyclerView, i: Int, i2: Int) {
                mFirstText!!.text = "First: " + layoutManager.findFirstVisibleItemPosition()
                mLastText!!.text =
                    "Existing: " + MainViewHolder.existing + " Created: " + MainViewHolder.createdTimes
                mCountText!!.text = "Count: " + recyclerView.childCount
                mTotalOffsetText!!.text = "Total Offset: " + layoutManager.offsetToStart
            }
        })

        recyclerView.layoutManager = layoutManager

        // layoutManager.setReverseLayout(true);

        val itemDecoration = object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                val position = (view.layoutParams as LayoutParams).viewPosition
                outRect.set(4, 4, 4, 4)
            }
        }


        val viewPool = RecyclerView.RecycledViewPool()

        recyclerView.setRecycledViewPool(viewPool)

        // recyclerView.addItemDecoration(itemDecoration);

        viewPool.setMaxRecycledViews(0, 20)

        layoutManager.setRecycleOffset(300)

        // viewLifeCycleListener should be used with setRecycleOffset()
        layoutManager.setViewLifeCycleListener(object : ViewLifeCycleListener {
            override fun onAppearing(view: View) {
                //                Log.e("ViewLifeCycleTest", "onAppearing: " + view);
            }

            override fun onDisappearing(view: View) {
                //                Log.e("ViewLifeCycleTest", "onDisappearing: " + view);
            }

            override fun onAppeared(view: View) {
                //                Log.e("ViewLifeCycleTest", "onAppeared: " + view);
            }

            override fun onDisappeared(view: View) {
                //                Log.e("ViewLifeCycleTest", "onDisappeared: " + view);
            }
        })

        layoutManager.setLayoutManagerCanScrollListener(object : LayoutManagerCanScrollListener {
            override fun canScrollVertically(): Boolean {
                Log.i("vlayout", "canScrollVertically: ")
                return true
            }

            override fun canScrollHorizontally(): Boolean {
                Log.i("vlayout", "canScrollHorizontally: ")
                return true
            }
        })

        val delegateAdapter = DelegateAdapter(layoutManager, true)

        recyclerView.adapter = delegateAdapter

        val adapters = LinkedList<DelegateAdapter.Adapter<*>>()

        if (BANNER_LAYOUT) {
            adapters.add(object : SubAdapter(this, LinearLayoutHelper(), 1) {

                override fun onViewRecycled(holder: MainViewHolder) {
                    if (holder.itemView is ViewPager) {
                        holder.itemView.adapter = null
                    }
                }

                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
                    return if (viewType == 1) MainViewHolder(
                        LayoutInflater.from(this@VLayoutActivity).inflate(
                            R.layout.view_pager,
                            parent,
                            false
                        )
                    ) else super.onCreateViewHolder(parent, viewType)

                }

                override fun getItemViewType(position: Int): Int {
                    return 1
                }

                override fun onBindViewHolderWithOffset(
                    holder: MainViewHolder,
                    position: Int,
                    offsetTotal: Int
                ) {

                }

                override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
                    if (holder.itemView is ViewPager) {
                        val viewPager = holder.itemView

                        viewPager.layoutParams =
                            LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200)

                        // from position to get adapter
                        viewPager.adapter =
                            PagerAdapter(
                                this,
                                viewPool
                            )
                    }
                }
            })
        }

        //{
        //    GridLayoutHelper helper = new GridLayoutHelper(10);
        //    helper.setAspectRatio(4f);
        //    helper.setGap(10);
        //    adapters.add(new SubAdapter(this, helper, 80));
        //}

        if (FLOAT_LAYOUT) {
            val layoutHelper = FloatLayoutHelper()
            layoutHelper.setAlignType(FixLayoutHelper.BOTTOM_RIGHT)
            layoutHelper.setDefaultLocation(100, 400)
            val layoutParams = LayoutParams(150, 150)
            adapters.add(
                SubAdapter(
                    this,
                    layoutHelper,
                    1,
                    layoutParams
                )
            )
        }

        if (LINEAR_LAYOUT) {
            val layoutHelper1 = LinearLayoutHelper()
            layoutHelper1.bgColor = Color.YELLOW
            layoutHelper1.aspectRatio = 2.0f
            layoutHelper1.setMargin(10, 10, 10, 10)
            layoutHelper1.setPadding(10, 10, 10, 10)
            val layoutHelper2 = LinearLayoutHelper()
            layoutHelper2.aspectRatio = 4.0f
            layoutHelper2.setDividerHeight(10)
            layoutHelper2.setMargin(10, 0, 10, 10)
            layoutHelper2.setPadding(10, 0, 10, 10)
            layoutHelper2.bgColor = -0xa59dd
            val mainHandler = Handler(Looper.getMainLooper())
            adapters.add(object : SubAdapter(this, layoutHelper1, 1) {
                override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
                    super.onBindViewHolder(holder, position)
                    val subAdapter = this
                    //mainHandler.postDelayed(new Runnable() {
                    //    @Override
                    //    public void run() {
                    //        //delegateAdapter.removeAdapter(subAdapter);
                    //        //notifyItemRemoved(1);
                    //        holder.itemView.setVisibility(View.GONE);
                    //        notifyItemChanged(1);
                    //        layoutManager.runAdjustLayout();
                    //    }
                    //}, 2000L);
                }
            })
            adapters.add(object : SubAdapter(this, layoutHelper2, 6) {

                override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
                    if (position % 2 == 0) {
                        val layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300)
                        layoutParams.mAspectRatio = 5f
                        holder.itemView.layoutParams = layoutParams
                    }
                }
            })
        }

        run {
            val layoutHelper = RangeGridLayoutHelper(4)
            layoutHelper.bgColor = Color.GREEN
            layoutHelper.setWeights(floatArrayOf(20f, 26.665f))
            layoutHelper.setPadding(15, 15, 15, 15)
            layoutHelper.setMargin(15, 50, 15, 150)
            layoutHelper.setHGap(10)
            layoutHelper.setVGap(10)


            val rangeStyle = GridRangeStyle()
            rangeStyle.setBgColor(Color.RED)
            rangeStyle.spanCount = 2
            rangeStyle.setWeights(floatArrayOf(46.665f))
            rangeStyle.setPadding(15, 15, 15, 15)
            rangeStyle.setMargin(15, 15, 15, 15)
            rangeStyle.setHGap(5)
            rangeStyle.setVGap(5)
//            layoutHelper.addRangeStyle(0, 7, rangeStyle)

            val rangeStyle1 = GridRangeStyle()
            rangeStyle1.setBgColor(Color.YELLOW)
            rangeStyle1.spanCount = 2
            rangeStyle1.setWeights(floatArrayOf(46.665f))
            rangeStyle1.setPadding(15, 15, 15, 15)
            rangeStyle1.setMargin(15, 15, 15, 15)
            rangeStyle1.setHGap(5)
            rangeStyle1.setVGap(5)
//            layoutHelper.addRangeStyle(8, 15, rangeStyle1)

            val rangeStyle2 = GridRangeStyle()
            rangeStyle2.setBgColor(Color.CYAN)
            rangeStyle2.spanCount = 2
            rangeStyle2.setWeights(floatArrayOf(46.665f))
            rangeStyle2.setPadding(15, 15, 15, 15)
            rangeStyle2.setMargin(15, 15, 15, 15)
            rangeStyle2.setHGap(5)
            rangeStyle2.setVGap(5)
//            layoutHelper.addRangeStyle(16, 22, rangeStyle2)


            val rangeStyle3 = GridRangeStyle()
            rangeStyle3.setBgColor(Color.DKGRAY)
            rangeStyle3.spanCount = 1
            rangeStyle3.setWeights(floatArrayOf(46.665f))
            rangeStyle3.setPadding(15, 15, 15, 15)
            rangeStyle3.setMargin(15, 15, 15, 15)
            rangeStyle3.setHGap(5)
            rangeStyle3.setVGap(5)
//            rangeStyle2.addChildRangeStyle(0, 2, rangeStyle3)


            val rangeStyle4 = GridRangeStyle()
            rangeStyle4.setBgColor(Color.BLUE)
            rangeStyle4.spanCount = 2
            rangeStyle4.setWeights(floatArrayOf(46.665f))
            rangeStyle4.setPadding(15, 15, 15, 15)
            rangeStyle4.setMargin(15, 15, 15, 15)
            rangeStyle4.setHGap(5)
            rangeStyle4.setVGap(5)
//            rangeStyle2.addChildRangeStyle(3, 6, rangeStyle4)

            val rangeStyle5 = GridRangeStyle()
            rangeStyle5.setBgColor(Color.RED)
            rangeStyle5.spanCount = 2
            rangeStyle5.setPadding(15, 15, 15, 15)
            rangeStyle5.setMargin(15, 15, 15, 15)
            rangeStyle5.setHGap(5)
            rangeStyle5.setVGap(5)
            layoutHelper.addRangeStyle(12, 22, rangeStyle5)


//            val rangeStyle6 = GridRangeStyle()
//            rangeStyle6.setBgColor(Color.MAGENTA)
//            rangeStyle6.spanCount = 2
//            rangeStyle6.setWeights(floatArrayOf(46.665f))
//            rangeStyle6.setPadding(15, 15, 15, 15)
//            rangeStyle6.setMargin(15, 15, 15, 15)
//            rangeStyle6.setHGap(5)
//            rangeStyle6.setVGap(5)
//            rangeStyle5.addChildRangeStyle(3, 7, rangeStyle6)

            adapters.add(
                SubAdapter(
                    this,
                    layoutHelper,
                    23
                )
            )
        }

//        run {
//            val layoutHelper = SingleLayoutHelper()
//            layoutHelper.bgColor = Color.BLUE
//            layoutHelper.setMargin(0, 30, 0, 200)
//            adapters.add(
//                SubAdapter(
//                    this,
//                    layoutHelper,
//                    1,
//                    LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100)
//                )
//            )
//        }

        if (STICKY_LAYOUT) {
            val layoutHelper = StickyLayoutHelper()
            //layoutHelper.setOffset(100);
            layoutHelper.aspectRatio = 4f
            adapters.add(
                SubAdapter(
                    this,
                    layoutHelper,
                    1,
                    LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100)
                )
            )
        }

        //{
        //    final StaggeredGridLayoutHelper helper = new StaggeredGridLayoutHelper(3, 10);
        //    helper.setBgColor(0xFF86345A);
        //    adapters.add(new SubAdapter(this, helper, 4) {
        //
        //        @Override
        //        public void onBindViewHolder(MainViewHolder holder, int position) {
        //            super.onBindViewHolder(holder, position);
        //            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300);
        //            if (position % 2 == 0) {
        //                layoutParams.mAspectRatio = 1.0f;
        //            } else {
        //                layoutParams.height = 340 + position % 7 * 20;
        //            }
        //            holder.itemView.setLayoutParams(layoutParams);
        //        }
        //    });
        //}
//        run {
//
//            val helper = GridLayoutHelper(3, 4)
//            helper.bgColor = -0x79cba6
//            adapters.add(object : SubAdapter(this, helper, 4) {
//                override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
//                    super.onBindViewHolder(holder, position)
//                    val layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300)
//                    holder.itemView.layoutParams = layoutParams
//                }
//            })
//        }

//        run {
//            val layoutHelper = RangeGridLayoutHelper(4)
//            layoutHelper.bgColor = Color.GREEN
//            layoutHelper.setWeights(floatArrayOf(20f, 26.665f))
//            layoutHelper.setPadding(15, 15, 15, 15)
//            layoutHelper.setMargin(15, 15, 15, 15)
//            layoutHelper.setHGap(10)
//            layoutHelper.setVGap(10)
//            val rangeStyle = GridRangeStyle()
//            rangeStyle.setBgColor(Color.RED)
//            rangeStyle.spanCount = 2
//            rangeStyle.setWeights(floatArrayOf(46.665f))
//            rangeStyle.setPadding(15, 15, 15, 15)
//            rangeStyle.setMargin(15, 15, 15, 15)
//            rangeStyle.setHGap(5)
//            rangeStyle.setVGap(5)
//            layoutHelper.addRangeStyle(4, 7, rangeStyle)
//            val rangeStyle1 = GridRangeStyle()
//            rangeStyle1.setBgColor(Color.YELLOW)
//            rangeStyle1.spanCount = 2
//            rangeStyle1.setWeights(floatArrayOf(46.665f))
//            rangeStyle1.setPadding(15, 15, 15, 15)
//            rangeStyle1.setMargin(15, 15, 15, 15)
//            rangeStyle1.setHGap(5)
//            rangeStyle1.setVGap(5)
//            layoutHelper.addRangeStyle(8, 11, rangeStyle1)
//            adapters.add(SubAdapter(this, layoutHelper, 16))
//
//        }

        if (SINGLE_LAYOUT) {
            val layoutHelper = SingleLayoutHelper()
            layoutHelper.bgColor = Color.rgb(135, 225, 90)
            layoutHelper.aspectRatio = 4f
            layoutHelper.setMargin(10, 20, 10, 20)
            layoutHelper.setPadding(10, 10, 10, 10)
            adapters.add(
                SubAdapter(
                    this,
                    layoutHelper,
                    1,
                    LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100)
                )
            )
        }

        if (COLUMN_LAYOUT) {
            val layoutHelper = ColumnLayoutHelper()
            layoutHelper.bgColor = -0xff0f10
            layoutHelper.setWeights(floatArrayOf(40.0f, java.lang.Float.NaN, 40f))
            adapters.add(object : SubAdapter(this, layoutHelper, 5) {

                override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
                    if (position == 0) {
                        val layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300)
                        layoutParams.mAspectRatio = 4f
                        holder.itemView.layoutParams = layoutParams
                    } else {
                        val layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300)
                        layoutParams.mAspectRatio = java.lang.Float.NaN
                        holder.itemView.layoutParams = layoutParams
                    }
                }

            })
        }

        if (ONEN_LAYOUT) {
            val helper = OnePlusNLayoutHelper()
            helper.bgColor = -0x789c7c
            helper.aspectRatio = 4.0f
            helper.setColWeights(floatArrayOf(40f, 45f))
            helper.setMargin(10, 20, 10, 20)
            helper.setPadding(10, 10, 10, 10)
            adapters.add(
                SubAdapter(
                    this,
                    helper,
                    2
                )
            )
        }

        if (ONEN_LAYOUT) {
            val helper = OnePlusNLayoutHelper()
            helper.bgColor = -0x10745d
            helper.aspectRatio = 2.0f
            helper.setColWeights(floatArrayOf(40f))
            helper.setRowWeight(30f)
            helper.setMargin(10, 20, 10, 20)
            helper.setPadding(10, 10, 10, 10)
            adapters.add(object : SubAdapter(this, helper, 4) {
                override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
                    super.onBindViewHolder(holder, position)
                    val lp = holder.itemView.layoutParams as LayoutParams
                    if (position == 0) {
                        lp.rightMargin = 1
                    } else if (position == 1) {

                    } else if (position == 2) {
                        lp.topMargin = 1
                        lp.rightMargin = 1
                    }
                }
            })
        }

        if (ONEN_LAYOUT) {
            adapters.add(
                SubAdapter(
                    this,
                    OnePlusNLayoutHelper(),
                    0
                )
            )
            val helper = OnePlusNLayoutHelper()
            helper.bgColor = -0x781abd
            helper.aspectRatio = 1.8f
            helper.setColWeights(floatArrayOf(33.33f, 50f, 40f))
            helper.setMargin(10, 20, 10, 20)
            helper.setPadding(10, 10, 10, 10)
            val lp = LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            adapters.add(object : SubAdapter(this, helper, 3, lp) {
                override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
                    super.onBindViewHolder(holder, position)
                    val lp = holder.itemView.layoutParams as LayoutParams
                    if (position == 0) {
                        lp.rightMargin = 1
                    }
                }
            })
        }

        if (COLUMN_LAYOUT) {
            adapters.add(
                SubAdapter(
                    this,
                    ColumnLayoutHelper(),
                    0
                )
            )
            adapters.add(
                SubAdapter(
                    this,
                    ColumnLayoutHelper(),
                    4
                )
            )
        }

        if (FIX_LAYOUT) {
            var layoutHelper = FixLayoutHelper(10, 10)
            adapters.add(
                SubAdapter(
                    this,
                    layoutHelper,
                    0
                )
            )

            layoutHelper = FixLayoutHelper(FixLayoutHelper.TOP_RIGHT, 20, 20)

            adapters.add(object : SubAdapter(this, layoutHelper, 1) {
                override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
                    super.onBindViewHolder(holder, position)
                    val layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200)
                    holder.itemView.layoutParams = layoutParams
                }
            })
        }

        //if (STICKY_LAYOUT) {
        //    StickyLayoutHelper layoutHelper = new StickyLayoutHelper(false);
        //    adapters.add(new SubAdapter(this, layoutHelper, 0));
        //    layoutHelper = new StickyLayoutHelper(false);
        //    layoutHelper.setOffset(100);
        //    adapters.add(new SubAdapter(this, layoutHelper, 1, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100)));
        //}

        if (GRID_LAYOUT) {
            var layoutHelper = GridLayoutHelper(2)
            layoutHelper.setMargin(7, 0, 7, 0)
            layoutHelper.setWeights(floatArrayOf(46.665f))
            layoutHelper.hGap = 3
            adapters.add(
                SubAdapter(
                    this,
                    layoutHelper,
                    2
                )
            )

            layoutHelper = GridLayoutHelper(4)
            layoutHelper.setWeights(floatArrayOf(20f, 26.665f))
            layoutHelper.setMargin(7, 0, 7, 0)
            layoutHelper.hGap = 3
            adapters.add(
                SubAdapter(
                    this,
                    layoutHelper,
                    8
                )
            )
        }


        if (GRID_LAYOUT) {
            adapters.add(
                SubAdapter(
                    this,
                    GridLayoutHelper(4),
                    0
                )
            )

            val helper = GridLayoutHelper(4)
            helper.aspectRatio = 4f
            //helper.setColWeights(new float[]{40, 20, 30, 30});
            // helper.setMargin(0, 10, 0, 10);
            helper.setGap(10)
            adapters.add(object : SubAdapter(this, helper, 80) {
                override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
                    super.onBindViewHolder(holder, position)
                    val lp = holder.itemView.layoutParams as LayoutParams
                    // lp.bottomMargin = 1;
                    // lp.rightMargin = 1;
                }
            })
        }

        if (FIX_LAYOUT) {
            adapters.add(object : SubAdapter(this, ScrollFixLayoutHelper(20, 20), 1) {
                override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
                    super.onBindViewHolder(holder, position)
                    val layoutParams = LayoutParams(200, 200)
                    holder.itemView.layoutParams = layoutParams
                }
            })
        }

        if (LINEAR_LAYOUT)
            adapters.add(
                SubAdapter(
                    this,
                    LinearLayoutHelper(),
                    10
                )
            )

        if (GRID_LAYOUT) {
            val helper = GridLayoutHelper(3)
            helper.setMargin(0, 10, 0, 10)
            adapters.add(
                SubAdapter(
                    this,
                    helper,
                    3
                )
            )
        }

        if (STAGGER_LAYOUT) {
            // adapters.add(new SubAdapter(this, new StaggeredGridLayoutHelper(2, 0), 0));
            val helper = StaggeredGridLayoutHelper(2, 10)
            helper.setMargin(20, 10, 10, 10)
            helper.setPadding(10, 10, 20, 10)
            helper.bgColor = -0x79cba6
            adapters.add(object : SubAdapter(this, helper, 27) {
                override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
                    super.onBindViewHolder(holder, position)
                    val layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200)
                    if (position % 2 == 0) {
                        layoutParams.mAspectRatio = 1.0f
                    } else {
                        layoutParams.height = 340 + position % 7 * 20
                    }
                    holder.itemView.layoutParams = layoutParams
                }
            })
        }

        if (COLUMN_LAYOUT) {
            // adapters.add(new SubAdapter(this, new ColumnLayoutHelper(), 3));
        }

        if (GRID_LAYOUT) {
            // adapters.add(new SubAdapter(this, new GridLayoutHelper(4), 24));
        }

        adapters.add(
            FooterAdapter(
                recyclerView,
                this@VLayoutActivity,
                GridLayoutHelper(1),
                1
            )
        )

        delegateAdapter.setAdapters(adapters)


        val mainHandler = Handler(Looper.getMainLooper())

        trigger = Runnable {
            //recyclerView.scrollToPosition(22);
            //recyclerView.getAdapter().notifyDataSetChanged();
            //mainHandler.postDelayed(trigger, 1000);
            //List<DelegateAdapter.Adapter> newAdapters = new ArrayList<>();
            //newAdapters.add((new SubAdapter(VLayoutActivity.this, new ColumnLayoutHelper(), 3)));
            //newAdapters.add((new SubAdapter(VLayoutActivity.this, new GridLayoutHelper(4), 24)));
            //delegateAdapter.addAdapter(0, new SubAdapter(VLayoutActivity.this, new ColumnLayoutHelper(), 3));
            //delegateAdapter.addAdapter(1, new SubAdapter(VLayoutActivity.this, new GridLayoutHelper(4), 24));
            //delegateAdapter.notifyDataSetChanged();
        }

        findViewById<View>(R.id.jump).setOnClickListener {
            val position = findViewById<View>(R.id.position) as EditText
            if (!TextUtils.isEmpty(position.text)) {
                try {
                    val pos = Integer.parseInt(position.text.toString())
                    recyclerView.scrollToPosition(pos)
                } catch (e: Exception) {
                    Log.e("VlayoutActivity", e.message, e)
                }

            } else {
                recyclerView.requestLayout()
            }
            //FooterAdapter footer = (FooterAdapter)adapters.get(adapters.size() - 1);
            //footer.toggleFoot();
        }


        mainHandler.postDelayed(trigger!!, 1000)

        mSwipeRefreshLayout!!.setOnRefreshListener {
            mainHandler.postDelayed({
                mSwipeRefreshLayout!!.isRefreshing = false
            }, 2000L)
        }
        setListenerToRootView()
    }

    fun setListenerToRootView() {
        val activityRootView = window.decorView.findViewById<View>(android.R.id.content)
        activityRootView.viewTreeObserver.addOnGlobalLayoutListener {
            val heightDiff = activityRootView.rootView.height - activityRootView.height
            if (heightDiff > 100) { // 99% of the time the height diff will be due to a keyboard.
                if (isOpened == false) {
                    //Do two things, make the view top visible and the editText smaller
                }
                isOpened = true
            } else if (isOpened == true) {
                isOpened = false
                val recyclerView = findViewById<View>(R.id.main_view) as RecyclerView
                recyclerView.adapter!!.notifyDataSetChanged()
            }
        }
    }

    internal class FooterAdapter @JvmOverloads constructor(
        private val mRecyclerView: RecyclerView,
        private val mContext: Context,
        private val mLayoutHelper: LayoutHelper,
        private val mCount: Int,
        private val mLayoutParams: LayoutParams = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            300
        )
    ) : DelegateAdapter.Adapter<MainViewHolder>() {

        private var showFooter = false

        override fun getItemViewType(position: Int): Int {
            return 100
        }

        override fun onCreateLayoutHelper(): LayoutHelper {
            return mLayoutHelper
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
            return MainViewHolder(
                LayoutInflater.from(mContext).inflate(
                    R.layout.item,
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
            val lp = holder.itemView.layoutParams as LayoutParams
            if (showFooter) {
                lp.height = 300
            } else {
                lp.height = 0
            }
            holder.itemView.layoutParams = lp
        }


        override fun onBindViewHolderWithOffset(
            holder: MainViewHolder,
            position: Int,
            offsetTotal: Int
        ) {
            (holder.itemView.findViewById<View>(R.id.title) as TextView).text =
                offsetTotal.toString()
        }

        override fun getItemCount(): Int {
            return mCount
        }

        fun toggleFoot() {
            this.showFooter = !this.showFooter
            mRecyclerView.adapter!!.notifyItemChanged(205)
            mRecyclerView.post {
                mRecyclerView.scrollToPosition(205)
                mRecyclerView.requestLayout()
            }
        }

    }

    // RecyclableViewPager

    internal class PagerAdapter(adapter: SubAdapter, pool: RecyclerView.RecycledViewPool) :
        RecyclablePagerAdapter<MainViewHolder>(adapter, pool) {

        override fun getCount(): Int {
            return 6
        }

        override fun onBindViewHolder(viewHolder: MainViewHolder, position: Int) {
            // only vertical
            viewHolder.itemView.layoutParams =
                LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            (viewHolder.itemView.findViewById<View>(R.id.title) as TextView).text =
                "Banner: $position"
        }

        override fun getItemViewType(position: Int): Int {
            return 0
        }
    }


    internal open class SubAdapter @JvmOverloads constructor(
        private val mContext: Context,
        private val mLayoutHelper: LayoutHelper,
        private val mCount: Int,
        private val mLayoutParams: LayoutParams = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            300
        )
    ) : DelegateAdapter.Adapter<MainViewHolder>() {

        override fun onCreateLayoutHelper(): LayoutHelper {
            return mLayoutHelper
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
            return MainViewHolder(
                LayoutInflater.from(mContext).inflate(
                    R.layout.item,
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
            // only vertical
            holder.itemView.layoutParams = LayoutParams(mLayoutParams)
        }


        override fun onBindViewHolderWithOffset(
            holder: MainViewHolder,
            position: Int,
            offsetTotal: Int
        ) {
            (holder.itemView.findViewById<View>(R.id.title) as TextView).text =
                offsetTotal.toString()
        }

        override fun getItemCount(): Int {
            return mCount
        }
    }


    internal class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            createdTimes++
            existing++
        }

        @Throws(Throwable::class)
        protected fun finalize() {
            existing--
        }

        companion object {

            @Volatile
            var existing = 0
            var createdTimes = 0
        }
    }

    companion object {
        private val BANNER_LAYOUT = true
        private val FIX_LAYOUT = true
        private val LINEAR_LAYOUT = true
        private val SINGLE_LAYOUT = true
        private val FLOAT_LAYOUT = true
        private val ONEN_LAYOUT = true
        private val COLUMN_LAYOUT = true
        private val GRID_LAYOUT = true
        private val STICKY_LAYOUT = true
        private val STAGGER_LAYOUT = true
    }
}
