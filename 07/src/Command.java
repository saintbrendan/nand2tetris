import java.util.HashMap;
import java.util.Map;

public class Command {
    protected static final Map<String, String> atSegmentMap;
    static final Map<String, String> codeMap;
    protected static final String[] THIS_THAT = {"@THIS", "@THAT"};
    private static String staticName;
    private static int commandCount = 0;

    static {
        codeMap = new HashMap<String, String>();
        String[][] pairs = {
                {"add", "@SP\t\n" +
                        "M=M-1\n" +
                        "A=M\n" +
                        "D=M\n" +
                        "@SP\n" +
                        "A=M-1\n" +
                        "M=D+M\n"},
                {"sub", "@SP\t\n" +
                        "M=M-1\n" +
                        "A=M\n" +
                        "D=-M\n" +
                        "@SP\n" +
                        "A=M-1\n" +
                        "M=D+M\n"},
                {"neg", " @SP\n" +
                        " M=M-1\n" +
                        " A=M\n" +
                        " D=-M\n" +
                        " M=D\n" +
                        " @SP\n" +
                        " M=M+1"},
                {"eq", " @SP\n" +
                        " M=M-1\n" +
                        " A=M\n" +
                        " D=M\n" +
                        " @SP\n" +
                        " A=M-1\n" +
                        " D=M-D\n" +
                        " @EQ_TRUE_%1$d\n" +
                        " D;JEQ\n" +
                        "(EQ_FALSE_%1$d)\n" +
                        " @0\n" +
                        " D=A\n" +
                        " @EQ_DONE_%1$d\n" +
                        " 0;JMP\n" +
                        "(EQ_TRUE_%1$d)\n" +
                        "    D=D-1\n" +
                        "(EQ_DONE_%1$d)\n" +
                        " @SP\n" +
                        " M=M-1\n" +
                        " A=M\n" +
                        " M=D\n" +
                        " @SP\n" +
                        " M=M+1"},
                {"gt", "@SP\n" +
                        "M=M-1\n" +
                        "A=M\n" +
                        "D=M \n" +
                        "@SP\n" +
                        "A=M-1 \n" +
                        "D=M-D \n" +
                        "@GT_TRUE_%1$d \n" +
                        "D;JGT\n" +
                        "(GT_FALSE_%1$d)\n" +
                        "@0\n" +
                        "D=A\n" +
                        "@GT_DONE_%1$d\n" +
                        "0;JMP\n" +
                        "(GT_TRUE_%1$d)\n" +
                        "@0\n" +
                        "D=A-1\n" +
                        "(GT_DONE_%1$d)\n" +
                        "@SP\n" +
                        "M=M-1\n" +
                        "A=M\n" +
                        "M=D\n" +
                        "@SP\n" +
                        "M=M+1"},
                {"lt", "@SP\n" +
                        "M=M-1\n" +
                        "A=M\n" +
                        "D=M \n" +
                        "@SP\n" +
                        "A=M-1 \n" +
                        "D=M-D \n" +
                        "@LT_TRUE_%1$d \n" +
                        "D;JLT\n" +
                        "(LT_FALSE_%1$d) \n" +
                        "@0\n" +
                        "D=A\n" +
                        "@LT_DONE_%1$d\n" +
                        "0;JMP\n" +
                        "(LT_TRUE_%1$d) \n" +
                        "@0\n" +
                        "D=A-1 \n" +
                        "(LT_DONE_%1$d)\n" +
                        "@SP\n" +
                        "M=M-1\n" +
                        "A=M\n" +
                        "M=D\n" +
                        "@SP\n" +
                        "M=M+1"},
                {"and", " @SP\n" +
                        " M=M-1\n" +
                        " A=M\n" +
                        " D=M    \n" +
                        " @SP\n" +
                        " A=M-1  \n" +
                        " M=D&M"},
                {"or", " @SP\n" +
                        " M=M-1\n" +
                        " A=M\n" +
                        " D=M    \n" +
                        " @SP\n" +
                        " A=M-1\n" +
                        " M=D|M"},
                {"not", " @SP\n" +
                        " M=M-1\n" +
                        " A=M\n" +
                        " D=!M  \n" +
                        " M=D\n" +
                        " @SP\n" +
                        " M=M+1"},
                {"", ""}
        };
        for (String[] pair : pairs) {
            codeMap.put(pair[0], pair[1]);
        }

        atSegmentMap = new HashMap<>();
        String[][] atSegments = {
                {"SP", "@SP"},
                {"local", "@LCL"},
                {"argument", "@ARG"},
                {"this", "@THIS"},
                {"that", "@THAT"}
        };
        for (String[] atSegment: atSegments) {
            atSegmentMap.put(atSegment[0], atSegment[1]);
        }
    }

