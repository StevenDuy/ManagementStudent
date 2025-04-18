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

    // --- Thuật toán Quick Sort viết lại theo dạng cơ bản ---
    // Phương thức sắp xếp này sẽ gọi quickSortBasic() đệ quy để sắp xếp danh sách
    public void quickSort(Comparator<Student> comp) {
        quickSortBasic(0, students.size() - 1, comp);
    }

    // Thuật toán quick sort cơ bản sử dụng pivot là phần tử đầu tiên
    private void quickSortBasic(int low, int high, Comparator<Student> comp) {
        if (low >= high)
            return;  // Điều kiện dừng

        // Chọn pivot là phần tử đầu tiên trong đoạn cần sắp xếp
        Student pivot = students.get(low);
        int left = low;
        int right = high;

        // Di chuyển con trỏ left và right cho đến khi gặp nhau
        while (left < right) {
            // Tìm phần tử từ bên phải nhỏ hơn pivot
            while (left < right && comp.compare(students.get(right), pivot) >= 0) {
                right--;
            }
            // Đưa phần tử ở vị trí right lên vị trí left
            students.set(left, students.get(right));
            // Tìm phần tử từ bên trái lớn hơn pivot
            while (left < right && comp.compare(students.get(left), pivot) <= 0) {
                left++;
            }
            // Đưa phần tử ở vị trí left xuống vị trí right
            students.set(right, students.get(left));
        }
        // Đưa pivot vào vị trí chính xác (left == right)
        students.set(left, pivot);

        // Đệ quy sắp xếp phần bên trái và bên phải
        quickSortBasic(low, left - 1, comp);
        quickSortBasic(left + 1, high, comp);
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
