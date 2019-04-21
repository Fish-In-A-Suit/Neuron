package org.multiverse.registration.firstTimeGoogleSignup;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import org.multiverse.R;
import org.multiverse.multiversetools.ButtonGroup;

public class SexTab extends Fragment {

    private OnFragmentInteractionListener mListener;

    private RadioButton sex_male;
    private RadioButton sex_female;

    private Button sex_register;

    private ImageView sex_tick;

    public SexTab() {
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
        View v = inflater.inflate(R.layout.fragment_sex_tab, container, false);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        sex_male = (RadioButton) getView().findViewById(R.id.sexTab_male);
        sex_female = (RadioButton) getView().findViewById(R.id.sexTab_female);
        sex_register = (Button) getView().findViewById(R.id.sexTab_register);
        sex_tick = (ImageView) getView().findViewById(R.id.sexTab_tick);
        sex_tick.setVisibility(View.INVISIBLE);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("[Neuron.SexTab]: Pausing sex fragment!");
    }

    @Override
    public void onStop() {
        super.onStop();
        System.out.println("[Neuron.SexTab]: Sex fragment is stopped!");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        System.out.println("[Neuron.SexTab]: Sex fragment view destroyed.");
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public ImageView getTickView() {
        return sex_tick;
    }

    public Button getRegisterButton() {
        return sex_register;
    }

    public RadioButton getMaleButton() {
        return sex_male;
    }

    public RadioButton getFemaleButton() {
        return sex_female;
    }
}
