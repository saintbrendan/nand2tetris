public class LabelCommand extends Command {
    private final String labelName;

    private LabelCommand(String source, String labelName) {
        super(source);
        this.labelName = labelName;
    }

    public static Command create(String source, String cmd, String labelName) {
        return new LabelCommand(source, labelName);
    }

    @Override
    public String getAsmCode() {
        return "(" + labelName + ")\n";
    }
}
