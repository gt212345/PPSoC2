package com.hrw.ppsoc2.Fragments;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Legend;
import com.github.mikephil.charting.utils.YLabels;
import com.hrw.ppsoc2.Activities.GraphicActivity;
import com.hrw.ppsoc2.Interface.ConnectListener;
import com.hrw.ppsoc2.Interface.DataListener;
import com.hrw.ppsoc2.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PieChartFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PieChartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PieChartFragment extends Fragment implements ConnectListener, DataListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private PieChart pieChart;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PieChartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PieChartFragment newInstance(String param1, String param2) {
        PieChartFragment fragment = new PieChartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            ((GraphicActivity)getActivity()).getActionBars().setTitle("嚴重度分析");
        }
    }

    public PieChartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pie_chart, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void setData(byte[] input,ArrayList<Integer> data) {
        // TODO: Fill blank setData method
        setUpChart();
        pieChart.setHoleRadius(60f);

        pieChart.setDescription("");
        pieChart.setValueTextColor(Color.BLACK);
        pieChart.setDrawYValues(true);
        pieChart.setDrawCenterText(true);

        pieChart.setDrawHoleEnabled(true);

        pieChart.setRotationAngle(0);

        // draws the corresponding description value into the slice
        pieChart.setDrawXValues(true);

        // enable rotation of the chart by touch
        pieChart.setRotationEnabled(true);

        // display percentage values
        pieChart.setUsePercentValues(true);

        ArrayList<Entry> yVals = new ArrayList<>();
        ArrayList<String> xVals = new ArrayList<>();
        for (int i = 0; i < data.size();i++){
            yVals.add(new Entry(data.get(i), i));
            xVals.add("嚴重程度: "+String .valueOf(i));
        }

        PieDataSet set = new PieDataSet(yVals,"嚴重度結果");
        set.setSliceSpace(3f);

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        set.setColors(colors);


        PieData temp = new PieData(xVals,set);
        pieChart.setData(temp);
        Legend legend = pieChart.getLegend();
        legend.setTextColor(Color.WHITE);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                pieChart.animateX(500);
            }
        });
    }

    @Override
    public void doAfterConnected() {

    }

    @Override
    public void doAfterDataReceived(byte[] input,ArrayList<Integer> data,int position) {
        setData(input,data);
    }

    private void setUpChart(){
        pieChart = (PieChart) getView().findViewById(R.id.piechart);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(pieChart != null) {
                    pieChart.setHoleColor(Color.rgb(235, 235, 235));
                    pieChart.setDescription("");
                    pieChart.setNoDataText("");
                    pieChart.setCenterText("嚴重度分析");
                }
            }
        });
    }



}
