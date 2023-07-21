package com.example.myapplicationtest.ui.home;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myapplicationtest.FirebaseDatabaseHelper;
import com.example.myapplicationtest.R;
import com.example.myapplicationtest.databinding.FragmentHomeBinding;
import com.example.myapplicationtest.internet.AlertDialogCustom;
import com.example.myapplicationtest.internet.ConnectionDetector;
import com.example.myapplicationtest.lessons.collectWord.MainActivity;
import com.orhanobut.hawk.Hawk;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class HomeFragment extends Fragment {

    BroadcastReceiver mNetworkReceiver;
    static AlertDialogCustom md;
    private FragmentHomeBinding binding;
    private android.app.AlertDialog dialog;

       int     lesson1Progress, lesson2Progress, lesson3Progress,
               lesson4Progress, lesson5Progress, lesson6Progress,
               lesson7Progress, lesson8Progress, lesson9Progress,
               lesson10Progress, lesson11Progress, lesson12Progress,
               lesson13Progress, lesson14Progress, lesson15Progress,
               lesson16Progress, lesson17Progress, lesson18Progress,
               lesson19Progress, lesson20Progress, lesson21Progress,
               lesson22Progress, lesson23Progress, lesson24Progress,
               lesson25Progress, lesson26Progress, lesson27Progress,
               lesson28Progress, lesson29Progress, lesson30Progress,
               lesson31Progress, lesson32Progress, lesson33Progress,
               lesson34Progress, lesson35Progress, lesson36Progress,
               lesson37Progress, lesson38Progress, lesson39Progress,
               lesson40Progress, lesson41Progress, lesson42Progress,
               lesson43Progress, lesson44Progress, lesson45Progress,
               lesson46Progress, lesson47Progress, lesson48Progress,
               lesson49Progress, lesson50Progress, lesson51Progress,
               lesson52Progress, lesson53Progress, lesson54Progress,
               lesson55Progress, lesson56Progress, lesson57Progress,
               lesson58Progress, lesson59Progress, lesson60Progress,
               lesson61Progress, lesson62Progress, lesson63Progress,
               lesson64Progress, lesson65Progress, lesson66Progress,
               lesson67Progress, lesson68Progress, lesson69Progress,
               lesson70Progress, lesson71Progress, lesson72Progress,
               lesson73Progress, lesson74Progress, lesson75Progress,
               lesson76Progress, lesson77Progress, lesson78Progress,
               lesson79Progress, lesson80Progress, lesson81Progress,
               lesson82Progress, lesson83Progress, lesson84Progress;


    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        md = new AlertDialogCustom(getActivity(), "Пожалуйста\nпроверьте подключение к интернету...");
        mNetworkReceiver = new ConnectionDetector(); // для проверки интернета
        registerNetworkBroadcastForNougat();
        Hawk.init(getContext()).build();
        FirebaseDatabaseHelper.getDailyGoal();
        // обновление недели по понедельникам
        try {
            updateWeek();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        getOverallProgress(); // метод установки прогресса в %

        // показ непрерывных дней
       binding.overallProgress.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               LayoutInflater inflater = LayoutInflater.from(getContext()); // место отображения

               View dialoglayout = inflater.inflate(R.layout.continuous_day, null); // получение размеки

               final TextView title = dialoglayout.findViewById(R.id.myTextDay);
               title.setText(Hawk.get("continuousDay").toString());
                       android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
               builder.setView(dialoglayout);

               dialog = builder.create(); // построение макета
               // для видимости участков вне макета
               dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
               dialog.show(); // показываем
           }
       });

        setupBar(); // заполнение круглишочков

        binding.lesson1Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hawk.put("lesson", "lesson1");
                Hawk.put("number", 1);
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        binding.lesson2Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hawk.put("lesson", "lesson2");
                Hawk.put("number", 2);
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        binding.lesson3Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hawk.put("lesson", "lesson3");
                Hawk.put("number", 3);
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        binding.lesson4Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hawk.put("lesson", "lesson4");
                Hawk.put("number", 4);
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        binding.lesson5Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hawk.put("lesson", "lesson5");
                Hawk.put("number", 5);
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        binding.lesson6Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hawk.put("lesson", "lesson6");
                Hawk.put("number", 6);
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        binding.lesson7Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hawk.put("lesson", "lesson7");
                Hawk.put("number", 7);
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        binding.lesson8Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hawk.put("lesson", "lesson8");
                Hawk.put("number", 8);
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        binding.lesson9Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hawk.put("lesson", "lesson9");
                Hawk.put("number", 9);
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        binding.lesson10Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hawk.put("lesson", "lesson10");
                Hawk.put("number", 10);
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        binding.lesson11Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hawk.put("lesson", "lesson11");
                Hawk.put("number", 11);
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        binding.lesson12Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hawk.put("lesson", "lesson12");
                Hawk.put("number", 12);
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        binding.lesson13Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hawk.put("lesson", "lesson13");
                Hawk.put("number", 13);
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        binding.lesson14Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hawk.put("lesson", "lesson14");
                Hawk.put("number", 14);
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        binding.lesson15Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hawk.put("lesson", "lesson15");
                Hawk.put("number", 15);
              Intent intent = new Intent(getContext(), MainActivity.class);
               startActivity(intent);
            }
        });
        binding.lesson16Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Hawk.put("lesson", "lesson16");
                Hawk.put("number", 16);
               Intent intent = new Intent(getContext(), MainActivity.class);
               startActivity(intent);
            }
        });
        binding.lesson17Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hawk.put("lesson", "lesson17");
                Hawk.put("number", 17);
               Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        binding.lesson18Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hawk.put("lesson", "lesson18");
                Hawk.put("number", 18);
               Intent intent = new Intent(getContext(), MainActivity.class);
               startActivity(intent);
            }
        });
        binding.lesson19Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hawk.put("lesson", "lesson19");
                Hawk.put("number", 19);
               Intent intent = new Intent(getContext(), MainActivity.class);
               startActivity(intent);
            }
        });
        binding.lesson20Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hawk.put("lesson", "lesson20");
                Hawk.put("number", 20);
               Intent intent = new Intent(getContext(), MainActivity.class);
               startActivity(intent);
            }
        });
        binding.lesson21Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hawk.put("lesson", "lesson21");
                Hawk.put("number", 21);
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        binding.lesson22Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hawk.put("lesson", "lesson22");
                Hawk.put("number", 22);
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        binding.lesson23Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hawk.put("lesson", "lesson23");
                Hawk.put("number", 23);
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        binding.lesson24Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Hawk.put("lesson", "lesson24");
                Hawk.put("number", 24);
               Intent intent = new Intent(getContext(), MainActivity.class);
               startActivity(intent);
            }
        });
        binding.lesson25Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hawk.put("lesson", "lesson25");
                Hawk.put("number", 25);
                Intent intent = new Intent(getContext(), MainActivity.class);
               startActivity(intent);
            }
        });
        binding.lesson26Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Hawk.put("lesson", "lesson26");
                Hawk.put("number", 26);
              Intent intent = new Intent(getContext(), MainActivity.class);
               startActivity(intent);
            }
        });
        binding.lesson27Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Hawk.put("lesson", "lesson27");
                Hawk.put("number", 27);
               Intent intent = new Intent(getContext(), MainActivity.class);
               startActivity(intent);
            }
        });
        binding.lesson28Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hawk.put("lesson", "lesson28");
                Hawk.put("number", 28);
                Intent intent = new Intent(getContext(), MainActivity.class);
               startActivity(intent);
            }
        });
        binding.lesson29Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hawk.put("lesson", "lesson29");
                Hawk.put("number", 29);
               Intent intent = new Intent(getContext(), MainActivity.class);
               startActivity(intent);
            }
        });
        binding.lesson30Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hawk.put("lesson", "lesson30");
                Hawk.put("number", 30);
                Intent intent = new Intent(getContext(), MainActivity.class);
              startActivity(intent);
            }
        });
        binding.lesson31Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Hawk.put("lesson", "lesson31");
                Hawk.put("number", 31);
                Intent intent = new Intent(getContext(), MainActivity.class);
               startActivity(intent);
            }
        });
        binding.lesson32Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hawk.put("lesson", "lesson32");
                Hawk.put("number", 32);
                Intent intent = new Intent(getContext(), MainActivity.class);
               startActivity(intent);
            }
        });
        binding.lesson33Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hawk.put("lesson", "lesson33");
                Hawk.put("number", 33);
               Intent intent = new Intent(getContext(), MainActivity.class);
               startActivity(intent);
            }
        });
        binding.lesson34Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hawk.put("lesson", "lesson34");
                Hawk.put("number", 34);
               Intent intent = new Intent(getContext(), MainActivity.class);
               startActivity(intent);
            }
        });
        binding.lesson35Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hawk.put("lesson", "lesson35");
                Hawk.put("number", 35);
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        binding.lesson36Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hawk.put("lesson", "lesson36");
                Hawk.put("number", 36);
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        binding.lesson37Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hawk.put("lesson", "lesson37");
                Hawk.put("number", 37);
               Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        binding.lesson38Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hawk.put("lesson", "lesson38");
                Hawk.put("number", 38);
               Intent intent = new Intent(getContext(), MainActivity.class);
               startActivity(intent);
            }
        });
        binding.lesson39Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hawk.put("lesson", "lesson39");
                Hawk.put("number", 39);
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        binding.lesson40Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Hawk.put("lesson", "lesson40");
                Hawk.put("number", 40);
               Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        binding.lesson41Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Hawk.put("lesson", "lesson41");
                Hawk.put("number", 41);
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        binding.lesson42Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hawk.put("lesson", "lesson42");
                Hawk.put("number", 42);
               Intent intent = new Intent(getContext(), MainActivity.class);
               startActivity(intent);
            }
        });
        binding.lesson43Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hawk.put("lesson", "lesson43");
                Hawk.put("number", 43);
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        binding.lesson44Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Hawk.put("lesson", "lesson44");
                Hawk.put("number", 44);
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        binding.lesson45Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hawk.put("lesson", "lesson45");
                Hawk.put("number", 45);
               Intent intent = new Intent(getContext(), MainActivity.class);
               startActivity(intent);
            }
        });
        binding.lesson46Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hawk.put("lesson", "lesson46");
                Hawk.put("number", 46);
               Intent intent = new Intent(getContext(), MainActivity.class);
               startActivity(intent);
            }
        });
        binding.lesson47Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hawk.put("lesson", "lesson47");
                Hawk.put("number", 47);
               Intent intent = new Intent(getContext(), MainActivity.class);
               startActivity(intent);
            }
        });
        binding.lesson48Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Hawk.put("lesson", "lesson48");
                Hawk.put("number", 48);
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        binding.lesson49Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Hawk.put("lesson", "lesson49");
                Hawk.put("number", 49);
               Intent intent = new Intent(getContext(), MainActivity.class);
               startActivity(intent);
            }
        });
        binding.lesson50Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Hawk.put("lesson", "lesson50");
                Hawk.put("number", 50);
               Intent intent = new Intent(getContext(), MainActivity.class);
              startActivity(intent);
            }
        });
        binding.lesson51Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hawk.put("lesson", "lesson51");
                Hawk.put("number", 51);
              Intent intent = new Intent(getContext(), MainActivity.class);
               startActivity(intent);
            }
        });
        binding.lesson52Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hawk.put("lesson", "lesson52");
                Hawk.put("number", 52);
                Intent intent = new Intent(getContext(), MainActivity.class);
               startActivity(intent);
            }
        });
        binding.lesson53Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hawk.put("lesson", "lesson53");
                Hawk.put("number", 53);
                Intent intent = new Intent(getContext(), MainActivity.class);
               startActivity(intent);
            }
        });
        binding.lesson54Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hawk.put("lesson", "lesson54");
                Hawk.put("number", 54);
                Intent intent = new Intent(getContext(), MainActivity.class);
               startActivity(intent);
            }
        });
        binding.lesson55Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hawk.put("lesson", "lesson55");
                Hawk.put("number", 55);
                Intent intent = new Intent(getContext(), MainActivity.class);
               startActivity(intent);
            }
        });
        binding.lesson56Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Hawk.put("lesson", "lesson56");
                Hawk.put("number", 56);
               Intent intent = new Intent(getContext(), MainActivity.class);
               startActivity(intent);
            }
        });
        binding.lesson57Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hawk.put("lesson", "lesson57");
                Hawk.put("number", 57);
               Intent intent = new Intent(getContext(), MainActivity.class);
              startActivity(intent);
            }
        });
        binding.lesson58Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Hawk.put("lesson", "lesson58");
                Hawk.put("number", 58);
                Intent intent = new Intent(getContext(), MainActivity.class);
               startActivity(intent);
            }
        });
        binding.lesson59Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Hawk.put("lesson", "lesson59");
                Hawk.put("number", 59);
               Intent intent = new Intent(getContext(), MainActivity.class);
              startActivity(intent);
            }
        });
        binding.lesson60Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Hawk.put("lesson", "lesson60");
                Hawk.put("number", 60);
               Intent intent = new Intent(getContext(), MainActivity.class);
               startActivity(intent);
            }
        });
        binding.lesson61Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Hawk.put("lesson", "lesson61");
                Hawk.put("number", 61);
               Intent intent = new Intent(getContext(), MainActivity.class);
              startActivity(intent);
            }
        });
        binding.lesson62Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hawk.put("lesson", "lesson62");
                Hawk.put("number", 62);
                Intent intent = new Intent(getContext(), MainActivity.class);
             startActivity(intent);
            }
        });
        binding.lesson63Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Hawk.put("lesson", "lesson63");
                Hawk.put("number", 63);
               Intent intent = new Intent(getContext(), MainActivity.class);
              startActivity(intent);
            }
        });
        binding.lesson64Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Hawk.put("lesson", "lesson64");
                Hawk.put("number", 64);
              Intent intent = new Intent(getContext(), MainActivity.class);
               startActivity(intent);
            }
        });
        binding.lesson64Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Hawk.put("lesson", "lesson65");
                Hawk.put("number", 65);
               Intent intent = new Intent(getContext(), MainActivity.class);
               startActivity(intent);
            }
        });
        binding.lesson66Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Hawk.put("lesson", "lesson66");
                Hawk.put("number", 66);
                Intent intent = new Intent(getContext(), MainActivity.class);
               startActivity(intent);
            }
        });
        binding.lesson67Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hawk.put("lesson", "lesson67");
                Hawk.put("number", 67);
               Intent intent = new Intent(getContext(), MainActivity.class);
              startActivity(intent);
            }
        });
        binding.lesson68Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hawk.put("lesson", "lesson68");
                Hawk.put("number", 68);
              Intent intent = new Intent(getContext(), MainActivity.class);
               startActivity(intent);
            }
        });
        binding.lesson69Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Hawk.put("lesson", "lesson69");
                Hawk.put("number", 69);
               Intent intent = new Intent(getContext(), MainActivity.class);
              startActivity(intent);
            }
        });
        binding.lesson70Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Hawk.put("lesson", "lesson70");
                Hawk.put("number", 70);
               Intent intent = new Intent(getContext(), MainActivity.class);
               startActivity(intent);
            }
        });
        binding.lesson71Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Hawk.put("lesson", "lesson71");
                Hawk.put("number", 71);
              Intent intent = new Intent(getContext(), MainActivity.class);
              startActivity(intent);
            }
        });
        binding.lesson72Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hawk.put("lesson", "lesson72");
                Hawk.put("number", 72);
               Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        binding.lesson73Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Hawk.put("lesson", "lesson73");
                Hawk.put("number", 73);
              Intent intent = new Intent(getContext(), MainActivity.class);
               startActivity(intent);
            }
        });
        binding.lesson74Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hawk.put("lesson", "lesson74");
                Hawk.put("number", 74);
              Intent intent = new Intent(getContext(), MainActivity.class);
               startActivity(intent);
            }
        });
        binding.lesson75Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Hawk.put("lesson", "lesson75");
                Hawk.put("number", 75);
               Intent intent = new Intent(getContext(), MainActivity.class);
               startActivity(intent);
            }
        });
        binding.lesson76Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hawk.put("lesson", "lesson76");
                Hawk.put("number", 76);
               Intent intent = new Intent(getContext(), MainActivity.class);
               startActivity(intent);
            }
        });
        binding.lesson77Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hawk.put("lesson", "lesson77");
                Hawk.put("number", 77);
                Intent intent = new Intent(getContext(), MainActivity.class);
               startActivity(intent);
            }
        });
        binding.lesson78Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Hawk.put("lesson", "lesson78");
                Hawk.put("number", 78);
             Intent intent = new Intent(getContext(), MainActivity.class);
               startActivity(intent);
            }
        });
        binding.lesson79Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hawk.put("lesson", "lesson79");
                Hawk.put("number", 79);
                Intent intent = new Intent(getContext(), MainActivity.class);
               startActivity(intent);
            }
        });
        binding.lesson80Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hawk.put("lesson", "lesson80");
                Hawk.put("number", 80);
                Intent intent = new Intent(getContext(), MainActivity.class);
               startActivity(intent);
            }
        });
        binding.lesson81Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Hawk.put("lesson", "lesson81");
                Hawk.put("number", 81);
              Intent intent = new Intent(getContext(), MainActivity.class);
               startActivity(intent);
            }
        });
        binding.lesson82Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Hawk.put("lesson", "lesson82");
                Hawk.put("number", 82);
                Intent intent = new Intent(getContext(), MainActivity.class);
               startActivity(intent);
            }
        });
        binding.lesson83Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Hawk.put("lesson", "lesson83");
                Hawk.put("number", 83);
               Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        binding.lesson84Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Hawk.put("lesson", "lesson84");
                Hawk.put("number", 84);
               Intent intent = new Intent(getContext(), MainActivity.class);
               startActivity(intent);
            }
        });

    return root;
    }

    private void getOverallProgress() {

        if(Hawk.get("overallProgress") != null){
            if((double)Hawk.get("overallProgress") > (double)100){
                binding.overallProgress.setText("Overall Progress: 100%");
            }
            else
            {
                DecimalFormat df = new DecimalFormat("#.##");
                binding.overallProgress.setText("Overall Progress: " + df.format(Hawk.get("overallProgress")) + "%");
            }
        } else {
            binding.overallProgress.setText("Overall Progress: 0%");
        }

    }

    private void updateWeek() throws ParseException {
        // получение текущего дня
        Date date = Calendar.getInstance().getTime();
        // формат дня недели словом
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE", Locale.US);
        // формат дня с отображением года, месяца, числа
        DateFormat form= new SimpleDateFormat("yyyy-MM-dd");
        // получение дня недели словом
        final String dayOfWeek = dateFormat.format(date);
        boolean week = false;
        try {
            week = (boolean) Hawk.get("updateWeek");
        } catch (Exception e){}

        String today = form.format(date);
        Date firstDate = form.parse(today);
        try {
            // последний ударный день в БД
            Date secondDate = form.parse(Hawk.get("lastDay"));
            // раздница между днем в БД и сегодня
            long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
            long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            // если больше 7 дней очищаем неделю
            if (diff > 7) {
                FirebaseDatabaseHelper.clearWeekXp();
            }
            // если сегодня понедельник очищаем неделю
            if (week == true && dayOfWeek.equals("Monday")) {
                FirebaseDatabaseHelper.clearWeekXp();
                FirebaseDatabaseHelper.setUpdateWeek(false);

            } else if (!dayOfWeek.equals("Monday")) {
                FirebaseDatabaseHelper.setUpdateWeek(true);
            }

        } catch (Exception e){}
    }


    private void setupBar() {
        // заполнение прогресса у уроков
        if (Hawk.get("lesson1") != null) {
            lesson1Progress = (int) Hawk.get("lesson1");
        } else {
            lesson1Progress = 0;
        }
        if (Hawk.get("lesson2") != null) {
            lesson2Progress = (int) Hawk.get("lesson2");
        } else {
            lesson2Progress = 0;
        }
        if (Hawk.get("lesson3") != null) {
            lesson3Progress = (int) Hawk.get("lesson3");
        } else {
            lesson3Progress = 0;
        }
        if (Hawk.get("lesson4") != null) {
            lesson4Progress = (int) Hawk.get("lesson4");
        } else {
            lesson4Progress = 0;
        }
        if (Hawk.get("lesson5") != null) {
            lesson5Progress =  (int)Hawk.get("lesson5");
        } else {
            lesson5Progress = 0;
        }
        if (Hawk.get("lesson6") != null) {
            lesson6Progress = (int) Hawk.get("lesson6");
        } else {
            lesson6Progress = 0;
        }
        if (Hawk.get("lesson7") != null) {
            lesson7Progress = (int) Hawk.get("lesson7");
        } else {
            lesson7Progress = 0;
        }
        if (Hawk.get("lesson8") != null) {
            lesson8Progress = (int) Hawk.get("lesson8");
        } else {
            lesson8Progress = 0;
        }
        if (Hawk.get("lesson9") != null) {
            lesson9Progress = (int) Hawk.get("lesson9");
        } else {
            lesson9Progress = 0;
        }
        if (Hawk.get("lesson10") != null) {
            lesson10Progress = (int) Hawk.get("lesson10");
        } else {
            lesson10Progress = 0;
        }
        if (Hawk.get("lesson11") != null) {
            lesson11Progress = (int) Hawk.get("lesson11");
        } else {
            lesson11Progress = 0;
        }
        if (Hawk.get("lesson12") != null) {
            lesson12Progress = (int) Hawk.get("lesson12");
        } else {
            lesson12Progress = 0;
        }
        if (Hawk.get("lesson13") != null) {
            lesson13Progress = (int) Hawk.get("lesson13");
        } else {
            lesson13Progress = 0;
        }
        if (Hawk.get("lesson14") != null) {
            lesson14Progress = (int) Hawk.get("lesson14");
        } else {
            lesson14Progress = 0;
        }
        if (Hawk.get("lesson15") != null) {
            lesson15Progress = (int) Hawk.get("lesson15");
        } else {
            lesson15Progress = 0;
        }
        if (Hawk.get("lesson16") != null) {
            lesson16Progress = (int) Hawk.get("lesson16");
        } else {
            lesson16Progress = 0;
        }
        if (Hawk.get("lesson17") != null) {
            lesson17Progress = (int) Hawk.get("lesson17");
        } else {
            lesson17Progress = 0;
        }
        if (Hawk.get("lesson18") != null) {
            lesson18Progress = (int) Hawk.get("lesson18");
        } else {
            lesson18Progress = 0;
        }
        if (Hawk.get("lesson19") != null) {
            lesson19Progress = (int) Hawk.get("lesson19");
        } else {
            lesson19Progress = 0;
        }
        if (Hawk.get("lesson20") != null) {
            lesson20Progress = (int) Hawk.get("lesson20");
        } else {
            lesson20Progress = 0;
        }
        if (Hawk.get("lesson21") != null) {
            lesson21Progress = (int) Hawk.get("lesson21");
        } else {
            lesson21Progress = 0;
        }
        if (Hawk.get("lesson22") != null) {
            lesson22Progress = (int) Hawk.get("lesson22");
        } else {
            lesson22Progress = 0;
        }
        if (Hawk.get("lesson23") != null) {
            lesson23Progress = (int) Hawk.get("lesson23");
        } else {
            lesson23Progress = 0;
        }
        if (Hawk.get("lesson24") != null) {
            lesson24Progress = (int) Hawk.get("lesson24");
        } else {
            lesson24Progress = 0;
        }
        if (Hawk.get("lesson25") != null) {
            lesson25Progress = (int) Hawk.get("lesson25");
        } else {
            lesson25Progress = 0;
        }
        if (Hawk.get("lesson26") != null) {
            lesson26Progress = (int) Hawk.get("lesson26");
        } else {
            lesson26Progress = 0;
        }
        if (Hawk.get("lesson27") != null) {
            lesson27Progress = (int) Hawk.get("lesson27");
        } else {
            lesson27Progress = 0;
        }
        if (Hawk.get("lesson28") != null) {
            lesson28Progress = (int) Hawk.get("lesson28");
        } else {
            lesson28Progress = 0;
        }
        if (Hawk.get("lesson29") != null) {
            lesson29Progress = (int) Hawk.get("lesson29");
        } else {
            lesson29Progress = 0;
        }
        if (Hawk.get("lesson30") != null) {
            lesson30Progress = (int) Hawk.get("lesson30");
        } else {
            lesson30Progress = 0;
        }
        if (Hawk.get("lesson31") != null) {
            lesson31Progress = (int) Hawk.get("lesson31");
        } else {
            lesson31Progress = 0;
        }
        if (Hawk.get("lesson32") != null) {
            lesson32Progress = (int) Hawk.get("lesson32");
        } else {
            lesson32Progress = 0;
        }
        if (Hawk.get("lesson33") != null) {
            lesson33Progress = (int) Hawk.get("lesson33");
        } else {
            lesson33Progress = 0;
        }
        if (Hawk.get("lesson34") != null) {
            lesson34Progress = (int) Hawk.get("lesson34");
        } else {
            lesson34Progress = 0;
        }
        if (Hawk.get("lesson35") != null) {
            lesson35Progress = (int) Hawk.get("lesson35");
        } else {
            lesson35Progress = 0;
        }
        if (Hawk.get("lesson36") != null) {
            lesson36Progress = (int) Hawk.get("lesson36");
        } else {
            lesson36Progress = 0;
        }
        if (Hawk.get("lesson37") != null) {
            lesson37Progress = (int) Hawk.get("lesson37");
        } else {
            lesson37Progress = 0;
        }
        if (Hawk.get("lesson38") != null) {
            lesson38Progress = (int) Hawk.get("lesson38");
        } else {
            lesson38Progress = 0;
        }
        if (Hawk.get("lesson39") != null) {
            lesson39Progress = (int) Hawk.get("lesson39");
        } else {
            lesson39Progress = 0;
        }
        if (Hawk.get("lesson40") != null) {
            lesson40Progress = (int) Hawk.get("lesson40");
        } else {
            lesson40Progress = 0;
        }
        if (Hawk.get("lesson41") != null) {
            lesson41Progress = (int) Hawk.get("lesson41");
        } else {
            lesson41Progress = 0;
        }
        if (Hawk.get("lesson42") != null) {
            lesson42Progress = (int) Hawk.get("lesson42");
        } else {
            lesson42Progress = 0;
        }
        if (Hawk.get("lesson43") != null) {
            lesson43Progress = (int) Hawk.get("lesson43");
        } else {
            lesson43Progress = 0;
        }
        if (Hawk.get("lesson44") != null) {
            lesson44Progress = (int) Hawk.get("lesson44");
        } else {
            lesson44Progress = 0;
        }
        if (Hawk.get("lesson45") != null) {
            lesson45Progress = (int) Hawk.get("lesson45");
        } else {
            lesson45Progress = 0;
        }
        if (Hawk.get("lesson46") != null) {
            lesson46Progress = (int) Hawk.get("lesson46");
        } else {
            lesson46Progress = 0;
        }
        if (Hawk.get("lesson47") != null) {
            lesson47Progress = (int) Hawk.get("lesson47");
        } else {
            lesson47Progress = 0;
        }
        if (Hawk.get("lesson48") != null) {
            lesson48Progress = (int) Hawk.get("lesson48");
        } else {
            lesson48Progress = 0;
        }
        if (Hawk.get("lesson49") != null) {
            lesson49Progress = (int) Hawk.get("lesson49");
        } else {
            lesson49Progress = 0;
        }
        if (Hawk.get("lesson50") != null) {
            lesson50Progress = (int) Hawk.get("lesson50");
        } else {
            lesson50Progress = 0;
        }
        if (Hawk.get("lesson51") != null) {
            lesson51Progress = (int) Hawk.get("lesson51");
        } else {
            lesson51Progress = 0;
        }
        if (Hawk.get("lesson52") != null) {
            lesson52Progress = (int) Hawk.get("lesson52");
        } else {
            lesson52Progress = 0;
        }
        if (Hawk.get("lesson53") != null) {
            lesson53Progress = (int) Hawk.get("lesson53");
        } else {
            lesson53Progress = 0;
        }
        if (Hawk.get("lesson54") != null) {
            lesson54Progress = (int) Hawk.get("lesson54");
        } else {
            lesson54Progress = 0;
        } if (Hawk.get("lesson55") != null) {
            lesson55Progress = (int) Hawk.get("lesson55");
        } else {
            lesson55Progress = 0;
        }
        if (Hawk.get("lesson56") != null) {
            lesson56Progress = (int) Hawk.get("lesson56");
        } else {
            lesson56Progress = 0;
        }
        if (Hawk.get("lesson57") != null) {
            lesson57Progress = (int) Hawk.get("lesson57");
        } else {
            lesson57Progress = 0;
        }
        if (Hawk.get("lesson58") != null) {
            lesson58Progress = (int) Hawk.get("lesson58");
        } else {
            lesson58Progress = 0;
        }
        if (Hawk.get("lesson59") != null) {
            lesson59Progress = (int) Hawk.get("lesson59");
        } else {
            lesson59Progress = 0;
        }
        if (Hawk.get("lesson60") != null) {
            lesson60Progress = (int) Hawk.get("lesson60");
        } else {
            lesson60Progress = 0;
        }
        if (Hawk.get("lesson61") != null) {
            lesson61Progress = (int) Hawk.get("lesson61");
        } else {
            lesson61Progress = 0;
        }
        if (Hawk.get("lesson62") != null) {
            lesson62Progress = (int) Hawk.get("lesson62");
        } else {
            lesson62Progress = 0;
        } if (Hawk.get("lesson63") != null) {
            lesson3Progress = (int) Hawk.get("lesson63");
        } else {
            lesson63Progress = 0;
        } if (Hawk.get("lesson64") != null) {
            lesson64Progress = (int) Hawk.get("lesson64");
        } else {
            lesson64Progress = 0;
        }
        if (Hawk.get("lesson65") != null) {
            lesson65Progress = (int) Hawk.get("lesson65");
        } else {
            lesson65Progress = 0;
        }
        if (Hawk.get("lesson66") != null) {
            lesson66Progress = (int) Hawk.get("lesson66");
        } else {
            lesson66Progress = 0;
        } if (Hawk.get("lesson67") != null) {
            lesson67Progress = (int) Hawk.get("lesson67");
        } else {
            lesson67Progress = 0;
        }
        if (Hawk.get("lesson68") != null) {
            lesson68Progress = (int) Hawk.get("lesson68");
        } else {
            lesson68Progress = 0;
        }
        if (Hawk.get("lesson69") != null) {
            lesson69Progress = (int) Hawk.get("lesson69");
        } else {
            lesson69Progress = 0;
        }
        if (Hawk.get("lesson70") != null) {
            lesson70Progress = (int) Hawk.get("lesson70");
        } else {
            lesson70Progress = 0;
        }
        if (Hawk.get("lesson71") != null) {
            lesson71Progress = (int) Hawk.get("lesson71");
        } else {
            lesson71Progress = 0;
        } if (Hawk.get("lesson72") != null) {
            lesson72Progress = (int) Hawk.get("lesson72");
        } else {
            lesson72Progress = 0;
        }
        if (Hawk.get("lesson73") != null) {
            lesson73Progress = (int) Hawk.get("lesson73");
        } else {
            lesson73Progress = 0;
        }
        if (Hawk.get("lesson74") != null) {
            lesson74Progress = (int) Hawk.get("lesson74");
        } else {
            lesson74Progress = 0;
        }
        if (Hawk.get("lesson75") != null) {
            lesson75Progress = (int) Hawk.get("lesson75");
        } else {
            lesson75Progress = 0;
        }
        if (Hawk.get("lesson76") != null) {
            lesson76Progress = (int) Hawk.get("lesson76");
        } else {
            lesson76Progress = 0;
        }
        if (Hawk.get("lesson77") != null) {
            lesson77Progress = (int) Hawk.get("lesson77");
        } else {
            lesson77Progress = 0;
        }
        if (Hawk.get("lesson78") != null) {
            lesson78Progress = (int) Hawk.get("lesson78");
        } else {
            lesson78Progress = 0;
        }
        if (Hawk.get("lesson79") != null) {
            lesson79Progress = (int) Hawk.get("lesson79");
        } else {
            lesson79Progress = 0;
        }
        if (Hawk.get("lesson80") != null) {
            lesson80Progress = (int) Hawk.get("lesson80");
        } else {
            lesson80Progress = 0;
        }
        if (Hawk.get("lesson81") != null) {
            lesson81Progress = (int) Hawk.get("lesson81");
        } else {
            lesson81Progress = 0;
        } if (Hawk.get("lesson82") != null) {
            lesson82Progress = (int) Hawk.get("lesson82");
        } else {
            lesson82Progress = 0;
        } if (Hawk.get("lesson83") != null) {
            lesson83Progress = (int) Hawk.get("lesson83");
        } else {
            lesson83Progress = 0;
        } if (Hawk.get("lesson84") != null) {
            lesson84Progress = (int) Hawk.get("lesson84");
        } else {
            lesson84Progress = 0;}

        binding.lesson1Bar.setProgress(lesson1Progress);
        binding.lesson2Bar.setProgress( lesson2Progress);
        binding.lesson3Bar.setProgress( lesson3Progress);
        binding.lesson4Bar.setProgress( lesson4Progress);
        binding.lesson5Bar.setProgress( lesson5Progress);
        binding.lesson6Bar.setProgress( lesson6Progress);
        binding.lesson7Bar.setProgress(lesson7Progress);
        binding.lesson8Bar.setProgress( lesson8Progress);
        binding.lesson9Bar.setProgress( lesson9Progress);
        binding.lesson10Bar.setProgress( lesson10Progress);
        binding.lesson11Bar.setProgress( lesson11Progress);
        binding.lesson12Bar.setProgress( lesson12Progress);
        binding.lesson13Bar.setProgress(lesson13Progress);
        binding.lesson14Bar.setProgress( lesson14Progress);
        binding.lesson15Bar.setProgress( lesson15Progress);
        binding.lesson16Bar.setProgress( lesson16Progress);
        binding.lesson17Bar.setProgress( lesson17Progress);
        binding.lesson18Bar.setProgress( lesson18Progress);
        binding.lesson19Bar.setProgress(lesson19Progress);
        binding.lesson20Bar.setProgress( lesson20Progress);
        binding.lesson21Bar.setProgress( lesson21Progress);
        binding.lesson22Bar.setProgress( lesson22Progress);
        binding.lesson23Bar.setProgress( lesson23Progress);
        binding.lesson24Bar.setProgress( lesson24Progress);
        binding.lesson25Bar.setProgress(lesson25Progress);
        binding.lesson26Bar.setProgress( lesson26Progress);
        binding.lesson27Bar.setProgress( lesson27Progress);
        binding.lesson28Bar.setProgress( lesson28Progress);
        binding.lesson29Bar.setProgress( lesson29Progress);
        binding.lesson30Bar.setProgress( lesson30Progress);
        binding.lesson31Bar.setProgress(lesson31Progress);
        binding.lesson32Bar.setProgress( lesson32Progress);
        binding.lesson33Bar.setProgress( lesson33Progress);
        binding.lesson34Bar.setProgress( lesson34Progress);
        binding.lesson35Bar.setProgress( lesson35Progress);
        binding.lesson36Bar.setProgress( lesson36Progress);
        binding.lesson37Bar.setProgress(lesson37Progress);
        binding.lesson38Bar.setProgress( lesson38Progress);
        binding.lesson39Bar.setProgress( lesson39Progress);
        binding.lesson40Bar.setProgress( lesson40Progress);
        binding.lesson41Bar.setProgress( lesson41Progress);
        binding.lesson42Bar.setProgress( lesson42Progress);
        binding.lesson43Bar.setProgress(lesson43Progress);
        binding.lesson44Bar.setProgress( lesson44Progress);
        binding.lesson45Bar.setProgress( lesson45Progress);
        binding.lesson46Bar.setProgress( lesson46Progress);
        binding.lesson47Bar.setProgress( lesson47Progress);
        binding.lesson48Bar.setProgress( lesson48Progress);
        binding.lesson49Bar.setProgress(lesson49Progress);
        binding.lesson50Bar.setProgress( lesson50Progress);
        binding.lesson51Bar.setProgress( lesson51Progress);
        binding.lesson52Bar.setProgress( lesson52Progress);
        binding.lesson53Bar.setProgress( lesson53Progress);
        binding.lesson54Bar.setProgress( lesson54Progress);
        binding.lesson55Bar.setProgress(lesson55Progress);
        binding.lesson56Bar.setProgress( lesson56Progress);
        binding.lesson57Bar.setProgress( lesson57Progress);
        binding.lesson58Bar.setProgress( lesson58Progress);
        binding.lesson59Bar.setProgress( lesson59Progress);
        binding.lesson60Bar.setProgress( lesson60Progress);
        binding.lesson61Bar.setProgress(lesson61Progress);
        binding.lesson62Bar.setProgress( lesson62Progress);
        binding.lesson63Bar.setProgress( lesson63Progress);
        binding.lesson64Bar.setProgress( lesson64Progress);
        binding.lesson65Bar.setProgress( lesson65Progress);
        binding.lesson66Bar.setProgress( lesson66Progress);
        binding.lesson67Bar.setProgress(lesson67Progress);
        binding.lesson68Bar.setProgress( lesson68Progress);
        binding.lesson69Bar.setProgress( lesson69Progress);
        binding.lesson70Bar.setProgress( lesson70Progress);
        binding.lesson71Bar.setProgress( lesson71Progress);
        binding.lesson72Bar.setProgress( lesson72Progress);
        binding.lesson73Bar.setProgress(lesson73Progress);
        binding.lesson74Bar.setProgress( lesson74Progress);
        binding.lesson75Bar.setProgress( lesson75Progress);
        binding.lesson76Bar.setProgress( lesson76Progress);
        binding.lesson77Bar.setProgress( lesson77Progress);
        binding.lesson78Bar.setProgress( lesson78Progress);
        binding.lesson79Bar.setProgress(lesson79Progress);
        binding.lesson80Bar.setProgress( lesson80Progress);
        binding.lesson81Bar.setProgress( lesson81Progress);
        binding.lesson82Bar.setProgress( lesson82Progress);
        binding.lesson83Bar.setProgress( lesson83Progress);
        binding.lesson84Bar.setProgress( lesson84Progress);

        binding.lesson1Bar.setMax(100);
        binding.lesson2Bar.setMax(100);
        binding.lesson3Bar.setMax(100);
        binding.lesson4Bar.setMax(100);
        binding.lesson5Bar.setMax(100);
        binding.lesson6Bar.setMax(100);
        binding.lesson7Bar.setMax(100);
        binding.lesson8Bar.setMax(100);
        binding.lesson9Bar.setMax(100);
        binding.lesson10Bar.setMax(100);
        binding.lesson11Bar.setMax(100);
        binding.lesson12Bar.setMax(100);
        binding.lesson13Bar.setMax(100);
        binding.lesson14Bar.setMax(100);
        binding.lesson15Bar.setMax(100);
        binding.lesson16Bar.setMax(100);
        binding.lesson17Bar.setMax(100);
        binding.lesson18Bar.setMax(100);
        binding.lesson19Bar.setMax(100);
        binding.lesson20Bar.setMax(100);
        binding.lesson21Bar.setMax(100);
        binding.lesson22Bar.setMax(100);
        binding.lesson23Bar.setMax(100);
        binding.lesson24Bar.setMax(100);
        binding.lesson25Bar.setMax(100);
        binding.lesson26Bar.setMax(100);
        binding.lesson27Bar.setMax(100);
        binding.lesson28Bar.setMax(100);
        binding.lesson29Bar.setMax(100);
        binding.lesson30Bar.setMax(100);
        binding.lesson31Bar.setMax(100);
        binding.lesson32Bar.setMax(100);
        binding.lesson33Bar.setMax(100);
        binding.lesson34Bar.setMax(100);
        binding.lesson35Bar.setMax(100);
        binding.lesson36Bar.setMax(100);
        binding.lesson37Bar.setMax(100);
        binding.lesson38Bar.setMax(100);
        binding.lesson39Bar.setMax(100);
        binding.lesson40Bar.setMax(100);
        binding.lesson41Bar.setMax(100);
        binding.lesson42Bar.setMax(100);
        binding.lesson43Bar.setMax(100);
        binding.lesson44Bar.setMax(100);
        binding.lesson45Bar.setMax(100);
        binding.lesson46Bar.setMax(100);
        binding.lesson47Bar.setMax(100);
        binding.lesson48Bar.setMax(100);
        binding.lesson49Bar.setMax(100);
        binding.lesson50Bar.setMax(100);
        binding.lesson51Bar.setMax(100);
        binding.lesson52Bar.setMax(100);
        binding.lesson53Bar.setMax(100);
        binding.lesson54Bar.setMax(100);
        binding.lesson55Bar.setMax(100);
        binding.lesson56Bar.setMax(100);
        binding.lesson57Bar.setMax(100);
        binding.lesson58Bar.setMax(100);
        binding.lesson59Bar.setMax(100);
        binding.lesson60Bar.setMax(100);
        binding.lesson61Bar.setMax(100);
        binding.lesson62Bar.setMax(100);
        binding.lesson63Bar.setMax(100);
        binding.lesson64Bar.setMax(100);
        binding.lesson65Bar.setMax(100);
        binding.lesson66Bar.setMax(100);
        binding.lesson67Bar.setMax(100);
        binding.lesson68Bar.setMax(100);
        binding.lesson69Bar.setMax(100);
        binding.lesson70Bar.setMax(100);
        binding.lesson71Bar.setMax(100);
        binding.lesson72Bar.setMax(100);
        binding.lesson73Bar.setMax(100);
        binding.lesson74Bar.setMax(100);
        binding.lesson75Bar.setMax(100);
        binding.lesson76Bar.setMax(100);
        binding.lesson77Bar.setMax(100);
        binding.lesson78Bar.setMax(100);
        binding.lesson79Bar.setMax(100);
        binding.lesson80Bar.setMax(100);
        binding.lesson81Bar.setMax(100);
        binding.lesson82Bar.setMax(100);
        binding.lesson83Bar.setMax(100);
        binding.lesson84Bar.setMax(100);
    }
    public static void dialog(boolean value){ // построение окна оповещения
        // проверка интернета
        if(value){
            md.build(false);

        }else {
            md.build(true);

        }
    }

      private void registerNetworkBroadcastForNougat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            getActivity().registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getActivity().registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

  protected void unregisterNetworkChanges() {
        try {
            getActivity().unregisterReceiver(mNetworkReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        unregisterNetworkChanges();
    }
}