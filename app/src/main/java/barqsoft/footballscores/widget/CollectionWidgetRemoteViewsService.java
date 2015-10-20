package barqsoft.footballscores.widget;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.SimpleDateFormat;
import java.util.Date;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.MainActivity;
import barqsoft.footballscores.R;
import barqsoft.footballscores.Utilies;

/**
 * Created by edu on 18/10/2015.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class CollectionWidgetRemoteViewsService extends RemoteViewsService {

    public static final int COL_ID = 0;
    public static final int COL_HOME = 1;
    public static final int COL_AWAY = 2;
    public static final int COL_HOME_GOALS = 3;
    public static final int COL_AWAY_GOALS = 4;
    public static final int COL_MATCHTIME = 5;

    private static final String[] SCORES_COLUMNS = {
            DatabaseContract.scores_table.MATCH_ID,
            DatabaseContract.scores_table.HOME_COL,
            DatabaseContract.scores_table.AWAY_COL,
            DatabaseContract.scores_table.HOME_GOALS_COL,
            DatabaseContract.scores_table.AWAY_GOALS_COL,
            DatabaseContract.scores_table.TIME_COL,
    };
    private static final String TAG = CollectionWidgetRemoteViewsService.class.getSimpleName();

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            private Cursor data = null;

            @Override
            public void onCreate() {

            }

            @Override
            public void onDataSetChanged() {
                final long identityToken = Binder.clearCallingIdentity();
                Date today = new Date();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

                Uri scoreWithDateUri = DatabaseContract.scores_table.buildScoreWithDate();
                data = getContentResolver().query(scoreWithDateUri,
                        SCORES_COLUMNS,
                        null,
                        new String[]{simpleDateFormat.format(today)},
                        DatabaseContract.scores_table.TIME_COL + " ASC");
                Binder.restoreCallingIdentity(identityToken);
                //                Log.d(TAG, "Cursor size: " + data.getCount());
            }

            @Override
            public void onDestroy() {
                if (data != null) {
                    data.close();
                    data = null;
                }
            }

            @Override
            public int getCount() {
                return data == null ? 0 : data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                if (position == AdapterView.INVALID_POSITION ||
                        data == null || !data.moveToPosition(position)) {
                    Log.d(TAG, "Returning null on getViewAt");
                    return null;
                }
                RemoteViews views = new RemoteViews(getPackageName(),
                        R.layout.widget_detail_list_item);

                String homeTeamName = data.getString(COL_HOME);
                views.setTextViewText(R.id.home_name, homeTeamName);

                views.setImageViewResource(R.id.home_crest, Utilies.getTeamCrestByTeamName(homeTeamName));

                String matchTime = data.getString(COL_MATCHTIME);
                views.setTextViewText(R.id.data_textview, matchTime);

                String scoresText = Utilies.getScores(data.getInt(COL_HOME_GOALS), data.getInt(COL_AWAY_GOALS));
                views.setTextViewText(R.id.score_textview, scoresText);

                String awayTeamName = data.getString(COL_AWAY);
                views.setTextViewText(R.id.away_name, awayTeamName);

                views.setImageViewResource(R.id.away_crest, Utilies.getTeamCrestByTeamName(awayTeamName));

                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                    views.setContentDescription(R.id.score_textview, getString(R.string.a11y_game_score, scoresText));
                    views.setContentDescription(R.id.away_name, getString(R.string.a11y_away_team, awayTeamName));
                    views.setContentDescription(R.id.away_crest, "");
                    views.setContentDescription(R.id.home_name, getString(R.string.a11y_home_team, homeTeamName));
                    views.setContentDescription(R.id.home_crest, "");
                    views.setContentDescription(R.id.data_textview, Utilies.getTimeContentDescription(getApplicationContext(), matchTime));
                }


                Log.d(TAG, "Values: " + homeTeamName + matchTime +
                        scoresText + awayTeamName);

                Bundle extras = new Bundle();
                extras.putInt(MainActivity.WIDGET_EXTRA, data.getInt(COL_ID));
                final Intent fillInIntent = new Intent();
                fillInIntent.putExtras(extras);
                views.setOnClickFillInIntent(R.id.widget_frame_list_item, fillInIntent);

                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.scores_list_item);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                if (data.moveToPosition(position))
                    return data.getLong(COL_ID);
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }
}
