package vivacity.com.br.testarea;

/**
 * Created by mac on 29/12/17.
 * @author Gabriel Dos santos Magalhães
 *
 * Class representing the Forecast for 15 days.
 */

public class Forecast15Bean {

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
     */
    private String country;

    /**
     * Array of instances of type {@link Data}
     */
    private Data data[];

    /**
     * Class representing forecast data per day.
     */
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
         * Instance of {@link Humidity}
         */
        private Humidity humidity;

        /**
         * Instance of {@link Rain}
         */
        private Rain rain;

        /**
         * Instance of {@link Wind}
         */
        private Wind wind;

        /**
         * Instance of {@link Uv}
         */
        private Uv uv;

        /**
         * Instance of {@link Thermal_sensation}
         */
        private Thermal_sensation thermal_sensation;

        /**
         * Instance of {@link Text_icon}
         */
        private Text_icon text_icon;

        /**
         * Instance of {@link Temperature}
         */
        private Temperature temperature;

        /**
         * Class representing humidity data.
         */
        public class Humidity {

            /**
             * Minimum Relative Humidity (%).
             */
            private double min;

            /**
             * Maximum Relative Humidity (%).
             */
            private double max;

            /**
             * @return double = Minimum Relative Humidity (%).
             */
            public double getMin() {
                return min;
            }

            /**
             * @return double = Maximum Relative Humidity (%).
             */
            public double getMax() {
                return max;
            }
        }

        /**
         * Class representing rain data.
         */
        public class Rain {

            /**
             * Chance of rain in percentage.
             */
            private double probability;

            /**
             * Precipitation in millimeters.
             */
            private double precipitation;

            /**
             * @return double = Chance of rain in percentage.
             */
            public double getProbability() {
                return probability;
            }

            /**
             * @return double = Precipitation in millimeters.
             */
            public double getPrecipitation() {
                return precipitation;
            }
        }

        /**
         * Class representing wind data.
         */
        public class Wind {

            /**
             * Minimum wind strength in km / h.
             */
            private double velocity_min;

            /**
             * Maximum wind strength in km / h.
             */
            private double velocity_max;

            /**
             * Average wind strength in km / h.
             */
            private double velocity_avg;

            /**
             * Gust maximum of wind in km/h.
             */
            private double gust_max;

            /**
             * Wind direction in degrees.
             */
            private double direction_degrees;

            /**
             * Wind Direction.
             */
            private String direction;

            /**
             * @return double = Minimum wind strength in km / h.
             */
            public double getVelocity_min() {
                return velocity_min;
            }

            /**
             * @return double = Maximum wind strength in km / h.
             */
            public double getVelocity_max() {
                return velocity_max;
            }

            /**
             * @return double = Average wind strength in km / h.
             */
            public double getVelocity_avg() {
                return velocity_avg;
            }

            /**
             * @return double = Gust maximum of wind in km/h.
             */
            public double getGust_max() {
                return gust_max;
            }

            /**
             * @return double = Wind direction in degrees.
             */
            public double getDirection_degrees() {
                return direction_degrees;
            }

            /**
             * @return String = Wind Direction.
             */
            public String getDirection() {
                return direction;
            }
        }

        /**
         * Class representing UV data.
         */
        public class Uv {

            /**
             * Maximum index.
             * */
            private double max;

            /**
             * @return double = Maximum index.
             */
            public double getUv() {
                return max;
            }
        }

        /**
         * Class representing thermal sensation data.
         */
        public class Thermal_sensation {

            /**
             * Minimal thermal sensation (° C).
             */
            private double min;

            /**
             * Maximum thermal sensation (° C).
             */
            private double max;

            /**
             * @return double = Minimal thermal sensation (° C).
             */
            public double getMin() {
                return min;
            }

            /**
             * @return double = Maximum thermal sensation (° C).
             */
            public double getMax() {
                return max;
            }
        }

        /**
         * Class representing icon and text of forecast.
         */
        public class Text_icon {

            /**
             * Instance of class {@link Icon}
             */
            private Icon icon;

            /**
             * Instance of class {@link Text}
             */
            private Text text;

            /**
             * Class representing the Forecast icon
             * */
            public class Icon {

                /**
                 * dawn icon.
                 */
                private String dawn;

                /**
                 * Morning icon.
                 * */
                private String morning;

                /**
                 * Afternoon icon.
                 * */
                private String afternoon;

                /**
                 * Night icon.
                 * */
                private String night;

                /**
                 * Full day icon.
                 * */
                private String day;

                /**
                 * @return String = dawn icon.
                 * */
                public String getDawn() {
                    return dawn;
                }

                /**
                 * @return String = Morning icon.
                 * */
                public String getMorning() {
                    return morning;
                }

                /**
                 * @return String = Afternoon icon.
                 * */
                public String getAfternoon() {
                    return afternoon;
                }

                /**
                 * @return String = Night icon.
                 * */
                public String getNight() {
                    return night;
                }

                /**
                 * @return String = Full day icon.
                 * */
                public String getDay() {
                    return day;
                }
            }

            /**
             * Class representing the Forecast text.
             * */
            public class Text {

                /**
                 * Forecast text in Portuguese.
                 */
                private String pt;

                /**
                 * Forecast text in english.
                 */
                private String en;

                /**
                 * Forecast text in spanish.
                 */
                private String es;

                /**
                 * Instance of class {@link Phrase}
                 */
                private Phrase phrase;

