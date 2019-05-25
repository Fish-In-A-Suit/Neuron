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
import android.widget.TextView;

import org.multiverse.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PasswordFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class PasswordFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private ImageView tick;
    private EditText passwordField;
    private TextView errorText;
    private Button register;

    public PasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        System.out.println("[Neuron.registration.FTGSmanager.PasswordFragment.onCreateView]: Creating the password tab!");
        //inflate layout for this fragment
        View v = inflater.inflate(R.layout.fragment_password_tab, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        tick = (ImageView) getView().findViewById(R.id.passwordTab_tick);
        tick.setVisibility(View.INVISIBLE);

        passwordField = (EditText) getView().findViewById(R.id.passwordTab_passwordField);
        errorText = (TextView) getView().findViewById(R.id.passwordTab_error);
        errorText.setTextColor(getResources().getColor(R.color.error));
        register = (Button) getView().findViewById(R.id.passwordTab_register);
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

    public EditText getPasswordField() {
        return passwordField;
    }

    public TextView getErrorText() {
        return errorText;
    }

    public ImageView getTickView() {
        return tick;
    }
}