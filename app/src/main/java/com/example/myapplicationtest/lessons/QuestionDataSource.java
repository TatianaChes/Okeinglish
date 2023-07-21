package com.example.myapplicationtest.lessons;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplicationtest.lessons.selectCard.PictureModel;
import com.example.myapplicationtest.lessons.selectPair.PairModel;
import com.example.myapplicationtest.ui.textlist.currentText.DatabaseHelper;
import com.orhanobut.hawk.Hawk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class QuestionDataSource {

    private QuestionModel questionModel;
    private PictureModel pictureModel;
    // Переменные для работы с БД
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    public int lesson = Hawk.get("number");
    ArrayList<String> answer = new ArrayList<>();
    Cursor  cursor = null;
    public  Random random = new Random();


    public void ConnectDb(Context context){
        mDBHelper = new DatabaseHelper(context);

        try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        try {
            mDb = mDBHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }

    }

    public  QuestionModel getRandomQuestionObj(Context context) {
        ConnectDb(context);
        int randomIndex = 0;

            if(lesson >= 1 && lesson <= 6) {
                randomIndex = random.nextInt(46 - 1) + 1;
            }
            else if (lesson >= 7 && lesson <= 12) {
                randomIndex = random.nextInt(76 - 46) + 46;
            }
            else if (lesson >= 13 && lesson <= 18) {
                randomIndex = random.nextInt(109 - 76) + 76;
            }
            else if (lesson >= 19 && lesson <= 24) {
                randomIndex = random.nextInt(139 - 109) + 109;
            }
            else if (lesson >= 25 && lesson <= 30) {
                randomIndex = random.nextInt(173 - 139) + 139;
            }
            else if (lesson >= 31 && lesson <= 36) {
                randomIndex = random.nextInt(197 - 173) + 173;
            }
            else if (lesson >= 37 && lesson <= 42) {
                randomIndex = random.nextInt(229 - 197) + 197;
            }
            else if (lesson >= 43 && lesson <= 48) {
                randomIndex = random.nextInt(256 - 229) + 229;
            }
            else if (lesson >= 49 && lesson <= 54) {
                randomIndex = random.nextInt(283 - 256) + 256;
            }
            else if (lesson >= 55 && lesson <= 60) {
                randomIndex = random.nextInt(325 - 283) + 283;
            }
            else if (lesson >= 61 && lesson <= 66) {
                randomIndex = random.nextInt(360 - 325) + 325;
            }
            else if (lesson >= 67 && lesson <= 72) {
                randomIndex = random.nextInt(385 - 360) + 360;
            }
            else if (lesson >= 73 && lesson <= 78) {
                randomIndex = random.nextInt(422 - 385) + 385;
            }
            else if (lesson >= 79 && lesson <= 84) {
                randomIndex = random.nextInt(461 - 422) + 422;
            }

      Cursor cursor = mDb.rawQuery("SELECT * FROM sentense where id = ?",
              new String[] {String.valueOf(randomIndex)});
      cursor.moveToFirst();
        Hawk.put("idSentense", randomIndex);

        questionModel = new QuestionModel(
                cursor.getString(1).trim() ,cursor.getString(2).trim()
                );

        cursor.close();
        return questionModel;
    }


    public  ArrayList<PairModel> getPairs(Context context) {

        ConnectDb(context);
        ArrayList<PairModel> pairs = new ArrayList<>();

        if(lesson >= 1 && lesson <= 6) {
            cursor = mDb.rawQuery("SELECT * FROM word where id between ? and ?", new String[] {"1", "62"});
        }
        else if (lesson >= 7 && lesson <= 12) {
            cursor = mDb.rawQuery("SELECT * FROM word where id between ? and ?", new String[] {"63", "84"});
        }
        else if (lesson >= 13 && lesson <= 18) {
            cursor = mDb.rawQuery("SELECT * FROM word where id between ? and ?", new String[] {"85", "103"});
        }
        else if (lesson >= 19 && lesson <= 24) {
            cursor = mDb.rawQuery("SELECT * FROM word where id between ? and ?", new String[] {"104", "123"});
        }
        else if (lesson >= 25 && lesson <= 30) {
            cursor = mDb.rawQuery("SELECT * FROM word where id between ? and ?", new String[] {"124", "146"});
        }
        else if (lesson >= 31 && lesson <= 36) {
            cursor = mDb.rawQuery("SELECT * FROM word where id between ? and ?", new String[] {"147", "166"});
        }
        else if (lesson >= 37 && lesson <= 42) {
            cursor = mDb.rawQuery("SELECT * FROM word where id between ? and ?", new String[] {"167", "195"});
        }
        else if (lesson >= 43 && lesson <= 48) {
            cursor = mDb.rawQuery("SELECT * FROM word where id between ? and ?", new String[] {"196", "215"});
        }
        else if (lesson >= 49 && lesson <= 54) {
            cursor = mDb.rawQuery("SELECT * FROM word where id between ? and ?", new String[] {"216", "244"});
        }
        else if (lesson >= 55 && lesson <= 60) {
            cursor = mDb.rawQuery("SELECT * FROM word where id between ? and ?", new String[] {"245", "284"});
        }
        else if (lesson >= 61 && lesson <= 66) {
            cursor = mDb.rawQuery("SELECT * FROM word where id between ? and ?", new String[] {"285", "313"});
        }
        else if (lesson >= 67 && lesson <= 72) {
            cursor = mDb.rawQuery("SELECT * FROM word where id between ? and ?", new String[] {"314", "352"});
        }
        else if (lesson >= 73 && lesson <= 78) {
            cursor = mDb.rawQuery("SELECT * FROM word where id between ? and ?", new String[] {"353", "388"});
        }
        else if (lesson >= 79 && lesson <= 84) {
            cursor = mDb.rawQuery("SELECT * FROM word where id between ? and ?", new String[] {"389", "420"});
        }

        cursor.moveToNext();
        while (!cursor.isAfterLast()) {
            pairs.add(new PairModel(cursor.getString(2).trim(), cursor.getString(3).trim()));
            cursor.moveToNext();
        }
        cursor.close();
        return pairs;
    }

    public ArrayList<String> getAnswer(Context context) {
        ConnectDb(context);
        int  randomIndex = random.nextInt(458 - 1) + 1;
        cursor = mDb.rawQuery("SELECT * FROM sentense where id between ? and ?", new String[] {Integer.toString(randomIndex), Integer.toString(randomIndex+3)});

        cursor.moveToNext();
        while (!cursor.isAfterLast()) {
            answer.add(cursor.getString(2).trim());
            cursor.moveToNext();
        }
        cursor.close();
        return answer;
    }

   public ArrayList<PictureModel> getCards(Context context) {

      return FillingData(context);
    }

    public ArrayList<PictureModel> FillingData(Context context){
        ConnectDb(context);
        ArrayList<Integer> numbers = new ArrayList<Integer>();
        int ran = 0;
        while (numbers.size() < 4) {

            if(lesson >= 1 && lesson <= 6) {
                ran = random.nextInt(63 - 1) + 1;
            }
            else if (lesson >= 7 && lesson <= 12) {

                ran = random.nextInt(85 - 63) + 63;
            }
            else if (lesson >= 13 && lesson <= 18) {

                ran = random.nextInt(104 - 85) + 85;
            }
            else if (lesson >= 19 && lesson <= 24) {

                ran = random.nextInt(124 - 104) + 104;
            }
            else if (lesson >= 25 && lesson <= 30) {

                ran = random.nextInt(147 - 124) + 124;
            }
            else if (lesson >= 31 && lesson <= 36) {

                ran = random.nextInt(167 - 147) + 147;
            }
            else if (lesson >= 37 && lesson <= 42) {

                ran = random.nextInt(196 - 167) + 167;
            }
            else if (lesson >= 43 && lesson <= 48) {

                ran = random.nextInt(216 - 196) + 196;
            }
            else if (lesson >= 49 && lesson <= 54) {

                ran = random.nextInt(245 - 216) + 216;
            }
            else if (lesson >= 55 && lesson <= 60) {

                ran = random.nextInt(285 - 245) + 245;
            }
            else if (lesson >= 61 && lesson <= 66) {

                ran = random.nextInt(314 - 285) + 285;
            }
            else if (lesson >= 67 && lesson <= 72) {

                ran = random.nextInt(353 - 314) + 314;
            }
            else if (lesson >= 73 && lesson <= 78) {

                ran = random.nextInt(389 - 353) + 353;
            }
            else if (lesson >= 79 && lesson <= 84) {

                ran = random.nextInt(421 - 389) + 389;
            }


            if (!numbers.contains(ran)) {
                numbers.add(ran);
            }
        }
        ArrayList<PictureModel>  cards = new ArrayList<>();
        final int index1 = numbers.get(0);
        cursor = mDb.rawQuery("SELECT * FROM word where id = ?", new String[] {String.valueOf(index1)});
        cursor.moveToFirst();
        cards.add(new PictureModel(cursor.getString(3).trim(), cursor.getString(2).trim(), cursor.getString(1).trim()));

        final int index2 = numbers.get (1);
        cursor = mDb.rawQuery("SELECT * FROM word where id = ?", new String[] {String.valueOf(index2)});
        cursor.moveToFirst();
        cards.add(new PictureModel(cursor.getString(3).trim(), cursor.getString(2).trim(), cursor.getString(1).trim()));

        final int index3 = numbers.get (2);
        cursor = mDb.rawQuery("SELECT * FROM word where id = ?", new String[] {String.valueOf(index3)});
        cursor.moveToFirst();
        cards.add(new PictureModel(cursor.getString(3).trim(), cursor.getString(2).trim(), cursor.getString(1).trim()));

        final int index4 = numbers.get (3);
        cursor = mDb.rawQuery("SELECT * FROM word where id = ?", new String[] {String.valueOf(index4)});
        cursor.moveToFirst();
        cards.add(new PictureModel(cursor.getString(3).trim(), cursor.getString(2).trim(), cursor.getString(1).trim()));

        cursor.close();

       return cards;
    }

    public  PictureModel getPicture(Context context) {
        ConnectDb(context);
        ArrayList<PictureModel>  cards =  FillingData(context);

        int randomIndex = random.nextInt(cards.size());

        pictureModel = new PictureModel(
                cards.get(randomIndex).getQuestion(),
                cards.get(randomIndex).getAnswer(),
                cards.get(randomIndex).getPicture());

        return pictureModel;
    }


}

