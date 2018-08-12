public class FunctionCommand extends Command {
    private final String functionName;
    private final int nLocalVars;

    private FunctionCommand(String source, String functionName, int nLocalVars) {
        super(source);
        this.functionName = functionName;
        this.nLocalVars = nLocalVars;
    }

    public static Command create(String source, String functionName, int nLocalVars) {
        return new FunctionCommand(source, functionName, nLocalVars);
    }

    @Override
    public String getAsmCode() {
        String asm = "(" + functionName + ")\n";
        for (int i = 0; i < nLocalVars; i++) {
            asm += "   D=0\n" +
                    PushCommand.PUSH_D;
        }
        return asm;
    }
}
