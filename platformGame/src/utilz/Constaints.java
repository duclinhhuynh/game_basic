package utilz;

public class Constaints {
	public static class Directions{
		public static final int LEFT = 0;
		public static final int RIGHT = 1;
		public static final int UP = 2;
		public static final int DOWN = 3;
	}
	
	// Hằng số người chơi
	
	public static class Playerconstaints {
		public static final int IDLE = 0;
		public static final int RUNNING = 1;
		public static final int JUMP = 2;
		public static final int FALLING = 3;
		public static final int GROUND = 4;
		public static final int HIT = 5;
		public static final int ATTACK_1 = 6;
		public static final int ATTACK_JUMP_1 = 7;
		public static final int ATTACK_JUMP_2 = 8;
		
		public static int GetSpriteAmount(int player_action) {
			switch(player_action) {
			// have 6 character running in img
			case RUNNING: 
				return 6;
				// have 5 character running in img
			case IDLE: 
				return 5;
			case HIT: 
				return 4;
			case JUMP: 
			case ATTACK_1:
			case ATTACK_JUMP_1:
			case ATTACK_JUMP_2:
				return 3;
			case GROUND:
				return 2;
			case FALLING:
				default:
				return 1;				
			}
		}
	}
}
