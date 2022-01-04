package fr.liste_wanted.ui.allos;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AllosViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AllosViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is allos fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}