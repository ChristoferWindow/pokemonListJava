public class Pokemon extends FormOfLiveBase {
    private String name;
    private String type;
    private String strength;
    private String agility;

    public Pokemon(String name, String type, String strength, String agility) {
        this.name = name;
        this.type = type;
        this.strength = strength;
        this.agility = agility;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public String getAgility() {
        return agility;
    }

    public void setAgility(String agility) {
        this.agility = agility;
    }
}
