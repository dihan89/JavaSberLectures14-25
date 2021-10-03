package lecture14Cuncurrent;
import java.io.*;
import java.util.zip.*;

public class ZipArchiver {
    final static int bufferLength = 1024;

    public static void compress(byte[] byteArrayInput, File directory, String outputFileName, String exp) {
        File outputFile = new File(directory, outputFileName + ".zip");
        try (ZipOutputStream zipOutputStream =
                     new ZipOutputStream(new FileOutputStream(outputFile))) {
            ZipEntry zipEntry = new ZipEntry(outputFileName + "."+ exp);
            zipOutputStream.putNextEntry(zipEntry);
            zipOutputStream.write(byteArrayInput, 0, byteArrayInput.length);
        } catch (IOException exc) {
            System.out.println("Cannot create zip-archive " + outputFile.getName() + ": " + exc.getMessage());
        }
    }

    public static byte[] expand(File inputFile) {
       ByteArrayOutputStream bos = new ByteArrayOutputStream();
       try (ZipInputStream zipInputStream =
                     new ZipInputStream(new FileInputStream(inputFile))) {
            byte[] buffer = new byte[bufferLength];
            while (zipInputStream.getNextEntry() != null) {
                int bufLength;
                while ((bufLength = zipInputStream.read(buffer, 0, bufferLength)) != -1) {
                    bos.write(buffer, 0, bufLength);
                }
            }
        } catch (FileNotFoundException exc) {
            System.out.printf("File %s is not found%n", inputFile.getName());
        } catch (IOException exc) {
            System.out.println("Cannot read zip-archive " + inputFile.getName() + ": " + exc.getMessage());
        }
        return bos.toByteArray();
    }
}




