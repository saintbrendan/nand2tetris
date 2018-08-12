public class GotoCommand extends Command {
    private final String labelName;

    private GotoCommand(String source, String labelName) {
        super(source);
        this.labelName = labelName;
    }

    public static Command create(String source, String cmd, String labelName) {
        return new GotoCommand(source, labelName);
    }

    @Override
    public String getAsmCode() {
        return "   @" + labelName + "\n" +
                "   0;JMP\n";
    }
}