    String originalSource;
    String cmd;
    String segment;
    int index;

    private Command() {
    }

    private Command(String source, String cmd) {
        originalSource = source;
        this.cmd = cmd;
    }

    protected Command(String source) {
        originalSource = source;
    }

    protected Command(String source, String cmd, String segment, int index) {
        originalSource = source;
        this.cmd = cmd;
        this.segment = segment;
        this.index = index;
    }

    public static Command create(String source) {
        commandCount += 1;
        int iComment = source.lastIndexOf("//");
        if (iComment >= 0) {
            source = source.substring(0, iComment);
        }
        String[] strings = source.split("\\s");
        if (strings.length == 1) {
            if (strings[0].length() == 0) {
                return null;
            }
            return new Command(source, strings[0]);
        }
        String errorMsg = "You should have 3 fields in your argument [" + source + "].  (Or 2 for labels and gotos.)  But you have " + source.length() + ".";
        if (strings.length == 2) {
            String cmd = strings[0];
            String labelName = strings[1];
            if ("label".equals(cmd)) {
                return LabelCommand.create(source, cmd, labelName);
            }
            if ("goto".equals(cmd)) {
                return GotoCommand.create(source, cmd, labelName);
            }
            if ("if-goto".equals(cmd)) {
                return IfGotoCommand.create(source, cmd, labelName);
            }
            // Push segment.  E.g. push LCL, push ARG, push THIS
            if ("push".equals(cmd)) {
                return PushSegment.create(source, labelName);
            }
        } else if (strings.length == 3) {
            try {
                String cmd = strings[0];
                String segment = strings[1];
                int index = Integer.parseInt(strings[2]);
                if (cmd.equals("call")) {
                    String functionName = strings[1];
                    int nArgs = index;
                    return CallCommand.create(source, cmd, functionName, nArgs);
                }
                if (cmd.equals("function")) {
                    String functionName = strings[1];
                    int nLocalVars = index;
                    return FunctionCommand.create(source,functionName, nLocalVars);
                }
                if (cmd.equals("push")) {
                    return PushCommand.create(source, cmd, segment, index);
                } else if (cmd.equals("pop")) {
                    if ("temp".equals(segment)) {
                        return new PopTempCommand(source, cmd, segment, index);
                    }
                    if ("pointer".equals(segment)) {
                        return new PopPointerCommand(source, cmd, segment, index);
                    }
                    if ("static".equals(segment)) {
                        return new PopStaticCommand(source, cmd, segment, index);
                    }
                    return new PopCommand(source, cmd, segment, index);
                }
                errorMsg = "Command " + source + " has 3 parameters.  So the command should be 'push' or 'pop.  But it is [" + cmd + "].";
            } catch (NumberFormatException e) {
                System.out.println("s:" + source + "  s.length():" + source.length() + "  strings[2]:" + strings[2]);
                errorMsg = "Command " + source + " should have a numeric third parameter.  But it is [" + strings[2] + "].";
                throw new IllegalArgumentException(errorMsg, e);
            }
        }
        throw new IllegalArgumentException(errorMsg);
    }

    public String getAsmCode() {
        String asm = codeMap.get(cmd);
        if (asm.contains("%1$d")) {
            asm = String.format(asm, commandCount);
        }
        return asm;
    }

    public String getCommentedAsmCode() {
        return "// " + originalSource + "\n" + getAsmCode();
    }


    protected boolean isTemp() {
        return "temp".equals(segment);
    }

    protected boolean isConstant() {
        return "constant".equals(segment);
    }
    
    public static void setStaticName(String staticName) {
        Command.staticName = staticName;
    }

    protected static String getStaticLabel(int index) {
        return "@" + staticName + "." + index + "\n";
    }
}
