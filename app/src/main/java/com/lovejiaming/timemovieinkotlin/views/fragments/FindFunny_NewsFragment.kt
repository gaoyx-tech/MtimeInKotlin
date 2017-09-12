package com.lovejiaming.timemovieinkotlin.views.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.lovejiaming.timemovieinkotlin.R
import com.lovejiaming.timemovieinkotlin.adapter.FindFunnyNewsAdapter
import com.lovejiaming.timemovieinkotlin.networkbusiness.NetWorkRealCall_Time
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_find_funny_news.*

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [FindFunny_NewsFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [FindFunny_NewsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FindFunny_NewsFragment : Fragment() {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    private val mAdapter: FindFunnyNewsAdapter by lazy {
        FindFunnyNewsAdapter(activity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments.getString(ARG_PARAM1)
            mParam2 = arguments.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_find_funny_news, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler_news.layoutManager = LinearLayoutManager(this.activity, LinearLayoutManager.VERTICAL, false)
        recycler_news.adapter = mAdapter
        NetWorkRealCall_Time.newInstance().getFindFunnyService()
                .requestFunnyNewsList(1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe {
                    mAdapter.insertNewsData(it)
                }
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.

         * @param param1 Parameter 1.
         * *
         * @param param2 Parameter 2.
         * *
         * @return A new instance of fragment FindFunny_NewsFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): FindFunny_NewsFragment {
            val fragment = FindFunny_NewsFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
