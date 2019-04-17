package com.imprexion.adplay.bean;

import java.util.List;

public class WeatherInfo {

    private List<HeWeather6> HeWeather6;

    public List<HeWeather6> getHeWeather6() {
        return HeWeather6;
    }

    public class HeWeather6 {
        private Basic basic;
        private Update update;
        private String status;
        private Now now;

        public Basic getBasic() {
            return basic;
        }

        public Update getUpdate() {
            return update;
        }

        public String getStatus() {
            return status;
        }

        public Now getNow() {
            return now;
        }

        public class Basic {
            private String cid;
            private String location;
            private String parent_city;
            private String admin_area;
            private String cnty;

            public String getCid() {
                return cid;
            }

            public String getLocation() {
                return location;
            }

            public String getParent_city() {
                return parent_city;
            }

            public String getAdmin_area() {
                return admin_area;
            }

            public String getCnty() {
                return cnty;
            }
        }

        public class Update {
            private String loc;
            private String utc;

            public String getLoc() {
                return loc;
            }

            public String getUtc() {
                return utc;
            }
        }

        public class Now {
            private int cloud;
            private int cond_code;
            private String cond_txt;
            private String fl;
            private int hum;
            private float pcpn;
            private int pres;
            private int tmp;
            private int vis;
            private int wind_deg;
            private String wind_dir;
            private int wind_sc;
            private int wind_spd;

            public int getCloud() {
                return cloud;
            }

            public int getCond_code() {
                return cond_code;
            }

            public String getCond_txt() {
                return cond_txt;
            }

            public String getFl() {
                return fl;
            }

            public int getHum() {
                return hum;
            }

            public float getPcpn() {
                return pcpn;
            }

            public int getPres() {
                return pres;
            }

            public int getTmp() {
                return tmp;
            }

            public int getVis() {
                return vis;
            }

            public int getWind_deg() {
                return wind_deg;
            }

            public String getWind_dir() {
                return wind_dir;
            }

            public int getWind_sc() {
                return wind_sc;
            }

            public int getWind_spd() {
                return wind_spd;
            }
        }
    }
}
