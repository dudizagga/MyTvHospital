package com.example.dudizagga.myworkers;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.example.dudizagga.myworkers.helpClassesAndTables.utlShared;

public class MainActivity extends AppCompatActivity {
    utlShared u;
    private TextInputLayout input_Name, input_Last, input_id, input_pass, input_pass2, input_phone;
    private EditText name, lastName, id, pass, pass2, phone;
    private Button btn, btn2;
    Context context;
    private boolean logged = false ;

    public void setLogged(Boolean log) {
        this.logged = log;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Backendless.initApp(this, "", "");

        setPointer();

    }

    private void setPointer() {
        this.context = this;
        u = new utlShared(context);
        logged = u.getBol(false);
        input_Name = (TextInputLayout) findViewById(R.id.Input_Name);
        input_Last = (TextInputLayout) findViewById(R.id.Input_Last);
        input_id = (TextInputLayout) findViewById(R.id.Input_id);
        input_pass = (TextInputLayout) findViewById(R.id.Input_pass);
        input_pass2 = (TextInputLayout) findViewById(R.id.Input_pass2);
        input_phone = (TextInputLayout) findViewById(R.id.Input_phone);

        name = (EditText) findViewById(R.id.name);
        lastName = (EditText) findViewById(R.id.lastName);
        id = (EditText) findViewById(R.id.id);
        pass = (EditText) findViewById(R.id.Pass);
        pass2 = (EditText) findViewById(R.id.Pass2);
        phone = (EditText) findViewById(R.id.phone);
        btn = (Button) findViewById(R.id.button);
        btn2 = (Button) findViewById(R.id.button2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reg();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logIn();
            }
        });
        logCheck();
    }

    private void logCheck() {
        if (logged == true) {
            StayLogged();
        } else {

            Toast.makeText(context, "login Failed ", Toast.LENGTH_SHORT).show();
        }
    }

    private void StayLogged() {
        AsyncCallback<Boolean> checker = new AsyncCallback<Boolean>() {
            @Override
            public void handleResponse(Boolean response) {
                Toast.makeText(context, "User Logged", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(context, MyRoom.class));
                //finish();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(context, "Faild to log in ", Toast.LENGTH_SHORT).show();
            }
        };
        Backendless.UserService.isValidLogin(checker);
    }

    private void logIn() {
        AlertDialog.Builder a = new AlertDialog.Builder(context);
        View i = (LayoutInflater.from(context).inflate(R.layout.log_in_window, null, false));
        final EditText ids = i.findViewById(R.id.id2);
        final EditText passt = i.findViewById(R.id.Passt);
        Button btn = i.findViewById(R.id.btn_log);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Backendless.UserService.login(ids.getText().toString(),
                        passt.getText().toString(), new AsyncCallback<BackendlessUser>() {
                            @Override
                            public void handleResponse(BackendlessUser response) {
                                String a = Backendless.UserService.CurrentUser().getObjectId();
                                Log.e("user",a);
                                u.putId(a);
                                logged = u.putBol(true);
                                Toast.makeText(MainActivity.this, "the user+" + a + " connected", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(context, MyRoom.class));
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                Toast.makeText(MainActivity.this, "" + fault.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }, logged = u.getBol(true));
            }
        });

        a.setView(i);
        a.show();
    }

    private void reg() {
        String idNum = id.getText().toString();
        String names = name.getText().toString();
        String phones = phone.getText().toString();
        String lastNames = lastName.getText().toString();
        String password = pass.getText().toString();
        String password2 = pass2.getText().toString();
        if (names.isEmpty() || names.length() < 2) {
            input_Name.setError("please Enter name - 2 letters min");
            input_Name.setErrorTextAppearance(R.style.error_appearance);
            lastName.setText("");
            return;
        } else {
            input_Name.setError(null);
        }
        if (lastNames.isEmpty() || lastNames.length() < 2) {
            input_Last.setError("please Enter last name - 2 letters min");
            input_Last.setErrorTextAppearance(R.style.error_appearance);
            lastName.setText("");
            return;
        } else {
            input_Last.setError(null);
        }
        if (phones.isEmpty() || phones.length() < 10 || phones.length() > 10) {
            input_phone.setError("please Enter a password (10 digits)");
            input_phone.setErrorTextAppearance(R.style.error_appearance);
        } else {
            input_phone.setError(null);
        }
        if (idNum.isEmpty() || idNum.length() < 9 || idNum.length() > 9) {
            input_id.setError("please Enter id number (9 digits)");
            input_id.setErrorTextAppearance(R.style.error_appearance);
            id.setText("");
            return;
        } else {
            input_id.setError(null);
        }
        if (password.isEmpty() && password.length() < 6 || password2.isEmpty() && password2.length() < 6) {
            input_pass.setError("please Enter a password (6 digits)");
            input_pass.setErrorTextAppearance(R.style.error_appearance);
            input_pass2.setError("please Enter a password again (6 digits)");
            input_pass2.setErrorTextAppearance(R.style.error_appearance);
            pass.setText("");
            pass2.setText("");
            return;
        } else if (!password.equals(password2)) {
            input_pass2.setError("the passwords not match");
            input_pass2.setErrorTextAppearance(R.style.error_appearance);
            pass2.setText("");
            return;
        } else {
            input_pass.setError(null);
            input_pass2.setError(null);

        }
        if (password.equals(password2)) {
            Toast.makeText(this, "register success", Toast.LENGTH_SHORT).show();
            backRegister(names, lastNames, password, idNum, phones);
            name.setText("");
            lastName.setText("");
            id.setText("");
            phone.setText("");
            pass.setText("");
            pass2.setText("");
            logIn();
        }
    }

    private void backRegister(String name, String lastName, String password, String id, String phone) {
        BackendlessUser user = new BackendlessUser();
        user.setProperty("id", id);
        user.setProperty("name", name);
        user.setProperty("lastName", lastName);
        user.setProperty("phone", phone);
        user.setPassword(password);

        Backendless.UserService.register(user, new AsyncCallback<BackendlessUser>() {
            public void handleResponse(BackendlessUser registeredUser) {
                Toast.makeText(MainActivity.this, "connection Successfully", Toast.LENGTH_SHORT).show();
            }

            public void handleFault(BackendlessFault fault) {
                fault.getMessage();
            }
        });
    }
}



