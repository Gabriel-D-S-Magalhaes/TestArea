package vivacity.com.br.testarea;

/**
 * Created by mac on 29/12/17.
 * @author Gabriel Dos Santos Magalhães
 *
 * Class representing the Forecast for 72 hours (3 days).
 */
public class Forecast3Bean {

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
     * Array of Object (json object) with forecast data per hour.
     * */
    private Data data[];

    /**
     * Class representing weather data.
     * */
    public class Data {

        /**
         * Date in the format "yyyy-mm-dd" (e. g. 2017-10-01).
         * */
        private String date;
        /**
         * Date in the format of Brazil "dd / mm / yyyy" (e. g. 01/10/2017).
         */
        private String date_br;
        /**
         * Instance of {@link Rain}
         */
        private Rain rain;
        /**
         * Instance of {@link Wind}
         */
        private Wind wind;
        /**
         * Instance od {@link Temperature}
         */
        private Temperature temperature;

        /**
         * Class representing rain data.
         * */
        public class Rain {
            /**
             * Precipitation in millimeters.
             */
            private double precipitation;

            /**
             * @return double = Precipitation in millimeters.
             */
            public double getPrecipitation() {
                return precipitation;
            }
        }

        /**
         * Class representing wind data.
         * */
        public class Wind {
            /**
             * Intensity of the wind in km / h.
             */
            private double velocity;
            /**
             * Wind Direction.
             */
            private String direction;
            /**
             * Wind direction in degrees.
             */
            private double directionDegrees;
            /**
             * Wind gust values in km/h.
             */
            private double gust;

            /**
             * @return double = Intensity of the wind in km / h.
             */
            public double getVelocity() {
                return velocity;
            }

            /**
             * @return String = Wind Direction.
             */
            public String getDirection() {
                return direction;
            }

            /**
             * @return double = Wind direction in degrees.
             */
            public double getDirectionDegrees() {
                return directionDegrees;
            }

            /**
             * @return double = Wind gust values in km/h.
             */
            public double getGust() {
                return gust;
            }
        }

        /**
         * Class representing temperature data.
         * */
        public class Temperature {
            /**
             * Temperature in degrees Celsius.
             */
            private double temperature;

            /**
             * @return double = Temperature in degrees Celsius.
             */
            public double getTemperature() {
                return temperature;
            }
        }

        /**
         * @return String = Date in the format "yyyy-mm-dd" (e. g. 2017-10-01).
         * */
        public String getDate() {
            return date;
        }

        /**
         * @return String =  Date in the format of Brazil "dd / mm / yyyy" (e. g. 01/10/2017).
         */
        public String getDate_br() {
            return date_br;
        }

        /**
         * @return Rain = Instance of {@link Rain}
         */
        public Rain getRain() {
            return rain;
        }

        /**
         * @return Wind = Instance of {@link Wind}
         */
        public Wind getWind() {
            return wind;
        }

        /**
         * @return Temperature = Instance of {@link Temperature}
         */
        public Temperature getTemperature() {
            return temperature;
        }
    }

    /**
     * @return int = city ID.
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
     * @return Data[] = Array of objects of type Data
     * @see Data
     * */
    public Data[] getData() {
        return data;
    }
}
