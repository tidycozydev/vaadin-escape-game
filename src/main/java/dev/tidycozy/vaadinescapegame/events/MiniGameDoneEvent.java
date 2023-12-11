package dev.tidycozy.vaadinescapegame.events;

import com.vaadin.flow.component.ComponentEvent;
import dev.tidycozy.vaadinescapegame.components.MiniGameView;

/**
 * Event fired when the player finishes a mini-game. We use it to update the game's elements.
 */
public class MiniGameDoneEvent extends ComponentEvent<MiniGameView> {

    public enum Minigames {
        PUZZLE,
        FILE,
        CHAT,
        CASE
    }

    private final Minigames miniGame;

    public MiniGameDoneEvent(MiniGameView source, Minigames miniGame) {
        super(source, false);

        this.miniGame = miniGame;
    }

    public Minigames getMiniGame() {
        return this.miniGame;
    }
}
