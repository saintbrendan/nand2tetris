public class IfGotoCommand extends Command {
    private final String labelName;

    private IfGotoCommand(String source, String labelName) {
        super(source);
        this.labelName = labelName;
    }

    public static Command create(String source, String cmd, String labelName) {
        return new IfGotoCommand(source, labelName);
    }

    @Override
    public String getAsmCode() {
        return PopCommand.POP_TO_D +
                "   @" + labelName + "\n" +
                "   D;JNE\n";
    }
}