public enum EventParam {
    //Generic values
    NONE(0),
    GOOMBA(2),
    RED_KOOPA(4),
    RED_KOOPA_WINGED(5),
    GREEN_KOOPA(6),
    GREEN_KOOPA_WINGED(7),
    SPIKY(8),
    BULLET_BILL(10),
    ENEMY_FLOWER(11),
    MUSHROOM(12),
    FIRE_FLOWER(13),
    SHELL(14),
    COIN(15),
    BRICK(22),
    QUESTION_BLOCK(24);

    private int value;

    EventParam(int newValue) {
        value = newValue;
    }

    public int getValue() {
        return value;
    }
}
