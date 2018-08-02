package iot.project.caretaker;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import iot.project.caretaker.interfacing_bridge.Server;
import iot.project.caretaker.interfacing_bridge.model.ServerData;
import iot.project.caretaker.interfacing_bridge.Processes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Akrit on 19-Feb-18.
 */

public class Quicktab extends AppCompatActivity {

    Button coffee_btn,drinks_btn,milk_btn,cancle_btn,serve_btn;
    EditText enter_time;
    TextView prc_indicator,time_indicator;
    Processes prc_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quicktab);

        coffee_btn = (Button) findViewById(R.id.cofe_btn);
        drinks_btn = (Button) findViewById(R.id.drnk_btn);
        milk_btn = (Button) findViewById(R.id.mlk_btn);
        cancle_btn = (Button) findViewById(R.id.cncl_btn);
        serve_btn = (Button) findViewById(R.id.serv_btn);
        enter_time = (EditText) findViewById(R.id.entr_mins);
        prc_indicator = (TextView) findViewById(R.id.indicator);
        time_indicator = (TextView) findViewById(R.id.time_indicator);

        //Getting Services from the Server...!
        Server server = new Server();
        prc_data = server.getProcess();

       coffee_btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(final View v_cofe) {
               prc_indicator.setText(R.string.coffee_btn);
               serve_btn.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       String getTime = enter_time.getText().toString();
                       if (TextUtils.isEmpty(getTime)){
                           Toast.makeText(getApplicationContext(),"Serving.....",Toast.LENGTH_SHORT).show();
                           process(v_cofe); //Calling Process
                       }
                       else {
                           final int minutes = Integer.parseInt(getTime) * 60;//Convert minutes into milliseconds
                           final CountDownTimer countDownTimer =  new CountDownTimer(minutes*1000,1000) {
                               @Override
                               public void onTick(long millisUntilFinished) {
                                   String hms = String.format("%02d:%02d:%02d",
                                           TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                                           TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                                           TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                                   time_indicator.setText(hms);
                                   //Disabling Buttons to avoid mixture....
                                    coffee_btn.setEnabled(false);
                                    drinks_btn.setEnabled(false);
                                    milk_btn.setEnabled(false);
                                    enter_time.setEnabled(false);
                               }

                               @Override
                               public void onFinish() {
                                   process(v_cofe); //Calling Process

                                   //Enabling Buttons back.....
                                   coffee_btn.setEnabled(true);
                                   drinks_btn.setEnabled(true);
                                   milk_btn.setEnabled(true);
                                   enter_time.setEnabled(true);
                                   time_indicator.setText("Coffee Served");
                               }


                           };
                           countDownTimer.start();
                           cancle_btn.setOnClickListener(new View.OnClickListener() {
                               @Override
                               public void onClick(View v) {
                                   final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Quicktab.this);
                                   alertDialog.setTitle("Cancle Timer");
                                   alertDialog.setMessage("Are you sure ?");
                                   alertDialog.setCancelable(false);
                                   alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                       @Override
                                       public void onClick(DialogInterface dialog, int which) {
                                           //Enabling Buttons back.....
                                           coffee_btn.setEnabled(true);
                                           drinks_btn.setEnabled(true);
                                           milk_btn.setEnabled(true);
                                           enter_time.setEnabled(true);
                                           time_indicator.setText("00:00:00");
                                           countDownTimer.cancel();
                                       }
                                   });
                                   alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                       @Override
                                       public void onClick(DialogInterface dialog, int which) {

                                       }
                                   });
                                   alertDialog.create();
                                   alertDialog.show();
                               }

                           });
                       }
                   }
               });
           }
       });

       drinks_btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(final View v_drnk) {
                prc_indicator.setText(R.string.drink_btn);
                serve_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String getTime = enter_time.getText().toString();
                        if (TextUtils.isEmpty(getTime)){
                            Toast.makeText(getApplicationContext(),"Serving.....",Toast.LENGTH_SHORT).show();
                            process(v_drnk); //Calling Specific Process
                        }
                        else {
                            int minutes = Integer.parseInt(getTime) * 60;//Convert minutes into milliseconds
                            final CountDownTimer countDownTimer = new CountDownTimer(minutes*1000,1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                    String hms = String.format("%02d:%02d:%02d",
                                            TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                                    time_indicator.setText(hms);
                                    //Disabling Buttons to avoid mixture....
                                    coffee_btn.setEnabled(false);
                                    drinks_btn.setEnabled(false);
                                    milk_btn.setEnabled(false);
                                    enter_time.setEnabled(false);
                                }

                                @Override
                                public void onFinish() {
                                    process(v_drnk); //Calling Process
                                    //Enabling Buttons back.....
                                    coffee_btn.setEnabled(true);
                                    drinks_btn.setEnabled(true);
                                    milk_btn.setEnabled(true);
                                    enter_time.setEnabled(true);
                                    time_indicator.setText("Drink Served");
                                }
                            };
                            countDownTimer.start();

                            cancle_btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Quicktab.this);
                                    alertDialog.setTitle("Cancle Timer");
                                    alertDialog.setMessage("Are you sure ?");
                                    alertDialog.setCancelable(false);
                                    alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //Enabling Buttons back.....
                                            coffee_btn.setEnabled(true);
                                            drinks_btn.setEnabled(true);
                                            milk_btn.setEnabled(true);
                                            enter_time.setEnabled(true);
                                            time_indicator.setText("00:00:00");
                                            countDownTimer.cancel();
                                        }
                                    });
                                    alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });
                                    alertDialog.create();
                                    alertDialog.show();
                                }
                            });
                        }
                    }
                });
           }
       });

       milk_btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(final View v_mlk) {
               prc_indicator.setText(R.string.mlk_btn);
               serve_btn.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       String getTime = enter_time.getText().toString();
                       if (TextUtils.isEmpty(getTime)){
                           Toast.makeText(getApplicationContext(),"Serving.....",Toast.LENGTH_SHORT).show();
                           process(v_mlk); //Calling Process
                       }
                       else {
                           int minutes = Integer.parseInt(getTime) * 60;//Convert minutes into milliseconds
                           final CountDownTimer countDownTimer = new CountDownTimer(minutes*1000,1000) {
                               @Override
                               public void onTick(long millisUntilFinished) {
                                   String hms = String.format("%02d:%02d:%02d",
                                           TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                                           TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                                           TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                                   time_indicator.setText(hms);
                                   //Disabling Buttons to avoid mixture....
                                   coffee_btn.setEnabled(false);
                                   drinks_btn.setEnabled(false);
                                   milk_btn.setEnabled(false);
                               }

                               @Override
                               public void onFinish() {
                                   process(v_mlk); //Calling Process
                                   //Enabling Buttons back.....
                                   coffee_btn.setEnabled(true);
                                   drinks_btn.setEnabled(true);
                                   milk_btn.setEnabled(true);
                                   time_indicator.setText("Milk Served");
                               }
                           };
                           countDownTimer.start();

                           cancle_btn.setOnClickListener(new View.OnClickListener() {
                               @Override
                               public void onClick(View v) {
                                   final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Quicktab.this);
                                   alertDialog.setTitle("Cancle Timer");
                                   alertDialog.setMessage("Are you sure ?");
                                   alertDialog.setCancelable(false);
                                   alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                       @Override
                                       public void onClick(DialogInterface dialog, int which) {
                                           //Enabling Buttons back.....
                                           coffee_btn.setEnabled(true);
                                           drinks_btn.setEnabled(true);
                                           milk_btn.setEnabled(true);
                                           time_indicator.setText("00:00:00");
                                           countDownTimer.cancel();
                                       }
                                   });
                                   alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                       @Override
                                       public void onClick(DialogInterface dialog, int which) {

                                       }
                                   });
                                   alertDialog.create();
                                   alertDialog.show();
                               }
                           });
                       }
                   }
               });
           }
       });
    }

    private void process(View v){
        int process_id = v.getId();

        if (process_id == R.id.cofe_btn) {
            prc_data.getCoffee().enqueue(new Callback<ServerData>() {
                @Override
                public void onResponse(Call<ServerData> call, Response<ServerData> response) {
                    prc_indicator.setText(response.body().getCoffee());
                }

                @Override
                public void onFailure(Call<ServerData> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), R.string.error_in_retrival, Toast.LENGTH_LONG).show();
                }
            });
        }
        else if (process_id == R.id.drnk_btn){
                prc_data.getdrink().enqueue(new Callback<ServerData>() {
                    @Override
                    public void onResponse(Call<ServerData> call, Response<ServerData> response) {
                        prc_indicator.setText(response.body().getDrink());
                    }

                    @Override
                    public void onFailure(Call<ServerData> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), R.string.error_in_retrival, Toast.LENGTH_LONG).show();
                    }
                });
        }
        else if (process_id == R.id.mlk_btn){
                prc_data.getMilk().enqueue(new Callback<ServerData>() {
                    @Override
                    public void onResponse(Call<ServerData> call, Response<ServerData> response) {
                        prc_indicator.setText(response.body().getMilk());
                    }

                    @Override
                    public void onFailure(Call<ServerData> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), R.string.error_in_retrival, Toast.LENGTH_LONG).show();
                    }
                });
        }
    }
}