                /**
                 * Class representing Forecast phrase.
                 */
                public class Phrase {

                    /**
                     * All day forecast phrase reduced.
                     */
                    private String reduced;

                    /**
                     * Sentence of forecast of the morning.
                     */
                    private String morning;

                    /**
                     * Sentence of forecast of the afternoon.
                     */
                    private String afternoon;

                    /**
                     * Sentence of forecast of the night.
                     */
                    private String night;

                    /**
                     * Sentence of forecast of the dawn.
                     */
                    private String dawn;

                    /**
                     * @return String = All day forecast phrase reduced.
                     */
                    public String getReduced() {
                        return reduced;
                    }

                    /**
                     * @return String = Sentence of forecast of the morning.
                     */
                    public String getMorning() {
                        return morning;
                    }

                    /**
                     * @return String = Sentence of forecast of the afternoon.
                     */
                    public String getAfternoon() {
                        return afternoon;
                    }

                    /**
                     * @return String = Sentence of forecast of the night.
                     */
                    public String getNight() {
                        return night;
                    }

                    /**
                     * @return String = Sentence of forecast of the dawn.
                     */
                    public String getDawn() {
                        return dawn;
                    }
                }

                /**
                 * @return String = Forecast text in portuguese.
                 */
                public String getPt() {
                    return pt;
                }

                /**
                 * @return String = Forecast text in english.
                 */
                public String getEn() {
                    return en;
                }

                /**
                 * @return String = Forecast text in spanish.
                 */
                public String getEs() {
                    return es;
                }

                /**
                 * @return Instance of class {@link Phrase}
                 */
                public Phrase getPhrase() {
                    return phrase;
                }
            }

            /**
             * @return Icon = Instance of class {@link Icon}
             */
            public Icon getIcon() {
                return icon;
            }

            /**
             * @return Text = Instance of class {@link Text}
             */
            public Text getText() {
                return text;
            }
        }

        /**
         * Class representing temperature (ºC) data.
         */
        public class Temperature {

            /**
             * Minimum temperature in degrees celsius.
             */
            private double min;

            /**
             * Maximum temperature in degrees Celsius.
             */
            private double max;

            /**
             * Instance of class {@link PeriodsOfTheDay} representing:
             * Morning temperature in degrees celsius.
             */
            private PeriodsOfTheDay morning;

            /**
             * Instance of class {@link PeriodsOfTheDay} representing:
             * Afternoon temperature in degrees celsius.
             */
            private PeriodsOfTheDay afternoon;

            /**
             * Instance of class {@link PeriodsOfTheDay} representing:
             * Night temperature in degrees celsius.
             */
            private PeriodsOfTheDay night;

            /**
             * Class representing periods of the day (i. e. Morning, afternoon and Night).
             * */
            public class PeriodsOfTheDay {

                /**
                 * Minimum temperature in degrees Celsius.
                 */
                private double min;

                /**
                 * Maximum temperature in degrees Celsius.
                 */
                private double max;

                /**
                 * @return double = Minimum temperature in degrees Celsius.
                 */
                public double getMin() {
                    return min;
                }

                /**
                 * @return double = Maximum temperature in degrees Celsius.
                 */
                public double getMax() {
                    return max;
                }
            }

            /**
             * @return double = Minimum temperature in degrees celsius.
             */
            public double getMin() {
                return min;
            }

            /**
             * @return double = Maximum temperature in degrees celsius.
             */
            public double getMax() {
                return max;
            }

            /**
             * @return PeriodsOfTheDay = Instance of class {@link PeriodsOfTheDay}
             */
            public PeriodsOfTheDay getMorning() {
                return morning;
            }

            /**
             * @return PeriodsOfTheDay = Instance of class {@link PeriodsOfTheDay}
             */
            public PeriodsOfTheDay getAfternoon() {
                return afternoon;
            }

            /**
             * @return PeriodsOfTheDay = Instance of class {@link PeriodsOfTheDay}
             */
            public PeriodsOfTheDay getNight() {
                return night;
            }
        }

        /**
         * @return String = Date in the format "yyyy-mm-dd" (e. g. 2017-10-01).
         */
        public String getDate() {
            return date;
        }

        /**
         * @return String = Date in the format of Brazil "dd / mm / yyyy" (e. g. 01/10/2017).
         */
        public String getDate_br() {
            return date_br;
        }

        /**
         * @return Humidity = Instance of {@link Humidity}
         */
        public Humidity getHumidity() {
            return humidity;
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
         * @return Uv = Instance of {@link Uv}
         */
        public Uv getUv() {
            return uv;
        }

        /**
         * @return Thermal_sensation = Instance of {@link Thermal_sensation}
         */
        public Thermal_sensation getThermal_sensation() {
            return thermal_sensation;
        }

        /**
         * @return Text_icon = Instance of {@link Text_icon}
         */
        public Text_icon getText_icon() {
            return text_icon;
        }

        /**
         * @return Temperature = Instance of {@link Temperature}
         */
        public Temperature getTemperature() {
            return temperature;
        }
    }

    /**
     * @return int = ID of city.
     */
    public int getId() {
        return id;
    }

    /**
     * @return String = Name of city.
     */
    public String getName() {
        return name;
    }

    /**
     * @return String = State of city.
     */
    public String getState() {
        return state;
    }

    /**
     * @return String = Country of city.
     */
    public String getCountry() {
        return country;
    }

    /**
     * @return Array = {@link Data} instances.
     */
    public Data[] getData() {
        return data;
    }
}
