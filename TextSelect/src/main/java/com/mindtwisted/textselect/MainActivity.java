package com.mindtwisted.textselect;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.text.BreakIterator;
import java.util.HashMap;
import java.util.Locale;


public class MainActivity extends ActionBarActivity {

    public static final HashMap<String, String> mDictionary = new HashMap<String, String>();
    static {
        mDictionary.put("born", "<b>adjective:</b> existing as a result of birth.");
        mDictionary.put("great", "<b>adjective:</b> of an extent, amount, or intensity considerably above average.");
        mDictionary.put("achieve", "<b>verb:</b> successfully bring about or reach (a desired objective or result) by effort, skill, or courage.");
        mDictionary.put("greatness", "<b>noun:</b> the quality of being great; eminence or distinction.");
        mDictionary.put("thrust", "<b>verb:</b> push suddenly or violently in a specified direction.");
        mDictionary.put("upon", "<b>preposition:</b> more formal term for on, especially in abstract senses.");
    }

    private TextView mMessageTextView;
    private TextView mDefinitionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMessageTextView = (TextView) findViewById(R.id.message_text_view);
        mDefinitionTextView = (TextView) findViewById(R.id.definition_text_view);

        String definition = getString(R.string.message);
        mMessageTextView.setMovementMethod(LinkMovementMethod.getInstance());
        mMessageTextView.setText(definition, TextView.BufferType.SPANNABLE);
        Spannable spans = (Spannable) mMessageTextView.getText();
        if(spans != null) {
            BreakIterator iterator = BreakIterator.getWordInstance(Locale.US);
            iterator.setText(definition);
            int start = iterator.first();
            for (int end = iterator.next(); end != BreakIterator.DONE; start = end, end = iterator.next()) {
                String possibleWord = definition.substring(start, end);
                if (Character.isLetterOrDigit(possibleWord.charAt(0))) {
                    ClickableSpan clickSpan = getClickableSpan(possibleWord);
                    spans.setSpan(clickSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private ClickableSpan getClickableSpan(final String word) {
        return new ClickableSpan() {
            final String mWord = word;

            @Override
            public void onClick(View widget) {
                if(widget.getContext() != null) {
                    String definition = mDictionary.get(mWord.toLowerCase());
                    if(definition != null) {
                        mDefinitionTextView.setText(Html.fromHtml(definition));
                        mDefinitionTextView.setTextColor(getResources().getColor(R.color.white));
                    }else {
                        mDefinitionTextView.setText("Definition not found. Please tap another word.");
                        mDefinitionTextView.setTextColor(getResources().getColor(R.color.light_red));
                    }
                }
            }

            public void updateDrawState(TextPaint ds) {
                // Override to disable default style
            }
        };
    }
}
