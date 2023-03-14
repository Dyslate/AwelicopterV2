package awele.bot.competitor.fullDogo;

import awele.bot.competitor.fullDogo.MinMaxNode;
import awele.core.Board;
import awele.core.InvalidBotException;

public class EvalSituation {
	
    public static double evalSituation(Board board, int depth) {
    	
    	int scoreSeeds = 0;

		int cHasKrou = 0;
		int aHasKrou = 0;
   	 	
   	 	boolean isPair; 
    	if(depth % 2 == 0) {
    		isPair = true;
    	}else {
    		isPair = false;
    	}
    	
    			
    	for(int i = 0; i < Board.NB_HOLES; i++) {
    		if(board.getPlayerHoles()[i] < 3 ) {
    			if(isPair)
    				scoreSeeds++;
				else
    				scoreSeeds--;
    		}

    		if(board.getOpponentHoles()[i] < 3 ) {
				if(isPair)
    				scoreSeeds--;
    			else
    				scoreSeeds++;
    		}
    		
            if(board.getPlayerHoles()[i] >= 11) {
                cHasKrou = 1;
            }
    		
    		if(board.getOpponentHoles()[i] >= 11) {
                aHasKrou = 1;
            }
    		
    	}
    	
        return scoreSeeds + aHasKrou - cHasKrou;
    }
    
}
