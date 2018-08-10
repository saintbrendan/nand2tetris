import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class VMTranslator {

    public static void main(String[] args) {
        String fileName = "C:\\Users\\saint\\OneDrive\\Documents\\nand2tetris\\projects\\07\\MemoryAccess\\StaticTest\\StaticTest.vm";
        OutputStream outstream = System.out;
        if (args.length >= 1) {
            fileName = args[0];
            String outFileName = fileName.substring(0, fileName.lastIndexOf('.')) + ".asm";
            System.out.println("Outputting to " + outFileName);
            try {
                outstream = new FileOutputStream(outFileName);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        PrintStream out = new PrintStream(outstream);
        Command.setStaticName(getFileBase(fileName));

        genCode(fileName, out);
    }

    private static void genCode(String fileName, PrintStream out) {
        //read file into stream, try-with-resources
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            stream.map(line -> Command.create(line))
                    .filter(command -> command != null)
                    .map(command -> command.getAsmCode())
                    .forEach(x->out.println(x));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getFileBase(String filename) {
        int iDot = filename.lastIndexOf('.');
        if (iDot > 0) {
            filename = filename.substring(0, iDot);
        }
        String[] pathParts = filename.split("\\\\");
        String pathPart = pathParts[pathParts.length-1];
        pathParts = pathPart.split("/");
        return pathParts[pathParts.length-1];
    }
}
