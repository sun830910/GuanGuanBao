package com.enjoygreenlife.guanguanbao.model;

/**
 * Created by luthertsai on 2017/11/12.
 */

public class HomeStatistic {

    private double _co2;
    private int _bottles;
    private int _points;
    private int _rewards;

    public HomeStatistic (double co2, int bottles, int points, int rewards) {
        this._co2 = co2;
        this._bottles = bottles;
        this._points = points;
        this._rewards = rewards;
    }

    /*
    * Getter of CO2
    */
    public double get_co2() {
        return _co2;
    }

    /*
    * Setter of CO2
    */
    public void set_co2(double _co2) {
        this._co2 = _co2;
    }

    /*
    * Getter of Bottle
    */
    public int get_bottles() {
        return _bottles;
    }

    /*
    * Setter of Bottle
    */
    public void set_bottles(int _bottles) {
        this._bottles = _bottles;
    }

    /*
    * Getter of Points
    */
    public int get_points() {
        return _points;
    }

    /*
    * Setter of Points
    */
    public void set_points(int _points) {
        this._points = _points;
    }

    /*
    * Getter of Rewards
    */
    public int get_rewards() {
        return _rewards;
    }

    /*
    * Setter of Rewards
    */
    public void set_rewards(int _rewards) {
        this._rewards = _rewards;
    }

}
