package com.example.myapplicationtest.ui.rules;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.myapplicationtest.R;
import com.example.myapplicationtest.input.CheckEnter;

public class AbbreviationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abbreviation);
        Bundle argument = getIntent().getExtras();
        int id = argument.getInt("id");
        switch (id) {
            case  R.id.PresentSimple:
                setContentView(R.layout.present_simple);
                break;
            case   R.id.PastSimple:
                setContentView(R.layout.past_simple);
                break;
            case   R.id.FutureSimple:
                setContentView(R.layout.future_simple);
                break;
            case   R.id.PresentContinuous:
                setContentView(R.layout.present_continuous);
                break;
            case   R.id.PastContinuous:
                setContentView(R.layout.past_continuous);
                break;
            case R.id.FutureContinuous:
                setContentView(R.layout.future_continuous);
                break;
            case  R.id.PresentPerfect:
                setContentView(R.layout.present_perfect);
                break;
            case  R.id.PastPerfect:
                setContentView(R.layout.past_perfect);
                break;
            case  R.id.FuturePerfect:
                setContentView(R.layout.future_perfect);
                break;
            case  R.id.PresentPerfectContinuous:
                setContentView(R.layout.present_perfect_continuous);
                break;
            case  R.id.PastandFuture:
                setContentView(R.layout.past_and_future);
                break;
            case  R.id.FuturePast:
                setContentView(R.layout.future_in_the_past);
                break;
            case  R.id.GoingTo:
                setContentView(R.layout.to_be_going_to);
                break;
            case  R.id.Sequence:
                setContentView(R.layout.sequence);
                break;
            case  R.id.Mood:
                setContentView(R.layout.the_mood);
                break;
            case  R.id.Voice:
                setContentView(R.layout.the_voice);
                break;
            case  R.id.Noun:
                setContentView(R.layout.noun);
                break;
            case  R.id.Adjective:
                setContentView(R.layout.adjective);
                break;
            case  R.id.Pronoun:
                setContentView(R.layout.pronoun);
                break;
            case  R.id.Numeral:
                setContentView(R.layout.numeral);
                break;
            case  R.id.Adverb:
                setContentView(R.layout.adverb);
                break;
            case   R.id.Verb:
                setContentView(R.layout.verb);
                break;
            case R.id.Preposition:
                setContentView(R.layout.preposition);
                break;
            case  R.id.There:
                setContentView(R.layout.there);
                break;
            case R.id.IrregularVerbs:
                setContentView(R.layout.activity_table_verds);
                break;
            case  R.id.Infinitive:
                setContentView(R.layout.infinitive);
                break;
            case   R.id.Gerund:
                setContentView(R.layout.gerund);
                break;
            case R.id.Participle:
                setContentView(R.layout.participle);
                break;
            case R.id.Speech:
                setContentView(R.layout.speech);
                break;
            case R.id.Interrogative:
                setContentView(R.layout.interragative_sentences);
                break;
            case  R.id.Abbreviations:
                setContentView(R.layout.activity_abbreviation);
                break;
            case  R.id.HelpTable:
                setContentView(R.layout.help_table);
                break;
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CheckEnter.update = false;
    }
}