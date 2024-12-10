package nure.khudiienkomykyta.practtask3_2;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


// Створюємо Handler, прив'язаний до головного потоку
        Handler handler = new Handler(Looper.getMainLooper());

// Дія, яка виконується при натисканні кнопки
        Button startHandlerButton = findViewById(R.id.startHandlerButton);
        startHandlerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Створюємо Runnable, який буде запущено через 2 секунди
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Оновлюємо текстове поле
                        TextView textView = findViewById(R.id.handlerMessageTextView);
                        textView.setText("Handler executed after delay");
                    }
                }, 2000); // 2 секунди затримки
            }
        });


        //Взаємодія між потоками за допомогою Handler

        Button backgroundThreadButton = findViewById(R.id.backgroundThreadButton);
        backgroundThreadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // Деяка довга операція в фоновому потоці
                        try {
                            Thread.sleep(3000); // 3 секунди
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // Відправляємо повідомлення в головний потік для оновлення інтерфейсу
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                TextView textView = findViewById(R.id.handlerMessageTextView);
                                textView.setText("Updated from background thread");
                            }
                        });
                    }
                }).start();
            }
        });



        // Створюємо Handler для обробки повідомлень
        Handler messageHandler  = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                // Отримуємо повідомлення та оновлюємо інтерфейс
                TextView textView = findViewById(R.id.handlerMessageTextView);
                textView.setText("Message received: " + msg.what);
            }
        };
        Button sendMessageButton = findViewById(R.id.sendMessageButton);
        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Відправляємо повідомлення після 2 секунд
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // Створюємо та надсилаємо повідомлення
                        Message msg = messageHandler.obtainMessage();
                        msg.what = 1;  // Код повідомлення
                        messageHandler.sendMessage(msg);
                    }
                }).start();
            }
        });

        //ворення HandlerThread
        HandlerThread handlerThread = new HandlerThread("BackgroundThread");
        handlerThread.start();
        Handler backgroundHandler = new Handler(handlerThread.getLooper());

        // Створюємо Handler для обробки повідомлень
        Handler messageHandler2  = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                // Отримуємо повідомлення та оновлюємо інтерфейс
                TextView textView = findViewById(R.id.handlerMessageTextView);
                textView.setText("Message received: " + msg.obj);
            }
        };

        Button handlerThreadButton = findViewById(R.id.handlerThreadButton);
        handlerThreadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backgroundHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // Створюємо та надсилаємо повідомлення
                        Message msg = messageHandler2.obtainMessage();
                        msg.obj = "handlerThread";  // Код повідомлення
                        messageHandler2.sendMessage(msg);
                    }
                });

            }
        });


    }
}