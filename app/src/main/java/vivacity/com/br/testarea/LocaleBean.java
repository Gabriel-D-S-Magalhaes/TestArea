package vivacity.com.br.testarea;

/**
 * Created by mac on 29/12/17.
 * @author Gabriel Dos santos Magalhães
 *
 * Class representing the city (s).
 */

public class LocaleBean {

    /**
     * ID of city.
     * */
    private int id;

    /**
     * Name of city.
     * */
    private String name;

    /**
     * State of city.
     * */
    private String state;

    /**
     * Country of city.
     * */
    private String country;

    /**
     * @return id = ID of city.
     */
    public int getId() {
        return id;
    }

    /**
     * @return String = Name of city.
     * */
    public String getName() {
        return name;
    }

    /**
     * @return String = State of city.
     * */
    public String getState() {
        return state;
    }

    /**
     * @return String = Country of city,
     * */
    public String getCountry() {
        return country;
    }
}
