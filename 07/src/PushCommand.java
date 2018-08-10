public class PushCommand extends Command {
    private String prefix = "@";
    private String midfix = "\n" +
            "D=A\n";
    private String suffix = "\n" +
            "A=D+M\n" +
            "D=M\n";
    protected static final String PUSH_D = "@SP\n" +
            "A=M\n" +
            "M=D\n" +
            "@SP\n" +
            "M=M+1";

    private String asm;

    protected PushCommand(String source, String cmd, String segment, int index) {
        super(source, cmd, segment, index);
        asm = getAdjustedPrefix() + getAdjustedIndex() + getAdjustedMidfix() + getAdjustedSuffix() + PUSH_D;
    }

    private String getAdjustedPrefix() {
        if (isTemp()) {
            return "@R";
        }
        return prefix;
    }

    private int getAdjustedIndex() {
        if (isTemp()) {
            return index + 5;
        }
        return index;
    }

    // usually "D=A"
    private String getAdjustedMidfix() {
        if (isTemp()) {
            return "\nD=M\n";
        }
        return midfix;
    }

    private String getAdjustedSuffix() {
        if (isTemp() || isConstant()) {
            return "";
        }
        return atSegmentMap.get(segment) + suffix;
    }

    /// TODO give PopCommand a factory method like this one
    public static Command create(String source, String cmd, String segment, int index) {
        if ("pointer".equals(segment)) {
            return new PushPointerCommand(source, cmd, segment, index);
        }
        if ("static".equals(segment)) {
            return new PushStaticCommand(source, cmd, segment, index);
        }

        return new PushCommand(source, cmd, segment, index);
    }

    @Override
    public String getAsmCode() {
        return "// " + originalSource + "\n" + asm;
    }
}
