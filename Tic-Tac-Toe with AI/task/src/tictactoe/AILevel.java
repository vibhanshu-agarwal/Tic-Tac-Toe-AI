package tictactoe;

public enum AILevel {
    EASY("easy"),
    MEDIUM("medium"),
    HARD("hard");

    //String value of the enum such as "easy", "medium", "hard"
    private String value;

    //Constructor
    AILevel(String value) {
        this.value = value;
    }

    //Get the value of the enum
    public String getValue() {
        return value;
    }

}
