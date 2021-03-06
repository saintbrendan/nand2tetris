public class ReturnCommand  extends Command {

    private ReturnCommand(String source) {
        super(source);
    }

    public static Command create(String source) {
        return new ReturnCommand(source);
    }

    private String decrementEndFrameAndRestoreSegment(String segmentName) {
        return "   @endFrame\n" +
                "   D=M-1\n" +
                "   AM=D\n" +
                "   D=M\n" +
                "   @"+segmentName+" \n" +
                "   M=D\n";
    }

    @Override
    public String getAsmCode() {
        return "   @LCL \n" +
                "   D=M\n" +
                "   @endFrame\n" +
                "   M=D\n" +
                "   @5\n" +
                "   A=D-A\n" +
                "   D=M\n" +
                "   @retAddr \n" +
                "   M=D\n" +
                PopCommand.POP_TO_D +
                "   @ARG \n" +
                "   A=M\n" +
                "   M=D\n" +
                "   @ARG \n" +
                "   D=M+1\n" +
                "   @SP\n" +
                "   M=D\n" +
                decrementEndFrameAndRestoreSegment("THAT") +
                decrementEndFrameAndRestoreSegment("THIS") +
                decrementEndFrameAndRestoreSegment("ARG") +
                decrementEndFrameAndRestoreSegment("LCL") +
                "   @retAddr\n" +
                "   A=M\n" +
                "   0;JMP\n";
    }
}
