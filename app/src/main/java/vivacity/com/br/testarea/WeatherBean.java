package vivacity.com.br.testarea;

/**
 * Created by mac on 28/12/17.
 * @author Gabriel Dos Santos Magalhães
 *
 * Class representing Weather (i. e. weather current with yours attributes), by ID of city.
 */

class WeatherBean {

    /**
     * ID of city.
     */
    private int id;

    /**
     * Name of city.
     */
    private String name;

    /**
     * State of city.
     */
    private String state;

    /**
     * Country of city.
     * */
    private String country;

    /**
     * Current weather data for of city.
     * */
    private Data data;

    /**
     * Class representing weather data for the city
     * */
    public class Data {

        /**
         * Temperature in Celsius
         * */
        private double temperature;
        private String wind_direction;

        /**
         * Intensity of the wind in km / h.
         * */
        private double wind_velocity;

        /**
         * Relative humidity (%).
         * */
        private double humidity;
        private String condition;

        /**
         * Pressure (hPa).
         * */
        private String pressure;

        /**
         * Forecast icon. Download Realistic icons v1.0
         * @see ://api.climatempo.com.br/download/realistic.zip
         * */
        private String icon;

        /**
         * Sensation in degrees Celsius (° C).
         * */
        private String sensation;
        private String date;

        /**
         * @return double = Temperature in Celsius.
         * */
        public double getTemperature() {
            return temperature;
        }

        /**
         * @return String = Wind direction
         * */
        public String getWind_direction() {
            return wind_direction;
        }

        /**
         * @return double = Wind velocity
         * */
        public double getWind_velocity() {
            return wind_velocity;
        }

        /**
         * @return double = Humidity
         * */
        public double getHumidity() {
            return humidity;
        }

        /**
         * @return String = Condition
         * */
        public String getCondition() {
            return condition;
        }

        /**
         * @return String = Pressure
         * */
        public String getPressure() {
            return pressure;
        }

        /**
         * @return String = Forecast Icon.
         * */
        public String getIcon() {
            return icon;
        }

        /**
         * @return String = Sensation in Celsius
         * */
        public String getSensation() {
            return sensation;
        }

        /**
         * @return String = Date
         * */
        public String getDate() {
            return date;
        }
    }

    /**
     * @return int = ID of city.
     * */
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
     * @return String = Country of city.
     * */
    public String getCountry() {
        return country;
    }

    /**
     * @return {@link Data} Current weather data for the city.
     * */
    public Data getData() {
        return data;
    }
}
