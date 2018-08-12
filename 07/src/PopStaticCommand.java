public class PopStaticCommand extends PopCommand {
    public PopStaticCommand(String source, String cmd, String segment, int index) {
        super(source, cmd, segment, index);
    }

    @Override
    public String getAsmCode() {
        return POP_TO_D + getStaticLabel(index) + "M=D";
    }
}
