package th.co.siamkubota.kubota.fragment;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.joooonho.SelectableRoundedImageView;

import java.io.File;
import java.util.Date;

import io.swagger.client.model.Image;
import io.swagger.client.model.Signature;
import th.co.siamkubota.kubota.R;
import th.co.siamkubota.kubota.activity.SignaturePadActivity;
import th.co.siamkubota.kubota.app.AppController;
import th.co.siamkubota.kubota.app.Config;
import th.co.siamkubota.kubota.model.Photo;
import th.co.siamkubota.kubota.utils.function.Converter;
import th.co.siamkubota.kubota.utils.function.ImageFile;
import th.co.siamkubota.kubota.utils.function.Ui;
import th.co.siamkubota.kubota.utils.function.Validate;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Step3SignFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Step3SignFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Step3SignFragment extends Fragment implements
        View.OnClickListener,
        CompoundButton.OnCheckedChangeListener{

    //private static final String ARG_PARAM_TITLE = "title";
    private static final String KEY_SIGNATURE = "signature";


    private LinearLayout rootLayout;
    private SelectableRoundedImageView imageCustomerSignature;
    private SelectableRoundedImageView imageTechnicianSignature;
    private EditText editTextTotalCost;
    private EditText editTextCustomerSignDate;
    private EditText editTextTechnicianSignDate;
    private EditText editTextCustomerName;
    private EditText editTextTechnicianName;
    private EditText editTextRemark;
    private LinearLayout signatureCustomerHintLayout;
    private LinearLayout signatureTechnicianHintLayout;
    private CheckBox checkBoxUserAccept;
    private CheckBox checkBoxTecnicianAccept;

    private String title;
    private Photo imageCustomer;
    private Photo imageTechnician;
    private boolean dataComplete = false;
    private Signature signature;
    private String customerName;


    private OnFragmentInteractionListener mListener;


    //////////////////////////////////////////////////////////////////// getter setter

    public OnFragmentInteractionListener getmListener() {
        return mListener;
    }

    public void setmListener(OnFragmentInteractionListener mListener) {
        this.mListener = mListener;
    }

    public boolean isDataComplete() {
        return dataComplete;
    }

    public void setDataComplete(boolean dataComplete) {
        this.dataComplete = dataComplete;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    //////////////////////////////////////////////////////////////////// constructor



    public static Step3SignFragment newInstance(Signature signature) {
        Step3SignFragment fragment = new Step3SignFragment();
        Bundle args = new Bundle();
        args.putParcelable(KEY_SIGNATURE, signature);
        fragment.setArguments(args);
        return fragment;
    }

    public Step3SignFragment() {
        // Required empty public constructor
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putParcelable(KEY_SIGNATURE, signature);
        //mListener.onFragmentSaveInstanceState(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState != null){

            //signature = savedInstanceState.getParcelable(KEY_SIGNATURE);

        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null){
            if (getArguments() != null) {
                signature = getArguments().getParcelable(KEY_SIGNATURE);
            }

            if(imageCustomer == null){
                imageCustomer = new Photo(5,getString(R.string.sign_pad_customer_signature));
            }

            if(imageTechnician == null){
                imageTechnician = new Photo(6,getString(R.string.sign_pad_technician_signature));
            }
        }

    }


    @Override
    public void onResume() {
        super.onResume();

        if(signature != null && signature.getCustomerName() != null && !signature.getCustomerName().isEmpty()){
            editTextCustomerName.setText(signature.getCustomerName());
        }else if(signature == null ||  signature.getCustomerName() == null || signature.getCustomerName().isEmpty() ){
            editTextCustomerName.setText(customerName);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_step3_sign, container, false);

        return v;
    }


    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {

        if(savedInstanceState == null){

            rootLayout = (LinearLayout) v.findViewById(R.id.rootLayout);
            imageCustomerSignature = (SelectableRoundedImageView) v.findViewById(R.id.imageCustomerSignature);
            imageTechnicianSignature = (SelectableRoundedImageView) v.findViewById(R.id.imageTechnicianSignature);
            editTextTotalCost = (EditText) v.findViewById(R.id.editTextTotalCost);
            editTextCustomerSignDate = (EditText) v.findViewById(R.id.editTextCustomerSignDate);
            editTextTechnicianSignDate = (EditText) v.findViewById(R.id.editTextTechnicianSignDate);
            editTextCustomerName = (EditText) v.findViewById(R.id.editTextCustomerName);
            editTextTechnicianName = (EditText) v.findViewById(R.id.editTextTechnicianName);
            editTextRemark = (EditText) v.findViewById(R.id.editTextRemark);
            signatureCustomerHintLayout = (LinearLayout) v.findViewById(R.id.signatureCustomerHintLayout);
            signatureTechnicianHintLayout = (LinearLayout) v.findViewById(R.id.signatureTechnicianHintLayout);
            checkBoxUserAccept = (CheckBox) v.findViewById(R.id.checkBoxUserAccept);
            checkBoxTecnicianAccept = (CheckBox) v.findViewById(R.id.checkBoxTecnicianAccept);


            if(imageCustomer != null && imageCustomer.getPath() != null && !imageCustomer.getPath().isEmpty()){
                imageCustomerSignature.setImageURI(Uri.fromFile(new File(imageCustomer.getPath())));
                editTextCustomerSignDate.setText(Converter.DateToString(imageCustomer.getDate(),"dd/MM/yyyy"));
                signatureCustomerHintLayout.setVisibility(View.GONE);
            }

            if(imageTechnician != null && imageTechnician.getPath() != null && !imageTechnician.getPath().isEmpty()){
                imageTechnicianSignature.setImageURI(Uri.fromFile(new File(imageTechnician.getPath())));
                editTextTechnicianSignDate.setText(Converter.DateToString(imageTechnician.getDate(),"dd/MM/yyyy"));
                signatureTechnicianHintLayout.setVisibility(View.GONE);
            }

            Ui.setupUI(getActivity(), rootLayout);

        }

        setData();
        setDataChangeListener();
        validateInput();


    }

    private void setDataChangeListener(){

        imageCustomerSignature.setOnClickListener(this);
        imageTechnicianSignature.setOnClickListener(this);

        editTextTotalCost.addTextChangedListener(new GenericTextWatcher(editTextTotalCost));
        editTextCustomerSignDate.addTextChangedListener(new GenericTextWatcher(editTextCustomerSignDate));
        editTextTechnicianSignDate.addTextChangedListener(new GenericTextWatcher(editTextTechnicianSignDate));
        editTextCustomerName.addTextChangedListener(new GenericTextWatcher(editTextCustomerName));
        editTextTechnicianName.addTextChangedListener(new GenericTextWatcher(editTextTechnicianName));
        editTextRemark.addTextChangedListener(new GenericTextWatcher(editTextRemark));

        checkBoxUserAccept.setOnCheckedChangeListener(this);
        checkBoxTecnicianAccept.setOnCheckedChangeListener(this);
    }


    private void setData() {

        signatureCustomerHintLayout.setVisibility(View.GONE);
        signatureTechnicianHintLayout.setVisibility(View.GONE);

        if(signature != null && signature.getTotalCost() != null ){
            editTextTotalCost.setText(signature.getTotalCost());
        }

        if(signature != null && signature.getRemark() != null ){
            editTextRemark.setText(signature.getRemark());
        }


        if(signature != null && signature.getCustomerSignatureImage() != null && signature.getCustomerSignatureImage().getImagePath() != null){
            imageCustomer.setPath(signature.getCustomerSignatureImage().getImagePath());
            imageCustomer.setDate(signature.getCustomerSignatureImage().getCapturedAt());
            imageCustomer.setComplete(true);
        }else{
            signatureCustomerHintLayout.setVisibility(View.VISIBLE);
        }

        if(signature != null && signature.getEngineerSignatureImage() != null && signature.getEngineerSignatureImage().getImagePath() != null){
            imageTechnician.setPath(signature.getEngineerSignatureImage().getImagePath());
            imageTechnician.setDate(signature.getEngineerSignatureImage().getCapturedAt());
            imageTechnician.setComplete(true);
        }else{
            signatureTechnicianHintLayout.setVisibility(View.VISIBLE);
        }

        setImage(imageCustomerSignature,imageCustomer );
        setImage(imageTechnicianSignature, imageTechnician);

        if(signature != null && signature.getCustomerName() != null && !signature.getCustomerName().isEmpty()){
            editTextCustomerName.setText(signature.getCustomerName());
        }else if(signature == null ||  signature.getCustomerName() == null || signature.getCustomerName().isEmpty() ){
            editTextCustomerName.setText(customerName);
        }


        if(signature != null && signature.getCustomerSignedDate() != null){
            editTextCustomerSignDate.setText(Converter.DateToString(signature.getCustomerSignedDate(), "dd/MM/yyyy"));
        }

        if(signature != null && signature.getEngineerName() != null){
            editTextTechnicianName.setText(signature.getEngineerName());
        }


        if(signature != null && signature.getEngineerSignedDate() != null){
            editTextTechnicianSignDate.setText(Converter.DateToString(signature.getEngineerSignedDate(), "dd/MM/yyyy"));
        }

        if(signature != null){
            checkBoxUserAccept.setChecked(signature.getCustomerAccept());
            checkBoxTecnicianAccept.setChecked(signature.getEngineerAccept());
        }

        setDefault();

        validateInput();

    }

    private void setImage(final SelectableRoundedImageView imageView, Photo data){

        imageView.setVisibility(View.VISIBLE);

        if(data.getPath() != null && !data.getPath().isEmpty()){

            imageView.setImageURI(Uri.fromFile(new File(data.getPath())));

        }else if(data.getServerPath() != null && !data.getServerPath().isEmpty()){

            ImageLoader imageLoader = AppController.getInstance().getImageLoader();

            String imagePath = Config.mediaService + data.getServerPath();

            imageLoader.get(imagePath, new ImageLoader.ImageListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    //Log.e("### ", "Image Load Error: " + error.getMessage());
                    //imageView.setImageResource(R.drawable.demo_logo_product);
                }

                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                    if (response.getBitmap() != null) {
                        // load image into imageview
                        imageView.setImageBitmap(response.getBitmap());
                    }else{
                        //imageView.setImageResource(R.drawable.demo_logo_product);
                    }
                }
            });


        }

    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
           // mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //mListener.onFragmentPresent(this, title);
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
        //mListener = null;
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
        //public void onFragmentPresent(Fragment fragment, String title);
        //public void onInvokeSignPad();
        public void onFragmentDataComplete(Fragment fragment, boolean complete, Object data);
        public void onFragmentSaveInstanceState(Fragment fragment);
    }


    /////////////////////////////////////////////////////////////////////// implement method

    @Override
    public void onClick(View v) {

        if(v == imageCustomerSignature || v == imageTechnicianSignature ){
            //mListener.onInvokeSignPad();
            int requestCode = 0;
            Bundle bundle = new Bundle();

            if(v == imageCustomerSignature){

              /*  if(imageCustomer == null){
                    imageCustomer = new Photo(5,getString(R.string.sign_pad_customer_signature));
                }*/

                requestCode = imageCustomer.getId();

                if(imageCustomer != null && imageCustomer.getPath() != null && !imageCustomer.getPath().isEmpty()){
                    bundle.putString(SignaturePadActivity.KEY_SIGNATURE_IMAGE_PATH, imageCustomer.getPath());
                }
                bundle.putString(SignaturePadActivity.KEY_TITLE, imageCustomer.getTitle());

            }else if(v == imageTechnicianSignature){
                //requestCode = 1;
/*
                if(imageTechnician == null){
                    imageTechnician = new Photo(6,getString(R.string.sign_pad_technician_signature));
                }*/

                requestCode = imageTechnician.getId();

                if(imageTechnician != null && imageTechnician.getPath() != null && !imageTechnician.getPath().isEmpty()){
                    bundle.putString(SignaturePadActivity.KEY_SIGNATURE_IMAGE_PATH, imageTechnician.getPath());
                }
                bundle.putString(SignaturePadActivity.KEY_TITLE, imageTechnician.getTitle());
            }


            Intent intent = new Intent(getActivity(), SignaturePadActivity.class);
            intent.putExtras(bundle);
            //startActivityForResult(intent, requestCode);
            getActivity().startActivityForResult(intent, requestCode);
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(buttonView == checkBoxUserAccept){

        }else if(buttonView == checkBoxTecnicianAccept){

        }

        validateInput();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK){

            Bundle bundle = data.getExtras();

            String imagePath = bundle.getString("imagePath");
            String dateInfo = bundle.getString("takenDate");
            //imageView.setTag(R.id.imagePath, imagePath);

            if(imageCustomer != null && requestCode == imageCustomer.getId()){

                if(imageCustomer.getPath() != null && !imageCustomer.getPath().isEmpty()){
                    ImageFile.deleteFile(imageCustomer.getPath());
                }

                imageCustomer.setPath(imagePath);
                //imageCustomer.setDate(Converter.StringToDate(dateInfo, "dd/MM/yyyy"));
                imageCustomer.setDate(new Date());
                imageCustomerSignature.setImageURI(Uri.fromFile(new File(imageCustomer.getPath())));
                //imageView.setVisibility(View.VISIBLE);
                editTextCustomerSignDate.setText(Converter.DateToString(imageCustomer.getDate(), "dd/MM/yyyy" ));
                signatureCustomerHintLayout.setVisibility(View.GONE);

                //signature.setCustomerSignedDate(new Date());

                imageCustomer.setComplete(true);
                validateInput();

            }else if(imageTechnician != null && requestCode == imageTechnician.getId()){

                if(imageTechnician.getPath() != null && !imageTechnician.getPath().isEmpty()){
                    ImageFile.deleteFile(imageTechnician.getPath());
                }

                imageTechnician.setPath(imagePath);
                //imageTechnician.setDate(Converter.StringToDate(dateInfo, "dd/MM/yyyy"));
                imageTechnician.setDate(new Date());
                imageTechnicianSignature.setImageURI(Uri.fromFile(new File(imageTechnician.getPath())));
                //imageView.setVisibility(View.VISIBLE);
                editTextTechnicianSignDate.setText(Converter.DateToString(imageTechnician.getDate(), "dd/MM/yyyy"));
                signatureTechnicianHintLayout.setVisibility(View.GONE);

                //signature.setEngineerSignedDate(new Date());

                imageTechnician.setComplete(true);
                validateInput();
            }

        }
    }


    /////////////////////////////////////////////////////////// TextWatcher

    private class GenericTextWatcher implements TextWatcher {

        private View view;
        private GenericTextWatcher(View view) {
            this.view = view;
        }

        public View getView() {
            return view;
        }

        public void setView(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        public void afterTextChanged(Editable editable) {

            String text = editable.toString();
            //save the value for the given tag :

            if(!text.isEmpty() && view != null){


            }

            validateInput();
        }
    }


    private void validateInput(){

        if((imageCustomer == null || (imageCustomer != null && !imageCustomer.isComplete())) ||
                (imageTechnician == null || (imageTechnician != null && !imageTechnician.isComplete())) ){
            dataComplete = false;
            mListener.onFragmentDataComplete(this, dataComplete, collectData());
            return;
        }

        View view = Validate.inputValidate(rootLayout, "required");
        if(view != null ){
            dataComplete = false;
            mListener.onFragmentDataComplete(this, dataComplete, collectData());
            return;
        }

        dataComplete = true;
        mListener.onFragmentDataComplete(this, dataComplete, collectData());
    }

    private Signature collectData(){

        Image imgCus = new Image();
        imgCus.setImagePath(imageCustomer.getPath());
        imgCus.setCapturedAt(imageCustomer.getDate());

        Image imgEngineer = new Image();
        imgEngineer.setImagePath(imageTechnician.getPath());
        imgEngineer.setCapturedAt(imageTechnician.getDate());

        signature.setCustomerSignatureImage(imgCus);
        signature.setEngineerSignatureImage(imgEngineer);

        if(editTextCustomerName.getText() != null){
            signature.setCustomerName(editTextCustomerName.getText().toString());
        }
        if(imageCustomer.getDate() != null){
            signature.setCustomerSignedDate(imageCustomer.getDate());
        }

        if(editTextTechnicianName.getText() != null){
            signature.setEngineerName(editTextTechnicianName.getText().toString());
        }
        if(imageTechnician.getDate() != null){
            signature.setEngineerSignedDate(imageTechnician.getDate());
        }

        signature.setCustomerAccept(checkBoxUserAccept.isChecked());
        signature.setEngineerAccept(checkBoxTecnicianAccept.isChecked());

        if(editTextTotalCost.getText() != null){
            signature.setTotalCost(editTextTotalCost.getText().toString());
        }

        if(editTextRemark.getText() != null){
            signature.setRemark(editTextRemark.getText().toString());
        }


        return signature;
    }

    private void setDefault(){

        if(Config.showDefault == true){
            signatureCustomerHintLayout.setVisibility(View.GONE);
            signatureTechnicianHintLayout.setVisibility(View.GONE);

            editTextTotalCost.setText("500");
            editTextRemark.setText("รออะไหล่");

            Uri path1 = ResourceToUri(getActivity(), R.drawable.signature1);//Uri.parse("android.resource://th.co.siamkubota.kubota/" + R.drawable.signature1);
            //String pathStr1 = path1.toString();
            String pathStr1 = ImageFile.createMockupImage(getActivity(), "signature1.jpg", R.drawable.signature1);

            imageCustomer.setPath(pathStr1);
            imageCustomer.setDate(new Date());
            imageCustomer.setComplete(true);


            //Uri path2 = ResourceToUri(getActivity(), R.drawable.signature2);//Uri.parse("android.resource://th.co.siamkubota.kubota/" + R.drawable.signature2);
            String pathStr2 = ImageFile.createMockupImage(getActivity(), "signature2.jpg", R.drawable.signature2);

            imageTechnician.setPath(pathStr2);
            imageTechnician.setDate(new Date());
            imageTechnician.setComplete(true);

            setImage(imageCustomerSignature, imageCustomer);
            setImage(imageTechnicianSignature, imageTechnician);

            editTextCustomerName.setText(customerName);

            editTextCustomerSignDate.setText(Converter.DateToString(new Date(), "dd/MM/yyyy"));
            editTextCustomerSignDate.setText(Converter.DateToString(new Date(), "dd/MM/yyyy"));
            editTextCustomerName.setText("สมชาย ทำไร่ไถนา");
            editTextTechnicianName.setText("ช่าง ชำนาญ");


            editTextTechnicianSignDate.setText(Converter.DateToString(new Date(), "dd/MM/yyyy"));

            checkBoxUserAccept.setChecked(true);
            checkBoxTecnicianAccept.setChecked(true);

        }
    }

    public static Uri ResourceToUri (Context context,int resID) {
        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                context.getResources().getResourcePackageName(resID) + '/' +
                context.getResources().getResourceTypeName(resID) + '/' +
                context.getResources().getResourceEntryName(resID) );
    }
}
