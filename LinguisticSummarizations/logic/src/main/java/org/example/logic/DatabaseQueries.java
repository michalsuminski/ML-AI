package org.example.logic;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseQueries {

    private Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:identifier.sqlite");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public List<Data> selectAll() {
        String sql = "SELECT * FROM sales";
        List<Data> database = new ArrayList<>();
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            // loop through the result set
            while (rs.next()) {
                database.add(new Data(rs.getLong("id"), rs.getString("date"), rs.getDouble("price"), rs.getDouble("bedrooms"),
                        rs.getDouble("bathrooms"), rs.getDouble("sqft_living"), rs.getDouble("sqft_lot"), rs.getDouble("floors"),
                        rs.getBoolean("waterfront"), rs.getDouble("view"), rs.getDouble("condition"), rs.getDouble("grade"),
                        rs.getDouble("sqft_above"), rs.getDouble("sqft_basement"), rs.getInt("yr_built"), rs.getInt("yr_renovated"),
                        rs.getInt("zipcode"), rs.getDouble("lat"), rs.getDouble("long"), rs.getDouble("sqft_living15"),
                        rs.getDouble("sqft_lot15")));
            }
            return database;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return database;
    }

    public List<Double> selectDateOfSale(String specificQuery) {
        String sql = "SELECT date FROM sales" + specificQuery;
        List<Double> database = new ArrayList<>();
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            // loop through the result set
            while (rs.next()) {
                database.add(rs.getDouble("date"));
            }
            return database;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return database;
    }

    public List<Double> selectPrice(String specificQuery) {
        String sql = "SELECT price FROM sales" + specificQuery;
        List<Double> database = new ArrayList<>();
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            // loop through the result set
            while (rs.next()) {
                database.add(rs.getDouble("price"));
            }
            return database;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return database;
    }

    public List<Double> selectSqftLiving(String specificQuery) {
        String sql = "SELECT sqft_living FROM sales" + specificQuery;
        List<Double> database = new ArrayList<>();
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            // loop through the result set
            while (rs.next()) {
                database.add(rs.getDouble("sqft_living"));
            }
            return database;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return database;
    }

    public List<Double> selectSqftLot(String specificQuery) {
        String sql = "SELECT sqft_lot FROM sales" + specificQuery;
        List<Double> database = new ArrayList<>();
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            // loop through the result set
            while (rs.next()) {
                database.add(rs.getDouble("sqft_lot"));
            }
            return database;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return database;
    }

    public List<Double> selectSqftBasement(String specificQuery) {
        String sql = "SELECT sqft_basement FROM sales" + specificQuery;
        List<Double> database = new ArrayList<>();
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            // loop through the result set
            while (rs.next()) {
                database.add(rs.getDouble("sqft_basement"));
            }
            return database;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return database;
    }

    public List<Double> selectYearBuilt(String specificQuery) {
        String sql = "SELECT yr_built FROM sales" + specificQuery;
        List<Double> database = new ArrayList<>();
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            // loop through the result set
            while (rs.next()) {
                database.add(2022 - rs.getDouble("yr_built"));
            }
            return database;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return database;
    }

    public List<Double> selectYearRenovated(String specificQuery) {
        String sql = "SELECT yr_renovated FROM sales" + specificQuery;
        List<Double> database = new ArrayList<>();
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            // loop through the result set
            while (rs.next()) {
                database.add(rs.getDouble("yr_renovated"));
            }
            return database;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return database;
    }

    public List<Double> selectBedrooms(String specificQuery) {
        String sql = "SELECT bedrooms FROM sales" + specificQuery;
        List<Double> database = new ArrayList<>();
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            // loop through the result set
            while (rs.next()) {
                database.add(rs.getDouble("bedrooms"));
            }
            return database;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return database;
    }

    public List<Double> selectBathrooms(String specificQuery) {
        String sql = "SELECT bathrooms FROM sales" + specificQuery;
        List<Double> database = new ArrayList<>();
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            // loop through the result set
            while (rs.next()) {
                database.add(rs.getDouble("bathrooms"));
            }
            return database;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return database;
    }

    public List<Double> selectFloors(String specificQuery) {
        String sql = "SELECT floors FROM sales" + specificQuery;
        List<Double> database = new ArrayList<>();
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            // loop through the result set
            while (rs.next()) {
                database.add(rs.getDouble("floors"));
            }
            return database;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return database;
    }

    public List<Double> selectGrade(String specificQuery) {
        String sql = "SELECT grade FROM sales" + specificQuery;
        List<Double> database = new ArrayList<>();
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            // loop through the result set
            while (rs.next()) {
                database.add(rs.getDouble("grade"));
            }
            return database;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return database;
    }
}
