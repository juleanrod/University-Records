package sample;

public class Grades {

        private String studentName;
        private String courseName;
        private String courseID;
        private String semester;
        private String year;
        private String grade;

        public Grades(String studentName, String courseName, String courseID, String semester, String year, String grade){

            this.studentName = studentName;
            this.courseName = courseName;
            this.courseID =  courseID;
            this.semester = semester;
            this.year = year;
            this.grade = grade;

        }

        public String getStudentName() {
            return studentName;
        }

        public void setStudentName(String studentName) {
            this.studentName = studentName;
        }

        public String getCourseName() {
            return courseName;
        }

        public void setCourseName(String courseName) {
            this.courseName = courseName;
        }

        public String getCourseID() {
            return courseID;
        }

        public void setCourseID(String courseID) {
            this.courseID = courseID;
        }

        public String getSemester() {
            return semester;
        }

        public void setSemester(String semester) {
            this.semester = semester;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

    }
