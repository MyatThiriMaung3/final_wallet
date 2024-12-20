package tdtu.edu.course.mobiledev.mobileappdevfinalwallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class CalculatorActivity extends AppCompatActivity {
    Button Num0,Num1,Num2,Num3,Num4,Num5,Num6,Num7,Num8,Num9,NumOn,NumAc,
            NumDel,NumOff,NumDiv,NumPlus,NumMinus,NumMul,NumPoint,NumEqual;
    TextView Screen;

    double firstnum;
    String operation;
    private String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calculator);

        Intent intentFromHome = getIntent();
        name = intentFromHome.getStringExtra("name");


        Num0 = findViewById(R.id.num0);
        Num1 = findViewById(R.id.num1);
        Num2 = findViewById(R.id.num2);
        Num3 = findViewById(R.id.num3);
        Num4 = findViewById(R.id.num4);
        Num5 = findViewById(R.id.num5);
        Num6 = findViewById(R.id.num6);
        Num7 = findViewById(R.id.num7);
        Num8 = findViewById(R.id.num8);
        Num9 = findViewById(R.id.num9);

        NumOn = findViewById(R.id.numon);
        NumAc = findViewById(R.id.numac);
        NumDel = findViewById(R.id.numdel);
        NumOff = findViewById(R.id.numoff);
        NumDiv = findViewById(R.id.numdiv);
        NumPlus = findViewById(R.id.numplus);
        NumMinus = findViewById(R.id.numminus);
        NumMul = findViewById(R.id.nummul);
        NumPoint = findViewById(R.id.numpoint);
        NumEqual = findViewById(R.id.numequal);

        Screen = findViewById(R.id.screen);
        NumOff.setOnClickListener(view -> Screen.setVisibility(View.GONE));
        NumOn.setOnClickListener(view -> {
            Screen.setVisibility(View.VISIBLE);
            Screen.setText("0");
        });

        ArrayList<Button> nums = new ArrayList<>();
        nums.add(Num0);
        nums.add(Num1);
        nums.add(Num2);
        nums.add(Num3);
        nums.add(Num4);
        nums.add(Num5);
        nums.add(Num6);
        nums.add(Num7);
        nums.add(Num8);
        nums.add(Num9);

        for(Button b : nums){
            b.setOnClickListener(view -> {
                if(!Screen.getText().toString().equals("0")){
                    Screen.setText(Screen.getText().toString() + b.getText().toString());
                }else{
                    Screen.setText(b.getText().toString());
                }
            });
        }


        ArrayList<Button> opers = new ArrayList<>();
        opers.add(NumDiv);
        opers.add(NumMul);
        opers.add(NumPlus);
        opers.add(NumMinus);
        for (Button b : opers){
            b.setOnClickListener(view -> {
                firstnum = Double.parseDouble(Screen.getText().toString());
                operation = b.getText().toString();
                Screen.setText("0");
            });
        }

        NumAc.setOnClickListener(view -> {
            firstnum = 0;
            Screen.setText("0");
        });

        NumDel.setOnClickListener(view -> {
            String num = Screen.getText().toString();
            if (num.length()>1){
                Screen.setText(num.substring(0,num.length()-1));
            }else if(num.length()==1&&!num.equals("0")){
                Screen.setText("0");
            }
        });

        NumPoint.setOnClickListener(view -> {
            if(!Screen.getText().toString().contains(".")){
                Screen.setText(Screen.getText().toString()+".");
            }
        });


        NumEqual.setOnClickListener(view -> {
            double secondNum = Double.parseDouble(Screen.getText().toString());
            double result;
            switch(operation){
                case"/":
                    result = firstnum/secondNum;
                    break;
                case"*":
                    result = firstnum*secondNum;
                    break;
                case"+":
                    result = firstnum+secondNum;
                    break;
                case"-":
                    result = firstnum-secondNum;
                    break;
                default:
                    result = firstnum+secondNum;
            }
            Screen.setText(String.valueOf(result));
            firstnum = result;

        });
    }

    public void backHome(View view) {
        Intent intentHome = new Intent(this, HomeActivity.class);
        intentHome.putExtra("name", name);
        startActivity(intentHome);
        finish();
    }
}