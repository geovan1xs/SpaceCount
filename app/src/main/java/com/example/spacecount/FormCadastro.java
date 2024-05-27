package com.example.spacecount;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FormCadastro extends AppCompatActivity {

    private EditText nome, email, senha,empresa;
    private Button bt_cadastrar;
    String[] mensagens = {"Preencha todos os campos", "Cadastro realizado com sucesso"};
    String usuarioID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cadastro);
        getSupportActionBar().hide();
        IniciarComponentes();

        bt_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String edit_nome = nome.getText().toString();
                String edit_email = email.getText().toString();
                String edit_senha = senha.getText().toString();
                String edit_empresa = empresa.getText().toString();
                if (edit_nome.isEmpty() || edit_email.isEmpty() || edit_senha.isEmpty() || edit_empresa.isEmpty()) {
                    Snackbar snackbar = Snackbar.make(view, mensagens[0], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                } else {
                    CadastrarUsuario(view);

                }
            }
        });
    }
    private void CadastrarUsuario(View view){

        String edit_email = email.getText().toString();
        String edit_senha = senha.getText().toString();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(edit_email,edit_senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    SalvarDadosUsuario();
                    Snackbar snackbar = Snackbar.make(view, mensagens[1], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                    //Intent intentcao = new Intent(FormCadastro.this,Home.class); // Volta para a tela de login apos cadastro
                    //startActivity(intentcao);
                    //finish();

                }
                else{
                    String erro;
                    try {
                        throw task.getException();
                    }
                    catch (FirebaseAuthWeakPasswordException e) {
                        erro = "Digite uma senha com no mínimo 6 caracteres ";
                    }
                    catch (FirebaseAuthUserCollisionException e){
                        erro = "Este e-mail já esta sendo usado ";
                    }
                    catch(FirebaseAuthInvalidCredentialsException e){
                        erro = "E-mail invalido";
                    }
                    catch (Exception e) {
                        erro = "Erro ao cadastrar usuário";
                    }
                    Snackbar snackbar = Snackbar.make(view, erro, Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                    }



            }
        });
    }


        private void SalvarDadosUsuario(){
        String edit_nome = nome.getText().toString();
        String edit_empresa = empresa.getText().toString();

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            Map<String,Object> usuarios = new HashMap<>();
            usuarios.put("nome",edit_nome);
            usuarios.put("empresa",edit_empresa);
            usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

            DocumentReference documentReference = db.collection("Usuarios").document(usuarioID);
            documentReference.set(usuarios).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Log.d("db","Sucesso ao salvar os dados");

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("db_error","Erro ao salvar os dados" + e.toString());

                }
            });



        }
        private void IniciarComponentes(){
            nome = findViewById(R.id.nome);
            email = findViewById(R.id.email);
            senha = findViewById(R.id.senha);
            empresa = findViewById(R.id.empresa);
            bt_cadastrar = findViewById(R.id.bt_cadastrar);
        }
    }
