package com.autodokta.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.autodokta.app.Adapters.Todo;

public class Todo_Services extends AppCompatActivity {
    LinearLayout income_layout,service_layout, expense_layout, refueling_layout, route_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo__services);
        getSupportActionBar().setTitle("Services");
        income_layout = (LinearLayout) findViewById(R.id.income_layout);
        service_layout = (LinearLayout) findViewById(R.id.servicing_layout);
        expense_layout= (LinearLayout) findViewById(R.id.expense_layout);
        refueling_layout = (LinearLayout) findViewById(R.id.refueling_layout);
        route_layout = (LinearLayout) findViewById(R.id.route_layout);

        income_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goto_todo_income = new Intent(Todo_Services.this, Todo_Income.class);
                goto_todo_income.putExtra("specific_todo_id",getIntent().getStringExtra("the_todoID"));
                startActivity(goto_todo_income);
            }
        });

        refueling_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goto_todo_refuel = new Intent(Todo_Services.this,Todo_Refuel.class);
                goto_todo_refuel.putExtra("specific_todo_id",getIntent().getStringExtra("the_todoID"));
                startActivity(goto_todo_refuel);
            }
        });

        service_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goto_todo_inner_service = new Intent(Todo_Services.this, Todo_inner_Service.class);
                goto_todo_inner_service.putExtra("specific_todo_id",getIntent().getStringExtra("the_todoID"));
                startActivity(goto_todo_inner_service);
            }
        });
    }
}
