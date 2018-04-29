package com.example.j32u4ukh.enigma;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import static com.example.j32u4ukh.enigma.Enigma.wheel;

public class MainActivity extends AppCompatActivity {
    NumberPicker numberLeft, numberMedium, numberRight;
    EditText editInput, editOutput;
    Button buttonMemory1, buttonMemory2, buttonMemory3;
    SharedPreferences sharedPreferences;
    static int[] rmlRML = new int[6];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getView();
        setView();
    }

    public void getView(){
        buttonMemory1 = (Button)findViewById(R.id.buttonMemory1);
        buttonMemory2 = (Button)findViewById(R.id.buttonMemory2);
        buttonMemory3 = (Button)findViewById(R.id.buttonMemory3);

        numberLeft = (NumberPicker)findViewById(R.id.numberLeft);
        numberMedium = (NumberPicker)findViewById(R.id.numberMedium);
        numberRight = (NumberPicker)findViewById(R.id.numberRight);

        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);

        editInput = (EditText)findViewById(R.id.editInput);
        editOutput = (EditText)findViewById(R.id.editOutput);
    }

    public void setView(){
        numberLeft.setMinValue(0);
        numberLeft.setMaxValue(9);
        numberMedium.setMinValue(0);
        numberMedium.setMaxValue(9);
        numberRight.setMinValue(0);
        numberRight.setMaxValue(9);

        buttonLongClick(buttonMemory1, 1);
        buttonLongClick(buttonMemory2, 2);
        buttonLongClick(buttonMemory3, 3);
    }

    public void onClick(View view){
        switch (view.getId()){
            // 記憶庫 一
            case R.id.buttonMemory1:
                String one = sharedPreferences.getString("code1" , "one");
                editInput.setText(one);
                break;
            // 記憶庫 二
            case R.id.buttonMemory2:
                String two = sharedPreferences.getString("code2" , "two");
                editInput.setText(two);
                break;
            // 記憶庫 三
            case R.id.buttonMemory3:
                String three = sharedPreferences.getString("code3" , "three");
                editInput.setText(three);
                break;
            // 轉換紐
            case R.id.buttonTranslation:
                rmlRML[0] = 0;
                rmlRML[1] = 0;
                rmlRML[2] = 0;
                rmlRML[3] = numberLeft.getValue();
                rmlRML[4] = numberMedium.getValue();
                rmlRML[5] = numberRight.getValue();

                String string = editInput.getText().toString();
                String enigma = "";
                char[] letter = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
                for(char c : string.toCharArray()){
                    if(in(c, letter)){
                        enigma += Encode(c);
                    }else{
                        enigma += c;
                    }
                    wheel(rmlRML);
                };
                editOutput.setText(enigma);
                break;
        }
    }

    static boolean in(char c, char[] har){
        for(char h : har){
            if(c == h){
                return true;
            }
        }
        return false;
    }

    public void buttonLongClick(View view, int memory){
        final String database = "code" + String.valueOf(memory);
        // 長按設定：修改記憶庫按鈕名稱&記憶的亂碼(加密後的密碼)
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final String code = editOutput.getText().toString().trim();
                editInput.setText(null);
                editOutput.setText(null);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(getResources().getString(R.string.memoryTitle))
                        // 訊息
                        .setMessage(getResources().getString(R.string.message))
                        // 正向鍵：取得名稱，修改記憶庫按鈕名稱
                        .setPositiveButton(R.string.memoryOk, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(code.length() != 0){
                                    sharedPreferences.edit().putString(database, code).apply();
                                }
                                dialogInterface.dismiss();
                            }
                        })
                        // 反向鍵：直接結束對話框
                        .setNegativeButton(R.string.memoryCancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .create().show();

                return false;
            }
        });
    }


    // 以下本來希望用 Enigma.java 取代
    // 旋轉功能
    static int[] wheel(int[] rmlRML){
        int r = rmlRML[0], m = rmlRML[1], l = rmlRML[2];
        int R = rmlRML[3], M = rmlRML[4], L = rmlRML[5];
        r += 1;
        if(R == 10){
            R = 0;
        }else{
            R += 1;
        };
        if(r % 10 == 0){
            m += 1;
            if(M == 10){
                M = 0;
            }else{
                M += 1;
            };
        };
        if(r % 100 == 0 && m != 0){
            l += 1;
            if(L == 10){
                L = 0;
            }else{
                L += 1;
            };
        };
        rmlRML[0] = r;
        rmlRML[1] = m;
        rmlRML[2] = l;
        rmlRML[3] = R;
        rmlRML[4] = M;
        rmlRML[5] = L;
        return rmlRML;
    }



    static char[] change(char[] old, int number){
        char[] test = new char[old.length];
        for(int i = 0; i < old.length; i++){
            if(i % number == 0){
                if((i + number) > (old.length - 1)){
                    test[0] = old[i];
                }else{
                    test[i + number] = old[i];
                }
            }else{
                test[i] = old[i];
            }
        };
        return test;
    };

    // 旋轉盤R
    static char EncodeR(char c){
        int w = rmlRML[3] % 10;
        char har = 'c';
        char[] letterOldR = {'T', 'A', 'a', '4', '6', 'u', '8', 'L', '1', 'q', 'y', 'Q', 'p', 'H', '5', '3', 'C', 'r', 'O', '2', 'h', 'W', 'B', 'E', '7', 'l', 'v', 't', 'z', 'G', 'e', 'n', 'P', 'R', '9', 'S', 'V', 'g', 'I', 'j', 'w', '0', 'X', 'd', 'f', 'i', 's', 'x', 'N', 'k', 'F', 'm', 'U', 'J', 'o', 'b', 'Y', 'K', 'M', 'D', 'c', 'Z'};
        if(w == 0){
            har = transform(letterOldR, c);
        }else{
            char[] letterNewR = new char[letterOldR.length];
            letterNewR = change(letterOldR, w);
            har = transform(letterNewR, c);
        };
        return har;
    };

    // 旋轉盤M
    static char EncodeM(char c){
        int w = rmlRML[4] % 10;
        char har = 'c';
        char[] letterOldM = {'K', 'V', '8', 'A', '1', 'r', '0', 'J', 'h', 'N', 'Q', 'P', 'q', 'F', 'o', 't', '7', 'U', '4', 'X', 'j', '9', 'x', 'k', 'v', 'e', 'T', '5', 'b', 'g', 'f', 'Y', 'L', '3', 'u', 'W', 'D', 'G', 'w', 'R', 'I', 'O', 'l', 'E', 'B', 'd', '2', 'Z', 's', 'S', 'z', 'i', '6', 'C', 'n', 'm', 'M', 'y', 'p', 'a', 'c', 'H'};
        if(w == 0){
            har = transform(letterOldM, c);
        }else{
            char[] letterNewM = new char[letterOldM.length];
            letterNewM = change(letterOldM, w);
            har = transform(letterNewM, c);
        };
        return har;
    };

    // 旋轉盤L
    static char EncodeL(char c){
        int w = rmlRML[5] % 10;
        char har = 'c';
        char[] letterOldL = {'i', 'T', 'C', 'O', 'E', '1', 's', 'M', 'F', 'P', 'j', 'L', 'h', 'S', 'k', 'Y', 'I', 'A', 'x', 'y', 'm', '4', 'w', '0', 'b', 'J', 'g', 'N', '6', 'Q', 'o', 'B', '7', 'd', 'v', 'H', '2', 'D', 'f', 'n', 'G', 'p', 'X', 'u', 'e', 'R', 'z', 'U', 'Z', '5', 'K', 'c', 'l', 'V', '8', '3', 't', '9', 'W', 'q', 'a', 'r'};
        if(w == 0){
            har = transform(letterOldL, c);
        }else{
            char[] letterNewL = new char[letterOldL.length];
            letterNewL = change(letterOldL, w);
            har = transform(letterNewL, c);
        };
        return har;
    };

    //反射板
    private static char Reflection(char c){
        char har = 'c';
        char[] letter = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        har = transform(letter, c);
        return har;
    };

    // 彙整加密&旋轉
    public static char Encode(char c){
        char har = EncodeR(EncodeM(EncodeL(Reflection(EncodeL(EncodeM(EncodeR(c)))))));
        return har;
    };

    // 返回索引值
    private static int findIndex(char[] letter, char c){
        int n = 0;
        for(int i = 0; i < letter.length; i++){
            if(letter[i] == c){
                n = i;
            };
        };
        return n;
    };

    // 交換功能
    private static char transform(char[] letter, char c){
        int i = findIndex(letter, c);
        if(i % 2 == 0){
            c = letter[i + 1];
        }else{
            c = letter[i - 1];
        };
        return c;
    };
}

