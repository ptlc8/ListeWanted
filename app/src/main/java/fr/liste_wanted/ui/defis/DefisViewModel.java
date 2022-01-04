package fr.liste_wanted.ui.defis;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DefisViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public DefisViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is defis fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}