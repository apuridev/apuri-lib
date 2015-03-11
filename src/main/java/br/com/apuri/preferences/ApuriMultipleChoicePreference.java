/*
 * Copyright  2015 apuri Developers
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.com.apuri.preferences;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;

import java.util.Map;

import br.com.apuri.utils.ApuriJavaUtils;

/**
 * Preference dialog that provided multiple choice options with checkbox.
 * The selection options are stored in a string with the format "value1, value2, value3..."
 */
public class ApuriMultipleChoicePreference extends DialogPreference {
    
    private Map<String,Object> entries;

    private boolean[] checkedItems;
    private String[] keys;
    private String[] values;
    private String[] currentValues;
    public ApuriMultipleChoicePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);
    }

    /**
     * Set the possible choices for this preference.
     *
     * @param entries A map were the keys are the label to be shown in the dialog and the values are the
     *                values that will be stored, AS STRINGS, in this preference
     *
     */
    public void setEntries(Map<String,Object> entries){
        this.entries = entries;
        keys = entries.keySet().toArray(new String[0]);
        checkedItems = new boolean[entries.values().size()];
    }
    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        CharSequence[] items = new CharSequence[0];
        if(currentValues != null && currentValues.length > 0){
            checkItemsForValues(currentValues);
        }
        builder.setMultiChoiceItems(entries.keySet().toArray(items), checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                checkedItems[which] = isChecked;
            }
        });
        super.onPrepareDialogBuilder(builder);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if(positiveResult){
            String values = getSelectedValuesAsString();
            persistString(getSelectedValuesAsString());
            currentValues = values.split(", ");
        }
    }

    private String getSelectedValuesAsString() {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < checkedItems.length; i++){
            if(checkedItems[i]) {
                if(stringBuilder.length() == 0)
                    stringBuilder.append(entries.get(keys[i]).toString());
                else
                    stringBuilder.append(", ").append(entries.get(keys[i]).toString());
            }
        }
        return stringBuilder.toString();
    }

    /**
     *
     * @return Return the selected values for this preference as an array of {@link java.lang.String}
     */
    public String[] getSelectedValues(){
        return currentValues;
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedString, Object defaultString) {
        if(restorePersistedString){
            String values = getPersistedString("");
            if(!ApuriJavaUtils.isEmptyString(values)) {
                this.currentValues = values.split(", ");
                Log.d("MultipleChoicePreferenc","current values: "+values);
            }
            this.setSummary(values);
        }else
            this.persistString(defaultString.toString());
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return "";
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        // Check whether this Preference is persistent (continually saved)
        if (isPersistent()) {
            // No need to save instance state since it's persistent,
            // use superclass state
            return superState;
        }

        // Create instance of custom BaseSavedState
        final SavedState myState = new SavedState(superState);
        // Set the state's value with the class member that holds current
        // setting value
        myState.value = getSelectedValuesAsString();
        return myState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        // Check whether we saved the state in onSaveInstanceState
        if (state == null || !state.getClass().equals(SavedState.class)) {
            // Didn't save the state, so call superclass
            super.onRestoreInstanceState(state);
            return;
        }

        // Cast state to custom BaseSavedState and pass to superclass
        SavedState myState = (SavedState) state;
        super.onRestoreInstanceState(myState.getSuperState());

        String[] selectedKeys = myState.value.split(", ");
        checkItemsForValues(selectedKeys);

    }

    private void checkItemsForValues(String[] selectedValues){
        String value = null;
        for(int i = 0; i < keys.length; i++){
            value = entries.get(keys[i]).toString();
            for(int j = 0; j < selectedValues.length; j++)
                if(selectedValues[j].equals(value))
                    checkedItems[i] = true;
        }
    }

    private static class SavedState extends BaseSavedState {
        // Member that holds the setting's value
        // Change this data type to match the type saved by your Preference
        String value;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        public SavedState(Parcel source) {
            super(source);
            // Get the current preference's value
            value = source.readString();  // Change this to read the appropriate data type
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            // Write the preference's value
            dest.writeString(value);  // Change this to write the appropriate data type
        }

        // Standard creator object using an instance of this class
        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {

                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }


}
