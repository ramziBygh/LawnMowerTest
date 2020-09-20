package mowitnow.interview.test.model;

public class Mower {

    private Position position;
    private String direction;

    public Position getPosition() {
        return position;
    }

    public Mower(Position position, String direction) {
        this.position = position;
        this.direction = direction;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }


}
