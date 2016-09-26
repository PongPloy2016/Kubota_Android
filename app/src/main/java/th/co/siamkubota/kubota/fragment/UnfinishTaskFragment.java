package th.co.siamkubota.kubota.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
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

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import io.swagger.client.model.Task;
import th.co.siamkubota.kubota.R;
import th.co.siamkubota.kubota.activity.MainActivity;
import th.co.siamkubota.kubota.activity.ResultActivity;
import th.co.siamkubota.kubota.activity.ServiceActivity;
import th.co.siamkubota.kubota.adapter.UnfinishTaskAdapter;
import th.co.siamkubota.kubota.adapter.UnfinishTaskSectionAdapter;
import th.co.siamkubota.kubota.adapter.UnsendTaskAdapter;
import th.co.siamkubota.kubota.logger.Logger;
import th.co.siamkubota.kubota.model.OfflineTask;
import th.co.siamkubota.kubota.sqlite.TaskDataSource;
import th.co.siamkubota.kubota.utils.function.Converter;

import static com.google.android.gms.internal.zzhu.runOnUiThread;

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
    public static int KEY = 1;
    public static int VALUE = 2;

    public static int KEY_VIEW = 1;
    public static int KEY_SEND = 2;
    public static int KEY_DELETE = 3;

    private ListView listView;
    private Button startNewTaskButton;
    private ArrayList<OfflineTask> datalist;
    private ArrayList<OfflineTask> unfinishdatalist;
    private ArrayList<OfflineTask> unsenddatalist;
    //private UnfinishTaskAdapter adapter;
    private UnfinishTaskSectionAdapter adapter;
    private UnfinishTaskAdapter unfinishTaskAdapter;
    private UnsendTaskAdapter unsendTaskAdapter;
    private String checkData ;
    private MaterialDialog materialDialog;

    private OnFragmentInteractionListener mListener;
    private Context mContext ;

    private Task taskSave;

    private TaskDataSource dataSource;
    private  AlertDialog alert;


    public static UnfinishTaskFragment newInstance(List<OfflineTask> datalist) {
        UnfinishTaskFragment fragment = new UnfinishTaskFragment();
        //Bundle args = new Bundle();
        //args.putParcelableArrayList(ARG_PARAM_DATA, (ArrayList<OfflineTask>)datalist);
        //fragment.setArguments(args);
        return fragment;
    }

    public UnfinishTaskFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {

        if (getArguments() != null) {
            checkData = getArguments().getString("fragment_noUnfinish");
            if(checkData != null){

                if(checkData.equals("123")){


                }
                else {

                }
                //  deleteTask("0");
            }
            Logger.Log("checkData",checkData);
        }
        else {

        }
        super.onResume();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    public void setmListener(OnFragmentInteractionListener mListener , Context context) {
        this.mListener = mListener;
        this.mContext = context ;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //datalist = getArguments().getParcelableArrayList(ARG_PARAM_DATA);
        }

        dataSource = new TaskDataSource(getActivity());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_unfinish_task, container, false);

        listView = (ListView) rootView.findViewById(R.id.listView);
        startNewTaskButton = (Button) rootView.findViewById(R.id.buttonWhite);

//        if(checkData.equals("123")){
//
//        }
//        else {
        //
