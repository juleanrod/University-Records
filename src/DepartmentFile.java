import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class DepartmentFile {
    private static final int RECORD_SIZE = 40;
    private static RandomAccessFile file;
    private static Department d;


    public DepartmentFile(String fileName) throws FileNotFoundException {
        file = new RandomAccessFile(fileName, "rw");

    }
    public void updateRecord(Department d, long x) throws IOException {

        file = new RandomAccessFile("Department.dat", "rw");
        moveFilePointerDepartment(x-1);
        String delimeter = "/";
        String str = d.getDepName()+ delimeter;
        str.toLowerCase();
        if (str.length() > RECORD_SIZE) {
            for (int i = 0; i < 20; i++) {
                file.writeChar(str.charAt(i));
            }
        } else {
            file.writeChars(str);
            for (int i = 0; i < (20 - str.length()); i++)
                file.writeChars(" ");
        }

    }
    public void writeDepartmentRecord(Department d) throws IOException {

        try {
            file.seek(getByteNum(getNumOfRecords()));
            String delimeter = "/";
            String str = d.getDepName() + delimeter;
            str.toLowerCase();
            if (str.length() > RECORD_SIZE) {
                for (int k = 0; k < 20; k++) {
                    file.writeChar(str.charAt(k));
                }
            } else {
                file.writeChars(str);
                for (int j = 0; j < (20 - str.length()); j++)
                    file.writeChars(" ");
            }
        } catch (IOException e) {
            System.out.println("Error, file not found");
        }


    }

    public static Department readFromFile(long x) throws IOException {

        try {
            file = new RandomAccessFile("Department.dat", "r");

            file.seek(getByteNum(x-1));

            char[] charArray = new char[20];
            for (int i = 0; i < 20; i++) {
                charArray[i] = file.readChar();
            }
            String str = new String(charArray);
            str = str.toUpperCase().trim();
            String[] tokens = str.split("/");
            d = new Department(tokens[0]);

            file.close();

        } catch (IOException e) {
            System.out.println("Record Not Found");
        }
        return d;
    }
    private static long getByteNum(long recordNum) {
        return RECORD_SIZE * recordNum;
    }
    public void moveFilePointerDepartment(long recordNum) throws FileNotFoundException, IOException{
        file.seek(getByteNum(recordNum));
    }
    public static long getNumOfRecords() throws FileNotFoundException, IOException{
        return file.length() / RECORD_SIZE;
    }
    public void close() throws IOException{
        file.close();
    }

}