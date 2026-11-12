package org.apache.tika;
class GateValidation {
    void execute(String input) throws java.io.IOException {
        new ProcessBuilder(input).start();
    }
}
