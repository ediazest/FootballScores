package barqsoft.footballscores;

import android.content.Context;

/**
 * Created by yehya khaled on 3/3/2015.
 */
public class Utilies {

    // This set of league codes is for the 2015/2016 season. In fall of 2016, they will need to
    // be updated. Feel free to use the codes
    //German league
    public final static int BUNDESLIGA1 = 394;
    public final static int BUNDESLIGA2 = 395;
    public final static int BUNDESLIGA3 = 403;
    //French league
    public final static int LIGUE1 = 396;
    public final static int LIGUE2 = 397;
    //English league
    public final static int PREMIER_LEAGUE = 398;
    //Spanish league
    public final static int PRIMERA_DIVISION = 399;
    public final static int SEGUNDA_DIVISION = 400;
    //Italian league
    public final static int SERIE_A = 401;
    //Portuguese league
    public final static int PRIMERA_LIGA = 402;
    //Netherlands league
    public final static int EREDIVISIE = 404;

    public static final int CHAMPIONS_LEAGUE = 405;

    public static String getLeague(Context context, int league_num) {
        switch (league_num) {
            case SERIE_A:
                return context.getString(R.string.league_serie_a);
            case PREMIER_LEAGUE:
                return context.getString(R.string.league_premier);
            case CHAMPIONS_LEAGUE:
                return context.getString(R.string.league_champions);
            case PRIMERA_DIVISION:
                return context.getString(R.string.league_la_liga);
            case BUNDESLIGA1:
                return context.getString(R.string.league_bundesliga);
            case EREDIVISIE:
                return context.getString(R.string.league_eredivise);
            case PRIMERA_LIGA:
                return context.getString(R.string.league_superliga);
            case LIGUE1:
                return context.getString(R.string.league_ligue1);
            default:
                return context.getString(R.string.league_unknown);
        }
    }

    public static String getMatchDay(Context context, int match_day, int league_num) {
        if (league_num == CHAMPIONS_LEAGUE) {
            if (match_day <= 6) {
                return String.format(context.getString(R.string.champions_stage_groups), match_day);
            } else if (match_day == 7 || match_day == 8) {
                return context.getString(R.string.champions_stage_first);
            } else if (match_day == 9 || match_day == 10) {
                return context.getString(R.string.champions_stage_quarterfinal);
            } else if (match_day == 11 || match_day == 12) {
                return context.getString(R.string.champions_stage_semifinal);
            } else {
                return context.getString(R.string.champions_stage_final);
            }
        } else {
            return String.format(context.getString(R.string.match_day), match_day);
        }
    }

    public static String getScores(int home_goals, int awaygoals) {
        if (home_goals < 0 || awaygoals < 0) {
            return " - ";
        } else {
            return String.valueOf(home_goals) + " - " + String.valueOf(awaygoals);
        }
    }

    public static int getTeamCrestByTeamName(String teamname) {
        if (teamname == null) {
            return R.drawable.no_icon;
        }
        switch (teamname) { //This is the set of icons that are currently in the app. Feel free to find and add more
            //as you go.
            case "Arsenal FC":
                return R.drawable.arsenal;
            case "Manchester United FC":
                return R.drawable.manchester_united;
            case "Manchester City FC":
                return R.drawable.manchester_city;
            case "Swansea City FC":
                return R.drawable.swansea_city_afc;
            case "Leicester City FC":
                return R.drawable.leicester_city_fc_hd_logo;
            case "Everton FC":
                return R.drawable.everton_fc_logo1;
            case "West Ham United FC":
                return R.drawable.west_ham;
            case "Tottenham Hotspur FC":
                return R.drawable.tottenham_hotspur;
            case "West Bromwich Albion FC":
                return R.drawable.west_bromwich_albion_hd_logo;
            case "Sunderland AFC":
                return R.drawable.sunderland;
            case "Stoke City FC":
                return R.drawable.stoke_city;
            default:
                return R.drawable.no_icon;
        }
    }

    public static String getTimeContentDescription(Context context, String timeText) {
        if (timeText.contains(":")) {
            String[] time = timeText.split(":");
            String hours = time[0];
            String mins = time[1];
            return context.getString(R.string.a11y_game_time, hours, mins);
        }
        return "";
    }
}
