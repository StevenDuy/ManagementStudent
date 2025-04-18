package com.fpi.duyhh.Assignment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Manages a list of students with features like adding, editing, deleting,
 * searching, sorting, and ranking based on average score.
 */
public class StudentManager {
    private ArrayList<Student> students;
    private HashMap<String, Student> studentMap;
    private DecimalFormat df;

    public StudentManager() {
        students = new ArrayList<>();
        studentMap = new HashMap<>();
        df = new DecimalFormat("#.##");
    }

    // Get the list of all students
    public ArrayList<Student> getStudents() {
        return students;
    }

    /**
     * Adds a new student to the list.
     * @param s The student to be added
     * @return false if a student with the same ID already exists
     */
    public boolean addStudent(Student s) {
        if (studentMap.containsKey(s.getId()))
            return false;
        students.add(s);
        studentMap.put(s.getId(), s);
        return true;
    }

    /**
     * Edits a student.
     * @param oldId The previous ID of the student
     * @param s The updated student object
     * @return false if the new ID is already taken (when changed)
     */
    public boolean editStudent(String oldId, Student s) {
        if (!oldId.equals(s.getId())) {
            if (studentMap.containsKey(s.getId()))
                return false;
            studentMap.remove(oldId);
        }
        studentMap.put(s.getId(), s);
        return true;
    }

    /**
     * Deletes a student by their index (use when no table filtering is applied)
     * @param index The index in the student list
     * @return true if deletion is successful
     */
    public boolean deleteStudent(int index) {
        if (index < 0 || index >= students.size())
            return false;
        Student removed = students.remove(index);
        studentMap.remove(removed.getId());
        return true;
    }

    /**
     * Deletes a student by their ID (useful when UI table is filtered)
     * @param id The student ID
     * @return true if deletion is successful
     */
    public boolean deleteStudentById(String id) {
        Student s = studentMap.remove(id);
        if (s == null)
            return false;
        return students.remove(s);
    }

    /**
     * Calculates the rank of a student based on average score.
     * @param avg The average score
     * @return A string representing the student's rank
     */
    public String calculateRank(double avg) {
        if (avg < 5)
            return "Fail";
        else if (avg < 6.5)
            return "Pass";
        else if (avg < 7.5)
            return "Good";
        else if (avg < 9)
            return "Very Good";
        else
            return "Excellent";
    }

    /**
     * Public method to trigger quick sort on the student list
     * @param comp Comparator to define sort behavior
     */
    public void quickSort(Comparator<Student> comp) {
        quickSortBasic(0, students.size() - 1, comp);
    }

    /**
     * Internal Quick Sort algorithm using the first element as the pivot.
     * @param low Start index
     * @param high End index
     * @param comp Comparator for comparing students
     */
    private void quickSortBasic(int low, int high, Comparator<Student> comp) {
        if (low >= high)
            return;

        Student pivot = students.get(low);
        int left = low;
        int right = high;

        while (left < right) {
            while (left < right && comp.compare(students.get(right), pivot) >= 0)
                right--;
            students.set(left, students.get(right));
            while (left < right && comp.compare(students.get(left), pivot) <= 0)
                left++;
            students.set(right, students.get(left));
        }

        students.set(left, pivot);

        quickSortBasic(low, left - 1, comp);
        quickSortBasic(left + 1, high, comp);
    }

    /**
     * Returns a comparator based on the selected column and sort direction
     * @param col The column index (0: ID, 1: Name, 2: Major, 3: Average, 4: Rank)
     * @param ascending true if ascending order, false for descending
     * @return A comparator for sorting
     */
    public Comparator<Student> getComparator(int col, boolean ascending) {
        return (s1, s2) -> {
            int res = 0;
            switch (col) {
                case 0: res = s1.getId().compareTo(s2.getId()); break;
                case 1: res = s1.getName().compareTo(s2.getName()); break;
                case 2: res = s1.getMajor().compareTo(s2.getMajor()); break;
                case 3: res = Double.compare(s1.getAverageScore(), s2.getAverageScore()); break;
                case 4: res = s1.getRank().compareTo(s2.getRank()); break;
                default: res = 0; break;
            }
            return ascending ? res : -res;
        };
    }

    /**
     * Binary search a student by average score (requires sorted list by score)
     * @param target The score to search
     * @return Index of student if found, -1 otherwise
     */
    public int binarySearchByScore(double target) {
        int low = 0, high = students.size() - 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            double midVal = students.get(mid).getAverageScore();
            if (Math.abs(midVal - target) < 1e-6)
                return mid;
            else if (midVal < target)
                low = mid + 1;
            else
                high = mid - 1;
        }
        return -1;
    }

    /**
     * Finds a student by ID using the map for fast lookup
     * @param id The student ID
     * @return Student object or null if not found
     */
    public Student findById(String id) {
        return studentMap.get(id);
    }
}
