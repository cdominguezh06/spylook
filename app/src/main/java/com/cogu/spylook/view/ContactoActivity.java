package com.cogu.spylook.view;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.cogu.spylook.R;
import com.cogu.spylook.adapters.SliderAdapter;
import com.cogu.spylook.DAO.ContactoDAO;
import com.cogu.spylook.bbdd.AppDatabase;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ContactoActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private TextView title;
    private AppDatabase db;
    private ContactoDAO contactoDAO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_contacto);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        db = AppDatabase.getInstance(this);
        contactoDAO = db.contactoDAO();
        title = findViewById(R.id.contactoTitle);
        title.setText(contactoDAO.findContactoById(getIntent().getIntExtra("id", 0)).getNombre());
        viewPager = findViewById(R.id.pager);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager.setAdapter(new SliderAdapter(this, contactoDAO.findContactoById(getIntent().getIntExtra("id", 0)),this));
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Información");
                            break;
                        case 1:
                            tab.setText("Amigos");
                            break;
                        // Puedes agregar más casos aquí para más pestañas
                    }
                }).attach();
    }
}