package org.multiverse.multiversetools.alternative;

import android.content.Context;
import android.support.v7.widget.AppCompatSpinner;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import org.multiverse.multiversetools.GeneralTools;
import org.multiverse.multiversetools.R;
import org.multiverse.utilities.ArrayUtilities;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import stringUtilities.StringUtilities;

/**
 * A spinner with a title which is displayed as a default value inside the spinner. This title is displayed by modifying
 * the spinner so that it doesn't automatically select the first entry in the list. Therefore, it shows the prompt if nothing is
 * selected. Limitations: it doesn't display the prompt if the entry list is empty
 *
 * source: https://stackoverflow.com/questions/867518/how-to-make-an-android-spinner-with-initial-text-select-one
 */
public class SpinnerWithTitle extends AppCompatSpinner {
    private String title;
    private ArrayList<String> values;
    private int spinnerItemLayoutId;

    //this constructor has to be present, so the class can be inflated!
    public SpinnerWithTitle(Context context, AttributeSet attrs) {
        super(context, attrs);

        values = StringUtilities.stringToArrayList(context.getResources().getStringArray(R.array.years));
        System.out.println("[Neuron.MT.alternative.SpinnerWithTitle]: Length of values " + values.size());
    }

    public void setTitle(String title) {
        this.title = title;
        this.setPrompt(title);
        System.out.println("[Neuron.MT.alternative.SpinnerWithTitle]: Set title " + title + " to spinner. Length of values is now " + values.size());
    }

    /**
     * Sets the id which will be used instead R.layout.android.R.layout.simple_spinner_item to display spinner items.
     * This method has to be set prior to calling setValues!
     * @param spinnerItemLayoutId
     */
    public void setSpinnerItemLayoutId(int spinnerItemLayoutId) {
        this.spinnerItemLayoutId = spinnerItemLayoutId;
    }

    public String getTitle() {
        return title;
    }

    /**
     * Adds values from the specified arrayId location (usually R.array._____) to this spinner
     * @param c
     * @param arrayId
     */
    public void setValues(Context c, int arrayId) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(c, arrayId, spinnerItemLayoutId);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.setAdapter(adapter);

        values = ArrayUtilities.mergeLists(values, GeneralTools.getArrayFromResources(c, arrayId));
    }

    //todo: override when a value (or values) are added to the spinner again ... update ArrayList<String> values as well!

    @Override
    public void setAdapter(SpinnerAdapter originalAdapter) {
        final SpinnerAdapter adapter = newProxy(originalAdapter);

        super.setAdapter(adapter);

        try {
            final Method m = AdapterView.class.getDeclaredMethod("setNextSelectedPositionInt", int.class);
            m.setAccessible(true);
            m.invoke(this, -1);

            final Method n = AdapterView.class.getDeclaredMethod("setSelectedPositionInt", int.class);
            n.setAccessible(true);
            n.invoke(this, -1);
        } catch (Exception e) {
            System.err.println("[Neuron.MT.alternative.SpinnerWithTitle.SpinnerAdapterProxy]: Error setting adapter!");
            throw new RuntimeException(e);
        }
    }

    protected SpinnerAdapter newProxy(SpinnerAdapter sp) {
        return (SpinnerAdapter) java.lang.reflect.Proxy.newProxyInstance(sp.getClass().getClassLoader(), new Class[]{SpinnerAdapter.class}, new SpinnerAdapterProxy(sp));
    }

    protected class SpinnerAdapterProxy implements InvocationHandler {
        protected SpinnerAdapter sp;
        protected Method getView;

        protected SpinnerAdapterProxy(SpinnerAdapter sp) {
            this.sp = sp;

            try {
                this.getView = SpinnerAdapter.class.getMethod("getView", int.class, View.class, ViewGroup.class);
            } catch (Exception e) {
                System.err.println("[Neuron.MT.alternative.SpinnerWithTitle.SpinnerAdapterProxy]: Error querying method getView from SpinnerAdapter");
                throw new RuntimeException(e);
            }
        }

        public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
            try {
                return m.equals(getView) && (Integer)(args[0])<0 ? getView((Integer)args[0],(View)args[1],(ViewGroup)args[2]) : m.invoke(sp, args);
            } catch (InvocationTargetException e) {
                System.err.println("[Neuron.MT.alternative.SpinnerWithTitle.SpinnerAdapterProxy]: Error inside the invoke method!");
                throw e.getTargetException();
            } catch (Exception e) {
                System.out.println("[Neuron.MT.alternative.SpinnerWithTitle.SpinnerAdapterProxy]: Error inside the invoke method!");
                throw new RuntimeException(e);
            }
        }

        protected View getView(int position, View convertView, ViewGroup parent) throws IllegalAccessException {
            if(position < 0) {
                //change the first argument of inflate method (spinnerItemLayoutId) to determine the layout used for the prompt
                final TextView v = (TextView) ((LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(spinnerItemLayoutId, parent, false);
                v.setText(getPrompt());
                return v;
            }
            return sp.getView(position, convertView, parent);
        }
    }


}
