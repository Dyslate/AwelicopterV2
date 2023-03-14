package awele.bot.competitor.fullDogo;

import awele.bot.CompetitorBot;
import awele.bot.DemoBot;
import awele.core.Board;
import awele.core.InvalidBotException;

public class fullDogoBot extends CompetitorBot{

	private static int ORIGIN_DEPTH = 7;
	
    public fullDogoBot () throws InvalidBotException
    {
        this.setBotName ("FullDogo");
        this.addAuthor ("VIADER - OTTINGER");
    }
	
	@Override
	public void initialize() {
		
	}

	@Override
	public void finish() {
		
	}

	@Override
	public double[] getDecision(Board board) {
        MinMaxNode.initialize(board, ORIGIN_DEPTH);
        return new MaxNode (board).getDecision ();
	}

	@Override
	public void learn() {
		
	}

}
