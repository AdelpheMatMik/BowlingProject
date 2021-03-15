
public interface IBowling {
	
	/**
     * Keeps track of pins knocked over
     * @param noOfPins knocked over per frame
     * @exception {@link au.com.dius.BowlingGameScoreBoard.BowlingException}
     */
    public void roll(int noOfPins);

    /**
     *
     * @return score of current frame only
     */
    public int score();

}
