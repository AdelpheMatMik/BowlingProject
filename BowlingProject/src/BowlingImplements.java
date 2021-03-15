import java.util.ArrayList;
import java.util.List;

public class BowlingImplements implements IBowling {
	
	 private final List<Frame> frames;
     private static final int MAX_FRAMES = 10; //There are 10 frames in a match
     private static final int MAX_PINS = 10;  //There are 10 pins in a frame
     private static final int MAX_ATTEMPTS_PER_FRAME = 2;
     private int frameCounter = 0;
     private int strikeCounter = 0;
     private static final int ALL_STRIKE_SCORE = 300;


     /**
      * setup the game, ie all frames
      */
     public BowlingImplements() {

      frames = new ArrayList<Frame>(MAX_FRAMES);

         for (int i = 0; i < MAX_FRAMES; i++) {
             frames.add(new Frame());
         }
     }

     @Override
     public void roll(int noOfPins) {

         if (noOfPins > MAX_PINS) {
             throw new BowlingException("illegal argument " + noOfPins);
         }

         Frame frame = getFrame();

         if (frame == null) {
             throw new BowlingException("all attempts exhausted - start new game");
         }

         frame.setScore(noOfPins);

         if (isBonusFrame()) {
             Frame prev = getPreviousFrame();
             // restrict to one attempt, when last frame was spare
             if (prev.isSpare()) {
                 frame.limitToOneAttempt();
             }
         }

     }

     /**
      * returns a frame and moves to next frame if current has used up attempts
      * @return
      */
     private Frame getFrame(){

         Frame frame = getCurrentFrame();

         if (frame.isDone()) {

             // new bonus frame
             if(isLastFrame() && (frame.isSpare() || frame.isStrike())) {
                 Frame bonus = new Frame();
                 frames.add(bonus);
                 frameCounter++;
                 return bonus;
             }

             frameCounter++;
             if (frameCounter == MAX_FRAMES || isBonusFrame()) {
                 return null;
             }

             frame = getCurrentFrame();
         }

         return frame;
     }
     
     
  /**
   If in 2 tries, the bowler knocks down all the pins, it is 
   a spare. The scoring of a spare 
     is the sum of the number of pins knocked down plus 
     the number of pins knocked down in the next bowl.
     
     E.g, if a bowler rolls, 4,6 | 5,0, then their score is 20 = (4 + 6 + 5) + (5 + 0).
	*/
     
  /**
   * If in one try, the bowler knocks down all the pins, it is a strike. 
   * The scoring of a strike is the sum of the number of pins knocked down
   *  plus the number of pins knocked down in the next two bowls.

	E.g, if a bowler rolls, 10 | 5, 4, then their score is 28 = (10 + 5 + 4) + (5 + 4).
   
   */

 @Override
 public int score() {

     int score;

     // first frame
     if (frameCounter == 0) {

         Frame curr = getCurrentFrame();
         return curr.score();

     } else {

         // score 300, strikes for all frames
         if (isLastFrame() && isAllStrikes()) {
             return ALL_STRIKE_SCORE;
         }

         Frame curr = getCurrentFrame();
         Frame prev = getPreviousFrame();

         // only add previous last frame to current score
         if (isBonusFrame()) {
             return prev.score() + curr.score();
         }

         score = curr.score();

         if(prev.isSpare()) {
             score += (prev.score() + curr.getFirstScore());
         }

         if(prev.isStrike()) {
             score += (prev.score() + curr.getFirstScore() +  curr.getSecondScore());
         }

     }

     return score;
 }

 private Frame getPreviousFrame() {
     return frames.get(frameCounter-1);
 }

 private Frame getCurrentFrame() {
     return frames.get(frameCounter);
 }

 private boolean isAllStrikes() {
 return strikeCounter == MAX_FRAMES ;
}

 private boolean isBonusFrame() {
     return frames.size() > MAX_FRAMES;
 }

 private boolean isLastFrame() {
     return frameCounter == MAX_FRAMES - 1;
 }
 
 //************************************************
 
 /**
  * This nested class encapsulates the concept of a frame
  * and manages score and attempts allowed for each frame
  */
 
 private class Frame {

     private int[] scores = new int[MAX_ATTEMPTS_PER_FRAME];
     private int noOfPins = 10;
     private int noAttempts = 0;
     private boolean isStrike = false;

     private boolean isSpare() {
         return noOfPins == 0 && noAttempts == MAX_ATTEMPTS_PER_FRAME && !isStrike;
     }

     private boolean isStrike() {
         return noOfPins == 0 && noAttempts == MAX_ATTEMPTS_PER_FRAME && isStrike;
     }

     private boolean isDone () {
         return noAttempts == MAX_ATTEMPTS_PER_FRAME;
     }

     private void setScore(int score) {

         scores[noAttempts++] = score;
         noOfPins -= score; // keep track of remaining pins/frame

         if (score == MAX_PINS) {
             isStrike = true;
             strikeCounter++;
         }
     }

     private void limitToOneAttempt(){
         scores[1] = 0;
         noAttempts++;
     }

     private int score() { 
    	 return scores[0] + scores[1];
    }

     private int getFirstScore() {
         return scores[0];
     }

     private int getSecondScore() {
         return scores[1];
     }

 }
 
 //*************************************
 
 

}
