package com.izone.globalweather.air_quality;

import java.util.List;

public class PollutionModel {
    private List<list> list;
    private coord coord;

    public PollutionModel.coord getCoord() {
        return coord;
    }

    public void setCoord(PollutionModel.coord coord) {
        coord = coord;
    }

    public class coord {
        private double lon;
        private double lat;

        public double getLon() {
            return lon;
        }

        public void setLon(double lon) {
            this.lon = lon;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }
    }

    public PollutionModel(List<list> entries) {
        this.list = entries;
    }

    public List<list> getEntries() {
        return list;
    }

    public void setEntries(List<list> entries) {
        this.list = entries;
    }


    class list {
        public PollutionModel.list.main getMain() {
            return main;
        }

        public void setMain(PollutionModel.list.main main) {
            this.main = main;
        }


        public PollutionModel.list.components getComponents() {
            return components;
        }

        public void setComponents(PollutionModel.list.components components) {
            this.components = components;
        }

        main main;
        components components;
        String dt;

        public list(String dt) {
            this.dt = dt;
        }

        public String getDt() {
            return dt;
        }

        public void setDt(String dt) {
            this.dt = dt;
        }

        class main {
            public int getAqi() {
                return aqi;
            }

            public void setAqi(int aqi) {
                this.aqi = aqi;
            }

            int aqi;
        }


        class components {
            double co;
            double no;
            double no2;
            double o3;
            double so2;
            double pm2_5;
            double pm10;
            double nh3;

            public double getCo() {
                return co;
            }

            public void setCo(double co) {
                this.co = co;
            }

            public double getNo() {
                return no;
            }

            public void setNo(double no) {
                this.no = no;
            }

            public double getNo2() {
                return no2;
            }

            public void setNo2(double no2) {
                this.no2 = no2;
            }

            public double getO3() {
                return o3;
            }

            public void setO3(double o3) {
                this.o3 = o3;
            }

            public double getSo2() {
                return so2;
            }

            public void setSo2(double so2) {
                this.so2 = so2;
            }

            public double getPm2_5() {
                return pm2_5;
            }

            public void setPm2_5(double pm2_5) {
                this.pm2_5 = pm2_5;
            }

            public double getPm10() {
                return pm10;
            }

            public void setPm10(double pm10) {
                this.pm10 = pm10;
            }

            public double getNh3() {
                return nh3;
            }

            public void setNh3(double nh3) {
                this.nh3 = nh3;
            }
        }

    }

}



