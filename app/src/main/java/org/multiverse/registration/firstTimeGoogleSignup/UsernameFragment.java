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
import org.w3c.dom.Text;

/**
 * This is the code responsible for the "username fragment" during the additional user info collection for first-time users signing in with google.
 */
public class UsernameFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private EditText usernameText;
    private TextView error;
    private Button username_register;
    private ImageView username_tick;

    public UsernameFragment() {
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
        return inflater.inflate(R.layout.fragment_username_tab, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        System.out.println("[Neuron.UsernameFragment.onViewCreated]: Defining the username edit text and error text view for tab " + this);
        usernameText = (EditText) getView().findViewById(R.id.usernameTab_username);
        error = (TextView) getView().findViewById(R.id.usernameTab_error);
        username_register = (Button) getView().findViewById(R.id.usernameTab_register);
        username_tick = (ImageView) getView().findViewById(R.id.usernameTab_tick);
        username_tick.setVisibility(View.INVISIBLE);

        System.out.println("[Neuron.UsernameFragment.onViewCreated]: usernameText = " + usernameText + " | username error reporting view = " + error);
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
        System.out.println("[Neuron.UsernameFragment]: Username fragment is paused.");
    }

    @Override
    public void onStop() {
        super.onStop();
        System.out.println("[Neuron.UsernameFragment]: Username fragment is stopped.");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        System.out.println("[Neuron.UsernameFragment]: Username fragment view destroyed.");
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
        return username_tick;
    }

    public EditText getUsernameEditText() {
        return usernameText;
    }

    public TextView getErrorTextView() {
        return error;
    }

    public Button getRegisterButton() {
        return username_register;
    }
}
