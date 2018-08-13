public class PushSegment  extends Command {
    private final String segmentName;

    private PushSegment(String segmentName) {
        super("push " + segmentName);
        this.segmentName = segmentName;
    }
    private PushSegment(String source, String segmentName) {
        super(source);
        this.segmentName = segmentName;
    }

    public static Command create(String source, String labelName) {
        return new PushSegment(source, labelName);
    }

    public static Command create(String labelName) {
        return new PushSegment(labelName);
    }

    @Override
    public String getAsmCode() {
        return "   @" + segmentName + "\n" +
                "   D=M\n" +
                PushCommand.PUSH_D;
    }
}
