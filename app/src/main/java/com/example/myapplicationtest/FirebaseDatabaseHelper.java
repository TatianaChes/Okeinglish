package com.example.myapplicationtest;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.hawk.Hawk;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FirebaseDatabaseHelper  {

    private static final String TAG = "FirebaseDatabaseHelper";
    public static DatabaseReference myRef;
    public static FirebaseAuth firebaseAuth;
    public static Boolean check = false;
    public static Boolean checkSecond = false;
    public static String currentUserID;

    public  static void Update(){
        myRef = FirebaseDatabase.getInstance().getReference("Users");
        firebaseAuth = FirebaseAuth.getInstance();
        currentUserID = firebaseAuth.getCurrentUser().getUid();
    }

    public static void setDailyXp(int xp) { // опыт на сегодня
           Update();
           Date date = Calendar.getInstance().getTime();
           SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE", Locale.US);
           final String dayOfWeek = dateFormat.format(date);
            myRef.child(currentUserID)
                    .child("week_xp")
                    .child(dayOfWeek)
                    .setValue(xp)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            Log.d(TAG, "XP for day " + dayOfWeek + " has been settled properly");
                        }
                    });


    }

    public static void setSettings(boolean sound, boolean send, String time, int xp) {
            Update();
            // пользовательские настройки/разрешения
            myRef.child(currentUserID).child("soundButtons").setValue(sound);
            myRef.child(currentUserID).child("sendingMessage").setValue(send);
            myRef.child(currentUserID).child("sendingTime").setValue(time);
            myRef.child(currentUserID).child("dailyGoal").setValue(xp);

    }
    public static void setOverallProgress(double progress) { // установка отбщего прогресса
            Update();
            myRef.child(currentUserID)
                    .child("overallProgress")
                    .setValue(progress)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {


                        }
                    });

    }
    public static void getOverallProgress() {
        Update();
        // получение общего прогресса
        myRef.child(currentUserID)
                    .child("overallProgress")
                    .addValueEventListener(new ValueEventListener() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                               Double Xp = (dataSnapshot.getValue(Double.class));
                                Hawk.put(dataSnapshot.getKey(), Xp);
                            //    Log.d("Overal progress", Xp.toString());


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

    }

    public static void getLastChild() { // для подсчета непрерывных дней
            Update();
            Query lastQuery = myRef.child(currentUserID).child("dates").orderByKey().limitToLast(1);
            lastQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override

                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot data : dataSnapshot.getChildren())
                    {
                        String key = data.getKey();
                        Hawk.put("lastDay", key);
                        Log.d(TAG, key);

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }});

    }

    public static void setLessonComplete(String lesson, int completeness) { // опыт урока - установка
            Update();
            myRef.child(currentUserID)
                    .child("lessons")
                    .child(lesson)
                    .setValue(completeness)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    });

    }
    public static void ContinuousDay(Boolean complete) { // логика непрерывного дня
        Update();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        String today = dateFormat.format(cal.getTime());
        try {

            myRef.child(currentUserID)
                    .child("continuousDay")
                    .addValueEventListener(new ValueEventListener() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int Xp = 0;
                            if(dataSnapshot.getValue() != null)
                            Xp = Math.toIntExact(dataSnapshot.getValue(Long.class));

                            Hawk.put("continuousDay", Xp);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
        } catch (Exception e){}
        try{
            cal.add(Calendar.DATE, -1); // вчера
            myRef.child(currentUserID)
                    .child("dates")
                    .addValueEventListener(new ValueEventListener() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if ((int) dataSnapshot.getChildrenCount() == 0) {
                                myRef.child(currentUserID).child("continuousDay").setValue(0);
                                Hawk.put("continuousDay", 0);

                            } else if ((int) dataSnapshot.getChildrenCount() >= 1) {
                                for (DataSnapshot weekDays : dataSnapshot.getChildren()) {

                                    if (weekDays.getKey().equals(today)) {
                                        // today
                                        checkSecond = true;

                                    }
                                    if (weekDays.getKey().equals(dateFormat.format(cal.getTime()))) {
                                        // yesterday
                                        check = true;
                                    }
                                }
                            }
                            if (check == true) {
                                if (complete) {
                                    myRef.child(currentUserID).child("continuousDay").setValue((int) Hawk.get("continuousDay") + 1);
                                    Hawk.put("continuousDay", (int) Hawk.get("continuousDay") + 1);
                                } else {
                                    // если нет просто получить
                                    //...
                                }
                            } else {
                                if (complete) {
                                    // если урок пройден
                                    myRef.child(currentUserID).child("continuousDay").setValue(1);
                                    Hawk.put("continuousDay", 1);

                                } else if (checkSecond) {
                                    // если просто зашли
                                    // если нет просто получить
                                    //...

                                } else {
                                    myRef.child(currentUserID).child("continuousDay").setValue(0);
                                    Hawk.put("continuousDay", 0);

                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        } catch (Exception e){}

    }

    public static void setDay() { // засчитывание дня
        Update();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();

        myRef.child(currentUserID)
                    .child("dates")
                    .child(dateFormat.format(cal.getTime()))
                    .setValue(true)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    });

        ContinuousDay(true);
      }

    public static void getDailyGoal(){ // получение ежедневной цели
        Update();

        myRef.child(currentUserID)
                .child("dailyGoal")
                .addValueEventListener(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                            int Xp = (dataSnapshot.getValue(Integer.class));
                            Hawk.put(dataSnapshot.getKey(), Xp);
                            Log.d("dailyGoal", String.valueOf(Xp));
                        }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

    public static void getUpdateWeek(){ // обновление недели - получение
        Update();
        myRef.child(currentUserID)
                .child("updateWeek")
                .addValueEventListener(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                    boolean result = false;
                        if (snapshot.exists()) {

                        result = snapshot.getValue(Boolean.class);
                        }
                        Hawk.put("updateWeek",result);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    public static void setUpdateWeek(boolean result) { // обновление недели - запись
            Update();
            myRef.child(currentUserID)
                   .child("updateWeek")
                   .setValue(result)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    });


    }

    public static void getDailyXp() { // получение опыта приобретенного сегодня
            Update();
            Date date = Calendar.getInstance().getTime();

            SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE", Locale.US);
            final String dayOfWeek = dateFormat.format(date);

            myRef.child(currentUserID)
                    .child("week_xp")
                    .child(dayOfWeek)
                    .addValueEventListener(new ValueEventListener() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            int dailyXp = 0;

                            if (dataSnapshot.getValue() != null) {

                                dailyXp = Math.toIntExact(dataSnapshot.getValue(Long.class));
                            }
                            Hawk.put("dailyXp", dailyXp);
                            Hawk.put(dayOfWeek, dailyXp);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

    }
    public static void clearWeekXp() {
            Update();
            myRef.child(currentUserID).child("week_xp").child("Monday").removeValue();
            Hawk.put("mondayXp", 0);
            myRef.child(currentUserID).child("week_xp").child("Tuesday").removeValue();
            Hawk.put("tuesdayXp", 0);
            myRef.child(currentUserID).child("week_xp").child("Wednesday").removeValue();
            Hawk.put("wednesdayXp", 0);
            myRef.child(currentUserID).child("week_xp").child("Thursday").removeValue();
            Hawk.put("thursdayXp", 0);
            myRef.child(currentUserID).child("week_xp").child("Friday").removeValue();
            Hawk.put("fridayXp", 0);
            myRef.child(currentUserID).child("week_xp").child("Saturday").removeValue();
            Hawk.put("saturdayXp", 0);
            myRef.child(currentUserID).child("week_xp").child("Sunday").removeValue();
            Hawk.put("sundayXp", 0);
    }

    public static void getWeekXp() { // получение опыта за всю неделю

        Update();
        myRef.child(currentUserID)
                    .child("week_xp")
                    .addValueEventListener(new ValueEventListener() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot weekDays : dataSnapshot.getChildren()) {

                                int dailyXp = Math.toIntExact(weekDays.getValue(Long.class));

                                switch (weekDays.getKey()) {

                                    case "Monday":
                                        Hawk.put("mondayXp", dailyXp);
                                        break;

                                    case "Tuesday":
                                        Hawk.put("tuesdayXp", dailyXp);
                                        break;

                                    case "Wednesday":
                                        Hawk.put("wednesdayXp", dailyXp);
                                        break;

                                    case "Thursday":
                                        Hawk.put("thursdayXp", dailyXp);
                                        break;

                                    case "Friday":
                                        Hawk.put("fridayXp", dailyXp);
                                        break;

                                    case "Saturday":
                                        Hawk.put("saturdayXp", dailyXp);
                                        break;

                                    case "Sunday":
                                        Hawk.put("sundayXp", dailyXp);
                                        break;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

    }

    public static void getLessonCompleted() { // получение прогресса текущего урока
            Update();
            myRef.child(currentUserID)
                  .child("lessons")
                    .addValueEventListener(new ValueEventListener() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot ds : dataSnapshot.getChildren()) {

                                int lessonXp = Math.toIntExact(ds.getValue(Long.class));
                                Hawk.put(ds.getKey(),lessonXp);
                                Log.d(ds.getKey(),  ds.getValue(Long.class).toString());

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

    }

    public static void getSoundButton() { // получение пользовательских настройек
        Update();
        myRef.child(currentUserID)
               .addValueEventListener(new ValueEventListener() {
                   @RequiresApi(api = Build.VERSION_CODES.N)
                   @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    Hawk.put("soundButtons",snapshot.child("soundButtons").getValue(Boolean.class));
                    Hawk.put("sendingMessage",snapshot.child("sendingMessage").getValue(Boolean.class));
                    Hawk.put("sendingTime",snapshot.child("sendingTime").getValue(String.class));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}
