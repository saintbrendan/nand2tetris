import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class VMTranslator {

    public static void main(String[] args) throws IOException {
        //String fileName = "C:\\Users\\saint\\OneDrive\\Documents\\nand2tetris\\projects\\08\\ProgramFlow\\FibonacciSeries\\FibonacciSeries.vm";
        //String fileName = "C:\\Users\\saint\\OneDrive\\Documents\\nand2tetris\\projects\\08\\FunctionCalls\\SimpleFunction\\SimpleFunction.vm";
        String fileName = "C:\\Users\\saint\\OneDrive\\Documents\\nand2tetris\\projects\\08\\FunctionCalls\\StaticsTest";

        if (args.length >= 1) {
            fileName = args[0];
        }
        File input = new File(fileName);
        PrintStream out = System.out;
        if (input.isDirectory())  {
            /// create the outputstream
            OutputStream outputstream = outstreamFromDir(fileName);
            out = new PrintStream(outputstream);
            /// write the bootstrap
            out.println("   @256");
            out.println("   D=A");
            out.println("   @SP");
            out.println("   M=D");
            CallCommand callCommand = CallCommand.create("call Sys.init", "Sys.init", 0);
            out.println(callCommand.getAsmCode());
            /// for each file in the directory, generate the asm
            File[] vmFiles = input.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".vm");
                }
            });
            for (File file: vmFiles){
                String fullpath = file.getAbsolutePath();
                Command.setStaticName(getFileBase(fullpath));
                genCode(fullpath, out);
                if (out != System.out) {
                    genCode(fullpath, System.out);
                }
            }
            return;
        }

        String outFileName = fileName.substring(0, fileName.lastIndexOf('.')) + ".asm";
        System.out.println("Outputting to " + outFileName);
        try {
            OutputStream outputstream = new FileOutputStream(outFileName);
            out = new PrintStream(outputstream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Command.setStaticName(getFileBase(fileName));

        genCode(fileName, out);
        if (out != System.out) {
            genCode(fileName, System.out);
        }
    }

    private static OutputStream outstreamFromDir(String path) throws IOException {
        String trimmedPath = path.replaceAll("[/\\\\]+$", "");   // strip trailing slashes
        String dirName = trimmedPath.substring(path.lastIndexOf('\\')+1);
        String filename = dirName.substring(dirName.lastIndexOf('/')+1) + ".asm";
        Path fullPath = Paths.get(trimmedPath,filename);
        return Files.newOutputStream(fullPath);
    }

    ///  TODO change first parameter to an input stream
    private static void genCode(String fileName, PrintStream out) {
        //read file into stream, try-with-resources
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            stream.map(line -> Command.create(line))
                    .filter(command -> command != null)
                    .map(command -> command.getCommentedAsmCode())
                    .forEach(x -> out.println(x));
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
        String pathPart = pathParts[pathParts.length - 1];
        pathParts = pathPart.split("/");
        return pathParts[pathParts.length - 1];
    }
}
