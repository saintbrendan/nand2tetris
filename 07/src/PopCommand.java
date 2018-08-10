public class PopCommand extends Command {
    private static final String SET_AND_POP_TO_ADDRESS = "D=D+A\n" +
            "@addr\n" +
            "M=D\n" +
            "@SP\n" +
            "M=M-1\n" +
            "A=M\n" +
            "D=M\n" +
            "@addr\n" +
            "A=M\n" +
            "M=D";
    protected static final String POP_TO_D = "@SP\n" +
            "M=M-1\n" +
            "A=M\n" +
            "D=M\n";

    public PopCommand(String source, String cmd, String segment, int index) {
        super(source, cmd, segment, index);
    }

    @Override
    public String getAsmCode() {
        return "// " + originalSource + "\n" + atSegmentMap.get(segment) + "\nD=M\n@" + index + "\n" + SET_AND_POP_TO_ADDRESS;
    }
}
