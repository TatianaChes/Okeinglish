package com.example.myapplicationtest.ui.rules;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myapplicationtest.R;
import com.example.myapplicationtest.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment implements View.OnClickListener {

    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {

        binding = com.example.myapplicationtest.databinding.FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        TextView PresentSimple = root.findViewById(R.id.PresentSimple);
        PresentSimple.setOnClickListener(this);

        TextView PastSimple =  root.findViewById(R.id.PastSimple);
        PastSimple.setOnClickListener(this);

        TextView  FutureSimple =  root.findViewById(R.id. FutureSimple);
        FutureSimple.setOnClickListener(this);

        TextView PresentContinuous =  root.findViewById(R.id.PresentContinuous);
        PresentContinuous.setOnClickListener(this);

        TextView PastContinuous =  root.findViewById(R.id.PastContinuous);
        PastContinuous.setOnClickListener(this);

        TextView FutureContinuous =  root.findViewById(R.id.FutureContinuous);
        FutureContinuous.setOnClickListener(this);


        TextView PresentPerfect=  root.findViewById(R.id.PresentPerfect);
        PresentPerfect.setOnClickListener(this);

        TextView PastPerfect =  root.findViewById(R.id.PastPerfect);
        PastPerfect.setOnClickListener(this);

        TextView FuturePerfect =  root.findViewById(R.id.FuturePerfect);
        FuturePerfect.setOnClickListener(this);


        TextView PresentPerfectContinuous =  root.findViewById(R.id.PresentPerfectContinuous);
        PresentPerfectContinuous.setOnClickListener(this);

        TextView PastandFuture =  root.findViewById(R.id.PastandFuture);
        PastandFuture.setOnClickListener(this);

        TextView FuturePast =  root.findViewById(R.id.FuturePast);
        FuturePast.setOnClickListener(this);

        TextView GoingTo =  root.findViewById(R.id.GoingTo);
        GoingTo.setOnClickListener(this);

        TextView  Sequence =  root.findViewById(R.id.Sequence);
        Sequence.setOnClickListener(this);

        TextView  Mood =  root.findViewById(R.id.Mood);
        Mood.setOnClickListener(this);

        TextView  Voice =  root.findViewById(R.id.Voice);
        Voice.setOnClickListener(this);

        TextView  Noun =  root.findViewById(R.id.Noun);
        Noun.setOnClickListener(this);

        TextView  Adjective =  root.findViewById(R.id.Adjective);
        Adjective.setOnClickListener(this);

        TextView  Pronoun =  root.findViewById(R.id.Pronoun);
        Pronoun.setOnClickListener(this);

        TextView  Numeral =  root.findViewById(R.id.Numeral);
        Numeral.setOnClickListener(this);

        TextView  Adverb =  root.findViewById(R.id.Adverb);
        Adverb.setOnClickListener(this);

        TextView  Verb =  root.findViewById(R.id.Verb);
        Verb.setOnClickListener(this);

        TextView  Preposition =  root.findViewById(R.id.Preposition);
        Preposition.setOnClickListener(this);

        TextView  There =  root.findViewById(R.id.There);
        There.setOnClickListener(this);

        TextView  Infinitive =  root.findViewById(R.id.Infinitive);
        Infinitive.setOnClickListener(this);

        TextView  Gerund =  root.findViewById(R.id.Gerund);
        Gerund.setOnClickListener(this);


        TextView  Participle =  root.findViewById(R.id.Participle);
        Participle.setOnClickListener(this);

        TextView  Speech =  root.findViewById(R.id.Speech);
        Speech.setOnClickListener(this);


        TextView Interrogative =  root.findViewById(R.id. Interrogative);
        Interrogative.setOnClickListener(this);

        TextView Abbreviation = root.findViewById(R.id.Abbreviations);
        Abbreviation.setOnClickListener(this);

        TextView IrregularVerbs = root.findViewById(R.id.IrregularVerbs);
        IrregularVerbs.setOnClickListener(this);

        TextView HelpTable = root.findViewById(R.id.HelpTable);
        HelpTable.setOnClickListener(this);
            return root;
    }


    @Override
    public void onClick(View view) {

       Intent intent = new Intent(getContext(), AbbreviationActivity.class);
       intent.putExtra("id", view.getId());
       startActivity(intent);

    }

}