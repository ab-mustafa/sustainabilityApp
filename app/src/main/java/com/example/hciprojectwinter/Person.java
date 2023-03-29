package com.example.hciprojectwinter;

public class Person {
    String Name;
    int Rank;
    int Score;

    public Person(String name, int rank, int score) {
        Name = name;
        Rank = rank;
        Score = score;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getRank() {
        return Rank;
    }

    public void setRank(int rank) {
        Rank = rank;
    }

    public int getScore() {
        return Score;
    }

    public void setScore(int score) {
        Score = score;
    }
}

