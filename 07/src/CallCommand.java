public class CallCommand extends Command {
    private final String functionName;
    private int nArgs;

    private CallCommand(String source, String functionName, int nArgs) {
        super(source);
        this.functionName = functionName;
        this.nArgs = nArgs;
    }

    public static CallCommand create(String source, String functionName, int nArgs) {
        return new CallCommand(source, functionName, nArgs);
    }

    @Override
    public String getAsmCode() {
        String returnLabel = "$RETURN$" + functionName;

        return "   @" + returnLabel + "\n" +
                "   D=A\n" +
                PushCommand.PUSH_D +
                PushSegment.create("LCL").getAsmCode() +
                PushSegment.create("ARG").getAsmCode() +
                PushSegment.create("THIS").getAsmCode() +
                PushSegment.create("THAT").getAsmCode() +
                "   @SP\n" +
                "   D=M\n" +
                "   @5\n" +
                "   D=D-A\n" +
                "   @" + nArgs + "\n" +
                "   D=D-A\n" +
                "   @ARG\n" +
                "   M=D\n" +
                "   @SP\n" +
                "   D=M\n" +
                "   @LCL\n" +
                "   M=D\n" +
                "   @" + functionName + "\n" +
                "   0;JMP\n" +
                "(" + returnLabel + ")\n";
    }
}

