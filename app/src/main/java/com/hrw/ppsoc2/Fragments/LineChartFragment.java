package com.hrw.ppsoc2.Fragments;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.Legend;
import com.github.mikephil.charting.utils.XLabels;
import com.github.mikephil.charting.utils.YLabels;
import com.hrw.ppsoc2.Activities.GraphicActivity;
import com.hrw.ppsoc2.Interface.ConnectListener;
import com.hrw.ppsoc2.Interface.DataListener;
import com.hrw.ppsoc2.R;

import java.util.ArrayList;
import java.util.List;

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

    private int position;

    public LineChart lineChart;
    public List<Integer> xData;
    private String TAG = "LineChartFragment";

    /**
     * Data structure
     */
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
        this.position = Integer.valueOf(mParam1);
        xData = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_line_chart, container, false);
        return v;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            switch (position){
                case 1:
                    ((GraphicActivity)getActivity()).getActionBars().setTitle("嚴重度");
                    break;
                case 2:
                    ((GraphicActivity)getActivity()).getActionBars().setTitle("震顫幅值平均");
                    break;
                case 3:
                    ((GraphicActivity)getActivity()).getActionBars().setTitle("震顫頻率");
                    break;
            }
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
    public void doAfterDataReceived(byte[] input,ArrayList<Integer> data, int position) {
        int temp;
        switch (position){
            case 1:
                xData.add((int)input[4]);
                drawLineChart(input,position);
                break;
            case 2:
                temp = (int)floatCalc(input);
                xData.add(temp);
                drawLineChart(input,position);
                break;
            case 3:
                temp = (int)floatCalc(input);
                xData.add(temp);
                drawLineChart(input,position);
                break;
        }
    }



    private void drawLineChart(byte[] input, int position) {
        setUpChart();
        Entry temp;
        ArrayList<Entry> valsComp1 = new ArrayList<Entry>();
        for(int i = 0; i < xData.size();i++){
            temp = new Entry(xData.get(i),i);//(Y軸數值,項數)
            valsComp1.add(temp);
        }
        LineDataSet setComp1 = null;
        switch (position) {
            case 1:
                setComp1 = new LineDataSet(valsComp1, "嚴重度");
                break;
            case 2:
                setComp1 = new LineDataSet(valsComp1, "幅值平均");
                break;
            case 3:
                setComp1 = new LineDataSet(valsComp1, "頻率");
                break;
        }
        setComp1.setLineWidth(4f);
        setComp1.setCircleSize(5f);
        setComp1.resetColors();
        setComp1.setColor(Color.parseColor("#ff263238"));
        setComp1.setCircleColor(Color.parseColor("#ff263238"));
        setComp1.setHighLightColor(Color.rgb(244, 117, 117));


        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(setComp1);
        //            dataSets.add(setComp2);

        ArrayList<String> xVals = new ArrayList<String>();
        /**
         * X軸參數
         */
        for(int i = 0;i < valsComp1.size();i++){
            xVals.add(2*(i+1)+" s");
        }

        LineData data = new LineData(xVals, dataSets);

        lineChart.setData(data);

        Legend legend = lineChart.getLegend();
        legend.setForm(Legend.LegendForm.SQUARE);
        legend.setFormSize(6f);
        legend.setTextColor(Color.BLACK);


        XLabels x = lineChart.getXLabels();
        x.setTextColor(Color.BLACK);

        YLabels y = lineChart.getYLabels();
        y.setTextColor(Color.BLACK);
        switch (position) {
            case 1:
                y.setLabelCount(3);
                break;
            case 2:
                break;
            case 3:
                break;
        }


        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                lineChart.animateX(500);
            }
        });
    }

    private void setUpChart(){
        lineChart = (LineChart) getView().findViewById(R.id.lineChart);
        if(lineChart != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    lineChart.setNoDataText("");
                    lineChart.setDescription("");
                    lineChart.setDrawYValues(false);
                    lineChart.setTouchEnabled(true);
                    lineChart.setDragEnabled(true);
                    lineChart.setScaleEnabled(true);
                    lineChart.setPinchZoom(true);
                    switch (position){
                        case 1:
                            lineChart.setYRange(0, 3, true);
                            break;
                        default:
                            break;
                    }
                    lineChart.setDrawGridBackground(false);
                    lineChart.setDrawHorizontalGrid(false);
                    lineChart.setDrawVerticalGrid(false);
                    lineChart.setBackgroundColor(getResources().getColor(R.color.background_material_light));
                }
            });
        }
    }

    private float floatCalc(byte[] input){
        return 0;
    }

}
