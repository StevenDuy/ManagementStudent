package com.fpi.duyhh.Assignment;

public class Student {
    private String id, name, major, rank;
    private double score1, score2, score3, averageScore;

    public Student(String id, String name, String major,
                   double score1, double score2, double score3,
                   double averageScore, String rank) {
        this.id = id;
        this.name = name;
        this.major = major;
        this.score1 = score1;
        this.score2 = score2;
        this.score3 = score3;
        this.averageScore = averageScore;
        this.rank = rank;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getMajor() { return major; }
    public double getScore1() { return score1; }
    public double getScore2() { return score2; }
    public double getScore3() { return score3; }
    public double getAverageScore() { return averageScore; }
    public String getRank() { return rank; }

    // Setters
    public void setId(String id){ this.id = id; }
    public void setName(String name){ this.name = name; }
    public void setMajor(String major){ this.major = major; }
    public void setScore1(double score1){ this.score1 = score1; }
    public void setScore2(double score2){ this.score2 = score2; }
    public void setScore3(double score3){ this.score3 = score3; }
    public void setAverageScore(double averageScore){ this.averageScore = averageScore; }
    public void setRank(String rank){ this.rank = rank; }
}
