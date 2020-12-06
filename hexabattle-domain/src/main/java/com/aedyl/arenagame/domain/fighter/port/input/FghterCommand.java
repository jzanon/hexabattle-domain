package com.aedyl.arenagame.domain.fighter.port.input;

/**
 * Exposed commands to deal with arena management.
 */
public interface FghterCommand {

	record CreateRandomHumansCommand(int nbHumans) implements FghterCommand {
		public static FghterCommand.CreateRandomHumansCommand create(int nbHumans) {
			return new FghterCommand.CreateRandomHumansCommand(nbHumans);
		}
	}

}