/*
    // 旋轉R
    private static char EncodeR(char c){
        int w = rmlRML[3] % 10;
        char har = 'c';
        Rloop:
        switch(w){
            case 0:
                char[] letterR0 = {'z', '6', 'L', 'W', 'A', '2', 'Y', 'V', 'M', 'v', 'T', 'D', 'P', 'p', 'j', 'O', 'J', 'e', '0', 'h', 's', '5', '1', 'w', 'R', 'n', 'k', 'q', 'a', 'C', 'I', 'G', 'x', 'E', 'f', '3', 'S', 'K', 'X', '4', 'y', 'F', '9', 'r', 'u', '8', 'g', 'b', 'U', 'Z', 'Q', 'c', 'H', 't', 'B', 'o', 'm', 'i', 'd', 'l', 'N', '7'};
                har = transform(letterR0, c);
                break Rloop;

            case 1:
                char[] letterR1 = {'a', '4', 'g', '8', 'U', 'Y', 't', 'W', 'd', 'q', 'h', 'o', 'D', '5', 'z', 'Z', 'n', 'i', '7', 'T', 'e', 'u', 'B', 'C', '2', 'y', 'G', '1', 'V', 'K', 'I', 'R', 'b', 'N', 'j', 'S', 'p', 'J', 'A', 'm', 'f', '3', 'H', 'x', '9', 'Q', 'c', 'k', '6', '0', 'O', 's', 'M', 'E', 'F', 'r', 'v', 'P', 'l', 'X', 'w', 'L'};
                har = transform(letterR1, c);
                break Rloop;

            case 2:
                char[] letterR2 = {'q', '1', 'r', 'S', '4', 'F', 'T', 'I', 'c', 'G', '5', '8', 'm', 'h', 'd', 'l', 'e', 'i', 'z', 'D', 't', 'B', '9', 'Q', 'H', 'y', 'p', 'Z', '0', 'n', 'w', 'X', 'g', 'N', 'k', '6', 'K', '3', 'u', 's', 'b', 'x', 'A', 'V', 'j', 'a', 'E', 'o', '2', '7', 'R', 'U', 'C', 'P', 'f', 'Y', 'M', 'v', 'W', 'J', 'O', 'L'};
                har = transform(letterR2, c);
                break Rloop;

            case 3:
                char[] letterR3 = {'w', 'N', 'O', 'Y', '7', 'l', 'C', 'n', 'e', 'P', 'L', 'G', 'h', 'z', 't', 'W', 'M', '0', 'Q', 'u', 'i', 'j', 'x', 'd', 'J', 'I', '5', 'V', 'm', 'o', '6', 'B', 'D', '8', 'A', 'p', 'K', '3', 'y', 'k', '9', 'U', 's', 'R', 'g', 'q', 'f', 'b', 'S', 'H', 'T', '1', 'X', 'a', 'F', '2', 'c', '4', 'r', 'v', 'Z', 'E'};
                har = transform(letterR3, c);
                break Rloop;

            case 4:
                char[] letterR4 = {'K', 'x', 'G', 'u', 'U', 'L', '6', 'h', 'E', 'F', 'i', 't', 'y', 'z', 'c', 'C', 'e', '7', 'Q', 'q', '0', 'P', 'b', 'l', 'd', 'M', 'n', 'I', 'R', 'T', 'H', 'V', 'W', 'O', 'v', 'o', 'B', 'a', 'X', 'D', 'm', 'w', 'r', '1', 'g', 's', 'f', '2', 'Y', '8', 'A', '5', 'J', 'j', '4', '9', 'Z', 'N', 'k', 'S', 'p', '3'};
                har = transform(letterR4, c);
                break Rloop;

            case 5:
                char[] letterR5 = {'s', 'N', 'C', 'u', 'I', 'd', 'F', 'l', 'j', 'n', 'q', '4', '6', 'p', 'H', 'V', 'M', 'y', 'R', 't', 'w', '7', 'Y', '0', '3', 'g', 'c', 'h', 'r', 'Q', 'E', 'a', 'k', 'b', 'f', 'm', 'i', 'O', 'S', 'P', '1', 'U', 'G', 'z', 'T', 'v', 'B', '9', '5', 'x', 'J', 'D', 'A', 'K', '2', 'Z', 'L', '8', 'W', 'o', 'e', 'X'};
                har = transform(letterR5, c);
                break Rloop;

            case 6:
                char[] letterR6 = {'a', '8', 'G', 'y', 'w', '0', 'd', 'V', 'x', 'l', 'p', 'F', 'f', 'h', 'm', 'u', 'c', 'L', 'k', 'D', '4', 'j', '6', 'H', 'N', '3', 'C', '7', 'q', 'J', 'E', 'B', 'P', '1', 'O', 's', 'v', '2', 'i', 'M', 'Q', 'K', 'g', '5', 'r', 'o', 'T', '9', 'e', 'n', 'z', 'Y', 'R', 'W', 'A', 'b', 'X', 'U', 'I', 't', 'Z', 'S'};
                har = transform(letterR6, c);
                break Rloop;

            case 7:
                char[] letterR7 = {'h', 'J', 'u', 'N', 'g', 'q', 'F', '0', 'w', 'a', 'm', 'K', 'y', 's', 'U', '7', 'i', 'o', 'l', 'k', '5', 'H', '6', 'n', 'Y', '8', 'G', 'M', 'p', 't', 'A', 'x', '4', 'R', 'I', 'b', 'B', 'd', '3', 'X', 'T', '1', 'O', 'C', 'E', '2', 'W', 'v', 'L', 'S', '9', 'e', 'D', 'Q', 'f', 'z', 'r', 'Z', 'j', 'P', 'V', 'c'};
                har = transform(letterR7, c);
                break Rloop;

            case 8:
                char[] letterR8 = {'S', 't', 'z', '0', 'F', 'N', 'o', '7', '8', 'Q', 's', 'V', 'j', 'Z', 'X', 'l', 'p', 'w', '5', '4', '9', 'f', 'a', 'x', '6', 'Y', 'h', 'd', 'q', 'H', 'P', 'W', 'R', 'k', 'K', 'n', 'v', 'y', 'g', 'L', 'E', 'u', '3', 'B', 'i', 'b', 'G', '2', 'm', 'O', 'D', 'e', 'I', 'r', 'U', 'T', 'A', 'C', 'M', '1', 'c', 'J'};
                har = transform(letterR8, c);
                break Rloop;

            case 9:
                char[] letterR9 = {'K', 'V', '8', 'A', '1', 'r', '0', 'J', 'h', 'N', 'Q', 'P', 'q', 'F', 'o', 't', '7', 'U', '4', 'X', 'j', '9', 'x', 'k', 'v', 'e', 'T', '5', 'b', 'g', 'f', 'Y', 'L', '3', 'u', 'W', 'D', 'G', 'w', 'R', 'I', 'O', 'l', 'E', 'B', 'd', '2', 'Z', 's', 'S', 'z', 'i', '6', 'C', 'n', 'm', 'M', 'y', 'p', 'a', 'c', 'H'};
                har = transform(letterR9, c);
                break Rloop;
        };
        return har;
    };

    // 旋轉M
    private static char EncodeM(char c){
        int w = rmlRML[4] % 10;
        char har = 'c';
        Mloop:
        switch(w){
            case 0:
                char[] letterM0 = {'7', 'Q', 'P', 'U', '1', '8', 'x', 'J', 'X', 'q', 'j', 'b', 'B', '5', 'h', 'O', 'p', 'a', '9', 'v', '0', 'E', 'N', 'i', 'V', 'g', 'c', '3', 'M', 'S', 'n', 'K', 't', 's', 'l', 'I', '4', 'u', 'f', 'Z', 'Y', 'R', 'H', 'w', 'T', '2', 'o', 'D', 'd', 'k', 'L', 'r', 'G', 'y', 'z', 'm', 'A', 'W', '6', 'C', 'F', 'e'};
                har = transform(letterM0, c);
                break Mloop;
            case 1:
                char[] letterM1 = {'r', '9', 'I', '1', 'm', '8', '5', 'e', 'B', 'o', 'G', 'Y', 'Q', 'X', 'i', 'g', '2', 'C', 'J', 'H', 'l', '0', 'h', 'E', 'A', 'u', 'M', 's', 'R', '3', '4', 'K', 'j', 'b', 'U', 'c', 'z', 'S', 'd', 'T', 'v', 'N', 'k', 'F', 'x', 'y', 'Z', 'f', 'q', 'p', 't', '6', 'n', 'w', 'D', 'V', 'L', '7', 'O', 'W', 'a', 'P'};
                har = transform(letterM1, c);
                break Mloop;
            case 2:
                char[] letterM2 = {'e', 'T', '0', '1', 'u', 'G', 'O', '4', 'L', 'S', 'Q', 'z', 's', 'H', 'A', 'I', 'g', 'o', 'Y', 'r', 'U', '9', 'j', 'l', 'h', '8', 'X', 'd', 'J', '2', 't', 'i', 'M', 'y', '3', '5', 'E', 'Z', 'x', 'R', 'N', 'n', 'B', '7', 'b', 'w', 'V', 'm', 'F', 'a', 'p', 'C', 'c', 'f', 'q', 'v', '6', 'D', 'P', 'k', 'K', 'W'};
                har = transform(letterM2, c);
                break Mloop;
            case 3:
                char[] letterM3 = {'F', 'q', 'T', 'e', '3', 'C', 'S', '0', 'x', 'P', 'I', 'U', 'g', 'i', 'r', 'u', '4', 'B', 's', 'h', 'b', 'n', 'Q', 'm', 'H', 'X', 'd', '7', 'l', 'y', 'N', 'J', 'V', 'Z', 'o', 'j', 'A', '5', 'E', '2', 'D', '8', 'w', 'L', 'Y', '6', 'z', 'c', '9', 'f', 'a', 'G', 'W', 'K', 'R', 'p', 'M', 'O', 't', '1', 'k', 'v'};
                har = transform(letterM3, c);
                break Mloop;
            case 4:
                char[] letterM4 = {'N', 'm', 'X', 'O', 'S', 'u', '1', 'G', 'r', '0', '6', 'l', '3', 'e', 'M', 'f', 'i', 'h', 'w', 'Y', '7', 'C', '4', 'B', 'o', 'D', 'T', 'x', 'Q', 'z', 'W', 'q', '5', 'y', '2', 'J', 'H', '9', 'n', 'E', 'v', 'K', 'V', 'F', 'p', 'k', 'L', 'Z', 'b', 'j', '8', 's', 'I', 't', 'P', 'U', 'A', 'g', 'c', 'R', 'd', 'a'};
                har = transform(letterM4, c);
                break Mloop;
            case 5:
                char[] letterM5 = {'g', 'e', 'f', 'V', 'a', '6', 'u', 'E', 'j', 'N', 'U', 'm', 'B', 'M', 'L', '7', '0', 'c', 'w', '3', 'y', 'Y', 'x', 'C', 'F', 'v', 'Q', '2', '9', 'P', 'R', '8', 'H', 'p', 'z', 'l', 'W', 't', 'k', 'n', 'O', 'h', 'b', 'G', 'd', 'T', 'D', '4', 'o', '1', 's', 'Z', 'i', '5', 'q', 'K', 'A', 'J', 'S', 'X', 'I', 'r'};
                har = transform(letterM5, c);
                break Mloop;
            case 6:
                char[] letterM6 = {'O', 'q', '0', 'o', 'f', 'b', 'S', 'X', '2', 'y', 'r', 'E', 'j', 'W', '5', 'k', 'F', '6', 'V', '1', 'u', 'U', 'v', 'm', 'x', 'P', 'c', 'H', '3', 'R', 'd', 'D', 'i', 'Q', 'e', '8', 'p', 'B', 'G', 't', 'T', '7', 'g', 'n', 'Y', 's', 'z', 'h', 'A', 'l', '4', 'I', 'J', '9', 'K', 'C', 'Z', 'N', 'M', 'a', 'L', 'w'};
                har = transform(letterM6, c);
                break Mloop;
            case 7:
                char[] letterM7 = {'I', 'z', 'B', 'k', 'e', 'O', 'V', 'b', 'c', 'F', 'w', 'r', 'o', '6', 'X', '1', 'E', 'i', 'y', 'Q', '5', '8', 'd', '2', 'Y', 'p', 'N', '3', 'R', 'a', 'W', 'g', 'T', 't', 'S', 'H', 's', 'J', 'q', 'M', 'l', 'f', 'm', 'v', 'K', 'Z', 'D', 'u', 'h', 'G', 'C', 'j', 'n', 'A', '7', 'U', 'P', '0', '9', '4', 'L', 'x'};
                har = transform(letterM7, c);
                break Mloop;

            case 8:
                char[] letterM8 = {'a', 'l', 'O', 'Y', '1', '0', 'z', 'h', 'C', '3', 'w', 'r', 'U', 'k', 'm', 'f', 'j', 'u', 'x', 'P', '6', 'F', 'T', 'B', 'o', 'I', 'N', 'S', '7', 'M', 'Q', 'H', 'R', 'A', 'd', 'E', 'Z', 't', 'J', 'V', '8', 'L', 'n', 'q', 'X', 'y', 'g', 'D', 'K', '2', 'p', 'e', 'G', 's', '5', 'v', 'W', 'i', 'b', 'c', '9', '4'};
                har = transform(letterM8, c);
                break Mloop;

            case 9:
                char[] letterM9 = {'i', 'T', 'C', 'O', 'E', '1', 's', 'M', 'F', 'P', 'j', 'L', 'h', 'S', 'k', 'Y', 'I', 'A', 'x', 'y', 'm', '4', 'w', '0', 'b', 'J', 'g', 'N', '6', 'Q', 'o', 'B', '7', 'd', 'v', 'H', '2', 'D', 'f', 'n', 'G', 'p', 'X', 'u', 'e', 'R', 'z', 'U', 'Z', '5', 'K', 'c', 'l', 'V', '8', '3', 't', '9', 'W', 'q', 'a', 'r'};
                har = transform(letterM9, c);
                break Mloop;
        };
        return har;
    };

    // 旋轉L
    private static char EncodeL(char c){
        int w = rmlRML[5] % 10;
        char har = 'c';
        Lloop:
        switch(w){
            case 0:
                char[] letterL0 = {'7', 'y', 'i', 'x', 'Y', '2', '4', 'K', 'h', 'M', '8', 'c', '5', 'D', '3', '6', '0', 'v', 'X', '9', 'd', 'V', 'f', 't', 'o', 'F', 'L', 'S', 'T', 'P', 'B', 'k', 'Z', 'G', 'q', 'j', 'N', 'I', 'n', 'J', 'w', 'a', 'e', 'H', 'E', 'g', 'm', 'u', 'C', 'r', 'l', 'A', 'b', 'p', 'U', 'z', 'Q', 'O', '1', 'W', 'R', 's'};
                har = transform(letterL0, c);
                break Lloop;
            case 1:
                char[] letterL1 = {'3', 'E', '4', 'r', 'k', 'o', 'u', '8', 'g', 'U', 'y', 'G', 'c', '5', 'P', '1', 'e', 'W', '6', 's', '9', 'O', 'S', 't', '2', '0', 'L', 'x', 'Z', 'T', 'F', 'A', 'd', 'V', 'B', 'f', 'l', 'n', 'j', 'I', 'C', 'w', 'N', 'R', 'v', 'i', 'J', 'D', 'Q', 'a', 'q', 'M', 'm', 'z', 'K', 'b', '7', 'h', 'p', 'Y', 'H', 'X'};
                har = transform(letterL1, c);
                break Lloop;
            case 2:
                char[] letterL2 = {'o', '0', 'q', 'b', 'H', 'S', 'r', 'm', 'e', 'p', 'L', '5', 'g', '1', 'A', 'P', 'B', '2', 'U', '4', 'T', 'G', 'c', '9', 'x', 'l', 'w', 'W', 'E', 's', 'V', 'n', '6', 'M', 'y', 'f', 'O', 'C', 'R', 'I', 'd', 'Y', 'a', 'Q', 'D', 't', '7', 'Z', 'j', 'z', 'u', 'k', 'i', 'X', '8', 'J', 'N', 'h', 'v', 'K', 'F', '3'};
                har = transform(letterL2, c);
                break Lloop;
            case 3:
                char[] letterL3 = {'p', 'H', 'y', 'F', '0', 'e', 'A', 'S', 'q', 's', '6', 'k', 'g', 'P', 'l', '2', '3', 'W', 'L', 'm', 'X', 'N', '4', 'J', 'j', '1', 'T', 'Q', 'w', 'n', 'Z', 'R', 'K', 'O', 'u', 'r', '5', 'c', 'M', 'D', 'Y', 't', 'v', 'G', 'U', '7', 'i', 'V', 'z', 'a', 'I', 'B', 'E', 'b', 'x', 'o', '9', 'h', 'f', 'd', 'C', '8'};
                har = transform(letterL3, c);
                break Lloop;
            case 4:
                char[] letterL4 = {'9', 'P', 'U', 'Q', 'A', 'E', 'L', 'k', 'D', 'X', 'l', 'w', 'W', '6', 'h', 'V', 'C', 'q', '3', 'M', '2', 's', 'm', '0', 'O', 'x', 'o', 'R', 'N', 'G', 'u', 'S', 'y', 'I', 'z', 'n', '5', '1', 'a', 'J', 'r', 'd', '7', 'Y', 'F', 't', 'K', 'b', 'Z', 'i', 'p', 'j', 'B', '8', 'c', 'v', 'H', 'e', 'T', 'f', 'g', '4'};
                har = transform(letterL4, c);
                break Lloop;
            case 5:
                char[] letterL5 = {'f', '8', 'R', 'B', 'D', 'X', 'g', 'w', 'y', 'c', 'V', 'L', 'P', 'M', '0', '2', '6', 'G', 'r', 'U', 'O', '9', 's', '3', 'C', 'I', 'm', 'n', 'Q', 'q', 'N', 'W', 'Z', '1', '7', 'd', 't', 'j', 'u', 'v', 'a', 'x', 'b', 'H', '5', 'J', 'i', 'k', 'T', 'h', 'E', 'S', 'F', 'o', 'e', 'l', '4', 'p', 'K', 'z', 'A', 'Y'};
                har = transform(letterL5, c);
                break Lloop;
            case 6:
                char[] letterL6 = {'U', 'r', 'g', 'i', 'y', 'w', 'x', 'z', 'M', '9', 'o', 'P', 'p', 'V', 'c', 'I', 'X', 'W', 'f', 'T', 'B', '8', '7', '2', 'm', 'G', '6', '0', 'j', 'n', 'C', 'l', 'N', 'b', 'S', 'u', 'a', 'Q', '3', '1', 'D', 'A', '4', 'H', 'E', 'O', 'v', 'R', 's', 'h', 't', 'd', 'Z', 'K', 'Y', 'J', 'k', 'e', 'F', 'q', 'L', '5'};
                har = transform(letterL6, c);
                break Lloop;
            case 7:
                char[] letterL7 = {'X', 'Z', 'F', 'r', 'y', 'C', '4', 'S', 'z', 'A', 'Y', 'm', 'v', 'W', '7', '5', 'o', 'q', 'l', '8', 'c', 'V', '3', 'w', 'G', 'g', 'H', 'J', 'f', 'j', 'B', 'h', 'p', 'I', 's', '0', 'K', 'D', 'b', 'M', 'R', 'T', 'e', 'n', 'O', '2', 'N', 'u', 't', 'a', 'P', 'Q', 'U', '1', 'k', '9', '6', 'i', 'E', 'L', 'd', 'x'};
                har = transform(letterL7, c);
                break Lloop;

            case 8:
                char[] letterL8 = {'Q', '3', 'u', 'C', '4', 'F', 'i', 'X', 'W', 'Y', 'Z', 'U', 'l', '7', 'A', 't', 'S', 'm', 'H', 'j', '8', 'K', 'x', '0', 'c', '5', 'L', 's', 'B', 'w', 'n', 'V', 'E', 'G', '2', 'q', 'R', 'p', '6', '9', 'M', 'I', 'P', 'D', 'f', 'e', 'N', 'T', 'k', 'z', 'o', 'v', 'b', 'y', '1', 'J', 'a', 'h', 'd', 'g', 'O', 'r'};
                har = transform(letterL8, c);
                break Lloop;

            case 9:
                char[] letterL9 = {'Y', 'Q', 'J', '7', 'F', 'y', 'K', 'A', 'x', 'a', '2', 'j', 'd', 'O', 'H', 'E', 'X', 't', '4', 's', '8', 'o', 'l', 'h', 'c', 'R', 'n', 'S', 'q', 'e', 'P', 'w', 'Z', 'f', '9', 'b', '1', 'L', 'T', '6', 'I', 'k', 'M', 'G', '0', 'N', 'U', '5', 'C', 'z', 'v', 'g', 'p', 'r', 'u', 'D', 'm', 'i', 'B', '3', 'W', 'V'};
                har = transform(letterL9, c);
                break Lloop;
        };
        return har;
    };
    */