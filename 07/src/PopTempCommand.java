public class PopTempCommand extends PopCommand {

    public PopTempCommand(String source, String cmd, String segment, int index) {
        /// TODO think about confirming that 0 <= index <= 7
        super(source, cmd, segment, index);
    }

    @Override
    public String getAsmCode() {
        return "\n@SP\n" +
                "M=M-1\n" +
                "A=M\n" +
                "D=M\n" +
                "@R" + (index + 5) + " \n" +
                "M=D\n";
    }
}
