
public class BowlingFactory {
	
	private BowlingFactory(){};

    public static IBowling createMatch() {
        return new BowlingImplements();
    }

}
