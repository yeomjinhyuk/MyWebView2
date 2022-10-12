package com.example.mywebview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText editUrl;
    WebView webView;

    Spinner spinner;
    ArrayList<String> searchDatas;
    ArrayAdapter<String> dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editUrl = findViewById(R.id.editUrl);
        webView = findViewById(R.id.webView1);

        //***아래의 webView Client 처리가 없으면, 새로운 화면으로 webview가 보여지는것에 주의!
        webView.setWebViewClient(new WebViewClient());

        findViewById(R.id.btnClear).setOnClickListener(this);
        findViewById(R.id.btnGoNaver).setOnClickListener(this);
        findViewById(R.id.btnGoGoogle).setOnClickListener(this);
        findViewById(R.id.btnBack).setOnClickListener(this);

        spinner = findViewById(R.id.history);
        searchDatas = new ArrayList<String>();
        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, searchDatas);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                editUrl.setText(spinner.getItemAtPosition(position).toString());
                findViewById(R.id.btnGoNaver).performClick();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner.setVisibility(View.GONE);

        editUrl.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean b) {
                if(b)
                    spinner.setVisibility(View.VISIBLE);
                else
                    spinner.setVisibility(View.GONE);
            }
        });
    }

    //소스에서 우측마우스 클릭-->Generate-->override methode-->onCreateOptionMenu;
    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        super.onCreateOptionsMenu(menu);
        //res->menu 폴더 만들고 ->menu.xml 파일 미리 만들어 둔다.
        // 메뉴를 띄우기 위해 아래의 코드를 추가해 준다.
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.menu1:
                AlertDialog.Builder dlg = new AlertDialog.Builder(this);
                dlg.setTitle("Hello")
                        .setMessage("안녕하세요")
                        .setPositiveButton("확인", null)
                        .setIcon(R.drawable.naver)
                        .show();
                break;
            case R.id.menu2:
                View dlgView = View.inflate(this, R.layout.profile, null);
                AlertDialog.Builder dlg2 = new AlertDialog.Builder(this);
                dlg2.setTitle("Test")
                        .setView(dlgView)
                        .setPositiveButton("취소", null)
                        .setNegativeButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText editText = dlgView.findViewById(R.id.dlg_nickname);
                                String nickname = editText.getText().toString().trim();
                                if (nickname.length() > 0)
                                    setTitle(nickname + "의 웹뷰");
                            }
                        })
                        .show();
                dlg2.setView(dlgView);
                break;
            case R.id.menu3:
                finish();
                break;
            case R.id.menu4:
                Intent intent = new Intent(MainActivity.this, SpinnerActivity.class);
                startActivity(intent);
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        //키보드가 있다면 안보이게 하자
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(editUrl.getWindowToken(), 0);
        switch (view.getId()) {
            case R.id.btnClear: //검색어 입력칸 지우기
                editUrl.setText("");

                break;
            case R.id.btnGoNaver: //네이버 검색
                webView.loadUrl("https://search.naver.com/search.naver?query=" +
                        editUrl.getText().toString());
                addHistoryData(editUrl.getText().toString());
                break;
            case R.id.btnGoGoogle: //구글검색
                webView.loadUrl("https://www.google.com/search?q=" +
                        editUrl.getText().toString());
                addHistoryData(editUrl.getText().toString());
                break;
            case R.id.btnBack:  //뒤로가기
                webView.goBack();
                break;
        }
        dataAdapter.notifyDataSetChanged();
    }

    private void addHistoryData(String data) {
        if(!searchDatas.contains(data))
            searchDatas.add(0,data);
        if(searchDatas.size()>5)
            searchDatas.remove(5);
    }


}




