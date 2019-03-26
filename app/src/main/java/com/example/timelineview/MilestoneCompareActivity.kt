package com.example.timelineview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.flexibleadapter.items.IFlexible
import kotlinx.android.synthetic.main.activity_milestone_compare.*
import java.util.*

/**
 * @author Alan Dreamer
 * @since 2019-03-27 Created
 */
class MilestoneCompareActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_milestone_compare)
        setSupportActionBar(toolbar)
        supportActionBar?.setTitle(R.string.app_name)
        val adapter: FlexibleAdapter<IFlexible<*>> = FlexibleAdapter(getItems(), this, true)
        milestones_list.layoutManager = SmoothScrollLinearLayoutManager(this)
        milestones_list.adapter = adapter
        milestones_list.setHasFixedSize(true)
    }

    private fun getItems(): List<AbstractFlexibleItem<*>> {
        val items = ArrayList<AbstractFlexibleItem<*>>()
        items.add(MilestoneCompareItem("1", "UI design", MilestoneCompareStatus.COMPLETED))
        items.add(
            MilestoneCompareItem(
                "2",
                "Android development",
                MilestoneCompareStatus.REQUEST_TO_MODIFY_THE_CONTRACT
            )
        )
        items.add(MilestoneCompareItem("3", "Server development", MilestoneCompareStatus.INACTIVE))
        return items
    }
}