package com.example.spacecount;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class Home extends AppCompatActivity {
    private TextView botao_cadastro;
    private EditText email,senha;
    private Button botao_entrar;
    private ProgressBar progressBar;
    String[] mensagens = {"Preencha todos os campos", "Login realizado com sucesso"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home3);
        IniciarComponentes();


        botao_cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, FormCadastro.class);
                startActivity(intent);
            }
        });

        botao_entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String edit_email = email.getText().toString();
                String edit_senha = senha.getText().toString();
                if (edit_email.isEmpty() || edit_senha.isEmpty()) {
                    Snackbar snackbar = Snackbar.make(view, mensagens[0], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
            }else {
                    AutenticarUsuario(view);
                }
                }
        });
    }

    private void AutenticarUsuario(View view){
        String edit_email = email.getText().toString();
        String edit_senha = senha.getText().toString();

        FirebaseAuth.getInstance().signInWithEmailAndPassword(edit_email,edit_senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressBar.setVisibility(View.VISIBLE);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            TelaPrincipal();
                        }
                    },3000);
                    }else {
                    String erro;
                    try {
                        throw task.getException();
                    }
                    catch (FirebaseAuthWeakPasswordException e) {
                        erro = "Digite uma senha com no mínimo 6 caracteres ";
                    }
                    catch(FirebaseAuthInvalidCredentialsException e){
                        erro = "E-mail ou senha invalidos";
                    }
                    catch (Exception e) {
                        erro = "Erro ao fazer login";
                    }
                    Snackbar snackbar = Snackbar.make(view, erro, Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }
                }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();

        if(usuarioAtual != null){
            TelaPrincipal();
        }
    }

    private void TelaPrincipal() {
        Intent intent = new Intent(Home.this, TelaEntrar.class);
        startActivity(intent);
        finish();
    }
    private void IniciarComponentes(){
        botao_cadastro = findViewById(R.id.botao_cadastro);
        email = findViewById(R.id.email);
        senha = findViewById(R.id.senha);
        botao_entrar = findViewById(R.id.botao_entrar);
        progressBar = findViewById(R.id.progress_bar);
    }

}