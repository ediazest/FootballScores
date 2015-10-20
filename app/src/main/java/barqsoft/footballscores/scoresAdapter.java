package barqsoft.footballscores;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by yehya khaled on 2/26/2015.
 */
public class scoresAdapter extends CursorAdapter {
    public static final int COL_HOME = 3;
    public static final int COL_AWAY = 4;
    public static final int COL_HOME_GOALS = 6;
    public static final int COL_AWAY_GOALS = 7;
    public static final int COL_DATE = 1;
    public static final int COL_LEAGUE = 5;
    public static final int COL_MATCHDAY = 9;
    public static final int COL_ID = 8;
    public static final int COL_MATCHTIME = 2;
    public double detailMatchId = 0;

    public scoresAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View mItem = LayoutInflater.from(context).inflate(R.layout.scores_list_item, parent, false);
        ViewHolder mHolder = new ViewHolder(mItem);
        mItem.setTag(mHolder);
        //Log.v(FetchScoreTask.LOG_TAG,"new View inflated");
        return mItem;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        final ViewHolder mHolder = (ViewHolder) view.getTag();

        String homeTeamName = cursor.getString(COL_HOME);
        mHolder.home_name.setText(homeTeamName);
        mHolder.home_name.setContentDescription(mContext.getString(R.string.a11y_home_team, homeTeamName));

        String awayTeamName = cursor.getString(COL_AWAY);
        mHolder.away_name.setText(awayTeamName);
        mHolder.away_name.setContentDescription(mContext.getString(R.string.a11y_away_team, awayTeamName));

        String matchTime = cursor.getString(COL_MATCHTIME);
        mHolder.date.setText(matchTime);
        mHolder.date.setContentDescription(Utilies.getTimeContentDescription(mContext,matchTime));

        String scoresText = Utilies.getScores(cursor.getInt(COL_HOME_GOALS), cursor.getInt(COL_AWAY_GOALS));
        mHolder.score.setText(scoresText);
        mHolder.score.setContentDescription(mContext.getString(R.string.a11y_game_score, scoresText));

        mHolder.match_id = cursor.getDouble(COL_ID);

        mHolder.home_crest.setImageResource(Utilies.getTeamCrestByTeamName(
                homeTeamName));
        mHolder.home_crest.setContentDescription("");

        mHolder.away_crest.setImageResource(Utilies.getTeamCrestByTeamName(
                awayTeamName));
        mHolder.away_crest.setContentDescription("");

        //Log.v(FetchScoreTask.LOG_TAG,mHolder.home_name.getText() + " Vs. " + mHolder.away_name.getText() +" id " + String.valueOf(mHolder.match_id));
        //Log.v(FetchScoreTask.LOG_TAG,String.valueOf(detailMatchId));
        LayoutInflater vi = (LayoutInflater) context.getApplicationContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.detail_fragment, null);
        ViewGroup container = (ViewGroup) view.findViewById(R.id.details_fragment_container);
        if (mHolder.match_id == detailMatchId) {
            //Log.v(FetchScoreTask.LOG_TAG,"will insert extraView");

            container.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                    , ViewGroup.LayoutParams.MATCH_PARENT));
            TextView match_day = (TextView) v.findViewById(R.id.matchday_textview);
            String matchDay = Utilies.getMatchDay(mContext, cursor.getInt(COL_MATCHDAY),
                    cursor.getInt(COL_LEAGUE));
            match_day.setText(matchDay);
            match_day.setContentDescription(matchDay);
            TextView league = (TextView) v.findViewById(R.id.league_textview);
            String leagueText = Utilies.getLeague(mContext, cursor.getInt(COL_LEAGUE));
            league.setText(leagueText);
            league.setContentDescription(mContext.getString(R.string.a11y_game_championship, leagueText));
            Button share_button = (Button) v.findViewById(R.id.share_button);
            share_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //add Share Action
                    context.startActivity(createShareGameIntent(mHolder.home_name.getText() + " "
                            + mHolder.score.getText() + " " + mHolder.away_name.getText() + " "));
                }
            });
        } else {
            container.removeAllViews();
        }

    }

    public Intent createShareGameIntent(String ShareText) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, ShareText + mContext.getString(R.string.hashtag));
        return shareIntent;
    }

    public int getItemPositionByObjectId(int matchId) {
        if (getCursor() != null) {
            getCursor().moveToFirst();
            for (int i = 0; i < getCursor().getCount(); i++) {
                if (getCursor().getDouble(COL_ID) == matchId) {
                    return i;
                }
                getCursor().moveToNext();
            }
        }
        return -1;

    }

}
