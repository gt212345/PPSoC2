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
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Legend;
import com.github.mikephil.charting.utils.XLabels;
import com.github.mikephil.charting.utils.YLabels;
import com.hrw.ppsoc2.Activities.GraphicActivity;
import com.hrw.ppsoc2.Interface.ConnectListener;
import com.hrw.ppsoc2.Interface.DataListener;
import com.hrw.ppsoc2.R;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LineChartFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LineChartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LineChartFragment extends Fragment implements ConnectListener, DataListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private LineChart lineChart;
    private ArrayList<Integer> xData;

    private String TAG = "LineChartFragment";

    /**
     * Data structure
     */
    private ArrayList<Entry> valsComp1 = new ArrayList<Entry>();
    private ArrayList<Entry> valsComp2 = new ArrayList<Entry>();

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LineChartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LineChartFragment newInstance(String param1, String param2) {
        LineChartFragment fragment = new LineChartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public LineChartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_line_chart, container, false);
        setUpChart();
        return v;
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            if(lineChart != null)lineChart.animateX(1500);
            ((GraphicActivity)getActivity()).getActionBars().setTitle("嚴重度");
        }
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
    public void doAfterConnected() {
    }


    @Override
    public void doAfterDataReceived(byte[] input) {
        if(lineChart != null) {
            xData = new ArrayList<>();
            xData.add((int) input[4]);
            drawLineChart(input);
        } else {
            Log.w(TAG,"LineChart = null");
        }
    }



    private void drawLineChart(byte[] input) {
        if(lineChart != null) {
            Entry temp;
            for(int i = 0; i < xData.size();i++){
                temp = new Entry(xData.get(0),i);//(Y軸數值,X軸數值)
                valsComp1.add(temp);
            }
            LineDataSet setComp1 = new LineDataSet(valsComp1, "嚴重度");

//            setComp1.setDrawCubic(true);
//            setComp1.setDrawCircles(false);
            setComp1.setLineWidth(5f);
            setComp1.setCircleSize(6f);
            setComp1.setHighLightColor(Color.rgb(244, 117, 117));
            setComp1.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);
            setComp1.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[0]);

            ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
            dataSets.add(setComp1);
//            dataSets.add(setComp2);

            ArrayList<String> xVals = new ArrayList<String>();
            /**
             * X軸參數
             */
            for(int i = 0;i < valsComp1.size();i++){
                xVals.add(i+1+" m");
            }

            LineData data = new LineData(xVals, dataSets);

            lineChart.setData(data);

            Legend legend = lineChart.getLegend();
            legend.setForm(Legend.LegendForm.SQUARE);
            legend.setFormSize(6f);
            legend.setTextColor(Color.WHITE);


            XLabels x = lineChart.getXLabels();
            x.setTextColor(Color.WHITE);

            YLabels y = lineChart.getYLabels();
            y.setTextColor(Color.WHITE);
            y.setLabelCount(3);


            lineChart.animateX(500);
        } else {
            Log.w("LineChart", "is null");
        }
    }

    private void setUpChart(){
        lineChart = (LineChart) getView().findViewById(R.id.lineChart);
        if(lineChart != null) {
            lineChart.setNoDataText("");
            lineChart.setDescription("");
            lineChart.setDrawYValues(false);
            lineChart.setTouchEnabled(true);
            lineChart.setDragEnabled(true);
            lineChart.setScaleEnabled(true);
            lineChart.setPinchZoom(true);
            lineChart.setYRange(0, 3, true);
            lineChart.setDrawGridBackground(false);
            lineChart.setDrawHorizontalGrid(false);
            lineChart.setDrawVerticalGrid(false);
            lineChart.setBackgroundColor(Color.parseColor("#ff303030"));
        }
    }

}
