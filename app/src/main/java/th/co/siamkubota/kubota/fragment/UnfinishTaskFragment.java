package th.co.siamkubota.kubota.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.swagger.client.model.Task;
import th.co.siamkubota.kubota.R;
import th.co.siamkubota.kubota.adapter.UnfinishTaskAdapter;
import th.co.siamkubota.kubota.adapter.UnfinishTaskSectionAdapter;
import th.co.siamkubota.kubota.adapter.UnsendTaskAdapter;
import th.co.siamkubota.kubota.utils.function.Converter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UnfinishTaskFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UnfinishTaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UnfinishTaskFragment extends Fragment implements
        AdapterView.OnItemClickListener,
        View.OnClickListener{


    private static final String ARG_PARAM_DATA = "data";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ListView listView;
    private Button startNewTaskButton;
    private ArrayList<Task> datalist;
    //private UnfinishTaskAdapter adapter;
    private UnfinishTaskSectionAdapter adapter;
    private UnfinishTaskAdapter unfinishTaskAdapter;
    private UnsendTaskAdapter unsendTaskAdapter;


    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UnfinishTaskFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UnfinishTaskFragment newInstance(List<Task> datalist) {
        UnfinishTaskFragment fragment = new UnfinishTaskFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM_DATA, (ArrayList<Task>)datalist);
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public UnfinishTaskFragment() {
        // Required empty public constructor
    }

    public void setmListener(OnFragmentInteractionListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            datalist = getArguments().getParcelableArrayList(ARG_PARAM_DATA);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_unfinish_task, container, false);

        listView = (ListView) rootView.findViewById(R.id.listView);

        if(datalist != null){
            //adapter = new UnfinishTaskAdapter(getActivity(), datalist);

            adapter = new UnfinishTaskSectionAdapter(getActivity(), R.layout.item_unfinish_header);

            unsendTaskAdapter = new UnsendTaskAdapter(getActivity(),datalist );
            unfinishTaskAdapter = new UnfinishTaskAdapter(getActivity(),datalist );

            adapter.addSection(getString(R.string.unfinish_task_unsend), unsendTaskAdapter);
            adapter.addSection(getString(R.string.unfinish_task), unfinishTaskAdapter);


            listView.setAdapter(adapter);
            listView.setOnItemClickListener(this);


//            LinearLayout listHeaderView = (LinearLayout)View.inflate(getActivity(),
//                    R.layout.header_listview, null);
//            LinearLayout listFooterView = (LinearLayout)View.inflate(getActivity(),
//                    R.layout.footer_listview, null);

            /*
            LinearLayout listHeaderView  = new LinearLayout(getActivity());
            listHeaderView.setGravity(Gravity.CENTER);
            //LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            AbsListView.LayoutParams params = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Converter.dpTopx(getActivity(), 70));
            listHeaderView.setLayoutParams(params);
            //params.setMargins(left, top, left, top);

            TextView listTitle = (TextView)View.inflate(getActivity(),
                    R.layout.textview_title, null);

            listHeaderView.addView(listTitle);

            */

            LinearLayout listFooterView = new LinearLayout(getActivity());
            AbsListView.LayoutParams paramsfooter = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Converter.dpTopx(getActivity(), 120));
            listFooterView.setGravity(Gravity.CENTER);
            listFooterView.setLayoutParams(paramsfooter);


            startNewTaskButton = (Button) View.inflate(getActivity(),
                    R.layout.button_white, null);
            startNewTaskButton.setOnClickListener(this);

            listFooterView.addView(startNewTaskButton);

            //listView.addHeaderView(listHeaderView);
            listView.addFooterView(listFooterView);
        }

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
  /*  public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
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
        //public void onFragmentInteraction(Uri uri);
        public void onDisplayTask(Task task);
    }

    //////////////////////////////////////////////////////////// Implement method

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if(parent == listView){

            int headerCount = listView.getHeaderViewsCount();

            mListener.onDisplayTask(datalist.get(position - headerCount));
        }
    }

    @Override
    public void onClick(View v) {
        if(v == startNewTaskButton){
            //getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
            //getActivity().getSupportFragmentManager().popBackStack();
            mListener.onDisplayTask(null);
        }
    }
}
