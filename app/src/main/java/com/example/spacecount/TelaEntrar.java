package com.example.spacecount;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;


import java.util.ArrayList;

public class TelaEntrar extends AppCompatActivity {
    private TextView nome,id_card,empresa;
    private Button bt_deslogar, bt_foto; //deslogar e foto
    private ImageView ic_botoes,ic_botoes2,ic_botoes3,ic_botoes4;




    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DatabaseReference referencia = FirebaseDatabase.getInstance().getReference(); // 1 pegamos a instancia do servidor do firebase (arduino)
    String usuarioID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_entrar);
        getSupportActionBar().hide();
        IniciarComponentes();



        bt_deslogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(TelaEntrar.this, Home.class);
                startActivity(intent);
                finish();
            }
        });

        ic_botoes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                referencia.child("data").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String data = snapshot.getValue(String.class);
                            id_card.setText("Ultima TagID: " + data);
                            // Faça algo com a string "data" aqui
                            Log.d("Firebase", "Valor recuperado com sucesso: " + data);
                        } else {
                            Log.d("Firebase", "O nó /data não existe");
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("Firebase", "Erro ao recuperar valor do nó /data: " + error.getMessage());
                    }
                }); //2 pegamos o nó que estão os dados
            }

        });

        //Referencia para pegar os dados "I" do Firebase
        referencia.child("i").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int data_int = snapshot.getValue(Integer.class);
                    updateChart(data_int);
                    Log.d("Firebase", "Valor recuperado com sucesso: " + data_int);
                } else {
                    Log.d("Firebase", "O nó /i não existe");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Erro ao recuperar valor do nó /data: " + error.getMessage());
            }
        });









        /*ic_botoes2.setOnClickListener(new View.OnClickListener() {
           public void onClick(View view) {
            }
        }

        ic_botoes3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

            }
        }

        ic_botoes4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            }
        }*/








    } //Fim do OnCreate



    //Função para atualizar os dados do grafico com as informações do firebase
    private void updateChart(int data_int) {
        PieChart chart = findViewById(R.id.chart);

        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(200, "Capacidade Total"));
        entries.add(new PieEntry(data_int, "Pessoas no local"));

        PieDataSet pieDataSet = new PieDataSet(entries, "");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setColors(Color.RED, Color.YELLOW);
        pieDataSet.setValueTextSize(20f);

        PieData pieData = new PieData(pieDataSet);
        chart.setData(pieData);
        chart.getDescription().setEnabled(false);
        chart.setDrawEntryLabels(false);

        chart.invalidate();
    }









    @Override
    protected void onStart() {
        super.onStart();

        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference documentReference = db.collection("Usuarios").document(usuarioID);

        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (documentSnapshot != null){
                    nome.setText(documentSnapshot.getString("nome"));
                    empresa.setText(documentSnapshot.getString("empresa"));
                }
            }
        });


    }

    private void IniciarComponentes(){
        id_card = findViewById(R.id.ids); //6 referencia ao edit text da tela_entrar
        nome = findViewById(R.id.text_name);
        empresa = findViewById(R.id.text_empresa);
        bt_foto = findViewById(R.id.containerBotoes);
        bt_deslogar = findViewById(R.id.botao_deslogar);
        ic_botoes = findViewById(R.id.icones_botoes);
        ic_botoes2 = findViewById(R.id.icones_botoes2);
        ic_botoes3 = findViewById(R.id.icones_botoes3);
        ic_botoes4 = findViewById(R.id.icones_botoes4);

    }
}