import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;

public class StringBuilderPlus {

    private StringBuilder sb;

    public StringBuilderPlus() {
         sb = new StringBuilder();
    }

    public void append(String str) {
        sb.append(str != null ? str : "");
    }

    public void appendLine(String str) {
        sb.append(str != null ? str : "").append(System.getProperty("line.separator"));
    }

    public String toString() {
        return sb.toString();
    }

    public void saveToFile(String filename) {
        try {
            File file = new File(filename);
            if (!file.exists()) {
                file.delete();
            }
            file.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.append(sb);
            writer.close();    
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
        }
    }
}
