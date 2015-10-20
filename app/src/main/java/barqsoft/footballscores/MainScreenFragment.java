package barqsoft.footballscores;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import barqsoft.footballscores.service.myFetchService;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainScreenFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final int SCORES_LOADER = 0;
    private static final String TAG = MainScreenFragment.class.getSimpleName();
    public scoresAdapter mAdapter;
    @Bind(R.id.scores_list)
    ListView mScoreList;
    private String[] fragmentDate = new String[1];

    public MainScreenFragment() {
    }

    private void updateScores() {
        Intent service_start = new Intent(getActivity(), myFetchService.class);
        getActivity().startService(service_start);
    }

    public void setFragmentDate(String date) {
        fragmentDate[0] = date;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        updateScores();
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);

        mAdapter = new scoresAdapter(getActivity(), null, 0);
        mScoreList.setAdapter(mAdapter);
        getLoaderManager().initLoader(SCORES_LOADER, null, this);
        mAdapter.detailMatchId = MainActivity.selectedMatchId;

        mScoreList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ViewHolder selected = (ViewHolder) view.getTag();
                mAdapter.detailMatchId = selected.match_id;
                MainActivity.selectedMatchId = (int) selected.match_id;
                mAdapter.notifyDataSetChanged();
            }
        });

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Log.d(TAG, "fragment date: " + fragmentDate[0]);
        return new CursorLoader(getActivity(), DatabaseContract.scores_table.buildScoreWithDate(),
                null, null, fragmentDate, null);
    }

    //EXTRAS FOR EXCEED SPECIFICATIONS: Saving fragmentDate to prevent crash when changing orientation
    //While creating query arguments cannot be null
    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (fragmentDate[0] != null)
            outState.putString("fragmentDate", fragmentDate[0]);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.getString("fragmentDate") != null) {
            setFragmentDate(savedInstanceState.getString("fragmentDate"));
        }
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        //Log.v(FetchScoreTask.LOG_TAG,"loader finished");
        //cursor.moveToFirst();
        /*
        while (!cursor.isAfterLast())
        {
            Log.v(FetchScoreTask.LOG_TAG,cursor.getString(1));
            cursor.moveToNext();
        }
        */

        int i = 0;
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            i++;
            cursor.moveToNext();
        }
        //Log.v(FetchScoreTask.LOG_TAG,"Loader query: " + String.valueOf(i));
        mAdapter.swapCursor(cursor);
        //mAdapter.notifyDataSetChanged();
        mScoreList.smoothScrollToPosition(mAdapter.getItemPositionByObjectId(MainActivity.selectedMatchId));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mAdapter.swapCursor(null);
    }


}
