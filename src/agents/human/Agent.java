package agents.human;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import engine.core.MarioAgent;
import engine.core.MarioForwardModel;
import engine.core.MarioTimer;
import engine.helper.MarioActions;

public class Agent extends KeyAdapter implements MarioAgent {
    private boolean[] actions = null;

    @Override
    public void initialize(MarioForwardModel model, MarioTimer timer) {
        actions = new boolean[MarioActions.numberOfActions()];
    }

    @Override
    public boolean[] getActions(MarioForwardModel model, MarioTimer timer) {
        return actions;
    }

    @Override
    public String getAgentName() {
        return "HumanAgent";
    }

    @Override
    public void keyPressed(KeyEvent e) {
        toggleKey(e.getKeyCode(), true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        toggleKey(e.getKeyCode(), false);
    }

    private void toggleKey(int keyCode, boolean isPressed) {
        if (this.actions == null) {
            return;
        }
        switch (keyCode) {
            case KeyEvent.VK_A -> this.actions[MarioActions.LEFT.getValue()] = isPressed;
            case KeyEvent.VK_D -> this.actions[MarioActions.RIGHT.getValue()] = isPressed;
            case KeyEvent.VK_S -> this.actions[MarioActions.DOWN.getValue()] = isPressed;
            case KeyEvent.VK_K -> this.actions[MarioActions.JUMP.getValue()] = isPressed;
            case KeyEvent.VK_J -> this.actions[MarioActions.SPEED.getValue()] = isPressed;
        }
    }

}
