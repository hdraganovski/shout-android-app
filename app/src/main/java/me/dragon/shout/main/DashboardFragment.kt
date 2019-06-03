package me.dragon.shout.main


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_dashboard.*
import me.dragon.shout.R


class DashboardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dashboardFragment_listView.apply {
            layoutManager = LinearLayoutManager(view.context.applicationContext)
            adapter = ShoutsRecyclerViewAdapter()
        }

        dashboardFragment_swipeRefresh.setOnRefreshListener {
            dashboardFragment_swipeRefresh.apply {
                postDelayed({
                    this.isRefreshing = false
                }, 4000)
            }
        }

        dashboardFragment_swipeRefresh.setColorSchemeResources(
            R.color.colorPrimary,
            R.color.colorAccent
        )

    }

    companion object {
        fun newInstance() = DashboardFragment()
    }
}
