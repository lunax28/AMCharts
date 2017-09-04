public enum GenreClass {

    BLUES("2"),

    CHILDRENS_MUSIC("4");

    /**

                  CLASSICAL
    COUNTRY
            ELECTRONIC
    HOLIDAY
            JAZZ
    LATINO
            NEW_AGE
    SOUNDTRACK
            DANCE
    WORLD
            ALTERNATIVE
    EASY_LISTENING
    FITNESS_&_WORKOUT
            INSTRUMENTAL
    ENVIRONMENTAL
            HEALING
    MEDITATION
            NATURE
    RELAXATION
            TRAVEL
    AMBIENT
            LOUNGE
    */

    private String field;

    GenreClass(String field) {
        this.field = field;
    }

    public String getField() {
        return this.field;
    }




}
