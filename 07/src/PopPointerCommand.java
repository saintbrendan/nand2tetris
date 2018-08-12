public class PopPointerCommand extends PopCommand {
    public PopPointerCommand(String source, String cmd, String segment, int index) {
        super(source, cmd, segment, index);
        /// TODO think about throwing exception if not 0 <= index <= 1
        if (index < 0 || index >= 2) {
            System.out.println(String.format("index must be 0 or 1, but it is %d.", index));
        }
    }


    @Override
    public String getAsmCode() {
        return POP_TO_D + THIS_THAT[index] + "\nM=D";
    }
}
