package fr.liste_wanted.ui.defis;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import fr.liste_wanted.R;
import fr.liste_wanted.data.Defis;

public class SendDefiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_defi);

        EditText inputAuthor = findViewById(R.id.input_author);
        EditText inputTask = findViewById(R.id.input_task);

        inputAuthor.requestFocus();

        ((Button)findViewById(R.id.button_send)).setOnClickListener((event) -> {
            String author = inputAuthor.getText().toString().trim();
            if (author.equals("")) {
                inputAuthor.setError(getResources().getString(R.string.who));
                return;
            }
            String task = inputTask.getText().toString().trim();
            if (task.equals("")) {
                inputTask.setError(getResources().getString(R.string.what_defi));
                return;
            }
            Defis.sendNew(author, task, () -> runOnUiThread(() -> {
                    Toast.makeText(getApplicationContext(), R.string.defi_sent, Toast.LENGTH_SHORT).show();
                    finish();
            }), ioe -> runOnUiThread(() ->
                Toast.makeText(getApplicationContext(), R.string.connection_error, Toast.LENGTH_SHORT).show()
            ), se -> runOnUiThread(() ->
                Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_SHORT).show()
            ));
        });

    }
}