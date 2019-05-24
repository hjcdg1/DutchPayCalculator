package hjcdg1.snuhabitat.dutchpaycalculator;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Stack;

public class Calculate2Activity extends AppCompatActivity {
    ArrayList<LinearLayout> entry;
    ArrayList<EditText> name;
    ArrayList<EditText> payMoney;
    ArrayList<EditText> weight;
    ArrayList<Person> list;
    Stack<LinearLayout> lastEntry;
    ConstraintLayout inputFrame, resultFrame;
    LinearLayout mainLayout, mainLayout2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate2);

        list = new ArrayList<Person>(2);
        entry = new ArrayList<LinearLayout>(2);
        name = new ArrayList<EditText>(2);
        payMoney = new ArrayList<EditText>(2);
        weight = new ArrayList<EditText>(2);
        lastEntry = new Stack<LinearLayout>();

        inputFrame = (ConstraintLayout)findViewById(R.id.inputFrame);
        resultFrame = (ConstraintLayout)findViewById(R.id.resultFrame);

        mainLayout = (LinearLayout)findViewById(R.id.mainLayout);
        mainLayout2 = (LinearLayout)findViewById(R.id.mainLayout2);

        entry.add((LinearLayout)findViewById(R.id.entry1));
        name.add((EditText)findViewById(R.id.name1));
        payMoney.add((EditText)findViewById(R.id.payMoney1));
        weight.add((EditText)findViewById(R.id.weight1));
        list.add(new Person(name.get(0), payMoney.get(0), weight.get(0)));
        lastEntry.add(entry.get(0));

        entry.add((LinearLayout)findViewById(R.id.entry2));
        name.add((EditText)findViewById(R.id.name2));
        payMoney.add((EditText)findViewById(R.id.payMoney2));
        weight.add((EditText)findViewById(R.id.weight2));
        list.add(new Person(name.get(1), payMoney.get(1), weight.get(1)));
        lastEntry.add(entry.get(1));
    }

    public void onAddButtonClicked(View v) {
        LinearLayout e = new LinearLayout(this);
        EditText n = new EditText(this);
        EditText p = new EditText(this);
        EditText w = new EditText(this);

        /** 리니어 레이아웃 **/
        e.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        e.setOrientation(LinearLayout.HORIZONTAL);

        /** 가로, 세로 길이 **/
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        n.setLayoutParams(params);
        p.setLayoutParams(params);
        w.setLayoutParams(params);

        /** 힌트, 텍스트 **/
        n.setHint("참석자 이름");
        p.setHint("부담한 금액 (원)");
        w.setHint("weight");

        /** 텍스트 크기 **/
        n.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        p.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        w.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);

        /** 뷰 추가 **/
        e.addView(n);
        e.addView(p);
        e.addView(w);
        mainLayout.addView(e);

        /** 리스트에 객체 추가 **/
        entry.add(e);
        name.add(n);
        payMoney.add(p);
        weight.add(w);
        list.add(new Person(n, p, w));
        lastEntry.add(e);
    }

    public void onDeleteButtonClicked(View v) {
        if(list.size() <= 1)
            Toast.makeText(getApplicationContext(), "최소한 한 명은 있어야 합니다.", Toast.LENGTH_LONG).show();
        else {
            int size = list.size();
            entry.remove(size - 1);
            name.remove(size - 1);
            payMoney.remove(size - 1);
            weight.remove(size - 1);
            list.remove(size - 1);
            mainLayout.removeView(lastEntry.pop());
        }
    }

    public void onCalculateButtonClicked(View v) {
        if(checkAllEditTexts()) {
            if(checkAllFormats()) {
                calculateChargeMoney();
                constructResultFrame();
                inputFrame.setVisibility(View.INVISIBLE);
                resultFrame.setVisibility(View.VISIBLE);
            }
            else
                Toast.makeText(getApplicationContext(), "부담한 금액과 weight는 정수로 입력해야 합니다.", Toast.LENGTH_LONG);
        }
        else {
            Toast.makeText(getApplicationContext(), "모든 칸을 입력해주세요.", Toast.LENGTH_LONG).show();
        }
    }

    public void onReturnButtonClicked(View v) {
        deconstructResultFrame();
        inputFrame.setVisibility(View.VISIBLE);
        resultFrame.setVisibility(View.INVISIBLE);
    }

    private void calculateChargeMoney() {
        int numberOfPeople = list.size();

        Person getPerson;

        int sumOfPayMoney = 0;
        int sumOfWeight = 0;
        for(int i=0; i<numberOfPeople; i++) {
            getPerson = list.get(i);
            sumOfPayMoney += getPerson.getPayMoney();
            sumOfWeight += getPerson.getWeight();
        }

        int pieceMoney = sumOfPayMoney / sumOfWeight;
        for(int i=0; i<numberOfPeople; i++) {
            getPerson = list.get(i);
            getPerson.setChargeMoney(pieceMoney * getPerson.getWeight() - getPerson.getPayMoney());
        }
    }

    private void constructResultFrame() {
        int numberOfPeople = list.size();
        int mainPersonIdx = getMaxPersonIdx();
        TextView[] textViews = new TextView[numberOfPeople];
        Person mainPerson = list.get(mainPersonIdx);
        Person getPerson;
        String getString;

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        list.get(mainPersonIdx).setChargeMoney(0);

        for(int i=0; i<numberOfPeople; i++) {
            textViews[i] = new TextView(this);

            getPerson = list.get(i);
            if(getPerson.getChargeMoney() > 0) {
                getString = getPerson.getName() + "  ▶  " + mainPerson.getName() + "  ["
                        + getPerson.getChargeMoney() + " 원]";
                textViews[i].setText(getString);
                textViews[i].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                textViews[i].setLayoutParams(params);
                mainLayout2.addView(textViews[i]);
            }
            else if(getPerson.getChargeMoney() < 0) {
                getString = mainPerson.getName() + "  ▶  " + getPerson.getName() + "  ["
                        + (-getPerson.getChargeMoney()) + " 원]";
                textViews[i].setText(getString);
                textViews[i].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                textViews[i].setLayoutParams(params);
                mainLayout2.addView(textViews[i]);
            }
        }
    }

    private void deconstructResultFrame() {
        int numberOfPeople = list.size();
        Person getPerson;

        for(int i=0; i<numberOfPeople; i++) {
            getPerson = list.get(i);
            getPerson.setChargeMoney(0);
        }
        mainLayout2.removeAllViews();
    }

    private int getMaxPersonIdx() {
        int maxPersonIdx = 0;
        int size = list.size();
        for(int i=1; i<size; i++) {
            if(list.get(i).getPayMoney() > list.get(maxPersonIdx).getPayMoney())
                maxPersonIdx = i;
        }
        return maxPersonIdx;
    }

    private boolean checkAllEditTexts() {
        int size = list.size();

        for(int i=0; i<size; i++) {
            if(name.get(i).getText().toString().equals("")
            || payMoney.get(i).getText().toString().equals("")
            || weight.get(i).getText().toString().equals(""))
                return false;
        }
        return true;
    }

    private boolean checkAllFormats() {
        return true;
    }
}

class Person {
    EditText name;
    EditText payMoney;
    EditText weight;
    int chargeMoney;

    public Person(EditText name, EditText payMoney, EditText weight) {
        this.name = name;
        this.payMoney = payMoney;
        this.weight = weight;
        this.chargeMoney = 0;
    }

    public String getName() {
        return name.getText().toString();
    }

    public int getPayMoney() {
        return Integer.parseInt(payMoney.getText().toString());
    }

    public int getWeight() {
        return Integer.parseInt(weight.getText().toString());
    }

    public void setChargeMoney(int chargeMoney) {
        this.chargeMoney = chargeMoney;
    }

    public int getChargeMoney() {
        return chargeMoney;
    }
}
