public class PushPointerCommand extends PopCommand {
    private static final String PUSH_M = "D=M\n" +
            /// TODO replace following string with PUSH_D
            "@SP\n" +
            "A=M\n" +
            "M=D\n" +
            "@SP\n" +
            "M=M+1\n";

    public PushPointerCommand(String source, String cmd, String segment, int index) {
        super(source, cmd, segment, index);
        /// TODO think about throwing exception if not 0 <= index <= 1
        if (index < 0 || index >= 2) {
            System.out.println(String.format("index must be 0 or 1, but it is %d.", index));
        }
    }

    @Override
    public String getAsmCode() {
        return "// " + originalSource + "\n" + THIS_THAT[index] + "\n" + PUSH_M;
    }
}
