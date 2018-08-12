public class PushStaticCommand extends PushCommand {
    public PushStaticCommand(String source, String cmd, String segment, int index) {
        super(source, cmd, segment, index);
    }

    @Override
    public String getAsmCode() {
        return getStaticLabel(index) + "D=M\n" + PUSH_D;
    }
}