//        }

        loadData(  );

        //   getOfflineTask();
        LinearLayout listFooterView = new LinearLayout(getActivity());
        AbsListView.LayoutParams paramsfooter = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Converter.dpTopx(getActivity(), 120));
        listFooterView.setGravity(Gravity.CENTER);
        listFooterView.setLayoutParams(paramsfooter);

        startNewTaskButton.setOnClickListener(this);


        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
  /*  public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/


    private void loadData( ) {
        new AsyncTask<Void, Void, Void>(){


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //     showIndeterminateProgressDialog(true);

                if(materialDialog == null) {
                    MaterialDialog.Builder builder = new MaterialDialog.Builder(UnfinishTaskFragment.this.getContext())
                            .title("แจ้งเตือน")
                            .content("กรุณารอซักครู่ค่ะ")
                            .progress(true, 0);

                    materialDialog = builder.build();
                    materialDialog.show();
                }


            }

            @Override
            protected Void doInBackground(Void... voids) {

                runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        // do some thing which you want in try block
                        try {
                            getOfflineTask();
                        } catch (Throwable e) {
                            e.printStackTrace();

                            //  intentChectSendEmail(e.toString());
                            //  intentChectSendEmail(nameId);
                        }
                    }
                });

                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);



                if(materialDialog.isShowing()){
                    materialDialog.dismiss();

                }

                // materialDialog.hide();
                //   materialDialog.hide();

                Logger.Log("pDialog.dismiss() News", "pDialog.dismiss() News");

            }
        }.execute();
    }

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
        public void onDisplayTask(OfflineTask task);
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
//            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
            //getActivity().getSupportFragmentManager().popBackStack();

            try{
                if(mListener != null) {
                    mListener.onDisplayTask(null);
                }

            }
            catch (Exception e){
                intentChectSendEmail(e.toString());
            }


            Logger.Log("onclick new ","onclick new");
        }else{

            Logger.Log("onclick new job","onclick new job");

            int key = (int)v.getTag(R.id.key);
            if(key == KEY_DELETE){

                OfflineTask task = (OfflineTask) v.getTag(R.id.value);
                buildAlertConfirmDelete(task);

            }else if(key == KEY_SEND){


                OfflineTask task = (OfflineTask) v.getTag(R.id.value);

                Intent intent = new Intent(getActivity(), ResultActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(ResultActivity.KEY_TASK, task.getTask());
                Logger.Log("task.getTask()", String.valueOf(task.getTask()));

                //bundle.putParcelable(ResultActivity.KEY_LOGIN_DATA, task.getLoginData());
                if(((ServiceActivity) mListener).getLoginData() != null){
                    bundle.putParcelable(ResultActivity.KEY_LOGIN_DATA,((ServiceActivity) mListener).getLoginData());
                }

                Logger.Log("(ServiceActivity) mListener).getLoginData()", String.valueOf(((ServiceActivity) mListener).getLoginData()));
                bundle.putString(ResultActivity.KEY_FROM, ResultActivity.class.getSimpleName());
                Logger.Log("ResultActivity.KEY_FROM",  ResultActivity.class.getSimpleName());


                intent.putExtras(bundle);
                getActivity().startActivity(intent);
                getActivity().finish();

            }else if(key == KEY_VIEW){
                OfflineTask task = (OfflineTask) v.getTag(R.id.value);
                mListener.onDisplayTask(task);
            }

        }
    }

    private void getOfflineTask(){


        datalist = (ArrayList<OfflineTask>) dataSource.getOfflineTasks(getContext());


        if(unsenddatalist == null){
            unsenddatalist = new ArrayList<OfflineTask>();
        }else{
            unsenddatalist.clear();
        }

        if(unfinishdatalist == null){
            unfinishdatalist = new ArrayList<OfflineTask>();
        }else{
            unfinishdatalist.clear();
        }

        for(OfflineTask t : datalist){

            if(t.getTask().getComplete()){
                unsenddatalist.add(t);
            }else{
                unfinishdatalist.add(t);
            }
        }

        if(datalist.size() > 0){


            setAdapter();


        }/*else{
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        }*/

        materialDialog.hide();
    }

    private void setAdapter() {
        if (adapter == null) {
            adapter = new UnfinishTaskSectionAdapter(getActivity(), R.layout.item_unfinish_header);
        }

        adapter.clearSection();

        if (unsendTaskAdapter == null) {
            unsendTaskAdapter = new UnsendTaskAdapter(getActivity(), unsenddatalist);
            unsendTaskAdapter.setOnClickListener(this);
        } else {
            unsendTaskAdapter.setData(unsenddatalist);
        }

        if (unfinishTaskAdapter == null) {
            unfinishTaskAdapter = new UnfinishTaskAdapter(getActivity(), unfinishdatalist);
            unfinishTaskAdapter.setOnClickListener(this);
        } else {
            unfinishTaskAdapter.setData(unfinishdatalist);
        }


        if(unsenddatalist.size() > 0){
            adapter.addSection(getString(R.string.unfinish_task_unsend), unsendTaskAdapter);
        }

        if(unfinishdatalist.size() > 0) {
            adapter.addSection(getString(R.string.unfinish_task), unfinishTaskAdapter);
        }

        listView.setAdapter(adapter);
        //listView.setOnItemClickListener(this);
    }

    private  void buildAlertConfirmDelete(final OfflineTask task)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getString(R.string.unfinish_task_confirm_delete))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.main_button_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dataSource.deleteTask(task.getTask().getTaskId());
                        //   getOfflineTask();
                        loadData();

                    }
                })
                .setNegativeButton(getString(R.string.main_button_no), null);

        alert = builder.create();
        alert.show();
    }


//    private void deleteTask(String taskId){
//
//        dataSource = new TaskDataSource(getContext());
//        dataSource.open();
//
//        dataSource.deleteTask(taskId);
//
//    }

    public void intentChectSendEmail(String Error) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"pongku71@gmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, " กรุณาระบุปัญหาของคุณด้วยค่ะ !!! ");
        i.putExtra(Intent.EXTRA_TEXT   ,Error);
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {

        }
    }

}
