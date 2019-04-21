package org.multiverse.registration.firstTimeGoogleSignup;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import org.multiverse.R;
import org.multiverse.multiversetools.valuekeepers.IntKeeper;
import org.multiverse.multiversetools.alternative.SpinnerWithTitle;


public class BirthdayTab extends Fragment {
    private SpinnerWithTitle month;
    private SpinnerWithTitle day;
    private SpinnerWithTitle year;

    private ImageView birthday_tick;
    private Button birthday_register;

    private OnFragmentInteractionListener mListener;

    public BirthdayTab() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_birthday_tab, container, false);
        // Inflate the layout for this fragment
        return view;
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        birthday_register = (Button) getView().findViewById(R.id.birthdayTab_register);
        birthday_tick = (ImageView) getView().findViewById(R.id.birthdayTab_tick);
        birthday_tick.setVisibility(View.INVISIBLE);

        //this adapter will be used to set the truncated month names in the month spinner with the specified layout.
        //truncatedMonths = new ArrayAdapter<CharSequence>(this.getContext(), R.layout.spinner_item_birthday_tab, (int) R.array.truncated_months);

        System.out.println("[Neuron.BirthdayTab.onViewCreated]: Finding month, day and year spinners!");
        month = (SpinnerWithTitle) view.findViewById(R.id.birthdayTab_month);
        day = (SpinnerWithTitle) view.findViewById(R.id.birthdayTab_day);
        year = (SpinnerWithTitle) view.findViewById(R.id.birthdayTab_year);

        System.out.println("[Neuron.BirthdayTab.onViewCreated]: month id: " + month + " | day id: " + day + " | year id: " + year);

        if(null!=month && null!=day && null!=year) {
            populateSpinners();
            configureSpinners();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("[Neuron.BirthdayTab]: Birthday fragment paused.");
    }

    @Override
    public void onStop() {
        super.onStop();
        System.out.println("[Neuron.BirthdayTab]: Birthday fragment stopped.");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        System.out.println("[Neuron.BirthdayTab]: Birthday fragment view destroyed.");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("[Neuron.BirthdayTab]: Birthday fragment destroyed.");
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

    private void populateSpinners() {
        System.out.println("[Neuron.BirthdayTab.populateSpinners]: Populating spinners.");
        month.setTitle("Month");
        //todo: change this layout to change how the selected spinner item is displayed
        month.setSpinnerItemLayoutId(R.layout.spinner_item_birthday_tab);
        month.setValues(this.getContext(), R.array.months);

        day.setTitle("Day");
        day.setSpinnerItemLayoutId(R.layout.spinner_item_birthday_tab);
        day.setValues(this.getContext(), R.array.days);

        year.setTitle("Year");
        year.setSpinnerItemLayoutId(R.layout.spinner_item_birthday_tab);
        year.setValues(this.getContext(), R.array.years);
    }

    private void configureSpinners() {
        System.out.println("[Neuron.BirthdayTab.configureSpinners]: Configuring spinners");

        //first, wait for the first selection. Then, when the first item is selected, restrict the month spinner textview to max 3 characters and remove the listener.
        final IntKeeper restriction = new IntKeeper(0);
        month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("[Neuron.BirthdayTab.configureSpinners]: Item with id " + id + " selected.");
                if(restriction.getValue() == 0) {
                    System.out.println("[Neuron.BirthdayTab.configureSpiners]: Changing the month spinner to only display three characters!");
                    //if this is the first item selected, add a max length to the textview instance of the month spinner and then delete the listener
                    month.setSpinnerItemLayoutId(R.layout.spinner_item_birthday_tab_shortened);
                    month.refresh(getContext());
                    month.setOnItemSelectedListener(null);
                    restriction.setValue(restriction.getValue()+1);
                } else {
                    System.out.println("[Neuron.BirthdayTab.configureSpiners]: aaaaaa!");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public ImageView getTickView() {
        return birthday_tick;
    }

    public Button getRegisterButton() {
        return birthday_register;
    }

    public Spinner getDaySpinner() {
        return day;
    }

    public Spinner getMonthSpinner() {
        return month;
    }

    public Spinner getYearSpinner() {
        return year;
    }
}
