package awele.bot.competitor.Awelicopter;

import awele.bot.CompetitorBot;
import awele.core.Board;
import awele.core.InvalidBotException;

import java.util.Arrays;


public class Awelicopter extends CompetitorBot {

    public Awelicopter () throws InvalidBotException
    {
        this.setBotName ("Awelicopter");
        this.addAuthor ("Lucas FRANCHINA");
        this.addAuthor ("Tim BRANSTETT");

    }


    @Override
    public void initialize()  {
    }

    @Override
    public void finish() {

    }

    @Override
    public double[] getDecision(Board board) {
        MinMaxNodeAwelicopter.initialize (board, 8);
        return new MaxNodeAwelicopter(board).getDecision ();
    }



    @Override
    public void learn() {
    }
}
