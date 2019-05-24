package hjcdg1.snuhabitat.dutchpaycalculator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

public class MainActivity extends AppCompatActivity {

    RadioButton radioButton1;
    RadioButton radioButton2;
    Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        radioButton1 = (RadioButton)findViewById(R.id.radioButton1);
        radioButton2 = (RadioButton)findViewById(R.id.radioButton2);
        nextButton = (Button)findViewById(R.id.nextButton);
    }

    public void onNextButtonClicked(View v) {
        if(radioButton1.isChecked()) {
            Intent intent1 = new Intent(getApplicationContext(), Calculate1Activity.class);
            startActivity(intent1);
        }
        else if(radioButton2.isChecked()) {
            Intent intent2 = new Intent(getApplicationContext(), Calculate2Activity.class);
            startActivity(intent2);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
