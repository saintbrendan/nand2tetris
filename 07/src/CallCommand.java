public class CallCommand extends Command {
    private final String functionName;

    private CallCommand(String source, String functionName, int nArgs) {
        super(source);
        this.functionName = functionName;
    }

    public static Command create(String source, String cmd, String functionName, int nArgs) {
        return new CallCommand(source, functionName, nArgs);
    }

    @Override
    public String getAsmCode() {
        String returnLabel = "$RETURN$" + functionName;
        return "   @" + functionName + "\n" +
                "   0;JMP\n";
    }
}

