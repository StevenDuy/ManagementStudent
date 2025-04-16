package com.fpi.duyhh.Assignment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class StudentManager {
    private ArrayList<Student> students;
    private HashMap<String, Student> studentMap;
    private DecimalFormat df;

    public StudentManager() {
        students = new ArrayList<>();
        studentMap = new HashMap<>();
        df = new DecimalFormat("#.##");
    }

    // Lấy danh sách sinh viên
    public ArrayList<Student> getStudents() {
        return students;
    }

    // Thêm sinh viên, trả về false nếu Student ID đã tồn tại
    public boolean addStudent(Student s) {
        if (studentMap.containsKey(s.getId()))
            return false;
        students.add(s);
        studentMap.put(s.getId(), s);
        return true;
    }

    // Chỉnh sửa sinh viên, cập nhật HashMap nếu Student ID thay đổi
    public boolean editStudent(String oldId, Student s) {
        if (!oldId.equals(s.getId())) {
            if (studentMap.containsKey(s.getId()))
                return false;
            studentMap.remove(oldId);
        }
        studentMap.put(s.getId(), s);
        return true;
    }

    // Xóa sinh viên theo index (dùng khi bảng không lọc)
    public boolean deleteStudent(int index) {
        if (index < 0 || index >= students.size())
            return false;
        Student removed = students.remove(index);
        studentMap.remove(removed.getId());
        return true;
    }

    // Xóa sinh viên theo Student ID (dùng khi bảng có lọc, để xoá chính xác)
    public boolean deleteStudentById(String id) {
        Student s = studentMap.remove(id);
        if (s == null)
            return false;
        return students.remove(s);
    }

    // Tính hạng dựa trên điểm trung bình
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

    // --- Thuật toán Quick Sort với Comparator ---
    public void quickSort(Comparator<Student> comp) {
        quickSort(students, 0, students.size() - 1, comp);
    }

    private void quickSort(ArrayList<Student> list, int low, int high, Comparator<Student> comp) {
        if (low < high) {
            int pi = partition(list, low, high, comp);
            quickSort(list, low, pi - 1, comp);
            quickSort(list, pi + 1, high, comp);
        }
    }

    private int partition(ArrayList<Student> list, int low, int high, Comparator<Student> comp) {
        Student pivot = list.get(high);
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (comp.compare(list.get(j), pivot) <= 0) {
                i++;
                swap(list, i, j);
            }
        }
        swap(list, i + 1, high);
        return i + 1;
    }

    private void swap(ArrayList<Student> list, int i, int j) {
        Student temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }

    // Trả về Comparator dựa trên cột (0: ID, 1: Name, 2: Major, 3: Average Score, 4: Rank)
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

    // Tìm kiếm nhị phân theo điểm trung bình (nếu cần)
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

    // Tìm kiếm nhanh theo Student ID
    public Student findById(String id) {
        return studentMap.get(id);
    }
}
