package awele.bot.competitor.Awelicopter;

import awele.bot.CompetitorBot;
import awele.bot.demo.random.RandomBot;
import awele.core.Board;
import awele.core.InvalidBotException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class Awelicopter extends CompetitorBot {

    public int depth;

    public static int goodDepth;
    public Awelicopter () throws InvalidBotException
    {
        this.setBotName ("Awelicopter");
        this.addAuthor ("Lucas FRANCHINA");
        this.addAuthor ("Tim BRANSTETT");

    }
    public Awelicopter (int depth) throws InvalidBotException
    {
        this.setBotName ("Awelicopter");
        this.addAuthor ("Lucas FRANCHINA");
        this.addAuthor ("Tim BRANSTETT");
        this.depth = depth;

    }



    @Override
    public void initialize()  {


    }

    @Override
    public void finish() {

    }

    @Override
    public double[] getDecision(Board board) {
        if(goodDepth!=0){
            MinMaxNodeAwelicopter.initialize (board, goodDepth);
            return new MaxNodeAwelicopter(board).getDecision ();
        } else {
            MinMaxNodeAwelicopter.initialize (board, depth);
            return new MaxNodeAwelicopter(board).getDecision ();
        }
    }

    private static String formatDuration (final long l)
    {
        final long hr = TimeUnit.MILLISECONDS.toHours (l);
        final long min = TimeUnit.MILLISECONDS.toMinutes (l - TimeUnit.HOURS.toMillis(hr));
        final long sec = TimeUnit.MILLISECONDS.toSeconds (l - TimeUnit.HOURS.toMillis(hr) - TimeUnit.MINUTES.toMillis (min));
        final long ms = TimeUnit.MILLISECONDS.toMillis(l - TimeUnit.HOURS.toMillis (hr) - TimeUnit.MINUTES.toMillis (min) - TimeUnit.SECONDS.toMillis (sec));
        return String.format("%02d:%02d:%02d.%03d", hr, min, sec, ms);
    }


    private static int extractMilliseconds(final long l) {
        return (int) (l % 1000);
    }

    @Override
    public void learn() {

        RandomBot random = null;
        try
        {
            random = new RandomBot ();
            random.learn ();
        } catch (InvalidBotException e) {

            e.printStackTrace();
            System.exit(0);
        }
        long randomRunningTime = 0;
        int nbMovesRandom = 0;

        for (int k = 0; k < 100; k++)
        {
            CoreLearn aweleRandom = new CoreLearn(random,random);
            try {
                aweleRandom.play ();
            } catch (InvalidBotException e)
            {}

            nbMovesRandom += aweleRandom.getNbMoves ();
            randomRunningTime += aweleRandom.getRunningTime ();

        }


        long randomAverageDecisionTime = randomRunningTime/nbMovesRandom;
        System.out.println("RADT : "+randomAverageDecisionTime);
        for(int i = 6;i<100;i++){
            System.out.println("profondeur learn : "+i);
            CoreLearn awele = null;
            try {
                awele = new CoreLearn(new Awelicopter(i), random);
            } catch (InvalidBotException e) {
                e.printStackTrace();
            }
            try {
                assert awele != null;
                awele.play ();

                long decisionTime = (long) (((2 * awele.getRunningTime ()) / awele.getNbMoves ())-randomAverageDecisionTime);
                System.out.println("aweleRunningTime : "+awele.getRunningTime());
                System.out.println("aweleNbMove : "+awele.getNbMoves());

                System.out.println("decision time: "+extractMilliseconds(decisionTime));


                String decisionTimeString = String.valueOf(extractMilliseconds(decisionTime));


                System.out.println("temps de decision fonction learn : "+decisionTimeString+" ms");
                if(Integer.parseInt(decisionTimeString)>200){
                    System.out.println("la bonne depth est : "+(i-1));
                    goodDepth = i - 1;
                    break;
                }
                System.out.println("Durée calculé dans la fonction learn: "+formatDuration(decisionTime));
            } catch (InvalidBotException e) {
                e.printStackTrace();
            }

        }

    }
}
